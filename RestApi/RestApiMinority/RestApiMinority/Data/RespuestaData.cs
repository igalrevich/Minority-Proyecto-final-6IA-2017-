using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RestApiMinority.Models;
using System.Data;

namespace RestApiMinority.Data
{
    public class RespuestaData
    {
        public static void Insert(Respuesta MiRespuesta)
        {
            string sInsert = "Insert into respuestas (Pregunta,Usuario,RespuestaFinal,RespuestaTemporal,Sala,NRonda) values ("+MiRespuesta.Pregunta.ToString()+","+MiRespuesta.Usuario.ToString()+",'"+MiRespuesta.RespuestaFinal.ToString()+"','"+MiRespuesta.RespuestaParcial.ToString()+"',"+MiRespuesta.Sala.ToString()+","+MiRespuesta.NRonda.ToString()+")";
            DBHelper.EjecutarIUD(sInsert);
        }
        public static int ObtenerCantVotos(string Opcion, int Sala)
        {
            string select = "select * from respuestas where RespuestaFinal=\"" + Opcion+ "\" and Sala="+Sala.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            int CantVotos = dt.Rows.Count;
            return CantVotos;
        }

        public static Resultado CalcularVotos(VotoACalcular MiVotoACalcular)
        {
            Respuesta MiRespuesta;
            Resultado MiResultado = new Resultado();
            string OpcionA = MiVotoACalcular.OpcionA;
            string OpcionB = MiVotoACalcular.OpcionB;
            int NuevoNRonda = MiVotoACalcular.NRonda + 1;
            bool MayoriaOpcionA = false;
            bool ActualizoCantJugadores = false;
            bool PidioOpcionesPregunta = false;
            int CantVotosOpcionA=0;
            string update = "UPDATE salasdejuegos SET CantCheckeoResultados=CantCheckeoResultados+1 WHERE Id=" + MiVotoACalcular.IdSala.ToString();
            DBHelper.EjecutarIUD(update);
            string select = "SELECT TerminoRonda FROM salasdejuegos WHERE Id="+MiVotoACalcular.IdSala.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            DataRow row = dt.Rows[0];
            bool TerminoRonda= row.Field<bool>("TerminoRonda");
            if (TerminoRonda == false)
            {
                update = "UPDATE salasdejuegos SET TerminoRonda=true WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                DBHelper.EjecutarIUD(update);
                select = "SELECT * FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda.ToString();
                dt = DBHelper.EjecutarSelect(select);
                foreach (DataRow Registro in dt.Rows)
                {
                    MiRespuesta = ObtenerPorRowRespuesta(Registro);
                    if (PidioOpcionesPregunta == false)
                    {
                        select = "SELECT * FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda.ToString() + " AND RespuestaFinal='" + OpcionA +"'";
                        dt = DBHelper.EjecutarSelect(select);
                        MiResultado.CantVotosOpcionA = dt.Rows.Count;
                        select = "SELECT * FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda.ToString() + " AND RespuestaFinal='" + OpcionB + "'";
                        dt = DBHelper.EjecutarSelect(select);
                        MiResultado.CantVotosOpcionB = dt.Rows.Count;
                        PidioOpcionesPregunta = true;
                        if (MiResultado.CantVotosOpcionA != MiResultado.CantVotosOpcionB)
                        {
                            MiResultado.Empate = false;
                            if (MiResultado.CantVotosOpcionA > MiResultado.CantVotosOpcionB)
                            {
                                MayoriaOpcionA = true;
                            }
                            else
                            {
                                MayoriaOpcionA = false;
                            }
                        }
                        else
                        {

                            MiResultado.Empate = true;
                            if (ActualizoCantJugadores == false)
                            {
                                update = "UPDATE salasdejuegos SET CantJugadores=0  WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                                DBHelper.EjecutarIUD(update);
                                update = "UPDATE usuariosxsala SET Sigue=false WHERE SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                                DBHelper.EjecutarIUD(update);
                                ActualizoCantJugadores = true;
                            }
                        }
                    }
                    if (MayoriaOpcionA)
                    {

                        if (ActualizoCantJugadores == false)
                        {
                            update = "UPDATE salasdejuegos SET CantJugadores=" + MiResultado.CantVotosOpcionB + ", NRonda=" + NuevoNRonda.ToString() + " WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                            DBHelper.EjecutarIUD(update);
                            MiResultado.MayoriaOpcionA = true;
                            ActualizoCantJugadores = true;
                        }
                        if (MiRespuesta.RespuestaFinal == OpcionB)
                        {
                            update = "UPDATE usuariosxsala SET Sigue=true WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                            DBHelper.EjecutarIUD(update);
                        }
                        else
                        {
                            update = "UPDATE usuariosxsala SET Sigue=false WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                            DBHelper.EjecutarIUD(update);
                            if(MiRespuesta.RespuestaFinal=="")
                            {
                                update = "UPDATE usuariosxsala SET VotoEnBlanco=true WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                                DBHelper.EjecutarIUD(update);
                            }
                        }
                    }
                    else
                    {
                        if (ActualizoCantJugadores == false)
                        {
                            update = "UPDATE salasdejuegos SET CantJugadores=" + MiResultado.CantVotosOpcionA + ", NRonda=" + NuevoNRonda.ToString() + " WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                            DBHelper.EjecutarIUD(update);
                            MiResultado.MayoriaOpcionA = false;
                            ActualizoCantJugadores = true;
                        }
                        if (MiRespuesta.RespuestaFinal == OpcionA)
                        {
                            update = "UPDATE usuariosxsala SET Sigue=true WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                            DBHelper.EjecutarIUD(update);
                        }
                        else
                        {
                            update = "UPDATE usuariosxsala SET Sigue=false WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                            DBHelper.EjecutarIUD(update);
                            if (MiRespuesta.RespuestaFinal == "")
                            {
                                update = "UPDATE usuariosxsala SET VotoEnBlanco=true WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                                DBHelper.EjecutarIUD(update);
                            }
                        }
                    }
                }

                select = "SELECT Sigue FROM usuariosxsala WHERE SalaDeJuego=" + MiVotoACalcular.IdSala.ToString() + " AND Usuario=" + MiVotoACalcular.IdUsuario.ToString();
                dt = DBHelper.EjecutarSelect(select);
                row = dt.Rows[0];
                bool Sigue = row.Field<bool>("Sigue");
                if (Sigue)
                {
                    MiResultado.Gano = true;
                }
                else
                {
                    MiResultado.Gano = false;
                }

            }

            else
            {
                select = "SELECT Sigue FROM usuariosxsala WHERE SalaDeJuego=" + MiVotoACalcular.IdSala.ToString() + " AND Usuario="+MiVotoACalcular.IdUsuario.ToString();
                dt = DBHelper.EjecutarSelect(select);
                row = dt.Rows[0];
                select = "SELECT * FROM usuariosxsala WHERE Sigue=false AND VotoEnBlanco=false AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                DataTable dtSigueFalse = DBHelper.EjecutarSelect(select);
                select = "SELECT * FROM usuariosxsala WHERE Sigue=true AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                DataTable dtSigueTrue = DBHelper.EjecutarSelect(select);
                bool Sigue = row.Field<bool>("Sigue");
                if (Sigue)
                {
                    MiResultado.Gano = true;
                    if (MiVotoACalcular.VotoJugador == OpcionA)
                    {
                        MiResultado.CantVotosOpcionA = dtSigueTrue.Rows.Count;
                        MiResultado.CantVotosOpcionB = dtSigueFalse.Rows.Count;
                    }
                    else
                    {
                        MiResultado.CantVotosOpcionB = dtSigueTrue.Rows.Count;
                        MiResultado.CantVotosOpcionA = dtSigueFalse.Rows.Count;
                    }
                }
                else
                {
                    MiResultado.Gano = false;
                    select = "SELECT CantJugadores FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString() ;
                    dt = DBHelper.EjecutarSelect(select);
                    row = dt.Rows[0];
                    int CantJugadores = row.Field<int>("CantJugadores");
                    if(CantJugadores==0)
                    {
                        MiResultado.Empate = true;
                        MiResultado.CantVotosOpcionA = dtSigueFalse.Rows.Count;
                        MiResultado.CantVotosOpcionB = dtSigueFalse.Rows.Count;
                    }
                    else
                    {
                        MiResultado.Empate = false;
                        select = "SELECT * FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda.ToString() + " AND RespuestaFinal='" + OpcionA +"'";
                        dt = DBHelper.EjecutarSelect(select);
                        CantVotosOpcionA = dt.Rows.Count;
                        if(CantVotosOpcionA==dtSigueTrue.Rows.Count)
                        {
                            MiResultado.CantVotosOpcionA = dtSigueTrue.Rows.Count;
                            MiResultado.CantVotosOpcionB = dtSigueFalse.Rows.Count;
                            MiResultado.MayoriaOpcionA = false;
                        }
                        else
                        {
                            MiResultado.CantVotosOpcionB = dtSigueTrue.Rows.Count;
                            MiResultado.CantVotosOpcionA = dtSigueFalse.Rows.Count;
                            MiResultado.MayoriaOpcionA = true;
                        }
                    }
                }

            }
            select = "SELECT * FROM usuariosxsala WHERE SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
            dt = DBHelper.EjecutarSelect(select);
            int CantJugadoresQueVotaron = dt.Rows.Count;
            select = "SELECT CantCheckeoResultados FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString();
            dt = DBHelper.EjecutarSelect(select);
            row = dt.Rows[0];
            int CantJugadoresQueCheckearonSuVoto = row.Field<int>("CantCheckeoResultados");
            if (CantJugadoresQueVotaron == CantJugadoresQueCheckearonSuVoto)
            {
                select = "SELECT CantJugadores FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                dt = DBHelper.EjecutarSelect(select);
                row = dt.Rows[0];
                int CantJugadoresQueQuedanEnSala = row.Field<int>("CantJugadores");
                if (CantJugadoresQueQuedanEnSala < 3)
                {
                    string delete = "DELETE FROM usuariosxsala WHERE SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                    DBHelper.EjecutarIUD(delete);
                    delete = "DELETE FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString();
                    DBHelper.EjecutarIUD(delete);
                    update = "UPDATE salasdejuegos SET CantCheckeoResultados=0 WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                    DBHelper.EjecutarIUD(update);
                }
            }
            return MiResultado;
        }
        public static void DeleteRespuestasSala(int IdSala)
        {
            string delete = "delete from respuestas where Sala=" + IdSala.ToString();
            DBHelper.EjecutarIUD(delete);
        }

        private static Respuesta ObtenerPorRowRespuesta(DataRow row)
        {
            Respuesta MiRespuesta = new Respuesta();
            MiRespuesta.Id = row.Field<int>("Id");
            MiRespuesta.RespuestaFinal = row.Field<string>("RespuestaFinal");
            MiRespuesta.RespuestaParcial = row.Field<string>("RespuestaTemporal");
            MiRespuesta.Pregunta = row.Field<int>("Pregunta");
            MiRespuesta.Sala = row.Field<int>("Sala");
            MiRespuesta.Usuario = row.Field<int>("Usuario");
            MiRespuesta.NRonda = row.Field<int>("NRonda");
            return MiRespuesta;
        }

    }
}
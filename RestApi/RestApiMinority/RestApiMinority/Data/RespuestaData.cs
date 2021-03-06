﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RestApiMinority.Models;
using System.Data;
using MySql.Data.MySqlClient;

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
            string OpcionA = "";
            string OpcionB = "";
            int NuevoNRonda = MiVotoACalcular.NRonda + 1;
            bool PidioOpcionesPregunta = false;
            string update = "UPDATE salasdejuegos SET CantCheckeoResultados=CantCheckeoResultados+1 WHERE Id=" + MiVotoACalcular.IdSala.ToString();
            DBHelper.EjecutarIUD(update);
            string select = "SELECT TerminoRonda FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            DataRow row = dt.Rows[0];
            bool TerminoRonda = row.Field<bool>("TerminoRonda");
            if (TerminoRonda == false)
            {
                update = "UPDATE salasdejuegos SET TerminoRonda=true WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                DBHelper.EjecutarIUD(update);
                select = "SELECT CantJugadores FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                DataTable dtCantJugadores = DBHelper.EjecutarSelect(select);
                row = dtCantJugadores.Rows[0];
                int CantJugadores = row.Field<int>("CantJugadores");
                select = "SELECT * FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda.ToString();
                DataTable dtCantRespuestas = DBHelper.EjecutarSelect(select);
                int CantRespuestas = dtCantRespuestas.Rows.Count;
                while (CantJugadores != CantRespuestas)
                {
                    select = "SELECT CantJugadores FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                    dtCantJugadores = DBHelper.EjecutarSelect(select);
                    row = dtCantJugadores.Rows[0];
                    CantJugadores = row.Field<int>("CantJugadores");
                    select = "SELECT * FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda.ToString();
                    dtCantRespuestas = DBHelper.EjecutarSelect(select);
                    CantRespuestas = dtCantRespuestas.Rows.Count;
                }
                foreach (DataRow Registro in dtCantRespuestas.Rows)
                {
                    MiRespuesta = ObtenerPorRowRespuesta(Registro);
                    if (PidioOpcionesPregunta == false)
                    {
                        /*MySqlCommand cmd;
                        cmd = new MySqlCommand("ObtenerResultadosSinTerminoRonda", new MySqlConnection(DBHelper.ConnectionString));
                        cmd.CommandType = CommandType.StoredProcedure;
                        cmd.Parameters.Add(new MySqlParameter("SalaParam", MiVotoACalcular.IdSala));
                        cmd.Parameters.Add(new MySqlParameter("NRondaParam", MiVotoACalcular.NRonda));
                        cmd.Parameters.Add(new MySqlParameter("UsuarioParam", MiVotoACalcular.IdUsuario));
                        cmd.Connection.Open();
                        MySqlDataReader dr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                        while (dr.Read())
                        {*/
                            select = "SELECT * FROM respuestas WHERE Sala="+ MiVotoACalcular.IdSala.ToString()+" AND NRonda="+MiVotoACalcular.NRonda+ " AND RespuestaFinal=(SELECT OpcionB FROM preguntas WHERE Id=(SELECT Pregunta FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda+" LIMIT 1))";
                            dt = DBHelper.EjecutarSelect(select);
                            MiResultado.CantVotosOpcionB = dt.Rows.Count;
                            select = "SELECT * FROM respuestas WHERE Sala="+ MiVotoACalcular.IdSala.ToString()+" AND NRonda="+MiVotoACalcular.NRonda+ " AND RespuestaFinal=(SELECT OpcionA FROM preguntas WHERE Id=(SELECT Pregunta FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda+" LIMIT 1))";
                            dt = DBHelper.EjecutarSelect(select);
                            MiResultado.CantVotosOpcionA = dt.Rows.Count;
                            select = "SELECT OpcionA FROM preguntas WHERE Id=(SELECT Pregunta FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda+" LIMIT 1)";
                            dt = DBHelper.EjecutarSelect(select);
                            row=dt.Rows[0];
                            OpcionA = row.Field<string>("OpcionA");
                            select = "SELECT OpcionB FROM preguntas WHERE Id=(SELECT Pregunta FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda+" LIMIT 1)";
                            dt = DBHelper.EjecutarSelect(select);
                            row = dt.Rows[0];
                            OpcionB = row.Field<string>("OpcionB");
                            CalculandoMinoria(ref MiResultado, NuevoNRonda, TerminoRonda, MiVotoACalcular.IdSala);
                        /*MiResultado.CantVotosOpcionB = dt.Rows.Count;
                      MiResultado.CantVotosOpcionA = Convert.ToInt32(dr["CantVotosOpcionA"]);
                      MiResultado.CantVotosOpcionB = Convert.ToInt32(dr["CantVotosOpcionB"]);
                      OpcionB = Convert.ToString(dr["OpcionB"]);
                      OpcionA = Convert.ToString(dr["OpcionA"]);

                      }*/

                        /*dr.Close();
                        cmd.Connection.Close();

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
                                update = "UPDATE salasdejuegos SET Empate=true  WHERE Id=" + MiVotoACalcular.IdSala.ToString();
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
                        }*/
                    }
                    GanoOPerdio(MiResultado.MayoriaOpcionA, OpcionB, MiRespuesta, OpcionA, MiVotoACalcular.IdSala,MiResultado.Empate);
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
                /*select = "SELECT Sigue FROM usuariosxsala WHERE SalaDeJuego=" + MiVotoACalcular.IdSala.ToString() + " AND Usuario="+MiVotoACalcular.IdUsuario.ToString();
                dt = DBHelper.EjecutarSelect(select);
                row = dt.Rows[0];
                select = "SELECT * FROM usuariosxsala WHERE Sigue=false AND VotoEnBlanco=false AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                DataTable dtSigueFalse = DBHelper.EjecutarSelect(select);
                select = "SELECT * FROM usuariosxsala WHERE Sigue=true AND SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                DataTable dtSigueTrue = DBHelper.EjecutarSelect(select);
                bool Sigue = row.Field<bool>("Sigue");
                MySqlCommand cmd;
                cmd = new MySqlCommand("ObtenerResultadosConTerminoRonda", new MySqlConnection(DBHelper.ConnectionString));
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Parameters.Add(new MySqlParameter("SalaParam", MiVotoACalcular.IdSala));
                cmd.Parameters.Add(new MySqlParameter("NRondaParam", MiVotoACalcular.NRonda));
                cmd.Parameters.Add(new MySqlParameter("UsuarioParam", MiVotoACalcular.IdUsuario));
                cmd.Connection.Open();*/
                int CantJugadoresQueVotaron = 0;
                int CantJugadoresQueCheckearonSuVoto = 0;
                int CantJugadoresQueQuedanEnSala = 0;
                /* MySqlDataReader dr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                 while (dr.Read())
                 {
                     select = "SELECT * FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda + " AND RespuestaFinal=(SELECT OpcionB FROM preguntas WHERE Id=(SELECT Pregunta FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda + "))";
                     dt = DBHelper.EjecutarSelect(select);
                     MiResultado.CantVotosOpcionB = dt.Rows.Count;*/
                    select = "SELECT * FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda + " AND RespuestaFinal=(SELECT OpcionB FROM preguntas WHERE Id=(SELECT Pregunta FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda + " LIMIT 1))";
                    dt = DBHelper.EjecutarSelect(select);
                    MiResultado.CantVotosOpcionB = dt.Rows.Count;
                    select = "SELECT * FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda + " AND RespuestaFinal=(SELECT OpcionA FROM preguntas WHERE Id=(SELECT Pregunta FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString() + " AND NRonda=" + MiVotoACalcular.NRonda + " LIMIT 1))";
                    dt = DBHelper.EjecutarSelect(select);
                    MiResultado.CantVotosOpcionA = dt.Rows.Count;
                    select = "SELECT * FROM usuariosxsala where SalaDeJuego="+MiVotoACalcular.IdSala.ToString();
                    dt = DBHelper.EjecutarSelect(select);
                    CantJugadoresQueVotaron = dt.Rows.Count;
                    select = "SELECT CantCheckeoResultados FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                    dt = DBHelper.EjecutarSelect(select);
                    row = dt.Rows[0];
                    CantJugadoresQueCheckearonSuVoto = row.Field<int>("CantCheckeoResultados");
                    select = "SELECT CantJugadores FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                    dt = DBHelper.EjecutarSelect(select);
                    row = dt.Rows[0];
                    CantJugadoresQueQuedanEnSala = row.Field<int>("CantJugadores");
                    select = "SELECT Sigue FROM usuariosxsala WHERE SalaDeJuego="+MiVotoACalcular.IdSala.ToString()+" AND Usuario=" + MiVotoACalcular.IdUsuario.ToString();
                    dt = DBHelper.EjecutarSelect(select);
                    row = dt.Rows[0];
                    MiResultado.Gano = row.Field<bool>("Sigue");
                /*MiResultado.CantVotosOpcionA = Convert.ToInt32(dr["CantVotosOpcionA"]);
                MiResultado.CantVotosOpcionB = Convert.ToInt32(dr["CantVotosOpcionB"]);
                CantJugadoresQueVotaron = Convert.ToInt32(dr["CantJugadoresQueVotaron"]);
                CantJugadoresQueCheckearonSuVoto = Convert.ToInt32(dr["CantCheckeoResultados"]);
                CantJugadoresQueQuedanEnSala = Convert.ToInt32(dr["CantJugadores"]);
                MiResultado.Gano = Convert.ToBoolean(dr["Sigue"]);

                }*/
                CalculandoMinoria(ref MiResultado, NuevoNRonda, TerminoRonda, MiVotoACalcular.IdSala);
                /*dr.Close();
                cmd.Connection.Close();
                select = "SELECT * FROM usuariosxsala WHERE SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                dt = DBHelper.EjecutarSelect(select);
                CantJugadoresQueVotaron = dt.Rows.Count;
                select = "SELECT CantCheckeoResultados FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                dt = DBHelper.EjecutarSelect(select);
                row = dt.Rows[0];
                CantJugadoresQueCheckearonSuVoto = row.Field<int>("CantCheckeoResultados");*/
                if (CantJugadoresQueVotaron == CantJugadoresQueCheckearonSuVoto)
                {
                    /*select = "SELECT CantJugadores FROM salasdejuegos WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                    dt = DBHelper.EjecutarSelect(select);
                    row = dt.Rows[0];
                    CantJugadoresQueQuedanEnSala = row.Field<int>("CantJugadores");*/
                    if (CantJugadoresQueQuedanEnSala < 3)
                    {
                        select = "SELECT MontoAGanar FROM `salasdejuegos` WHERE Id="+MiVotoACalcular.IdSala.ToString();
                        dt = DBHelper.EjecutarSelect(select);
                        row = dt.Rows[0];
                        int MontoAGanar = row.Field<int>("MontoAGanar");
                        select = "SELECT * FROM `usuariosxsala` WHERE Sigue=true";
                        dt = DBHelper.EjecutarSelect(select);
                        int CantGanadores = dt.Rows.Count;
                        if(MiResultado.Gano)
                        {
                            switch(CantGanadores)
                            {
                                case 1:
                                    update = "UPDATE usuarios SET Monedas=Monedas+" + MontoAGanar.ToString()+ " WHERE Id="+MiVotoACalcular.IdUsuario.ToString();
                                    DBHelper.EjecutarIUD(update);
                                    break;
                                case 2:
                                    update = "UPDATE usuarios SET Monedas=Monedas+" + (MontoAGanar/2).ToString() + " WHERE Id=" + MiVotoACalcular.IdUsuario.ToString();
                                    DBHelper.EjecutarIUD(update);
                                    break;
                            }
                        }
                        string delete = "DELETE FROM usuariosxsala WHERE SalaDeJuego=" + MiVotoACalcular.IdSala.ToString();
                        DBHelper.EjecutarIUD(delete);
                        delete = "DELETE FROM respuestas WHERE Sala=" + MiVotoACalcular.IdSala.ToString();
                        DBHelper.EjecutarIUD(delete);
                        update = "UPDATE salasdejuegos SET TerminoRonda=false, CantCheckeoResultados=0 WHERE Id=" + MiVotoACalcular.IdSala.ToString();
                        DBHelper.EjecutarIUD(update);
                    }
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

        private static void GanoOPerdio( bool  MayoriaOpcionA, string OpcionB, Respuesta MiRespuesta,string OpcionA,int SalaDeJuego, bool Empate)
        {
            string update = "";
            if (MayoriaOpcionA)
            {
                if (MiRespuesta.RespuestaFinal == OpcionB)
                {
                    update = "UPDATE usuariosxsala SET Sigue=true WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + SalaDeJuego;
                    DBHelper.EjecutarIUD(update);
                }
                else
                {
                    update = "UPDATE usuariosxsala SET Sigue=false WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + SalaDeJuego;
                    DBHelper.EjecutarIUD(update);
                    if (MiRespuesta.RespuestaFinal == "")
                    {
                        update = "UPDATE usuariosxsala SET VotoEnBlanco=true WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + SalaDeJuego;
                        DBHelper.EjecutarIUD(update);
                    }
                }
            }
            else
            {
                if (Empate)
                {
                    update = "UPDATE usuarios SET Monedas= Monedas+1 WHERE Id=" + MiRespuesta.Usuario.ToString() ;
                    DBHelper.EjecutarIUD(update);
                }
                else
                {
                    if (MiRespuesta.RespuestaFinal == OpcionA)
                    {
                        update = "UPDATE usuariosxsala SET Sigue=true WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + SalaDeJuego;
                        DBHelper.EjecutarIUD(update);
                    }
                    else
                    {
                        update = "UPDATE usuariosxsala SET Sigue=false WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + SalaDeJuego;
                        DBHelper.EjecutarIUD(update);
                        if (MiRespuesta.RespuestaFinal == "")
                        {
                            update = "UPDATE usuariosxsala SET VotoEnBlanco=true WHERE Usuario=" + MiRespuesta.Usuario.ToString() + " AND SalaDeJuego=" + SalaDeJuego;
                            DBHelper.EjecutarIUD(update);
                        }
                    }
                }
                
            }
        }

        private static void CalculandoMinoria(ref Resultado MiResultado, int NuevoNRonda, bool TerminoRonda, int SalaDeJuego)
        {
            string update = "";
            if (MiResultado.CantVotosOpcionA != MiResultado.CantVotosOpcionB)
            {
                MiResultado.Empate = false;
                if (MiResultado.CantVotosOpcionA > MiResultado.CantVotosOpcionB)
                {
                    if (TerminoRonda == false)
                    {
                        update = "UPDATE salasdejuegos SET CantJugadores=" + MiResultado.CantVotosOpcionB + ", NRonda=" + NuevoNRonda.ToString() + " WHERE Id=" + SalaDeJuego;
                        DBHelper.EjecutarIUD(update);
                    }
                    MiResultado.MayoriaOpcionA = true;
                }
                else
                {
                    if (TerminoRonda == false)
                    {
                        update = "UPDATE salasdejuegos SET CantJugadores=" + MiResultado.CantVotosOpcionA + ", NRonda=" + NuevoNRonda.ToString() + " WHERE Id=" + SalaDeJuego;
                        DBHelper.EjecutarIUD(update);
                    }
                    MiResultado.MayoriaOpcionA = false;
                }
            }
            else
            {

                MiResultado.Empate = true;
                update = "UPDATE usuariosxsala SET Sigue=false WHERE SalaDeJuego=" + SalaDeJuego;
                DBHelper.EjecutarIUD(update);
            }
        }


    }
}
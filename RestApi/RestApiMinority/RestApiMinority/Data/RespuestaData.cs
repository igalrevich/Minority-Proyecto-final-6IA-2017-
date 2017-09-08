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
            string sInsert = "Insert into respuestas (Pregunta,Usuario,RespuestaFinal,RespuestaTemporal,Sala) values ('" + MiRespuesta.Pregunta + "','" + MiRespuesta.Usuario + "','" + MiRespuesta.RespuestaFinal + "','" + MiRespuesta.RespuestaParcial + "','" + MiRespuesta.Sala + "')";
            DBHelper.EjecutarIUD(sInsert);
        }
        public static int ObtenerCantVotos(string Opcion, int Sala)
        {
            string select = "select * from respuestas where RespuestaFinal=\"" + Opcion+ "\" and Sala="+Sala.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            int CantVotos = dt.Rows.Count;
            return CantVotos;
        }

        public static void CalcularVotos(int IdSala, int NRonda)
        {
            Respuesta MiRespuesta;
            string OpcionA = "";
            string OpcionB = "";
            bool PidioOpcionesPregunta = false;
            int CantVotosOpcionA=0, CantVotosOpcionB = 0;
            string select = "SELECT TerminoRonda FROM salasdejuegos WHERE Id="+IdSala.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            DataRow row = dt.Rows[0];
            bool TerminoRonda= row.Field<bool>("TerminoRonda");
            if(TerminoRonda==false)
            {
                string update= "UPDATE salasdejuegos SET TerminoRonda=true WHERE Id="+ IdSala.ToString();
                DBHelper.EjecutarIUD(update);
                select = "SELECT * FROM respuestas WHERE Sala=" + IdSala.ToString() + " AND NRonda=" + NRonda.ToString();
                dt = DBHelper.EjecutarSelect(select);
                foreach (DataRow Registro in dt.Rows)
                {
                    MiRespuesta = ObtenerPorRowRespuesta(Registro);
                    if(PidioOpcionesPregunta==false)
                    {
                        select = "SELECT OpcionA,OpcionB FROM preguntas WHERE Id=" + MiRespuesta.Pregunta.ToString();
                        dt = DBHelper.EjecutarSelect(select);
                        row = dt.Rows[0];
                        OpcionA = row.Field<string>("OpcionA");
                        OpcionB = row.Field<string>("OpcionB");
                        select = "SELECT * FROM respuestas WHERE Sala=" + IdSala.ToString() + " AND NRonda=" + NRonda.ToString()+ " AND RespuestaFinal="+OpcionA ;
                        dt = DBHelper.EjecutarSelect(select);
                        CantVotosOpcionA = dt.Rows.Count;
                        select = "SELECT * FROM respuestas WHERE Sala=" + IdSala.ToString() + " AND NRonda=" + NRonda.ToString() + " AND RespuestaFinal=" + OpcionB;
                        dt = DBHelper.EjecutarSelect(select);
                        CantVotosOpcionB = dt.Rows.Count;
                        if(CantVotosOpcionA!=CantVotosOpcionB)
                        {

                        }
                    }
                }

            }
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
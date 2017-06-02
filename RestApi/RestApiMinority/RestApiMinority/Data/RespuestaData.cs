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
        public static int ObtenerCantVotos(string Opcion)
        {
            string select = "select * from respuestas where RespuestaFinal=\"" + Opcion+ "\"";
            DataTable dt = DBHelper.EjecutarSelect(select);
            int CantVotos = dt.Rows.Count;
            return CantVotos;
        }
        public static void DeleteRespuestasSala(int IdSala)
        {
            string delete = "delete from respuestas where Sala=" + IdSala.ToString();
            DBHelper.EjecutarIUD(delete);
        }
    }
}
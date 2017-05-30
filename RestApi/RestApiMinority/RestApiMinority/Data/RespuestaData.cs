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
    }
}
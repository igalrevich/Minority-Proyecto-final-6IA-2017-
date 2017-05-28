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
            string sInsert = "Insert into personas (Nombre,FechaNac) values ('" + persona.Nombre + "','" + persona.FechaNac.ToString("yyyy-MM-dd HH:mm") + "')";
            DBHelper.EjecutarIUD(sInsert);
        }
    }
}
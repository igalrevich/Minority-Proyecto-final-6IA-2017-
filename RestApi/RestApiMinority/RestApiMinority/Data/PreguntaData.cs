using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RestApiMinority.Models;
using System.Data;
using MySql.Data.MySqlClient;

namespace RestApiMinority.Data
{
    public class PreguntaData
    {
        public static Pregunta ObtenerPreguntaPorId(int id)
        {
            string select = "select * from preguntas where id=" + id.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            Pregunta MiPregunta;
            if (dt.Rows.Count > 0)
            {
                MiPregunta = ObtenerPorRow(dt.Rows[0]);
                return MiPregunta;
            }
            return null;
        }

        private static Pregunta ObtenerPorRow(DataRow row)
        {
            Pregunta MiPregunta = new Pregunta();
            MiPregunta.Id = row.Field<int>("Id");
            MiPregunta.OpcionA = row.Field<string>("OpcionA");
            MiPregunta.OpcionB = row.Field<string>("OpcionB");
            MiPregunta.Categoria = row.Field<int>("Categoria");
            return MiPregunta;
        }
    }
}
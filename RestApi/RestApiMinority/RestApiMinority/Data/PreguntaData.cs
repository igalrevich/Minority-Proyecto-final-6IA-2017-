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
            string select = "SELECT * FROM `preguntas` WHERE Id=(SELECT PreguntaPA FROM preguntasactivas WHERE CategoriaPA=" + id.ToString()+")";
            DataTable dt = DBHelper.EjecutarSelect(select);
            Pregunta MiPregunta;
            if (dt.Rows.Count > 0)
            {
                MiPregunta = ObtenerPorRow(dt.Rows[0]);
                return MiPregunta;
            }
            return null;
        }

        public static Pregunta ObtenerPreguntaPorIdPreguntasPorJuego(int id,int NRondaCategoria)
        {
            string select = "SELECT * FROM `preguntas` WHERE Id=(SELECT IdPregunta FROM preguntasxjuego WHERE IdSala=" + id.ToString() + " AND IdRonda="+NRondaCategoria.ToString()+")";
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

        public static void InsertarPreguntas(int idSala)
        {
            MySqlCommand cmd = new MySqlCommand("InsertarPreguntasSala", new MySqlConnection(DBHelper.ConnectionString));

            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.Add(new MySqlParameter("IdSalaParam", idSala));

            cmd.Connection.Open();

            cmd.ExecuteNonQuery();

            cmd.Connection.Close();
        }
    }
}
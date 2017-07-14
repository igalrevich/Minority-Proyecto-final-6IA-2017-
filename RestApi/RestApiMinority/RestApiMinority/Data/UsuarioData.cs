using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RestApiMinority.Models;
using System.Data;
using MySql.Data.MySqlClient;

namespace RestApiMinority.Data
{
    public class UsuarioData
    {
        public static void Delete(int id)
        {
            string delete = "delete from usuarios where id=" + id.ToString();
            DBHelper.EjecutarIUD(delete);
        }

        public static void ModificarMonedasUsuario(int IdUsuario, int Monedas)
        {
            string update = "UPDATE FROM usuarios SET Monedas=" + Monedas + " WHERE Id=" + IdUsuario.ToString();
            DBHelper.EjecutarIUD(update);
        }
       public static void ModificarMonedasYSalasUsuario(int IdUsuario, Usuario MiUsuario)
        {
            MySqlCommand cmd = new MySqlCommand("ActualizarUsuarios", new MySqlConnection(DBHelper.ConnectionString));

            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.Add(new MySqlParameter("IdUsuario", IdUsuario));

            cmd.Parameters.Add(new MySqlParameter("MonedasParam", MiUsuario.Monedas));

            cmd.Parameters.Add(new MySqlParameter("IdSalaDeJuego", MiUsuario.SalaDeJuego));

            cmd.Connection.Open();

            cmd.ExecuteNonQuery();

            cmd.Connection.Close();
        }

        public static Usuario ObtenerPorMailYPassword(string Mail, string Password)
        {
            MySqlCommand cmd = new MySqlCommand("ValidarUsuario", new MySqlConnection(DBHelper.ConnectionString));
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add(new MySqlParameter("MailUsuario", Mail));
            cmd.Parameters.Add(new MySqlParameter("PasswordUsuario", Password));
            cmd.Connection.Open();
            MySqlDataReader dr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
            Usuario MiUsuario=new Usuario();
            int ContRows = 0;
            while (dr.Read())
            {
                ContRows++;

                MiUsuario.Id = Convert.ToInt32(dr["Id"]);

                MiUsuario.Nombre = Convert.ToString(dr["Nombre"]);

                MiUsuario.Mail = Convert.ToString(dr["Mail"]);

                MiUsuario.Password = Convert.ToString(dr["password"]);

                MiUsuario.Monedas = Convert.ToInt32(dr["Monedas"]);
            }
            dr.Close();

            if (ContRows == 0)
            {
                MiUsuario.LlenarDatosCon0();
            }
            return MiUsuario;
        }

        private static Usuario ObtenerPorRow(DataRow row)
        {
            Usuario MiUsuario = new Usuario();
            MiUsuario.Id = row.Field<int>("Id");
            MiUsuario.Nombre = row.Field<string>("Nombre");
            MiUsuario.Mail = row.Field<string>("Mail");
            MiUsuario.Password = row.Field<string>("password");
            MiUsuario.Monedas = row.Field<int>("Monedas");
            return MiUsuario;
        }

         public static int ObtenerIdPorNombre(string tabla,string Nombre)
        {
            string Campo = "Nombre";
            if(tabla=="preguntas")
            {
                Campo = "OpcionA";
            }
            string select = "select Id from "+tabla+ " where "+Campo+"=\"" + Nombre + "\"";
            DataTable dt = DBHelper.EjecutarSelect(select);
            if (dt.Rows.Count > 0)
            {
                int Id = ObtenerIdPorNombrePorRow(dt.Rows[0]);
                return Id;
            }
            return 0;
        }

         private static int ObtenerIdPorNombrePorRow(DataRow row)
        {
            int Id= row.Field<int>("Id");
            return Id;
        }

     }
}

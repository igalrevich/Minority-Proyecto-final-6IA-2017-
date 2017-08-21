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

        public static void IngresarUserSala(int IdUsuario, string NombreSala)
        {
            MySqlCommand cmd;
            cmd = new MySqlCommand("ObtenerIdSala", new MySqlConnection(DBHelper.ConnectionString));
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add(new MySqlParameter("NombreSala", NombreSala));
            cmd.Connection.Open();
            MySqlDataReader dr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
            int IdSala = 0;
            int CantJugadoresSala = 0;
            int MonedasUsuario = 0;
            bool ExisteUsuarioEnSala;
            while (dr.Read())
            {
              IdSala = Convert.ToInt32(dr["Id"]);
            }
            dr.Close();
            cmd.Connection.Close();

            cmd = new MySqlCommand("IngresarUserSala", new MySqlConnection(DBHelper.ConnectionString));
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add(new MySqlParameter("IdUsuario", IdUsuario));
            cmd.Parameters.Add(new MySqlParameter("IdSala", IdSala));
            cmd.Connection.Open();
            cmd.ExecuteNonQuery();
            cmd.Connection.Close();

            string select;
            DataTable dt;

            select = "SELECT CantJugadores FROM salasdejuegos WHERE Id="+IdSala.ToString();
            dt = DBHelper.EjecutarSelect(select);
            if (dt.Rows.Count > 0)
            {
                CantJugadoresSala = dt.Rows[0].Field<int>("CantJugadores");
            }
            select = "SELECT Monedas FROM usuarios WHERE Id=" + IdUsuario.ToString();
            dt = DBHelper.EjecutarSelect(select);
            if (dt.Rows.Count > 0)
            {
                MonedasUsuario= dt.Rows[0].Field<int>("Monedas");
            }

            select = "SELECT Id FROM `usuariosxsala` WHERE Usuario=" + IdUsuario.ToString()+ "  AND SalaDeJuego="+IdSala.ToString();
            dt = DBHelper.EjecutarSelect(select);
            if (dt.Rows.Count > 0)
            {
                ExisteUsuarioEnSala = true;
            }
            else
            {
                ExisteUsuarioEnSala = false;
            }

            if (MonedasUsuario > 0 && CantJugadoresSala < 50 && ExisteUsuarioEnSala == false)
            {
                string query = "INSERT INTO `usuariosxsala`(`Usuario`, `SalaDeJuego`) VALUES (" + IdUsuario.ToString() + "," + IdSala.ToString() + ")";
                DBHelper.EjecutarIUD(query);
            }

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

                MiUsuario.Monedas = Convert.ToInt32(dr["Monedas"]);
            }
            dr.Close();

            if (ContRows == 0)
            {
                MiUsuario.LlenarDatosCon0();
            }
            cmd.Connection.Close();
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

        public static int ValidarQueUsuarioEntroEnSala(int IdUsuario, int IdSala)
        {
            string select = "SELECT * FROM `usuariosxsala` WHERE Usuario="+IdUsuario.ToString()+" AND SalaDeJuego="+IdSala.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            if (dt.Rows.Count > 0)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }

    }
}

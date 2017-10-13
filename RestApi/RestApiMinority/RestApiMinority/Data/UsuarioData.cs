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

        public static string AgregarUsuario(Usuario MiUsuario)
        {
            string MensajeARetornar = "";
            string select = "SELECT * FROM usuarios WHERE Mail="+ MiUsuario.Mail;
            DataTable dtMail = DBHelper.EjecutarSelect(select);
            select = "SELECT * FROM usuarios WHERE Nombre=" + MiUsuario.Nombre;
            DataTable dtNombre = DBHelper.EjecutarSelect(select);
            if(dtMail.Rows.Count>0 || dtNombre.Rows.Count>0)
            {
                MensajeARetornar = "El mail y/o nombre de usuario ya estan en uso";
            }
            else
            {
                MensajeARetornar = "Se ha registrado con exito";
                string sInsert = "INSERT INTO `usuarios`(`Nombre`, `Mail`, `password`, `Monedas`) VALUES ('"+MiUsuario.Nombre+"','"+MiUsuario.Mail+ "','PASSWORD('" +MiUsuario.Password+"'),"+MiUsuario.Monedas+")";
                DBHelper.EjecutarIUD(sInsert);
            }
            return MensajeARetornar;

        }

        public static string IngresarUserSala(int IdUsuario, int IdSala)
        {
            string MensajeARetornar = "";
            MySqlCommand cmd;
            /*cmd = new MySqlCommand("ObtenerIdSala", new MySqlConnection(DBHelper.ConnectionString));
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add(new MySqlParameter("NombreSala", NombreSala));
            cmd.Connection.Open();
            MySqlDataReader dr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
            int IdSala = 0;*/
            int CantJugadoresSala = 0;
            int MonedasUsuario = 0;
            bool ExisteUsuarioEnSala=false;
            int UsuarioUXS = 0;
            /*while (dr.Read())
            {
              IdSala = Convert.ToInt32(dr["Id"]);
            }
            dr.Close();
            cmd.Connection.Close();*/


            string select = "SELECT Monedas FROM usuarios WHERE Id=" + IdUsuario.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            DataRow row = dt.Rows[0];
            MonedasUsuario = row.Field<int>("Monedas");
            select = "SELECT CantJugadores FROM salasdejuegos WHERE Id=" + IdSala.ToString();
            dt = DBHelper.EjecutarSelect(select);
            row = dt.Rows[0];
            CantJugadoresSala = row.Field<int>("CantJugadores");
            select = "SELECT Usuario FROM usuariosxsala WHERE Usuario=" + IdUsuario.ToString()+" AND SalaDeJuego="+IdSala.ToString();
            dt = DBHelper.EjecutarSelect(select);
            try
            {
                row = dt.Rows[0];
                UsuarioUXS = row.Field<int>("Usuario");
                ExisteUsuarioEnSala = true;
            }
            catch
            {
                ExisteUsuarioEnSala = false;
            }
            /*cmd = new MySqlCommand("ObtenerDatosEntradaUserSala", new MySqlConnection(DBHelper.ConnectionString));
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add(new MySqlParameter("IdUsuario", IdUsuario));
            cmd.Parameters.Add(new MySqlParameter("IdSala", IdSala));
            cmd.Connection.Open();
            MySqlDataReader datareader = cmd.ExecuteReader(CommandBehavior.CloseConnection);
            while (datareader.Read())
            {
                MonedasUsuario = Convert.ToInt32(datareader["Monedas"]);
                CantJugadoresSala = Convert.ToInt32(datareader["CantJugadores"]);
                try
                {
                    UsuarioUXS = Convert.ToInt32(datareader["Usuario"]);
                    ExisteUsuarioEnSala = true;
                }
                catch
                {
                    ExisteUsuarioEnSala = false;
                } 
            }
            datareader.Close();
            cmd.Connection.Close();*/



            /*select = "SELECT CantJugadores FROM salasdejuegos WHERE Id="+IdSala.ToString();
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
            }*/

            if (MonedasUsuario > 0 && CantJugadoresSala < 50 && ExisteUsuarioEnSala == false)
            {
                cmd = new MySqlCommand("IngresarUserSala", new MySqlConnection(DBHelper.ConnectionString));
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Parameters.Add(new MySqlParameter("IdUsuario", IdUsuario));
                cmd.Parameters.Add(new MySqlParameter("IdSala", IdSala));
                cmd.Connection.Open();
                cmd.ExecuteNonQuery();
                cmd.Connection.Close();
                MensajeARetornar="Ingreso a sala restando moneda";
            }
            else
            {
                if(MonedasUsuario<=0)
                {
                    MensajeARetornar = "No tiene suficientes monedas para entrar a la sala";
                }
                else
                {
                    if (CantJugadoresSala >= 50)
                    {
                        MensajeARetornar = "La cantidad de jugadores esta al maximo en la sala";
                    }
                    else
                    {
                        MensajeARetornar = "Ingreso a sala";
                    }
                }
            }

            return MensajeARetornar;

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

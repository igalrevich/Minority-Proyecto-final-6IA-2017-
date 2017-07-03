using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RestApiMinority.Models;
using System.Data;
using MySql.Data.MySqlClient;

namespace RestApiMinority.Data
{
    public class SalaDeJuegoData
    {
        public static void ModificarDisponibilidadSala(int IdSala, bool Estado)
        {
            string update = "";
            if (Estado)
            {
                string delete = "DELETE FROM usuariosxsala";
                DBHelper.EjecutarIUD(delete);
                update = "update salasdejuegos set Disponible=" + Estado.ToString() + "HoraComienzo= TIMESTAMPADD(MINUTE,2,TIME(CONVERT_TZ(LOCALTIME,'+00:00','-03:00')) ) where Id=" + IdSala.ToString();
            }
            else
            {
                update = "update salasdejuegos set Disponible=" + Estado.ToString() + "HoraComienzo= TIMESTAMPADD(MINUTE,4,TIME(CONVERT_TZ(LOCALTIME,'+00:00','-03:00')) ) where Id=" + IdSala.ToString();
            }
            DBHelper.EjecutarIUD(update);
        }

        public static void ModificarMHCSala(int IdSalaDeJuego, SalasDeJuego MiSalaDeJuego)
        {
            string update = "UPDATE salasdejuegos SET ModificarHComienzo=" + MiSalaDeJuego.ModificarHComienzo + " WHERE Id=" + IdSalaDeJuego;
            DBHelper.EjecutarIUD(update);
        }

        public static void ModificarCantJugadoresONRondaSala(int IdSalaDeJuego, SalasDeJuego MiSalaDeJuego)
        {
            string update = "";
            if (MiSalaDeJuego.NRonda == -1)
            {
                update = "UPDATE salasdejuegos SET CantJugadores=" + MiSalaDeJuego.CantJugadores + " , MontoAGanar=" + MiSalaDeJuego.MontoAGanar + " WHERE Id=" + IdSalaDeJuego;
            }
            else
            {
                update = "UPDATE salasdejuegos SET NRonda="+MiSalaDeJuego.NRonda + " WHERE Id=" + IdSalaDeJuego;
            }
            DBHelper.EjecutarIUD(update);
        }

        public static void ModificarCantJugadoresYRespuestasSala(int IdSalaDeJuego, SalasDeJuego MiSalaDeJuego)
        {
            MySqlCommand cmd = new MySqlCommand("ActualizarSalas", new MySqlConnection(DBHelper.ConnectionString));

            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.Add(new MySqlParameter("NuevaCantJugadores", MiSalaDeJuego.CantJugadores));

            cmd.Parameters.Add(new MySqlParameter("NuevoNRonda", MiSalaDeJuego.NRonda));

            cmd.Parameters.Add(new MySqlParameter("IdSala", IdSalaDeJuego));

            cmd.Connection.Open();

            cmd.ExecuteNonQuery();

            cmd.Connection.Close();
        }


        public static SalasDeJuego ObtenerPorIdSalaDeJuego(int id)
        {
            bool CantSalasMayor0 = false;
            MySqlCommand cmd = new MySqlCommand("TraerSalaPorId", new MySqlConnection(DBHelper.ConnectionString));
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add(new MySqlParameter("IdSala", id));
            cmd.Connection.Open();
            MySqlDataReader dr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
            SalasDeJuego MiSalaDeJuego = new SalasDeJuego(); ;
            while (dr.Read())

            {
                CantSalasMayor0 = true;

                MiSalaDeJuego.Id = Convert.ToInt32(dr["Id"]);

                MiSalaDeJuego.Nombre = Convert.ToString(dr["Nombre"]);

                MiSalaDeJuego.CantJugadores = Convert.ToInt32(dr["CantJugadores"]);

                MiSalaDeJuego.MontoAGanar = Convert.ToInt32(dr["MontoAGanar"]);

                MiSalaDeJuego.NRonda = Convert.ToInt32(dr["NRonda"]);

                MiSalaDeJuego.Disponible = Convert.ToBoolean(dr["Disponible"]);

                MiSalaDeJuego.HoraComienzo = Convert.ToString(dr["HoraComienzo"]);

                MiSalaDeJuego.ModificarHComienzo = Convert.ToBoolean(dr["ModificarHComienzo"]);

            }

            dr.Close();

            if (CantSalasMayor0 == false)
            {
                MiSalaDeJuego = null;
            }

            return MiSalaDeJuego;
        }
           
        public static List<SalasDeJuego> ObtenerSalasDeJuego()
        {
            string select = "select Id, Nombre, CantJugadores, MontoAGanar, Disponible, NRonda from salasdejuegos order by Nombre";
            DataTable dt = DBHelper.EjecutarSelect(select);
            List<SalasDeJuego> ListaSalasDeJuego = new List<SalasDeJuego>();
            SalasDeJuego MiSalaDeJuego;
            if (dt.Rows.Count > 0)
            {
                foreach (DataRow row in dt.Rows)
                {
                    MiSalaDeJuego = ObtenerPorRowSalaDeJuego(row);
                    ListaSalasDeJuego.Add(MiSalaDeJuego);
                }
                MiSalaDeJuego = ObtenerPorRowSalaDeJuego(dt.Rows[0]);
            }
            return ListaSalasDeJuego;
        }

        public static int ObtenerCantJugadoresSalaDeJuego(int IdSalaDeJuego)
        {
            string select = "select CantJugadores from salasdejuegos WHERE Id="+IdSalaDeJuego.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            int CantJugadoresSalaDeJuego = 0;
            if (dt.Rows.Count > 0)
            {
                foreach (DataRow row in dt.Rows)
                {
                    CantJugadoresSalaDeJuego = row.Field<int>("CantJugadores");
                }
                
            }
            return CantJugadoresSalaDeJuego;
        }

        public static List<SalasDeJuego> ObtenerSalasDeJuegoConHoraComienzo()
        {
            MySqlCommand cmd = new MySqlCommand("TraerSalas", new MySqlConnection(DBHelper.ConnectionString));
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Connection.Open();
            MySqlDataReader dr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
            List<SalasDeJuego> ListaSalasDeJuego = new List<SalasDeJuego>();
            SalasDeJuego MiSalaDeJuego;
            while (dr.Read())

            {

                MiSalaDeJuego= new SalasDeJuego();

                MiSalaDeJuego.Id = Convert.ToInt32(dr["Id"]);

                MiSalaDeJuego.Nombre= Convert.ToString(dr["Nombre"]);

                MiSalaDeJuego.CantJugadores= Convert.ToInt32(dr["CantJugadores"]);

                MiSalaDeJuego.MontoAGanar = Convert.ToInt32(dr["MontoAGanar"]);

                MiSalaDeJuego.NRonda = Convert.ToInt32(dr["NRonda"]);

                MiSalaDeJuego.Disponible = Convert.ToBoolean(dr["Disponible"]);

                MiSalaDeJuego.HoraComienzo = Convert.ToString(dr["HoraComienzo"]);

                MiSalaDeJuego.ModificarHComienzo= Convert.ToBoolean(dr["ModificarHComienzo"]);

                ListaSalasDeJuego.Add(MiSalaDeJuego);

            }

            dr.Close();

            return ListaSalasDeJuego;
        }

        private static SalasDeJuego ObtenerPorRowSalaDeJuego(DataRow row)
        {
            SalasDeJuego MiSalaDeJuego = new SalasDeJuego();
            MiSalaDeJuego.Id = row.Field<int>("Id");
            MiSalaDeJuego.Nombre = row.Field<string>("Nombre");
            MiSalaDeJuego.CantJugadores = row.Field<int>("CantJugadores");
            MiSalaDeJuego.MontoAGanar = row.Field<int>("MontoAGanar");
            MiSalaDeJuego.Disponible = row.Field<bool>("Disponible");
            MiSalaDeJuego.NRonda = row.Field<int>("NRonda");
            return MiSalaDeJuego;
        }

        public static void DeleteUsuarioEnSala(int IdUsuario)
        {
            string delete = "delete from usuariosxsala where Usuario=" + IdUsuario.ToString();
            DBHelper.EjecutarIUD(delete);
        }

        public static void AnadirJugadorSalaDeJuego(int IdSalaDeJuego)
        {
            MySqlCommand cmd = new MySqlCommand("AnadirJugadorSalaDeJuego", new MySqlConnection(DBHelper.ConnectionString));

            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.Add(new MySqlParameter("IdSalaDeJuego", IdSalaDeJuego));

            cmd.Connection.Open();

            cmd.ExecuteNonQuery();

            cmd.Connection.Close();
        }

    }
}
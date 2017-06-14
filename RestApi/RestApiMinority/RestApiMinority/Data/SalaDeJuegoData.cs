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
                //string delete = "DELETE FROM usuariosxsala";
                //DBHelper.EjecutarIUD(delete);
                update = "update salasdejuegos set Disponible=" + Estado.ToString() + "HoraComienzo= localtime() where Id=" + IdSala.ToString();
            }
            else
            {
                update = "update salasdejuegos set Disponible=" + Estado.ToString() + "HoraComienzo= timestampadd(MINUTE,4,localtime()) where Id=" + IdSala.ToString();
            }
            DBHelper.EjecutarIUD(update);
        }

        public static SalasDeJuego ObtenerPorIdSalaDeJuego(int id)
        {
            string select = "select Id, Nombre, CantJugadores, MontoAGanar, Disponible, NRonda from salasdejuegos where id=" + id.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            SalasDeJuego MiSalaDeJuego;
            if (dt.Rows.Count > 0)
            {
                MiSalaDeJuego = ObtenerPorRowSalaDeJuego(dt.Rows[0]);
                return MiSalaDeJuego;
            }
            return null;
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

    }
}
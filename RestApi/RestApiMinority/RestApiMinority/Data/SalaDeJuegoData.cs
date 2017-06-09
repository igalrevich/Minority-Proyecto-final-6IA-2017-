using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RestApiMinority.Models;
using System.Data;

namespace RestApiMinority.Data
{
    public class SalaDeJuegoData
    {
        public static void ModificarDisponibilidadSala(int IdSala, bool Estado)
        {
            string update = "update salasdejuegos set Disponible=" + Estado.ToString() + " where Id=" + IdSala.ToString();
            DBHelper.EjecutarIUD(update);
        }

        public static SalasDeJuego ObtenerPorIdSalaDeJuego(int id)
        {
            string select = "select * from salasdejuegos where id=" + id.ToString();
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
            string select = "select * from salasdejuegos order by Nombre";
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

    }
}
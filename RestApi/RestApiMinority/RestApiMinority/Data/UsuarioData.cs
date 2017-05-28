using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using RestApiMinority.Models;
using System.Data;

namespace RestApiMinority.Data
{
    public class UsuarioData
    {
        public static void Delete(int id)
        {
            string delete = "delete from usuarios where id=" + id.ToString();
            DBHelper.EjecutarIUD(delete);
        }

        public static Usuario ObtenerPorId(int id)
        {
            string select = "select * from usuarios where id=" + id.ToString();
            DataTable dt = DBHelper.EjecutarSelect(select);
            Usuario MiUsuario;
            if (dt.Rows.Count > 0)
            {
                MiUsuario= ObtenerPorRow(dt.Rows[0]);
                return MiUsuario;
            }
            return null;
        }
         public static int ObtenerIdSalaDeJuego(string Nombre)
        {
            string select = "select * from salasdejuego where id=" + Nombre;
            DataTable dt = DBHelper.EjecutarSelect(select);
            if (dt.Rows.Count > 0)
            {
                int IdSalaDeJuego = ObtenerIdSalaDeJuegoPorRow(dt.Rows[0]);
                return IdSalaDeJuego;
            }
            return 0;
        }

        

        private static Usuario ObtenerPorRow(DataRow row)
        {
            Usuario MiUsuario = new Usuario();
            MiUsuario.Id = row.Field<int>("Id");
            MiUsuario.Nombre = row.Field<string>("Nombre");
            MiUsuario.Mail = row.Field<string>("Mail");
            MiUsuario.Password = row.Field<string>("password");
            MiUsuario.Monedas= row.Field<int>("Monedas");
            MiUsuario.SalasDeJuego = row.Field<string>("SalasDeJuego");
            return MiUsuario;
        }
       

         private static int ObtenerIdSalaDeJuegoPorRow(DataRow row)
        {
            int IdSalaDeJuego = row.Field<int>("Id");
            return IdSalaDeJuego;
        }

        private static SalasDeJuego ObtenerPorRowSalaDeJuego(DataRow row)
        {
            SalasDeJuego MiSalaDeJuego = new SalasDeJuego();
            MiSalaDeJuego.Id = row.Field<int>("Id");
            MiSalaDeJuego.Nombre = row.Field<string>("Nombre");
            MiSalaDeJuego.CantJugadores = row.Field<int>("CantJugadores");
            MiSalaDeJuego.MontoAGanar= row.Field<int>("MontoAGanar");
            MiSalaDeJuego.Disponible = row.Field<bool>("Disponible");
            MiSalaDeJuego.NRonda = row.Field<int>("NRonda");
            return MiSalaDeJuego;
        }
    }
}

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
        public static void ModificarDisponibilidadSala(int IdSala, bool Estado)
        {
            string update = "update salasdejuegos set Disponible=" + Estado.ToString() + " where Id=" + IdSala.ToString();
            DBHelper.EjecutarIUD(update);
        }
        public static void ModificarMonedasYSalasUsuario(int IdUsuario, Usuario MiUsuario)
        { 
            string update = "update usuarios set Monedas=" + MiUsuario.Monedas.ToString() + ", SalasDeJuego=CONCAT(SalasDeJuego, "+ MiUsuario.SalasDeJuego +") where Id=" + IdUsuario.ToString();
            DBHelper.EjecutarIUD(update);
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
                foreach(DataRow row in dt.Rows)
                {
                    MiSalaDeJuego = ObtenerPorRowSalaDeJuego(row);
                    ListaSalasDeJuego.Add(MiSalaDeJuego);
                }
                MiSalaDeJuego = ObtenerPorRowSalaDeJuego(dt.Rows[0]);
            }
            return ListaSalasDeJuego;
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

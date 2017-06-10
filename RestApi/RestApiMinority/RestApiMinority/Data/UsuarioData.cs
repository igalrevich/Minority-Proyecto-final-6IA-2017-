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
       public static void ModificarMonedasYSalasUsuario(int IdUsuario, Usuario MiUsuario)
        { 
            string update = "update usuarios set Monedas=" + MiUsuario.Monedas.ToString() + ", SalasDeJuego="+ MiUsuario.SalasDeJuego +" where Id=" + IdUsuario.ToString();
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

     }
}

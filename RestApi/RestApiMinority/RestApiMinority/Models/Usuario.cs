using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace RestApiMinority.Models
{
    public class Usuario
    {
        public int Id { get; set; }
        public string Nombre { get; set; }
        public string Mail { get; set; }
        public string Password{ get; set; }
        public int Monedas{ get; set; }
        public int SalaDeJuego{ get; set; }

        public void LlenarDatosCon0()
        {
            Id = 0;
            Nombre = "0";
            Mail = "0";
            Password = "0";
            SalaDeJuego = 0;
        }
    }
}
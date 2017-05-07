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
        public string SalasDeJuego{ get; set; }
    }
}
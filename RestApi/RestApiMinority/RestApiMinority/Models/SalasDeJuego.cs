using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace RestApiMinority.Models
{
    public class SalasDeJuego
    {
        public int Id { get; set; }
        public string Nombre { get; set; }
        public int CantJugadores { get; set; }
        public int MontoAGanar { get; set; }
        public bool Disponible{ get; set; }
        public int NRonda { get; set; }
    }
}
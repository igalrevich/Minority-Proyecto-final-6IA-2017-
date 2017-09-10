using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace RestApiMinority.Models
{
    public class VotoACalcular
    {
        public string OpcionA { get; set; }
        public string OpcionB { get; set; }
        public string VotoJugador { get; set; }
        public int IdUsuario { get; set; }
        public int IdSala { get; set; }
        public int NRonda { get; set; }
    }
}
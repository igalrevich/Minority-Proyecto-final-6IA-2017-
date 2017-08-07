using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace RestApiMinority.Models
{
    public class PreguntaxJuego
    {
        public int IdSala { get; set; }
        public int IdPregunta { get; set; }
        public int IdRonda { get; set; }
        public string TiempoFinalizacion { get; set; }

    }
}
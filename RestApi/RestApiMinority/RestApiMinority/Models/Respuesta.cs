using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace RestApiMinority.Models
{
    public class Respuesta
    {
        public int Usuario { get; set; }
        public int Pregunta { get; set; }
        public string RespuestaParcial { get; set; }
        public string RespuestaFinal { get; set; }
        public int Sala { get; set; }
    }
}
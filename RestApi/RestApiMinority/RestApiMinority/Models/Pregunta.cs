using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace RestApiMinority.Models
{
    public class Pregunta
    {
        public int Id { get; set; }
        public string OpcionA { get; set; }
        public string OpcionB { get; set; }
        public int Categoria { get; set; }
    }
}
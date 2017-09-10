using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace RestApiMinority.Models
{
    public class Usuariosxsala
    {
        public int Id { get; set; }
        public int Usuario { get; set; }
        public int SalaDeJuego { get; set; }
        public string NombreSalaDeJuego { get; set; }
        public bool Sigue { get; set; }
        public bool VotoEnBlanco { get; set; }
    }
}
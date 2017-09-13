using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace RestApiMinority.Models
{
    public class Resultado
    {
        public bool Empate { get; set; }
        public bool MayoriaOpcionA { get; set; }
        public bool Gano { get; set; }
        public int CantVotosOpcionA { get; set; }
        public int CantVotosOpcionB { get; set; }

        public bool PasoSigueEnTrue { get; set; }
    }
}
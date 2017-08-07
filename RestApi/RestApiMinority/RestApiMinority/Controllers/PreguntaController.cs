using RestApiMinority.Data;
using RestApiMinority.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;

namespace RestApiMinority.Controllers
{
    public class PreguntaController : ApiController
    {
        // GET: api/pregunta/GetPregunta/5/1
        [ResponseType(typeof(Pregunta))]
        [Route("api/pregunta/GetPregunta/{IdSala}/{NRonda}")]
        public IHttpActionResult GetPregunta(int IdSala, int NRonda)
        {
            Pregunta MiPregunta = PreguntaData.ObtenerPreguntaPorIdPreguntasPorJuego(IdSala,NRonda);
            if (MiPregunta == null)
            {
                return NotFound();
            }
            return Ok(MiPregunta);
        }

        // POST: api/respuesta/InsertarPreguntas
        [ResponseType(typeof(PreguntaxJuego))]
        public IHttpActionResult InsertarPreguntas(PreguntaxJuego MiPreguntaxJuego)
        {
            if (MiPreguntaxJuego == null)
            {
                return BadRequest("Datos incorrectos.");
            }
            else
            {
                PreguntaData.InsertarPreguntas(MiPreguntaxJuego.IdSala);
                return Ok();
            }
        }
    }
}

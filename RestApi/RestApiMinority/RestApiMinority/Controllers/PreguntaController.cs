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
        /* GET: api/pregunta/GetPregunta/5/1
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
        }*/

        // GET: api/pregunta/GetPregunta/5
        [ResponseType(typeof(Pregunta))]
        public IHttpActionResult GetPregunta(int id)
        {
            Pregunta MiPregunta = PreguntaData.ObtenerPreguntaPorId(id);
            if (MiPregunta == null)
            {
                return NotFound();
            }
            return Ok(MiPregunta);
        }

        // POST: api/pregunta/InsertarPreguntas
        [ResponseType(typeof(PreguntaxJuego))]
        public IHttpActionResult InsertarPreguntas(PreguntaxJuego MiPreguntasxJuego)
        {
            if (MiPreguntasxJuego == null)
            {
                return BadRequest("Datos incorrectos.");
            }
            else
            {
                PreguntaData.InsertarPreguntas(MiPreguntasxJuego.IdSala);
                return Ok();
            }
        }
    }
}

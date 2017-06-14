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
    public class RespuestaController : ApiController
    {

        // GET: api/respuesta/GetCantVotos/Coca
        [Route("api/respuesta/GetCantVotos/{Opcion}/{Sala}")]
        public IHttpActionResult GetCantVotos(string Opcion, int Sala)
        {
            int CantVotos = RespuestaData.ObtenerCantVotos(Opcion, Sala);
            return Ok(CantVotos);
        }
        // POST: api/respuesta/InsertarRespuesta
        [ResponseType(typeof(Respuesta))]
        public IHttpActionResult InsertarRespuesta(Respuesta MiRespuesta)
        {
            if (MiRespuesta == null)
            {
                return BadRequest("Datos incorrectos.");
            }
            else
            {
                RespuestaData.Insert(MiRespuesta);
                return Ok();
            }
        }

        // DELETE: api/respuesta/DeleteRespuestasSala/1
        public IHttpActionResult DeleteRespuestasSala(int id)
        {
            RespuestaData.DeleteRespuestasSala(id);
            return Ok();
        }
    }
}

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
    public class RestController : ApiController
    {
        // GET: api/Rest/GetSala
        [ResponseType(typeof(SalasDeJuego))]
        [Route("api/rest/GetSala")]
        public IEnumerable<SalasDeJuego> GetSala()
        {

            return UsuarioData.ObtenerSalasDeJuego();
        }

        // GET: api/Rest/GetSala/5
        [ResponseType(typeof(SalasDeJuego))]
        public IHttpActionResult GetSala(int id)
        {
            SalasDeJuego MiSalaDeJuego = UsuarioData.ObtenerPorIdSalaDeJuego(id);
            if (MiSalaDeJuego == null)
            {
                return NotFound();
            }
            return Ok(MiSalaDeJuego);
        }

        // GET: api/Rest/GetIdByNombre/tabla/nombre
        public IHttpActionResult GetIdByNombre(string tabla,string nombre)
        {
            int IdSala = UsuarioData.ObtenerIdPorNombre(tabla,nombre);
            if (IdSala == 0)
            {
                return NotFound();
            }
            else
            {
                return Ok(IdSala);
            }
        }
        // GET: api/Rest/GetCantVotos/Coca
        [Route("api/rest/GetCantVotos/{Opcion}")]
        public IHttpActionResult GetCantVotos(string Opcion)
        {
            int CantVotos = RespuestaData.ObtenerCantVotos(Opcion);
            return Ok(CantVotos);
        }




        // POST: api/Rest/InsertarRespuesta
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

        // PUT: api/Rest/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE: api/Rest/DeleteRespuestasSala/1
        public IHttpActionResult DeleteRespuestasSala(int IdSala)
        {
            RespuestaData.DeleteRespuestasSala(IdSala);
            return Ok();
        }
    }
}

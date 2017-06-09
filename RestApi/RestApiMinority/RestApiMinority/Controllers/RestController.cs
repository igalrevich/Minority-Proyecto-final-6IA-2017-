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
        [Route("api/rest/GetIdByNombre/{tabla}/{nombre}")]
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
        [Route("api/rest/GetCantVotos/{Opcion}/{Sala}")]
        public IHttpActionResult GetCantVotos(string Opcion, int Sala)
        {
            int CantVotos = RespuestaData.ObtenerCantVotos(Opcion,Sala);
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

        // PUT: api/Rest/ModificarSalaDeJuego/1
        [ResponseType(typeof(SalasDeJuego))]
        [Route("api/rest/ModificarSalaDeJuego/{id}")]
        [HttpPut]
        public IHttpActionResult ModificarSalaDeJuego(int id, [FromBody] SalasDeJuego MiSalaDeJuego)
        {
            UsuarioData.ModificarDisponibilidadSala(id, MiSalaDeJuego.Disponible);
            return Ok();
        }

        // PUT: api/Rest/ModificarUsuario/1
        [ResponseType(typeof(Usuario))]
        [Route("api/rest/ModificarUsuario/{id}")]
        [HttpPut]
        public IHttpActionResult ModificarUsuario(int id, [FromBody] SalasDeJuego MiSalaDeJuego)
        {
            UsuarioData.ModificarDisponibilidadSala(id, MiSalaDeJuego.Disponible);
            return Ok();
        }

        // DELETE: api/Rest/DeleteRespuestasSala/1
        public IHttpActionResult DeleteRespuestasSala(int id)
        {
            RespuestaData.DeleteRespuestasSala(id);
            return Ok();
        }
    }
}

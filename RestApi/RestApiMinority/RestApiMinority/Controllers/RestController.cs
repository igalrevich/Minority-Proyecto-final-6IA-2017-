﻿using RestApiMinority.Data;
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
        // GET: api/Rest
        public IEnumerable<string> GetHolaMundo()
        {
            return new string[] { "Hola mundo" };
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

        // GET: api/Rest/GetIdSalaByNombre/nombre
        [Route("api/Rest/GetIdSalaByNombre/{nombre}")]
        public IHttpActionResult GetIdSalaByNombre(string nombre)
        {
            int IdSala = UsuarioData.ObtenerIdSalaDeJuego(nombre);
            if (IdSala == 0)
            {
                return NotFound();
            }
            else
            {
                return Ok(IdSala);
            }
        }



        // POST: api/Rest/InsertarRespuesta
        [ResponseType(typeof(Respuesta))]
        public IHttpActionResult Post(Respuesta MiRespuesta)
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

        // DELETE: api/Rest/5
        public IHttpActionResult DeleteSala(int id)
        {
            if (UsuarioData.ObtenerPorId(id) == null)
            {
                return NotFound();
            }
            UsuarioData.Delete(id);
            return Ok();
        }
    }
}
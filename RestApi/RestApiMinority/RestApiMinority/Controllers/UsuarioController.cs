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
    public class UsuarioController : ApiController
    {

        // GET: api/Usuario/GetMonedas
        [ResponseType(typeof(Usuario))]
        public IHttpActionResult GetMonedas(int id)
        {
            int MonedasUsuario = UsuarioData.PreguntarMonedasUsuario(id);
            return Ok(MonedasUsuario);
        }


        // GET: api/Usuario/GetExisteUsuario/Mail/Password
        [Route("api/usuario/GetExisteUsuario/{mail}/{password}")]
        public IHttpActionResult GetExisteUsuario(string mail, string password)
        {
            Usuario MiUsuario=UsuarioData.ObtenerPorMailYPassword(mail,password);
            return Ok(MiUsuario);
        }

        // GET: api/Usuario/ValidarUsuarioEnSala/3/1
        [Route("api/usuario/ValidarUsuarioEnSala/{IdUsuario}/{IdSala}")]
        public IHttpActionResult ValidarUsuarioEnSala(int IdUsuario, int IdSala)
        {
            int ResultadoValidacion = UsuarioData.ValidarQueUsuarioEntroEnSala(IdUsuario,IdSala);
            return Ok(ResultadoValidacion);
        }


        // GET: api/Usuario/GetIdByNombre/tabla/nombre
        [Route("api/usuario/GetIdByNombre/{tabla}/{nombre}")]
        public IHttpActionResult GetIdByNombre(string tabla, string nombre)
        {
            int IdSala = UsuarioData.ObtenerIdPorNombre(tabla, nombre);
            if (IdSala == 0)
            {
                return NotFound();
            }
            else
            {
                return Ok(IdSala);
            }
        }
        // PUT: api/Usuario/ModificarUsuario/1
        [ResponseType(typeof(Usuario))]
        [Route("api/usuario/ModificarUsuario/{id}")]
        [HttpPut]
        public IHttpActionResult ModificarUsuario(int id, [FromBody] Usuario MiUsuario)
        {
            UsuarioData.ModificarMonedasYSalasUsuario(id, MiUsuario);
            return Ok();
        }

        // PUT: api/Usuario/ModificarMonedasUsuario/1
        [ResponseType(typeof(Usuario))]
        [Route("api/usuario/ModificarMonedasUsuario/{id}")]
        [HttpPut]
        public IHttpActionResult ModificarMonedasUsuario(int id, [FromBody] Usuario MiUsuario)
        {
            UsuarioData.ModificarMonedasUsuario(id, MiUsuario.Monedas);
            return Ok();
        }


        // POST: api/usuario/IngresarUserSala
        [ResponseType(typeof(Usuariosxsala))]
        public IHttpActionResult IngresarUserSala(Usuariosxsala MiUsuariosxsala)
        {
            string MensajeEntrarASala=UsuarioData.IngresarUserSala(MiUsuariosxsala.Usuario, MiUsuariosxsala.SalaDeJuego);
            return Ok(MensajeEntrarASala);
            
        }

        // POST: api/usuario/AgregarUsuario
        [ResponseType(typeof(Usuario))]
        public IHttpActionResult AgregarUsuario(Usuario MiUsuario)
        {
            string MensajeEntrarASala = UsuarioData.AgregarUsuario(MiUsuario);
            return Ok(MensajeEntrarASala);

        }



    }
}

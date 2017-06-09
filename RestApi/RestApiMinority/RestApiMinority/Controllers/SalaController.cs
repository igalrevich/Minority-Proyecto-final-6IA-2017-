using RestApiMinority.Data;
using RestApiMinority.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;


    public class SalaController : ApiController
    {
        // GET: api/Rest/GetSala
        [ResponseType(typeof(SalasDeJuego))]
        public IEnumerable<SalasDeJuego> Get()
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

       // GET: api/Rest/GetCantVotos/Coca
       [Route("api/rest/GetCantVotos/{Opcion}/{Sala}")]
       public IHttpActionResult GetCantVotos(string Opcion, int Sala)
       {
        int CantVotos = RespuestaData.ObtenerCantVotos(Opcion, Sala);
        return Ok(CantVotos);
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
}

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
        // GET: api/Sala/GetSala
        [ResponseType(typeof(SalasDeJuego))]
        public IEnumerable<SalasDeJuego> Get()
        {

            return SalaDeJuegoData.ObtenerSalasDeJuegoConHoraComienzo();
        }

    // GET: api/Sala/GetSala/5
    [ResponseType(typeof(SalasDeJuego))]
        public IHttpActionResult GetSala(int id)
        {
        SalasDeJuego MiSalaDeJuego = SalaDeJuegoData.ObtenerPorIdSalaDeJuego(id);
        if (MiSalaDeJuego == null)
        {
            return NotFound();
        }
        return Ok(MiSalaDeJuego);
        }

    // PUT: api/Sala/ModificarSalaDeJuego/1
       [ResponseType(typeof(SalasDeJuego))]
       [Route("api/sala/ModificarSalaDeJuego/{id}")]
       [HttpPut]
       public IHttpActionResult ModificarSalaDeJuego(int id, [FromBody] SalasDeJuego MiSalaDeJuego)
       {
        SalaDeJuegoData.ModificarDisponibilidadSala(id, MiSalaDeJuego.Disponible);
        return Ok();
       }

    // PUT: api/Sala/ModificarSalaDeJuego/1
    [ResponseType(typeof(SalasDeJuego))]
    [Route("api/sala/ModificarSalaDeJuegoMHC/{id}")]
    [HttpPut]
    public IHttpActionResult ModificarSalaDeJuegoMHC(int id, [FromBody] SalasDeJuego MiSalaDeJuego)
    {
        SalaDeJuegoData.ModificarMHCSala(id, MiSalaDeJuego);
        return Ok();
    }

    // DELETE: api/Rest/DeleteUsuarioxSala/1
    public IHttpActionResult DeleteUsuarioxSala(int id)
    {
        SalaDeJuegoData.DeleteUsuarioEnSala(id);
        return Ok();
    }
}

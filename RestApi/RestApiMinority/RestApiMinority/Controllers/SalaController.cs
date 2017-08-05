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
        // GET: api/Sala/Get
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

    // GET: api/Sala/GetCantJugadoresSala/5
    [ResponseType(typeof(SalasDeJuego))]
    public IHttpActionResult GetCantJugadoresSala(int id)
    {
        int CantJugadoresSala = SalaDeJuegoData.ObtenerCantJugadoresSalaDeJuego(id);
        return Ok(CantJugadoresSala);
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

    // PUT: api/Sala/ActualizarSalaDeJuegoMenosDe3Jugadores/1
    [ResponseType(typeof(SalasDeJuego))]
    [Route("api/sala/ActualizarSalaDeJuegoMenosDe3Jugadores/{id}")]
    [HttpPut]
    public IHttpActionResult ActualizarSalaDeJuegoMenosDe3Jugadores(int id, [FromBody] SalasDeJuego MiSalaDeJuego)
    {
        SalaDeJuegoData.ActualizarSalaDeJuegoMenosDe3Jugadores(id);
        return Ok();
    }

    // PUT: api/Sala/ModificarSalaDeJuegoMHC/1
    [ResponseType(typeof(SalasDeJuego))]
    [Route("api/sala/ModificarSalaDeJuegoMHC/{id}")]
    [HttpPut]
    public IHttpActionResult ModificarSalaDeJuegoMHC(int id, [FromBody] SalasDeJuego MiSalaDeJuego)
    {
        SalaDeJuegoData.ModificarMHCSala(id, MiSalaDeJuego);
        return Ok();
    }

    // PUT: api/Sala/ModificarCantJugadoresONRondaSala/1
    [ResponseType(typeof(SalasDeJuego))]
    [Route("api/sala/ModificarCantJugadoresONRondaSala/{id}")]
    [HttpPut]
    public IHttpActionResult ModificarCantJugadoresONRondaSala(int id, [FromBody] SalasDeJuego MiSalaDeJuego)
    {
        SalaDeJuegoData.ModificarCantJugadoresONRondaSala(id, MiSalaDeJuego);
        return Ok();
    }

    // PUT: api/Sala/ModificarCantJugadoresYRespuestasSala/1
    [ResponseType(typeof(SalasDeJuego))]
    [Route("api/sala/ModificarCantJugadoresYRespuestasSala/{id}")]
    [HttpPut]
    public IHttpActionResult ModificarCantJugadoresYRespuestasSala(int id, [FromBody] SalasDeJuego MiSalaDeJuego)
    {
        SalaDeJuegoData.ModificarCantJugadoresYRespuestasSala(id, MiSalaDeJuego);
        return Ok();
    }

    // PUT: api/Sala/AnadirJugadorSalaDeJuego/1
    [ResponseType(typeof(SalasDeJuego))]
    [Route("api/sala/AnadirJugadorSalaDeJuego/{id}")]
    [HttpPut]
    public IHttpActionResult AnadirJugadorSalaDeJuego(int id, [FromBody] SalasDeJuego MiSalaDeJuego)
    {
        SalaDeJuegoData.AnadirJugadorSalaDeJuego(id);
        return Ok();
    }

    // DELETE: api/sala/DeleteUsuarioxSala/1
    public IHttpActionResult DeleteUsuarioxSala(int id)
    {
        SalaDeJuegoData.DeleteUsuarioEnSala(id);
        return Ok();
    }
}

package com.revich.mobile.minority;

/**
 * Created by Igal on 8/7/2017.
 */

public class PreguntasxJuego {

    public int IdSala;
    public int IdPregunta;
    public int IdRonda;
    public String TiempoFinalizacion;

    public void LlenarDatosPreguntasxJuego(int IdSalaParam)
    {
      this.IdSala=IdSalaParam;
      this.IdPregunta=0;
      this.IdRonda=0;
      this.TiempoFinalizacion="Igal";
    }
}

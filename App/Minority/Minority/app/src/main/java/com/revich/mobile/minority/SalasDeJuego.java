package com.revich.mobile.minority;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Igal on 5/10/2017.
 */

public class SalasDeJuego {
    public int Id;
    public int CantJugadores;
    public int MontoAGanar;
    public int NRonda;
    public boolean Disponible;
    public String Nombre;
    public Date HoraComienzo;

    public void LlenarDatos(int Id, int CantJugadores, int MontoAGanar, int NRonda, boolean Disponible, String Nombre)
    {
        this.Id=Id;
        this.CantJugadores=CantJugadores;
        this.MontoAGanar=MontoAGanar;

        this.NRonda=NRonda;
        this.Disponible=Disponible;
        this.Nombre=Nombre;
    }
    public void LlenarDisponibilidad(boolean Estado) {
        Id = 4;
        CantJugadores = 4;
        MontoAGanar = 4;
        NRonda = 4;
        Nombre = "Igal";
        Disponible = Estado;
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        try {
            HoraComienzo = dateFormat.parse("11:05:05");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}

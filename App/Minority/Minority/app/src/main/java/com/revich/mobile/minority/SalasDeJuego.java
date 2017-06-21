package com.revich.mobile.minority;

import java.util.Calendar;
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
    public String HoraComienzo;
    public boolean ModificarHComienzo;

    public void LlenarDatos(int Id, int CantJugadores, int MontoAGanar, int NRonda, boolean Disponible, String Nombre , String HoraComienzoDateTime, boolean ModificarHComienzo)
    {
        this.Id=Id;
        this.CantJugadores=CantJugadores;
        this.MontoAGanar=MontoAGanar;
        this.NRonda=NRonda;
        this.Disponible=Disponible;
        this.Nombre=Nombre;
        this.HoraComienzo=HoraComienzoDateTime;
        this.ModificarHComienzo=  ModificarHComienzo;
    }
    public void LlenarDisponibilidad(boolean Estado)
    {
        Id = 4;
        CantJugadores = 4;
        MontoAGanar = 4;
        NRonda = 4;
        Nombre = "Igal";
        Disponible = Estado;
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Date HoraActual= Calendar.getInstance().getTime();
        if(Estado)
        {
            HoraComienzo= dateFormat.format(HoraActual);
        }
        else
        {
            Date CuatroMin15Seg=null;
            try {
                CuatroMin15Seg = dateFormat.parse("00:04:15");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long HoraComienzoSalaLong= HoraActual.getTime() + CuatroMin15Seg.getTime();
            HoraComienzo= String.valueOf(HoraComienzoSalaLong);
        }
        ModificarHComienzo=Estado;

    }

    public void LlenarCantJugadoresMas1(int CantJugadoresActual, int MontoAGanarActual, int NumRonda)
    {
        Id = 4;
        CantJugadores = CantJugadoresActual+1;
        MontoAGanar = MontoAGanarActual+1;
        NRonda = NumRonda;
        Nombre = "Igal";
        Disponible = true;
        HoraComienzo= "Igal";
        ModificarHComienzo=true;
    }

    public void ActualizarDatosSalas(int NuevaCantJugadores,int NumRondaActual)
    {
        Id = 4;
        CantJugadores = NuevaCantJugadores;
        MontoAGanar = 1;
        NRonda = NumRondaActual+1;
        Nombre = "Igal";
        Disponible = true;
        HoraComienzo= "Igal";
        ModificarHComienzo=true;
    }

}

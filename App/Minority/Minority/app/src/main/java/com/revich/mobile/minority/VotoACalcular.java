package com.revich.mobile.minority;

/**
 * Created by Igal on 9/10/2017.
 */

public class VotoACalcular {
    public String OpcionA ;
    public String OpcionB ;
    public String VotoJugador ;
    public int IdUsuario ;
    public int IdSala ;
    public int NRonda ;

    public void LlenarDatos(int usuario, String opcionA, String opcionB, String votoJugador, int idSala, int nronda)
    {
        this.OpcionA=opcionA;
        this.OpcionB=opcionB;
        this.VotoJugador=votoJugador;
        this.IdUsuario=usuario;
        this.IdSala=idSala;
        this.NRonda=nronda;
    }
}

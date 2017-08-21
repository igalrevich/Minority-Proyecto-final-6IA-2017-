package com.revich.mobile.minority;

/**
 * Created by Igal on 8/21/2017.
 */

public class Usuariosxsala {

    public int Id;
    public int Usuario;
    public int SalaDeJuego;
    public String NombreSalaDeJuego;

    public void LlenarDatos(int usuario, String nombreSalaDeJuego)
    {
        this.Id=0;
        this.Usuario=usuario;
        this.NombreSalaDeJuego=nombreSalaDeJuego;
        this.SalaDeJuego=0;
    }
}

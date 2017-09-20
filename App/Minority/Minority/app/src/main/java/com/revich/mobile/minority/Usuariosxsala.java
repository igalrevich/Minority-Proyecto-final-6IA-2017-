package com.revich.mobile.minority;

/**
 * Created by Igal on 8/21/2017.
 */

public class Usuariosxsala {

    public int Id;
    public int Usuario;
    public int SalaDeJuego;
    public String NombreSalaDeJuego;
    public boolean Sigue;
    public boolean VotoEnBlanco;

    public void LlenarDatos(int usuario, int salaDeJuego)
    {
        this.Id=0;
        this.Usuario=usuario;
        this.NombreSalaDeJuego="";
        this.SalaDeJuego=salaDeJuego;
        this.Sigue=false;
        this.VotoEnBlanco=false;
    }
}

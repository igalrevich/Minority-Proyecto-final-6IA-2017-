package com.revich.mobile.minority;

/**
 * Created by Igal on 6/13/2017.
 */

public class Usuario
{
    public int Id;
    public String Nombre;
    public String Mail;
    public String Password;
    public int Monedas;
    public int SalaDeJuego;

    public void LlenarDatos(int Id, int Monedas, int SalaDeJuego )
    {
        this.Id=Id;
        this.Nombre="Igal";
        this.Mail="Igal";
        this.Password="igal";
        this.Monedas=Monedas;
        this.SalaDeJuego= SalaDeJuego;
    }
}



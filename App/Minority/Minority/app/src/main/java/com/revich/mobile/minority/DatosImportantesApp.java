package com.revich.mobile.minority;

/**
 * Created by Igal on 7/2/2017.
 */

public class DatosImportantesApp {

    public static boolean EntroSalaA;
    public static boolean EntroSalaB;
    public static boolean EntroSalaC;
    public static boolean EntroSalaD;
    public static boolean EntroSalaE;
    public static boolean EntroSalaF;
    public static String NombreUsuario;
    public static int MonedasUsuario;
    public static boolean [] VecEntroSalas= new boolean[] {EntroSalaA,EntroSalaB,EntroSalaC,EntroSalaD,EntroSalaE,EntroSalaF};

    public static boolean GetEntroSala(int Indice)
    {
        return VecEntroSalas[Indice];
    }

    public static void SetEntroSala(int Indice,boolean Entro)
    {
       VecEntroSalas[Indice] = Entro;
    }

    public static String GetNombreUsuario()
    {
        return NombreUsuario;
    }

    public static void SetNombreUsuario(String Nombre)
    {
        NombreUsuario = Nombre;
    }

    public static int GetMonedasUsuario()
    {
        return MonedasUsuario;
    }

    public static void SetMonedasUsuario(int Monedas)
    {
        MonedasUsuario = Monedas;
    }
}

package com.revich.mobile.minority;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    public static int IdUsuario;

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

    public static int GetIdUsuario()
    {
        return IdUsuario;
    }

    public static void SetIdUsuario(int idUsuario)
    {
        IdUsuario = idUsuario;
    }




}

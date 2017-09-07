package com.revich.mobile.minority;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

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
    public static int [] IdPreguntasSalas= new int [6];

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

    public static void SetIdPreguntaSala( int IdSala,int IdPregunta)
    {
        switch (IdSala)
        {
            case 3:
               IdPreguntasSalas[0]=IdPregunta;
                break;
            case 5:
                IdPreguntasSalas[1]=IdPregunta;
                break;
            case 6:
                IdPreguntasSalas[2]=IdPregunta;
                break;
            case 1:
                IdPreguntasSalas[3]=IdPregunta;
                break;
            case 7:
                IdPreguntasSalas[4]=IdPregunta;
                break;
            case 8:
                IdPreguntasSalas[5]=IdPregunta;
                break;

        }
    }

    public static int GetIdPreguntaSala(int IdSala)
    {
        int IdPreguntaADevolver=0;
        switch (IdSala)
        {
            case 3:
                IdPreguntaADevolver=IdPreguntasSalas[0];
                break;
            case 5:
                IdPreguntaADevolver=IdPreguntasSalas[1];
                break;
            case 6:
                IdPreguntaADevolver=IdPreguntasSalas[2];
                break;
            case 1:
                IdPreguntaADevolver=IdPreguntasSalas[3];
                break;
            case 7:
                IdPreguntaADevolver=IdPreguntasSalas[4];
                break;
            case 8:
                IdPreguntaADevolver=IdPreguntasSalas[5];
                break;

        }
        return IdPreguntaADevolver;
    }




}

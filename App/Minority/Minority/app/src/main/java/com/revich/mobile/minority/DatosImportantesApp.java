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
    public static int IndiceVecUtilizado;
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

    private class BuscarIdAPasarTaskOActualizarUsuario extends AsyncTask<String, Void, Integer> {
        private OkHttpClient client= new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        @Override
        protected void onPostExecute(Integer Id)
        {
            if(Id!=2)
            {
              if(Id==0)
              {
                SetEntroSala(IndiceVecUtilizado,false);
              }
              else
              {
                  SetEntroSala(IndiceVecUtilizado,true);
              }
                //return VecEntroSalas[IndiceVecUtilizado];
            }

        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];
            if(method.equals("GET"))
            {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonStr= response.body().string();
                    return Integer.parseInt(jsonStr);

                }
                catch (IOException e)
                {
                    Log.d("Error", e.getMessage());
                    return 2;
                }
            }
            else
            {
               return 2;
            }

        }

    }
}

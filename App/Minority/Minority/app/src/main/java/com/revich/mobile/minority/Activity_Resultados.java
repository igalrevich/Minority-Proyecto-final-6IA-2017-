package com.revich.mobile.minority;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_Resultados extends AppCompatActivity {
     TextView tvOpcion1,tvOpcion2,tvVotosOpcion1,tvVotosOpcion2,tvGanastePerdiste,tvIndicacion1,tvIndicacion2;
     String Opcion1,Opcion2,ResultadoUsuario,VotoJugador,url,Opcion;
     boolean MayoriaOpcion1=false,VotoOpcion1=false;
     int CantVotosOpcion1,CantVotosOpcion2,CantVotosEnBlanco,CantVotosOpciones,Sala,Usuario,Pregunta,CantJugadores,IndiceFor;
     Random rand;
     Respuesta MiRespuesta;
     Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__resultados);
        getSupportActionBar().hide();
        ObtenerReferencias();
        GenerarVotosAlAzar();

    }


    private void ObtenerReferencias()
    {
        tvOpcion1= (TextView) findViewById(R.id.tvOpcion1);
        tvOpcion2= (TextView) findViewById(R.id.tvOpcion2);
        tvVotosOpcion1= (TextView) findViewById(R.id.tvVotosOpcion1);
        tvVotosOpcion2= (TextView) findViewById(R.id.tvVotosOpcion2);
        tvGanastePerdiste= (TextView) findViewById(R.id.tvGanastePerdiste);
        tvIndicacion1= (TextView) findViewById(R.id.tvIndicacion1);
        tvIndicacion2= (TextView) findViewById(R.id.tvIndicacion2);
    }
    private void GenerarVotosAlAzar()
    {
        Intent ELIntentQueVino= getIntent();
        Bundle ElBundle= ELIntentQueVino.getExtras();
        Opcion1= ElBundle.getString("Opcion1");
        Opcion2= ElBundle.getString("Opcion2");
        VotoJugador= ElBundle.getString("Voto");
        CantJugadores =ElBundle.getInt("CantJugadores");
        Sala =ElBundle.getInt("IdSala");
        Usuario =ElBundle.getInt("IdUsuario");
        Pregunta=ElBundle.getInt("IdPregunta");
        rand = new Random();
        for(int i=1;i<=CantJugadores-1;i++)
        {
            int IdUsuario= i;
            if(IdUsuario==Usuario)
            {
              IdUsuario=50;
            }
            MiRespuesta=new Respuesta();
            MiRespuesta.Usuario=IdUsuario;
            MiRespuesta.Pregunta=Pregunta;
            MiRespuesta.Sala=Sala;
            int OpcionesDeVoto= rand.nextInt(3);
            switch(OpcionesDeVoto)
            {
                case 0:
                    MiRespuesta.RespuestaParcial=Opcion1;
                    break;
                case 1:
                    MiRespuesta.RespuestaParcial=Opcion2;
                    break;
                case 2:
                    MiRespuesta.RespuestaParcial="";
                    break;
            }
            MiRespuesta.RespuestaFinal=MiRespuesta.RespuestaParcial;
            url ="http://apiminorityproyecto.azurewebsites.net/api/respuesta/InsertarRespuesta";
            gson= new Gson();
            new TraerIdsInsertarResultados().execute("POST",url,gson.toJson(MiRespuesta),String.valueOf(i));

        }
        /*CantVotosEnBlanco= rand.nextInt(50);
        CantVotosOpciones=49-CantVotosEnBlanco;
        CantVotosOpcion1=rand.nextInt(CantVotosOpciones+1);
        CantVotosOpcion2=CantVotosOpciones-CantVotosOpcion1;*/
    }
    private class TraerIdsInsertarResultados extends AsyncTask<String, Void, Integer> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPostExecute(Integer CantVotos) {
           if(CantVotos!=-1)
           {
               if(CantVotos!=-2)
               {
                   switch (Opcion) {
                       case "Opcion1":
                           CantVotosOpcion1 = CantVotos;
                           url = "http://apiminorityproyecto.azurewebsites.net/api/respuesta/GetCantVotos/" + Opcion2 + "/" + Sala;
                           new TraerIdsInsertarResultados().execute("GET", url, "Opcion2");
                           break;
                       case "Opcion2":
                           CantVotosOpcion2 = CantVotos;
                           GenerarResultadoUsuarioParte2();
                           break;
                   }
               }
               else
               {
                  if(IndiceFor==CantJugadores-1)
                  {
                      GenerarResultadoUsuarioParte1();
                      ImprimirResultadosPantalla(ResultadoUsuario);
                  }
               }
           }

        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];
            if (method.equals("GET")) {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Opcion= parametros[2];
                try {
                    Response response = client.newCall(request).execute();
                    String jsonStr = response.body().string();
                    return Integer.parseInt(jsonStr);

                } catch (IOException e) {
                    Log.d("Error :", e.getMessage());
                    return -1;
                }
            }
            else
            {
                if(method.equals("POST"))
                {
                    String json = parametros[2];
                    IndiceFor= Integer.parseInt(parametros[3]);
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        return -2;

                    }
                    catch (IOException e) {
                        Log.d("Error :", e.getMessage());
                        return -1;
                    }

                }
                else
                {
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                    }
                    catch (IOException e) {
                        Log.d("Error :", e.getMessage());
                    }
                    return -1;
                }
            }
        }
    }
    private void GenerarResultadoUsuarioParte1()
    {
        url ="http://apiminorityproyecto.azurewebsites.net/api/respuesta/GetCantVotos/"+Opcion1+"/"+Sala;
        new TraerIdsInsertarResultados().execute("GET",url,"Opcion1");
    }
    private void GenerarResultadoUsuarioParte2()
    {
        if(VotoJugador.equals("")==false)
        {
            if(VotoJugador.equals(Opcion1))
            {
                CantVotosOpcion1++;
                VotoOpcion1=true;
            }
            else
            {
                CantVotosOpcion2++;

            }
            if(CantVotosOpcion1!=CantVotosOpcion2)
            {
                if(CantVotosOpcion1>CantVotosOpcion2)
                {
                    MayoriaOpcion1=true;
                }
                ResultadoUsuario=CheckearResultados(MayoriaOpcion1,VotoJugador);

            }
            else
            {
                ResultadoUsuario="Empato";
            }
        }
        else
        {
            ResultadoUsuario="Perdio";
        }
    }

    private String CheckearResultados(boolean MayoriaOpcion1,String VotoJugador)
    {   String Resultado="";
        if(MayoriaOpcion1)
        {
            if(VotoOpcion1)
            {
                Resultado="Perdio";
            }
            else
            {
                Resultado="Gano";
            }
        }
        else
        {
            if(VotoOpcion1)
            {
                Resultado="Gano";
            }
            else
            {
                Resultado="Perdio";
            }
        }
        return  Resultado;
    }

    private void ImprimirResultadosPantalla(String Resultado)
    {
        tvVotosOpcion1.setText(String.valueOf(CantVotosOpcion1));
        tvVotosOpcion2.setText(String.valueOf(CantVotosOpcion2));
        if(MayoriaOpcion1)
       {
           tvOpcion2.setTextColor(Color.parseColor("#8ef686"));
           tvVotosOpcion2.setTextColor(Color.parseColor("#8ef686"));
           tvOpcion1.setTextColor(Color.parseColor("#f61525"));
           tvVotosOpcion1.setTextColor(Color.parseColor("#f61525"));
           GanoOPerdio(Resultado);
       }
       else
        {
          if(Resultado=="Empato")
          {
              tvGanastePerdiste.setTextColor(Color.parseColor("#f61525"));
              tvIndicacion1.setTextColor(Color.parseColor("#f61525"));
              tvIndicacion2.setTextColor(Color.parseColor("#f61525"));
              tvGanastePerdiste.setText("Empate!!");
              tvIndicacion1.setText("No hay minoria");
              tvIndicacion2.setText("Quedaste eliminado!!!");
              tvOpcion2.setTextColor(Color.parseColor("#f61525"));
              tvVotosOpcion2.setTextColor(Color.parseColor("#f61525"));
              tvOpcion1.setTextColor(Color.parseColor("#f61525"));
              tvVotosOpcion1.setTextColor(Color.parseColor("#f61525"));
          }
          else
          {
              tvOpcion2.setTextColor(Color.parseColor("#f61525"));
              tvVotosOpcion2.setTextColor(Color.parseColor("#f61525"));
              tvOpcion1.setTextColor(Color.parseColor("#8ef686"));
              tvVotosOpcion1.setTextColor(Color.parseColor("#8ef686"));
              GanoOPerdio(Resultado);
          }
        }
        /*url ="http://apiminorityproyecto.azurewebsites.net/api/respuesta/DeleteRespuestasSala/"+Sala;
        new TraerIdsInsertarResultados().execute("DELETE",url);*/

    }

    private void GanoOPerdio(String Resultado)
    {
        if(Resultado=="Gano")
        {
            tvGanastePerdiste.setTextColor(Color.parseColor("#8ef686"));
            tvIndicacion1.setTextColor(Color.parseColor("#8ef686"));
            tvIndicacion2.setTextColor(Color.parseColor("#8ef686"));
        }
        if(Resultado=="Perdio")
        {
            tvGanastePerdiste.setTextColor(Color.parseColor("#f61525"));
            tvIndicacion1.setTextColor(Color.parseColor("#f61525"));
            tvIndicacion2.setTextColor(Color.parseColor("#f61525"));
            tvGanastePerdiste.setText("Perdiste!!");
            tvIndicacion1.setText("Sos parte de la mayoria");
            tvIndicacion2.setText("Quedaste eliminado!!!");
        }
    }




}

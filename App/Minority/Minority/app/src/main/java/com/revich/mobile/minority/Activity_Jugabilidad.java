package com.revich.mobile.minority;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_Jugabilidad extends AppCompatActivity {
    Button btnOpcion1,btnOpcion2, btnVotar;
    TextView tvSegundosTimer,tvVotoFinal,tvMontoGanador,tvCantJugadores,tvNRonda,tvSala,tvTimer;
    boolean VotoOpcion1=false,PublicarProgresoPut=true,PusoNRonda=false;
    boolean VotoOpcion2=false;
    boolean VotoFinalmente=false, BotonesVisibles=false, TrajoSalaPrimeraVez=false, InsertoPreguntas=false;
    boolean PrimeraVezQueJuega;
    String VotoFinal,Usuario,SegundosTimer,url,AtributoRespuesta,QueModifica,Opcion1,Opcion2,TimerString;
    int Idbtn, IdSala, SegundosDisponiblesSala,MonedasUsuario,NumRandJugadoresMontoAGanar;
    int [] MinMaxIds= new int [] {2,7,8,13,14,19,20,25};
    CountDownTimer Timer;
    Gson gson;
    SalasDeJuego SalaDeJuegoTraida;
    Date TiempoALlegarSala;
    SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
    SimpleDateFormat dateFormatSoloHora= new SimpleDateFormat("KK:mm:ss");
    Respuesta MiRespuesta;
    Date HoraActual,TiempoDiferencia;
    Calendar cal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jugabilidad_landscape);
        getSupportActionBar().hide();
        ObtenerReferencias();
        CambiarBotones(false);
        Intent ElIntentQueVino= getIntent();
        Bundle ElBundleQueVino= ElIntentQueVino.getExtras();
        IdSala=ElBundleQueVino.getInt("IdSala");
        Usuario= ElBundleQueVino.getString("Usuario");
        //MonedasUsuario=ElBundleQueVino.getInt("Monedas");
        PrimeraVezQueJuega= ElBundleQueVino.getBoolean("PrimeraVezQueJuegaSala");
        SegundosDisponiblesSala= ElBundleQueVino.getInt("SegundosParaReclutarJugadores");
        if(PrimeraVezQueJuega==false)
        {
            CambiarBotones(true);
        }
        url ="http://apiminorityproyecto.azurewebsites.net/api/sala/GetSala/"+IdSala;
        new BuscarDatosTask().execute(url);

    }
    private class BuscarDatosTask extends AsyncTask<String, Void, SalasDeJuego> {
        private  OkHttpClient client= new OkHttpClient();
        @Override
        protected void onPostExecute(SalasDeJuego MiSalaDeJuego) {
            super.onPostExecute(MiSalaDeJuego);
            tvSala.setText(MiSalaDeJuego.Nombre);
            Log.d("TVNRSetText", "DespuesDePostExecute");
            tvNRonda.setText(String.valueOf(MiSalaDeJuego.NRonda));
            if(MiSalaDeJuego.CantJugadores!=0 && MiSalaDeJuego.MontoAGanar!=0)
            {
                tvCantJugadores.setText(String.valueOf(MiSalaDeJuego.CantJugadores));
                tvMontoGanador.setText(String.valueOf(MiSalaDeJuego.MontoAGanar));
                SalaDeJuegoTraida=MiSalaDeJuego;
            }
            if(TrajoSalaPrimeraVez==false)
            {
                if(MiSalaDeJuego.CantJugadores<3 && PrimeraVezQueJuega==false)
                {
                    TrajoSalaPrimeraVez=true;
                    IniciarActivitySeleccionarSalas();
                    //IniciarActivityPremiacion(IdSala,MiSalaDeJuego.MontoAGanar,MiSalaDeJuego.CantJugadores);
                }
                else
                {
                    TrajoSalaPrimeraVez=true;
                    SetearTimerSegundosDisponibles();
                }
            }


        }

        @Override
        protected SalasDeJuego doInBackground(String... parametros) {
            String url = parametros[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String jsonStr= response.body().string();
                return parsearResultado(jsonStr);

            } catch (IOException | JSONException e) {
                String err = (e.getMessage()==null)?"Fallo ParsearSalas":e.getMessage();
                Log.d("Error :", err);
                return new SalasDeJuego();
            }
        }

        SalasDeJuego parsearResultado(String JSONstr) throws JSONException {
            gson= new Gson();
            SalasDeJuego MiSalaDeJuego= gson.fromJson(JSONstr,SalasDeJuego.class);
            return MiSalaDeJuego;
        }
    }

    private class TraerIdsInsertarResultados extends AsyncTask<String, String, Integer> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPostExecute(Integer Id) {
            if (Id != 0)
            {
                if(Id!=-1)
                {
                    switch (AtributoRespuesta)
                    {
                        case "Usuario":
                            MiRespuesta.Usuario = Id;
                            String Opcion1= btnOpcion1.getText().toString();
                            url="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/preguntas/"+Opcion1;
                            new TraerIdsInsertarResultados().execute("GET",url,"Pregunta");
                            break;
                        case "Pregunta":
                            MiRespuesta.Pregunta = Id;
                            url ="http://apiminorityproyecto.azurewebsites.net/api/respuesta/InsertarRespuesta";
                            new TraerIdsInsertarResultados().execute("POST",url,gson.toJson(MiRespuesta));
                            break;
                    }
                }
                else
                {
                    if(QueModifica.equals("NRonda"))
                    {
                        tvTimer.setText("Haciendo ultimos ajustes 2/3");
                        Log.d("TVNRSetText", "DespuesDeInsertarResultadosNRonda");
                        tvNRonda.setText("1");
                        CambiarBotones(true);
                        BotonesVisibles=true;
                        SalaDeJuegoTraida.NRonda=1;
                        BuscarPreguntaConVec();
                    }
                    else
                    {
                        if(QueModifica.equals("CantJugadoresPJSD0"))
                        {
                            SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                            /*MiSalaDeJuego.LlenarDisponibilidad(true);
                            String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarSalaDeJuegoMHC/"+IdSala;
                            gson=new Gson();
                            new ActualizarMHCSala().execute("PUT",url,gson.toJson(MiSalaDeJuego));*/
                            CambiarBotones(true);
                            BotonesVisibles=true;
                            MiSalaDeJuego.LlenarCantJugadoresMas1(0,0,1);
                            String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarCantJugadoresONRondaSala/"+IdSala;
                            gson=new Gson();
                            new TraerIdsInsertarResultados().execute("PUT",url,gson.toJson(MiSalaDeJuego),"NRonda");
                        }
                        else
                        {
                            if(QueModifica.equals("CantJugadoresNTT"))
                            {
                                String CantJugadoresString= tvCantJugadores.getText().toString();
                                int CantJugadores= Integer.parseInt(CantJugadoresString) +1;
                                SalaDeJuegoTraida.CantJugadores=CantJugadores;
                                String MontoAGanarString= tvMontoGanador.getText().toString();
                                int MontoAGanar= Integer.parseInt(MontoAGanarString) +1;
                                SalaDeJuegoTraida.MontoAGanar= MontoAGanar;
                                tvCantJugadores.setText(String.valueOf(SalaDeJuegoTraida.CantJugadores));
                                tvMontoGanador.setText(String.valueOf(SalaDeJuegoTraida.MontoAGanar));
                            }
                            else
                            {
                                tvTimer.setText("Buscando preguntas 4/6");
                                /*SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                                MiSalaDeJuego.LlenarDisponibilidad(true);
                                String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarSalaDeJuegoMHC/"+IdSala;
                                gson=new Gson();
                                new ActualizarMHCSala().execute("PUT",url,gson.toJson(MiSalaDeJuego));*/
                                CambiarBotones(true);
                                BotonesVisibles=true;
                                SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                                MiSalaDeJuego.LlenarCantJugadoresMas1(0,0,1);
                                String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarCantJugadoresONRondaSala/"+IdSala;
                                gson=new Gson();
                                new TraerIdsInsertarResultados().execute("PUT",url,gson.toJson(MiSalaDeJuego),"NRonda");
                            }

                        }

                    }
                }

            }
            else
            {
                if(VotoFinalmente==false)
                {
                    IniciarActivityResultados();
                }
            }


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvTimer.setText(values[0]);
        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];
            if (method.equals("GET")) {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                AtributoRespuesta = parametros[2];
                try {
                    Response response = client.newCall(request).execute();
                    String jsonStr = response.body().string();
                    return Integer.parseInt(jsonStr);

                } catch (IOException e) {
                    Log.d("Error :", e.getMessage());
                    return 0;
                }
            } else
                {

                if(method.equals("POST"))
                {
                    String json = parametros[2];
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();

                    } catch (IOException e) {
                        Log.d("Error :", e.getMessage());

                    }
                    return 0;

                }
                else
                {
                    if(PublicarProgresoPut)
                    {
                        publishProgress("Buscando preguntas 2/6");
                    }
                    String json = parametros[2];
                    QueModifica= parametros[3];
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .put(body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        if(PublicarProgresoPut)
                        {
                            publishProgress("Buscando preguntas 3/6");
                            PublicarProgresoPut=false;
                        }
                        return -1;

                    } catch (IOException e)
                    {
                        Log.d("Error :", e.getMessage());
                        return 0;
                    }


                }


            }
        }
    }

    private class InsertarPreguntas extends AsyncTask<String, String, Integer> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPostExecute(Integer Id) {
            if (Id != 0)
            {
               if(Id==1)
               {
                  if(SegundosDisponiblesSala<=0)
                  {
                      SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                      MiSalaDeJuego.LlenarCantJugadoresMas1(NumRandJugadoresMontoAGanar,NumRandJugadoresMontoAGanar,-1);
                      String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarCantJugadoresONRondaSala/"+IdSala;
                      gson=new Gson();
                      new TraerIdsInsertarResultados().execute("PUT",url,gson.toJson(MiSalaDeJuego),"CantJugadoresPJSD0");
                  }
                  else
                  {
                      url ="http://apiminorityproyecto.azurewebsites.net/api/sala/GetSala/"+IdSala;
                      new BuscarDatosTask().execute(url);
                  }
               }
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvTimer.setText(values[0]);
        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];
            if(method.equals("POST"))
            {
                String json = parametros[2];
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                try
                {
                    client.newCall(request).execute();
                    return 1;
                }
                catch (IOException e)
                {
                    Log.d("Error :", e.getMessage());
                    return 0;
                }
            }
            else
            {
                return 0;
            }
        }
    }

    private class ActualizarSalaMenosDe3Jugadores extends AsyncTask<String, String, Integer> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPostExecute(Integer Id) {
            if (Id != 0)
            {
                tvTimer.setText("Saliendo de la sala 3/3");
                IniciarActivitySeleccionarSalas();
            }


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvTimer.setText(values[0]);
        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];
            if (method.equals("GET")) {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                AtributoRespuesta = parametros[2];
                try {
                    Response response = client.newCall(request).execute();
                    String jsonStr = response.body().string();
                    return Integer.parseInt(jsonStr);

                } catch (IOException e) {
                    Log.d("Error :", e.getMessage());
                    return 0;
                }
            } else
            {

                if(method.equals("POST"))
                {
                    String json = parametros[2];
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        IniciarActivityResultados();

                    } catch (IOException e) {
                        Log.d("Error :", e.getMessage());

                    }
                    return 0;

                }
                else
                {
                    publishProgress("Saliendo de la sala 1/3");
                    String json = parametros[2];
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .put(body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        publishProgress("Saliendo de la sala 2/3");
                        return -1;

                    } catch (IOException e)
                    {
                        Log.d("Error :", e.getMessage());
                        return 0;
                    }


                }


            }
        }
    }

    private void BuscarPreguntaConVec()
    {
        /*Random r = new Random();
        tvTimer.setText("Haciendo ultimos ajustes 3/3");
        if(PrimeraVezQueJuega)
        {
            int IdPreguntaABuscar = r.nextInt(MinMaxIds[1] +1 - MinMaxIds[0]) + MinMaxIds[0];
            url ="http://apiminorityproyecto.azurewebsites.net/api/pregunta/GetPregunta/"+IdPreguntaABuscar;
            new BuscarPregunta().execute("GET",url);
        }
        else
        {
            int IdPreguntaABuscar=0;
            switch (SalaDeJuegoTraida.NRonda)
            {
                case 2:
                    IdPreguntaABuscar = r.nextInt(MinMaxIds[3] +1 - MinMaxIds[2]) + MinMaxIds[2];
                    break;
                case 3:
                    IdPreguntaABuscar = r.nextInt(MinMaxIds[5] +1 - MinMaxIds[4]) + MinMaxIds[4];
                    break;
                case 4:
                    IdPreguntaABuscar = r.nextInt(MinMaxIds[7] +1 - MinMaxIds[6]) + MinMaxIds[6];
                    break;
            }*/
            Log.d("NRondaDespuesDeSetText5", tvNRonda.getText().toString());
            Timer.cancel();
            Log.d("NRondaDespuesDeSetText6", tvNRonda.getText().toString());
            url ="http://apiminorityproyecto.azurewebsites.net/api/pregunta/GetPregunta/"+IdSala+"/"+SalaDeJuegoTraida.NRonda;
            Log.d("NRondaDespuesDeSetText7", tvNRonda.getText().toString());
            /*url ="http://apiminorityproyecto.azurewebsites.net/api/pregunta/GetPregunta/"+IdSala;*/
            new BuscarPregunta().execute("GET",url);
    }


    private class ActualizarMHCSala extends AsyncTask<String, String, Integer> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        @Override
        protected void onPostExecute(Integer Id)
        {
            if(Id!=0 )
            {
                tvTimer.setText("Haciendo ultimos ajustes 1/3");
                CambiarBotones(true);
                BotonesVisibles=true;
                SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                MiSalaDeJuego.LlenarCantJugadoresMas1(0,0,1);
                String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarCantJugadoresONRondaSala/"+IdSala;
                gson=new Gson();
                new TraerIdsInsertarResultados().execute("PUT",url,gson.toJson(MiSalaDeJuego),"NRonda");
            }

        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url= parametros[1];
            Log.d("Put", "Puteo");
            publishProgress("Buscando preguntas 5/6");
            String json = parametros[2];
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.d("Put", "Puteo");
                publishProgress("Buscando preguntas 6/6");
                return  -1;

            }
            catch (IOException e)
            {
                Log.d("Error :", e.getMessage());
                return 0;

            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvTimer.setText(values[0]);
        }
    }

    private class BuscarPregunta extends AsyncTask<String, Void, Pregunta> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPostExecute(Pregunta MiPregunta) {
            Log.d("NRondaDespuesDeSetText8", tvNRonda.getText().toString());
            btnOpcion1.setText(MiPregunta.OpcionA);
            Log.d("NRondaDespuesDeSetText9", tvNRonda.getText().toString());
            btnOpcion2.setText(MiPregunta.OpcionB);
            Log.d("NRondaDDeSetText10", tvNRonda.getText().toString());
            DatosImportantesApp.SetIdPreguntaSala(IdSala,MiPregunta.Id);
            Log.d("NRondaDDeSetText11", tvNRonda.getText().toString());
            SetearTimer();
        }

        @Override
        protected Pregunta doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];

                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonStr = response.body().string();
                    return parsearResultado(jsonStr);

                } catch (IOException | JSONException e) {
                    Log.d("Error", e.getMessage());
                    return new Pregunta();
                }
        }

        Pregunta parsearResultado(String JSONstr) throws JSONException {
            gson= new Gson();
            Pregunta MiPregunta= gson.fromJson(JSONstr,Pregunta.class);
            return MiPregunta;
        }
    }

        /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            setContentView(R.layout.layout_jugabilidad_landscape);
            ReestablecerCondicionesLayout();

        }
        else
        {
            setContentView(R.layout.layout_jugabilidad_landscape);
            ReestablecerCondicionesLayout();
        }
    }*/

    private void ReestablecerCondicionesLayout()
    {
        ObtenerReferencias();
        tvSala.setText(SalaDeJuegoTraida.Nombre);
        tvCantJugadores.setText(String.valueOf(SalaDeJuegoTraida.CantJugadores));
        Log.d("TVNRSetText", "ReestablecerCondicionesLayout");
        tvNRonda.setText(String.valueOf(SalaDeJuegoTraida.NRonda));
        tvMontoGanador.setText(String.valueOf(SalaDeJuegoTraida.MontoAGanar));
        CambiarBotones(BotonesVisibles);
        if(VotoOpcion1)
        {
            btnOpcion1.setBackgroundColor(Color.parseColor("#FF000000"));
            btnOpcion1.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
        else
        { if(VotoOpcion2)
        {
            btnOpcion2.setBackgroundColor(Color.parseColor("#FF000000"));
            btnOpcion2.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
        }
        if(SegundosTimer=="1")
        {
            DeterminarVotoFinal();
            tvSegundosTimer.setText("1");
        }
        if(VotoFinalmente)
        {
            tvSegundosTimer.setText(SegundosTimer);
            
        }
    }
    private void SetearTimer()
    {   Log.d("NRondaDDeSetText12", tvNRonda.getText().toString());
        SetearListeners();
        Log.d("NRondaDDeSetText13", tvNRonda.getText().toString());
        Log.d("SetearTimer","start");
        Log.d("NRondaDDeSetText14", tvNRonda.getText().toString());
        Timer=new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvSegundosTimer.setText(String.valueOf(millisUntilFinished/1000));
                Log.d("NRondaDDeSetText15", tvNRonda.getText().toString());
                if(PusoNRonda==false && PrimeraVezQueJuega)
                {
                    SalaDeJuegoTraida.NRonda=1;
                    Log.d("TVNRSetText", "DespuesDeTimer30s");
                    tvNRonda.setText("1");
                    PusoNRonda=true;
                }
                Log.d("SetearTimer",String.valueOf(millisUntilFinished/1000));
                //SegundosTimer=tvSegundosTimer.getText().toString();

            }

            public void onFinish() {
                Log.d("SetearTimer","finish");
                if(VotoFinalmente==false)
                {
                    InsertarRespuestaBD("");
                }
                else
                {
                    IniciarActivityResultados();
                }
            }
        }.start();
    }
    private void SetearTimerSegundosDisponibles()
    {
       if(SegundosDisponiblesSala<=0)
       {
           if(PrimeraVezQueJuega)
           {
               /*Random rand= new Random();
               NumRandJugadoresMontoAGanar= rand.nextInt(49-3)+3;
               tvCantJugadores.setText(String.valueOf(NumRandJugadoresMontoAGanar+1));
               tvMontoGanador.setText(String.valueOf(NumRandJugadoresMontoAGanar+1));
               InsertoPreguntas=true;
               String url ="http://apiminorityproyecto.azurewebsites.net/api/pregunta/InsertarPreguntas";
               gson=new Gson();
               PreguntasxJuego MiPreguntasxJuego= new PreguntasxJuego();
               MiPreguntasxJuego.LlenarDatosPreguntasxJuego(IdSala);
               new InsertarPreguntas().execute("POST",url,gson.toJson(MiPreguntasxJuego));
               SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
               MiSalaDeJuego.LlenarCantJugadoresMas1(NumRandJugadoresMontoAGanar,NumRandJugadoresMontoAGanar,-1);
               String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarCantJugadoresONRondaSala/"+IdSala;
               gson=new Gson();
               new TraerIdsInsertarResultados().execute("PUT",url,gson.toJson(MiSalaDeJuego),"CantJugadoresPJSD0");*/
           }
           else
           {
               BuscarPreguntaConVec();
           }

       }
       else
       {
           Log.d("TimerSD", "Arranca");
           int SegundosDisponiblesSalaTimer= 1000*(SegundosDisponiblesSala+1);
           String SegundosTimer;
           if(SegundosDisponiblesSala%60<10)
           {
               SegundosTimer= "0"+String.valueOf(SegundosDisponiblesSala % 60);
           }
           else
           {
               SegundosTimer=String.valueOf(SegundosDisponiblesSala % 60);
           }
           String MinutosTimer= "0"+String.valueOf(SegundosDisponiblesSala/60);
           String HorasTimer= "00";
           TimerString= HorasTimer+":"+MinutosTimer+":"+SegundosTimer;
           tvTimer.setText(TimerString);
           Timer=new CountDownTimer(SegundosDisponiblesSalaTimer, 1000) {

               public void onTick(long millisUntilFinished) {

                   try {
                       TiempoDiferencia = dateFormatSoloHora.parse(TimerString);
                       cal= Calendar.getInstance();
                       cal.setTime(TiempoDiferencia);
                       cal.add(Calendar.SECOND,-1);
                       TiempoDiferencia=cal.getTime();
                       TimerString=dateFormatSoloHora.format(TiempoDiferencia);
                       tvTimer.setText(TimerString);
                       String TextoTimer=tvTimer.getText().toString();
                       Log.d("TimerSD", TextoTimer);
                       if(TextoTimer.equals("00:00:00"))
                       {
                           tvTimer.setText("Verificando CantJugadores 50%");
                           Timer.onFinish();
                       }
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }


                   /*if(SalaDeJuegoTraida.CantJugadores==3 && InsertoPreguntas==false)
                   {
                       InsertoPreguntas=true;
                       String url ="http://apiminorityproyecto.azurewebsites.net/api/pregunta/InsertarPreguntas/";
                       gson=new Gson();
                       PreguntasxJuego MiPreguntasxJuego= new PreguntasxJuego();
                       MiPreguntasxJuego.LlenarDatosPreguntasxJuego(IdSala);
                       new InsertarPreguntas().execute("POST",url,gson.toJson(MiPreguntasxJuego));
                   }
                   else
                   {*/
                       url ="http://apiminorityproyecto.azurewebsites.net/api/sala/GetSala/"+IdSala;
                       new BuscarDatosTask().execute(url);

                   /*}url ="http://apiminorityproyecto.azurewebsites.net/api/sala/GetSala/"+IdSala;
                   new BuscarDatosTask().execute(url);
                   /*Random rand= new Random();
                   int Num0o1= rand.nextInt(2);
                   String CantJugadoresSalaString= tvCantJugadores.getText().toString();
                   int CantJugadoresSala= Integer.parseInt(CantJugadoresSalaString);
                   SalaDeJuegoTraida.CantJugadores=CantJugadoresSala;
                   String MontoAGanarString= tvMontoGanador.getText().toString();
                   int MontoAGanar= Integer.parseInt(MontoAGanarString);
                   SalaDeJuegoTraida.MontoAGanar=MontoAGanar;
                   if(Num0o1==1 && CantJugadoresSala<50)
                   {
                       SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                       MiSalaDeJuego.LlenarCantJugadoresMas1(CantJugadoresSala,MontoAGanar,-1);
                       String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarCantJugadoresONRondaSala/"+IdSala;
                       gson=new Gson();
                       new TraerIdsInsertarResultados().execute("PUT",url,gson.toJson(MiSalaDeJuego),"CantJugadoresNTT");

                   }
                   else
                   {
                       Log.d("Mayor50", "Mayor50");
                   }*/
               }

               public void onFinish()
               {
                   //tvTimer.setText("Verificando CantJugadores 100%");
                   String CantJugadoresSalaString= tvCantJugadores.getText().toString();
                   int CantJugadoresSala= Integer.parseInt(CantJugadoresSalaString);
                   String MontoAGanarString= tvMontoGanador.getText().toString();
                   int MontoAGanar= Integer.parseInt(MontoAGanarString);
                   SalaDeJuegoTraida.CantJugadores=CantJugadoresSala;
                   SalaDeJuegoTraida.MontoAGanar=MontoAGanar;
                   if(CantJugadoresSala<3==false)
                   {
                       /*tvTimer.setText("Buscando preguntas 1/6");
                       MiSalaDeJuego.LlenarCantJugadoresMas1(CantJugadoresSala-1,MontoAGanar-1,-1);
                       SalaDeJuegoTraida.MontoAGanar=SalaDeJuegoTraida.MontoAGanar+1;
                       String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarCantJugadoresONRondaSala/"+IdSala;
                       gson=new Gson();
                       new TraerIdsInsertarResultados().execute("PUT",url,gson.toJson(MiSalaDeJuego),"CantJugadoresTT");
                       tvTimer.setText("Haciendo ultimos ajustes 2/3");*/
                       Log.d("NRondaAntesDeSetText", tvNRonda.getText().toString());
                       Log.d("TVNRSetText", "DespuesDeSetearTimerDisponible");
                       tvNRonda.setText("1");
                       Log.d("NRondaDespuesDeSetText1", tvNRonda.getText().toString());
                       CambiarBotones(true);
                       Log.d("NRondaDespuesDeSetText2", tvNRonda.getText().toString());
                       BotonesVisibles=true;
                       Log.d("NRondaDespuesDeSetText3", tvNRonda.getText().toString());
                       SalaDeJuegoTraida.NRonda=1;
                       Log.d("NRondaDespuesDeSetText4", tvNRonda.getText().toString());
                       BuscarPreguntaConVec();
                   }
                   else
                   {
                       /*tvTimer.setText("CantJugadores menor a 3");
                       MiSalaDeJuego.LlenarCantJugadoresMas1(CantJugadoresSala,MontoAGanar,-1);
                       String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ActualizarSalaDeJuegoMenosDe3Jugadores/"+IdSala;
                       gson=new Gson();
                       new ActualizarSalaMenosDe3Jugadores().execute("PUT",url,gson.toJson(MiSalaDeJuego));*/
                       IniciarActivitySeleccionarSalas();
                   }


               }
           }.start();
       }
    }
    private void CambiarBotones(boolean SegundosDisp120)
    {
        tvTimer.setText("");
        if(SegundosDisp120)
        {
            btnOpcion1.setEnabled(SegundosDisp120);
            btnOpcion1.setVisibility(View.VISIBLE);
            btnOpcion2.setVisibility(View.VISIBLE);
            btnOpcion2.setEnabled(SegundosDisp120);
            btnVotar.setEnabled(SegundosDisp120);
            btnVotar.setVisibility(View.VISIBLE);
        }
        else
        {
            btnOpcion1.setEnabled(SegundosDisp120);
            btnOpcion1.setVisibility(View.INVISIBLE);
            btnOpcion2.setVisibility(View.INVISIBLE);
            btnOpcion2.setEnabled(SegundosDisp120);
            btnVotar.setEnabled(SegundosDisp120);
            btnVotar.setVisibility(View.INVISIBLE);
        }
    }
    private void ObtenerReferencias()
    {
        btnOpcion1= (Button) findViewById(R.id.btnOpcion1);
        btnOpcion2=(Button)findViewById(R.id.btnOpcion2);
        btnVotar=(Button) findViewById(R.id.btnVotar);
        tvSegundosTimer=(TextView) findViewById(R.id.tvSegundosTimer);
        tvVotoFinal= (TextView) findViewById(R.id.tvVotoFinal);
        tvMontoGanador= (TextView) findViewById(R.id.tvMontoGanador);
        tvCantJugadores= (TextView) findViewById(R.id.tvCantJugadores);
        tvSala= (TextView) findViewById(R.id.tvSala);
        tvNRonda=(TextView) findViewById(R.id.tvNRonda);
        tvTimer= (TextView) findViewById(R.id.tvTimer);

    }
    private void SetearListeners()
    {
        btnOpcion1.setOnClickListener(btnOpcion_click);
        btnOpcion2.setOnClickListener(btnOpcion_click);
        btnVotar.setOnClickListener(btnVotar_click);
    }
    private View.OnClickListener btnOpcion_click= new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Idbtn= view.getId();
            int idbtnOpcion1=R.id.btnOpcion1;
            if(Idbtn==idbtnOpcion1)
            {
                if(VotoOpcion1==false)
                {
                  CambiarColorBotonOpcion(btnOpcion1,btnOpcion2);
                    VotoOpcion1=true;
                    VotoOpcion2=false;
                }
            }
            else
            {
                if(VotoOpcion2==false)
                {
                    CambiarColorBotonOpcion(btnOpcion2,btnOpcion1);
                    VotoOpcion2=true;
                    VotoOpcion1=false;
                }
            }
         }
    };

    private void CambiarColorBotonOpcion(Button btnClickeado,Button btnNoClickeado)
    {

           BotonVotado(btnClickeado);
           BotonNoVotado(btnNoClickeado);
    }
    private void BotonVotado(Button btn)
    {
        btn.setBackgroundColor(Color.parseColor("#FF000000"));
        btn.setTextColor(Color.parseColor("#FFFFFFFF"));
    }
    private void BotonNoVotado(Button btn)
    {
        btn.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        btn.setTextColor(Color.parseColor("#FF000000"));
    }
    private View.OnClickListener btnVotar_click= new View.OnClickListener(){
        @Override
        public void onClick(View view)
        {
            //Timer.cancel();
            Toast msg= Toast.makeText(getApplicationContext(),"Voto Enviado",Toast.LENGTH_SHORT);
            msg.show();
            btnVotar.setEnabled(false);
            DeterminarVotoFinal();
            VotoFinalmente=true;
        }
    };
    private void DeterminarVotoFinal()
    {
        Opcion1=btnOpcion1.getText().toString();
        Opcion2=btnOpcion2.getText().toString();
        btnOpcion1.setEnabled(false);
        btnOpcion2.setEnabled(false);
        btnVotar.setEnabled(false);
        if(VotoOpcion1==false && VotoOpcion2==false)
        {
            VotoFinal="";
        }
        else
        {
            if(VotoOpcion1)
            {
                VotoFinal=Opcion1;
            }
            else
            {
                VotoFinal=Opcion2;
            }
        }
        InsertarRespuestaBD(VotoFinal);
        /*url="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/usuarios/"+Usuario;
        new TraerIdsInsertarResultados().execute("GET",url,"Usuario");*/
    }
    private void IniciarActivityResultados()
    {
        Intent MiIntent= new Intent(Activity_Jugabilidad.this,Activity_Resultados.class);
        Bundle ElBundle= new Bundle();
        ElBundle.putString("Voto",VotoFinal);
        int CantJugadores= Integer.parseInt(tvCantJugadores.getText().toString());
        int NRonda= Integer.parseInt(tvNRonda.getText().toString());;
        ElBundle.putInt("CantJugadores",CantJugadores);
        ElBundle.putInt("IdSala",MiRespuesta.Sala);
        ElBundle.putInt("IdPregunta",MiRespuesta.Pregunta);
        ElBundle.putInt("NRonda",NRonda);
        ElBundle.putString("Usuario",Usuario);
        ElBundle.putString("Opcion1",Opcion1);
        ElBundle.putString("Opcion2",Opcion2);
        //ElBundle.putInt("Monedas",MonedasUsuario);
        MiIntent.putExtras(ElBundle);
        startActivity(MiIntent);
    }
    private void IniciarActivitySeleccionarSalas() {
        Intent MiIntent = new Intent(Activity_Jugabilidad.this, Activity_SeleccionarSala.class);
        startActivity(MiIntent);
        finish();
    }

    private void IniciarActivityPremiacion(int IdSala, int MontoGanador, int CantJugadores)
    {
        Intent MiIntent = new Intent(Activity_Jugabilidad.this, Activity_Premiacion.class);
        Bundle ElBundle= new Bundle();
        ElBundle.putInt("IdSala",IdSala);
        ElBundle.putInt("MontoAGanar",MontoGanador);
        ElBundle.putInt("CantJugadores",CantJugadores);
        MiIntent.putExtras(ElBundle);
        startActivity(MiIntent);
        finish();
    }

    private void InsertarRespuestaBD(String VotoJugador)
    {
        MiRespuesta=new Respuesta();
        MiRespuesta.RespuestaParcial=VotoJugador;
        MiRespuesta.RespuestaFinal=MiRespuesta.RespuestaParcial;
        MiRespuesta.Sala=IdSala;
        MiRespuesta.Usuario=DatosImportantesApp.GetIdUsuario();
        MiRespuesta.Pregunta=DatosImportantesApp.GetIdPreguntaSala(MiRespuesta.Sala);
        MiRespuesta.NRonda=SalaDeJuegoTraida.NRonda;
        url ="http://apiminorityproyecto.azurewebsites.net/api/respuesta/InsertarRespuesta";
        new TraerIdsInsertarResultados().execute("POST",url,gson.toJson(MiRespuesta));
    }
}

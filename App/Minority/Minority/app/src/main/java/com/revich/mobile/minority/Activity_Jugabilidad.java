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

import java.io.IOException;
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
    TextView tvSegundosTimer,tvVotoFinal,tvMontoGanador,tvCantJugadores,tvNRonda,tvSala;
    boolean VotoOpcion1=false;
    boolean VotoOpcion2=false;
    boolean VotoFinalmente=false, BotonesVisibles=false;
    boolean PrimeraVezQueJuega;
    String VotoFinal,Usuario,SegundosTimer,url,AtributoRespuesta,QueModifica,Opcion1,Opcion2;
    int Idbtn, IdSala, SegundosDisponiblesSala,MonedasUsuario;
    int [] MinMaxIds= new int [] {2,7,8,13,14,19,20,25};
    CountDownTimer Timer;
    Gson gson;
    SalasDeJuego SalaDeJuegoTraida;
    Date TiempoALlegarSala;
    SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
    Respuesta MiRespuesta;
    Date HoraActual;
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
            tvCantJugadores.setText(String.valueOf(MiSalaDeJuego.CantJugadores));
            tvNRonda.setText(String.valueOf(MiSalaDeJuego.NRonda));
            tvMontoGanador.setText(String.valueOf(MiSalaDeJuego.MontoAGanar));
            SalaDeJuegoTraida=MiSalaDeJuego;
            if(MiSalaDeJuego.CantJugadores<3 && PrimeraVezQueJuega==false)
            {
             IniciarActivitySeleccionarSalas();
            }
            else
            {
                SetearTimerSegundosDisponibles();
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
                Log.d("Error", e.getMessage());
                return new SalasDeJuego();
            }
        }

        SalasDeJuego parsearResultado(String JSONstr) throws JSONException {
            gson= new Gson();
            SalasDeJuego MiSalaDeJuego= gson.fromJson(JSONstr,SalasDeJuego.class);
            return MiSalaDeJuego;
        }
    }

    private class TraerIdsInsertarResultados extends AsyncTask<String, Void, Integer> {
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
                        tvNRonda.setText("1");
                        BuscarPreguntaConVec(PrimeraVezQueJuega);
                    }
                    else
                    {
                        if(QueModifica.equals("CantJugadoresPJSD0"))
                        {
                            SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                            MiSalaDeJuego.LlenarDisponibilidad(true);
                            String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarSalaDeJuegoMHC/"+IdSala;
                            gson=new Gson();
                            new ActualizarMHCSala().execute("PUT",url,gson.toJson(MiSalaDeJuego));
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
                                SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                                MiSalaDeJuego.LlenarDisponibilidad(true);
                                String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarSalaDeJuegoMHC/"+IdSala;
                                gson=new Gson();
                                new ActualizarMHCSala().execute("PUT",url,gson.toJson(MiSalaDeJuego));
                            }

                        }

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
                    String json = parametros[2];
                    QueModifica= parametros[3];
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .put(body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
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

    private void BuscarPreguntaConVec(boolean PrimeraVezQueJuega)
    {
        Random r = new Random();
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
            }
            url ="http://apiminorityproyecto.azurewebsites.net/api/pregunta/GetPregunta/"+IdPreguntaABuscar;
            new BuscarPregunta().execute("GET",url);
        }
    }

    private class ActualizarMHCSala extends AsyncTask<String, Void, Integer> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        @Override
        protected void onPostExecute(Integer Id)
        {
            if(Id!=0 )
            {
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
            String json = parametros[2];
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.d("Put", "Puteo");
                return  -1;

            }
            catch (IOException e)
            {
                Log.d("Error :", e.getMessage());
                return 0;

            }

        }
    }

    private class BuscarPregunta extends AsyncTask<String, Void, Pregunta> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPostExecute(Pregunta MiPregunta) {
            btnOpcion1.setText(MiPregunta.OpcionA);
            btnOpcion2.setText(MiPregunta.OpcionB);
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
    {
        Timer=new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvSegundosTimer.setText(String.valueOf(millisUntilFinished/1000));
                SegundosTimer=tvSegundosTimer.getText().toString();
                SetearListeners();
            }

            public void onFinish() {
                DeterminarVotoFinal();
            }
        }.start();
    }
    private void SetearTimerSegundosDisponibles()
    {
       if(SegundosDisponiblesSala<=0)
       {
           if(PrimeraVezQueJuega)
           {
               Random rand= new Random();
               int NumRandJugadoresMontoAGanar= rand.nextInt(49-3)+3;
               tvCantJugadores.setText(String.valueOf(NumRandJugadoresMontoAGanar+1));
               tvMontoGanador.setText(String.valueOf(NumRandJugadoresMontoAGanar+1));
               SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
               MiSalaDeJuego.LlenarCantJugadoresMas1(NumRandJugadoresMontoAGanar,NumRandJugadoresMontoAGanar,-1);
               String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarCantJugadoresONRondaSala/"+IdSala;
               gson=new Gson();
               new TraerIdsInsertarResultados().execute("PUT",url,gson.toJson(MiSalaDeJuego),"CantJugadoresPJSD0");
           }
           else
           {
               BuscarPreguntaConVec(PrimeraVezQueJuega);
           }

       }
       else
       {
           int SegundosDisponiblesSalaTimer= 1000*(SegundosDisponiblesSala+1);
           Timer=new CountDownTimer(SegundosDisponiblesSalaTimer, 1000) {

               public void onTick(long millisUntilFinished) {

                   Random rand= new Random();
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
                   }
               }

               public void onFinish()
               {
                   String CantJugadoresSalaString= tvCantJugadores.getText().toString();
                   int CantJugadoresSala= Integer.parseInt(CantJugadoresSalaString)-1;
                   String MontoAGanarString= tvMontoGanador.getText().toString();
                   int MontoAGanar= Integer.parseInt(MontoAGanarString)-1;
                   SalaDeJuegoTraida.CantJugadores=CantJugadoresSala;
                   SalaDeJuegoTraida.MontoAGanar=MontoAGanar;
                   SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                   MiSalaDeJuego.LlenarCantJugadoresMas1(CantJugadoresSala,MontoAGanar,-1);
                   String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarCantJugadoresONRondaSala/"+IdSala;
                   gson=new Gson();
                   new TraerIdsInsertarResultados().execute("PUT",url,gson.toJson(MiSalaDeJuego),"CantJugadoresTT");

               }
           }.start();
       }
    }
    private void CambiarBotones(boolean SegundosDisp120)
    {
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
            Timer.cancel();
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
        MiRespuesta=new Respuesta();
        MiRespuesta.RespuestaParcial=VotoFinal;
        MiRespuesta.RespuestaFinal=MiRespuesta.RespuestaParcial;
        MiRespuesta.Sala=IdSala;
        url="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/usuarios/"+Usuario;
        new TraerIdsInsertarResultados().execute("GET",url,"Usuario");
    }
    private void IniciarActivityResultados()
    {
        Intent MiIntent= new Intent(Activity_Jugabilidad.this,Activity_Resultados.class);
        Bundle ElBundle= new Bundle();
        ElBundle.putString("Voto",VotoFinal);
        int CantJugadores= Integer.parseInt(tvCantJugadores.getText().toString());
        int NRonda= Integer.parseInt(tvNRonda.getText().toString());
        ElBundle.putInt("CantJugadores",CantJugadores);
        ElBundle.putInt("IdSala",MiRespuesta.Sala);
        ElBundle.putInt("IdPregunta",MiRespuesta.Pregunta);
        ElBundle.putInt("NRonda",NRonda);
        ElBundle.putInt("IdUsuario",MiRespuesta.Usuario);
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
    }
}

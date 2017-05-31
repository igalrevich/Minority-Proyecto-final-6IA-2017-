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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

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
    boolean VotoFinalmente=false;
    String VotoFinal,NombreBoton,SegundosTimer,url,AtributoRespuesta;
    int Idbtn;
    CountDownTimer Timer;
    Gson gson;
    SalasDeJuego SalaDeJuegoTraida;
    Respuesta MiRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jugabilidad_landscape);
        getSupportActionBar().hide();
        ObtenerReferencias();
        url ="http://apiminorityproyecto.azurewebsites.net/api/rest/GetSala/1";
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
            SetearTimer();

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

    private class TraerIdsInsertarResultados extends AsyncTask<String, Void, int> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPostExecute(int Id) {
            if (Id != 0)
            {
                switch (AtributoRespuesta)
                {
                    case "Sala":
                        MiRespuesta.Sala = Id;
                        break;
                    case "Usuario":
                        MiRespuesta.Usuario = Id;
                        break;
                    case "Pregunta":
                        MiRespuesta.Pregunta = Id;
                        break;
                }
            }
        }

        @Override
        protected int doInBackground(String... parametros) {
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
            } else {
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
        }
    }




        @Override
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
    }

    private void ReestablecerCondicionesLayout()
    {
        ObtenerReferencias();
        tvSala.setText(SalaDeJuegoTraida.Nombre);
        tvCantJugadores.setText(String.valueOf(SalaDeJuegoTraida.CantJugadores));
        tvNRonda.setText(String.valueOf(SalaDeJuegoTraida.NRonda));
        tvMontoGanador.setText(String.valueOf(SalaDeJuegoTraida.MontoAGanar));
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
            int idbtnOpcion2=R.id.btnOpcion2;
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
            DeterminarVotoFinal();
            VotoFinalmente=true;
        }
    };
    private void DeterminarVotoFinal()
    {
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
                VotoFinal=btnOpcion1.getText().toString();
            }
            else
            {
                VotoFinal=btnOpcion2.getText().toString();
            }
        }
        MiRespuesta=new Respuesta();
        String Usuario="IgalRevich";
        String Opcion1= btnOpcion1.getText().toString();
        String Sala= tvSala.getText().toString();
        url="http://apiminorityproyecto.azurewebsites.net/api/Rest/GetIdByNombre/salasdejuegos/"+Sala;
        new TraerIdsInsertarResultados().execute("GET",url,"Sala");
        url="http://apiminorityproyecto.azurewebsites.net/api/Rest/GetIdByNombre/usuarios/"+Usuario;
        new TraerIdsInsertarResultados().execute("GET",url,"Sala");
        url="http://apiminorityproyecto.azurewebsites.net/api/Rest/GetIdByNombre/preguntas/"+Opcion1;
        new TraerIdsInsertarResultados().execute("GET",url,"Sala");
        MiRespuesta.RespuestaParcial=VotoFinal;
        MiRespuesta.RespuestaFinal=MiRespuesta.RespuestaParcial;
        /*url ="http://apiminorityproyecto.azurewebsites.net/api/rest/InsertarRespuesta";
        new TraerIdsInsertarResultados().execute("POST",url,gson.toJson(MiRespuesta));*/
        IniciarActivityResultados();
    }
    private void IniciarActivityResultados()
    {
        Intent MiIntent= new Intent(Activity_Jugabilidad.this,Activity_Resultados.class);
        Bundle ElBundle= new Bundle();
        ElBundle.putString("Voto",VotoFinal);
        MiIntent.putExtras(ElBundle);
        startActivity(MiIntent);
    }
}

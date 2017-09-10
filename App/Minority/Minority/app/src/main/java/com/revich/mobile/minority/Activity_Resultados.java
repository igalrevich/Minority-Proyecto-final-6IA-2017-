package com.revich.mobile.minority;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
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
    TextView tvOpcion1, tvOpcion2, tvVotosOpcion1, tvVotosOpcion2, tvGanastePerdiste, tvIndicacion1, tvIndicacion2;
    String Opcion1, Opcion2, ResultadoUsuario, VotoJugador, url, Opcion, UsuarioString, ResultadoDerrota;
    boolean MayoriaOpcion1 = false, VotoOpcion1 = false, Gano;
    int CantVotosOpcion1, CantVotosOpcion2, NRonda, Sala, Usuario, Pregunta, CantJugadores, IndiceFor, MonedasUsuario;
    Random rand;
    Respuesta MiRespuesta;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__resultados);
        getSupportActionBar().hide();
        ObtenerReferencias();
        GenerarResultadoUsuarioParte1();

    }


    private void ObtenerReferencias() {
        tvOpcion1 = (TextView) findViewById(R.id.tvOpcion1);
        tvOpcion2 = (TextView) findViewById(R.id.tvOpcion2);
        tvVotosOpcion1 = (TextView) findViewById(R.id.tvVotosOpcion1);
        tvVotosOpcion2 = (TextView) findViewById(R.id.tvVotosOpcion2);
        tvGanastePerdiste = (TextView) findViewById(R.id.tvGanastePerdiste);
        tvIndicacion1 = (TextView) findViewById(R.id.tvIndicacion1);
        tvIndicacion2 = (TextView) findViewById(R.id.tvIndicacion2);
    }

    private void GenerarVotosAlAzar() {
        tvIndicacion2.setText("25%");
        Intent ELIntentQueVino = getIntent();
        Bundle ElBundle = ELIntentQueVino.getExtras();
        Opcion1 = ElBundle.getString("Opcion1");
        Opcion2 = ElBundle.getString("Opcion2");
        VotoJugador = ElBundle.getString("Voto");
        CantJugadores = ElBundle.getInt("CantJugadores");
        //MonedasUsuario= ElBundle.getInt("Monedas");
        Sala = ElBundle.getInt("IdSala");
        NRonda = ElBundle.getInt("NRonda");
        Usuario = ElBundle.getInt("IdUsuario");
        UsuarioString = ElBundle.getString("Usuario");
        Pregunta = ElBundle.getInt("IdPregunta");
        rand = new Random();
        for (int i = 1; i <= CantJugadores - 1; i++) {
            int IdUsuario = i;
            if (IdUsuario == Usuario) {
                IdUsuario = 50;
            }
            MiRespuesta = new Respuesta();
            MiRespuesta.Usuario = IdUsuario;
            MiRespuesta.Pregunta = Pregunta;
            MiRespuesta.Sala = Sala;
            int OpcionesDeVoto = rand.nextInt(3);
            switch (OpcionesDeVoto) {
                case 0:
                    MiRespuesta.RespuestaParcial = Opcion1;
                    break;
                case 1:
                    MiRespuesta.RespuestaParcial = Opcion2;
                    break;
                case 2:
                    MiRespuesta.RespuestaParcial = "";
                    break;
            }
            MiRespuesta.RespuestaFinal = MiRespuesta.RespuestaParcial;
            url = "http://apiminorityproyecto.azurewebsites.net/api/respuesta/InsertarRespuesta";
            gson = new Gson();
            new TraerResultados().execute("POST", url, gson.toJson(MiRespuesta), String.valueOf(i));

        }
        /*CantVotosEnBlanco= rand.nextInt(50);
        CantVotosOpciones=49-CantVotosEnBlanco;
        CantVotosOpcion1=rand.nextInt(CantVotosOpciones+1);
        CantVotosOpcion2=CantVotosOpciones-CantVotosOpcion1;*/
    }

    private class ActualizarSalasBD extends AsyncTask<String, Void, Integer> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPostExecute(Integer num) {
            if (num != -1) {
                if (Gano) {
                    IniciarActivityJugabilidad();
                } else {
                    IniciarActivitySeleccionarSalas();
                }

            }

        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];
            if (method.equals("PUT")) {
                String json = parametros[2];
                Gano = Boolean.parseBoolean(parametros[3]);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .put(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    return 1;

                } catch (IOException e) {
                    Log.d("Error :", e.getMessage());
                    return -1;
                }
            } else {
                return -1;
            }


        }
    }



    private class TraerResultados extends AsyncTask<String, Void, Resultado> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPostExecute(Resultado MiResultado) {

            //tvIndicacion2.setText("3/3");
            tvOpcion1.setText(Opcion1);
            tvOpcion2.setText(Opcion2);
            tvVotosOpcion1.setText(String.valueOf(MiResultado.CantVotosOpcionA));
            tvVotosOpcion2.setText(String.valueOf(MiResultado.CantVotosOpcionB));
            if(MiResultado.Empate)
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
                if(MiResultado.MayoriaOpcionA)
                {
                    tvOpcion2.setTextColor(Color.parseColor("#8ef686"));
                    tvVotosOpcion2.setTextColor(Color.parseColor("#8ef686"));
                    tvOpcion1.setTextColor(Color.parseColor("#f61525"));
                    tvVotosOpcion1.setTextColor(Color.parseColor("#f61525"));
                }
                else
                {
                    tvOpcion2.setTextColor(Color.parseColor("#f61525"));
                    tvVotosOpcion2.setTextColor(Color.parseColor("#f61525"));
                    tvOpcion1.setTextColor(Color.parseColor("#8ef686"));
                    tvVotosOpcion1.setTextColor(Color.parseColor("#8ef686"));
                }
                GanoOPerdio(MiResultado.Gano);
             }
             Esperar5SegundosAntesDeActualizar(MiResultado.Gano);
            /*if (CantVotos != -1 && CantVotos != -3) {
                if (CantVotos != -2) {
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
                } else {
                    if (IndiceFor == CantJugadores - 1) {
                        GenerarResultadoUsuarioParte1();
                    }
                }
            } else {
                if (CantVotos == -3)
                {
                        //if(ResultadoDerrota.equals("Empato"))
                        Esperar3SegundosAntesDeActualizar(false);
                }
            }*/


        }

        @Override
        protected Resultado doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];
            String json = parametros[2];
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try
            {
                Response response = client.newCall(request).execute();
                String jsonStr = response.body().string();
                gson= new Gson();
                Resultado MiResultado= gson.fromJson(jsonStr,Resultado.class);
                return MiResultado;

            } catch (IOException e) {
                Log.d("Error :", e.getMessage());
                return new Resultado();
            }
            /*if (method.equals("GET")) {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Opcion = parametros[2];
                try {
                    Response response = client.newCall(request).execute();
                    String jsonStr = response.body().string();
                    return Integer.parseInt(jsonStr);

                } catch (IOException e) {
                    Log.d("Error :", e.getMessage());
                    return -1;
                }
            } else {
                if (method.equals("POST")) {
                    String json = parametros[2];
                    IndiceFor = Integer.parseInt(parametros[3]);
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        return -2;

                    } catch (IOException e) {
                        Log.d("Error :", e.getMessage());
                        return -1;
                    }

                } else {
                    Request request = new Request.Builder()
                            .url(url)
                            .delete()
                            .build();
                    try {
                        //ResultadoDerrota=parametros[2];
                        Response response = client.newCall(request).execute();
                        return -3;
                    } catch (IOException e) {
                        Log.d("Error :", e.getMessage());
                        return -1;
                    }

                }
            }*/
        }
    }

    private void GenerarResultadoUsuarioParte1() {
        Intent ELIntentQueVino = getIntent();
        Bundle ElBundle = ELIntentQueVino.getExtras();
        Opcion1 = ElBundle.getString("Opcion1");
        Opcion2 = ElBundle.getString("Opcion2");
        VotoJugador = ElBundle.getString("Voto");
        Sala = ElBundle.getInt("IdSala");
        NRonda = ElBundle.getInt("NRonda");
        VotoACalcular MiVotoACalcular= new VotoACalcular();
        MiVotoACalcular.LlenarDatos(DatosImportantesApp.GetIdUsuario(),Opcion1,Opcion2,VotoJugador,Sala,NRonda);
        gson=new Gson();
        /*CantJugadores = ElBundle.getInt("CantJugadores");
        MonedasUsuario= ElBundle.getInt("Monedas");
        UsuarioString = ElBundle.getString("Usuario");
        Pregunta = ElBundle.getInt("IdPregunta");
        tvIndicacion2.setText("1/3");*/
        url = "http://apiminorityproyecto.azurewebsites.net/api/respuesta/CalcularResultados";
        new TraerResultados().execute("POST", url, gson.toJson(MiVotoACalcular));
    }

    private void IniciarActivityJugabilidad() {
        Intent MiIntent = new Intent(Activity_Resultados.this, Activity_Jugabilidad.class);
        Bundle ElBundle = new Bundle();
        ElBundle.putString("Usuario", UsuarioString);
        ElBundle.putInt("IdSala", Sala);
        ElBundle.putBoolean("PrimeraVezQueJuegaSala", false);
        ElBundle.putInt("SegundosParaReclutarJugadores", 0);
        MiIntent.putExtras(ElBundle);
        startActivity(MiIntent);
        finish();
    }

    private void IniciarActivitySeleccionarSalas() {
        Intent MiIntent = new Intent(Activity_Resultados.this, Activity_SeleccionarSala.class);
        startActivity(MiIntent);
        finish();
    }

    private void ActualizarSalas(boolean Gano) {
        url = "http://apiminorityproyecto.azurewebsites.net/api/Sala/ModificarCantJugadoresYRespuestasSala/" + Sala;
        SalasDeJuego MiSalaDeJuego = new SalasDeJuego();
        int CantJugadoresPasanDeRonda;
        int NuevoNRonda = NRonda + 1;
        if (MayoriaOpcion1) {
            CantJugadoresPasanDeRonda = Integer.parseInt(tvVotosOpcion2.getText().toString());

        } else {
            CantJugadoresPasanDeRonda = Integer.parseInt(tvVotosOpcion1.getText().toString());
        }

        MiSalaDeJuego.ActualizarDatosSalas(CantJugadoresPasanDeRonda, NuevoNRonda);
        gson = new Gson();
        new ActualizarSalasBD().execute("PUT", url, gson.toJson(MiSalaDeJuego), String.valueOf(Gano));
    }

    private void GenerarResultadoUsuarioParte2() {
        tvIndicacion2.setText("2/3");
        if (VotoJugador.equals("") == false)
        {
            if (VotoJugador.equals(Opcion1))
            {
                VotoOpcion1 = true;
            }
            if (CantVotosOpcion1 != CantVotosOpcion2)
            {
                if (CantVotosOpcion1 > CantVotosOpcion2)
                {
                    MayoriaOpcion1 = true;
                }
                ResultadoUsuario = CheckearResultados(MayoriaOpcion1, VotoJugador);

            }
            else
            {
                ResultadoUsuario = "Empato";
            }
        }
        else
        {
            ResultadoUsuario = "Perdio";
        }
        ImprimirResultadosPantalla(ResultadoUsuario);
    }

    private String CheckearResultados(boolean MayoriaOpcion1, String VotoJugador) {
        String Resultado = "";
        if (MayoriaOpcion1)
        {
            if (VotoOpcion1)
            {
                Resultado = "Perdio";
            }
            else
            {
                Resultado = "Gano";
            }
        }
        else
        {
            if (VotoOpcion1)
            {
                Resultado = "Gano";
            }
            else
            {
                Resultado = "Perdio";
            }
        }
        return Resultado;
    }

    private void ImprimirResultadosPantalla(String Resultado) {
        tvIndicacion2.setText("3/3");
        tvOpcion1.setText(Opcion1);
        tvOpcion2.setText(Opcion2);
        tvVotosOpcion1.setText(String.valueOf(CantVotosOpcion1));
        tvVotosOpcion2.setText(String.valueOf(CantVotosOpcion2));
        if (MayoriaOpcion1)
        {
            tvOpcion2.setTextColor(Color.parseColor("#8ef686"));
            tvVotosOpcion2.setTextColor(Color.parseColor("#8ef686"));
            tvOpcion1.setTextColor(Color.parseColor("#f61525"));
            tvVotosOpcion1.setTextColor(Color.parseColor("#f61525"));
            GanoOPerdio(true);
        }
        else
        {
            if (Resultado == "Empato")
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
                url = "http://apiminorityproyecto.azurewebsites.net/api/sala/DeleteUsuarioxSala/" + Usuario;
                new TraerResultados().execute("DELETE", url);

            }
            else
            {
                tvOpcion2.setTextColor(Color.parseColor("#f61525"));
                tvVotosOpcion2.setTextColor(Color.parseColor("#f61525"));
                tvOpcion1.setTextColor(Color.parseColor("#8ef686"));
                tvVotosOpcion1.setTextColor(Color.parseColor("#8ef686"));
                GanoOPerdio(false);
            }
        }
    }

    private void GanoOPerdio(boolean Gano) {
        if (Gano)
        {
            tvGanastePerdiste.setTextColor(Color.parseColor("#8ef686"));
            tvIndicacion1.setTextColor(Color.parseColor("#8ef686"));
            tvIndicacion2.setTextColor(Color.parseColor("#8ef686"));
            tvGanastePerdiste.setText("Ganaste!!");
            tvIndicacion1.setText("Sos parte de la minoria");
            tvIndicacion2.setText("Pasaste de ronda!!!");
        }
        else
        {
            tvGanastePerdiste.setTextColor(Color.parseColor("#f61525"));
            tvIndicacion1.setTextColor(Color.parseColor("#f61525"));
            tvIndicacion2.setTextColor(Color.parseColor("#f61525"));
            tvGanastePerdiste.setText("Perdiste!!");
            tvIndicacion1.setText("Sos parte de la mayoria");
            tvIndicacion2.setText("Quedaste eliminado!!!");
            /*url = "http://apiminorityproyecto.azurewebsites.net/api/sala/DeleteUsuarioxSala/" + Usuario;
            new TraerIdsInsertarResultados().execute("DELETE", url);*/
        }
    }

    private void Esperar5SegundosAntesDeActualizar(final boolean GanoRonda)
    {
        CountDownTimer Timer=new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish()
            {
                if (GanoRonda) {
                    IniciarActivityJugabilidad();
                } else {
                    IniciarActivitySeleccionarSalas();
                }
            }
        }.start();
    }
    }






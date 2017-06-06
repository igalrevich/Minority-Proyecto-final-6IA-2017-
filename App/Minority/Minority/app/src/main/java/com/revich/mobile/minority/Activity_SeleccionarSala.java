package com.revich.mobile.minority;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_SeleccionarSala extends AppCompatActivity {
    TextView tvEstadoSalaA,tvEstadoSalaB, tvEstadoSalaC, tvEstadoSalaD,tvEstadoSalaE,tvEstadoSalaF;
    TextView [] VecEstadosSalas= new TextView[]{tvEstadoSalaA,tvEstadoSalaB,tvEstadoSalaC,tvEstadoSalaD,tvEstadoSalaE,tvEstadoSalaF};
    Button  btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF;
    Boolean [] JuegoPrevioSalas= new Boolean[] {true,false,false,false,false,false};
    Boolean TrajoEstados=false, EstadoACambiar;
    String[] TiempoDisponibleSalas= new String[] {"00:00:00","00:01:00","00:02:00","00:03:00","00:04:00","00:05:00"};
    String[] NombresSalas= new String[]{"A","B","C","D","E","F"};
    Button [] VecBotones=new Button[]{btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF};
    Date HoraDateTime;
    Gson gson;
    Toast msg;
    int [] SegundosDisponibleSalas= new int [] {0,0,0,0,0,0};
    ArrayList<SalasDeJuego> ListaSalasDeJuego= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__seleccionar_sala);
        getSupportActionBar().hide();
        ObtenerReferencias();
        TraerEstadosSalas();
        /*Calendar MiCalendar=Calendar.getInstance();
        int Hora= MiCalendar.get(Calendar.HOUR);
        int Minutos=MiCalendar.get(Calendar.MINUTE);
        String HoraActual= String.valueOf(Hora)+":"+String.valueOf(Minutos);
        HoraDateTime= Date.valueOf(HoraActual);
        SetearTimerA();*/

    }
    private void ObtenerReferencias()
    {
        for(int i=0;i<6;i++)
        {
          switch (i)
          {
              case 0:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaA);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaA);
                  break;
              case 1:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaB);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaB);
                  break;
              case 2:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaC);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaC);
                  break;
              case 3:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaD);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaD);
                  break;
              case 4:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaE);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaE);
                  break;
              case 5:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaF);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaF);
                  break;
          }
        }
    }
    private void TraerEstadosSalas()
    {
        CountDownTimer Timer=new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
              if(TrajoEstados)
              {
                  Log.d("TrajoEstados", "Trajo estados");
                  for(int i=0; i<TiempoDisponibleSalas.length;i++)
                {
                    if(TiempoDisponibleSalas[i].equals("Esperando2min")==false)
                    {
                        if(TiempoDisponibleSalas[i].equals("00:00:00")==false)
                        {
                            String[] TiempoDisponible= TiempoDisponibleSalas[i].split(":");
                            if(TiempoDisponible[2].equals("00"))
                            {
                                TiempoDisponible[2]="59";
                                int Minutos= Integer.parseInt(TiempoDisponible[1]);
                                Minutos=Minutos-1;
                                TiempoDisponible[1]="0"+String.valueOf(Minutos);
                            }
                            else
                            {
                                int Segundos= Integer.parseInt(TiempoDisponible[2]);
                                Segundos=Segundos-1;
                                if(Segundos<10)
                                {
                                    TiempoDisponible[2]="0"+String.valueOf(Segundos);
                                }
                                else
                                {
                                    TiempoDisponible[2]=String.valueOf(Segundos);
                                }
                            }
                            String NuevoTiempoDisponible= TiempoDisponible[0]+":"+TiempoDisponible[1] +":" +TiempoDisponible[2];
                            TiempoDisponibleSalas[i] =NuevoTiempoDisponible;
                            VecEstadosSalas[i].setText(TiempoDisponibleSalas[i]);
                        }

                    }
                    else
                    {
                        if(SegundosDisponibleSalas[i]<120)
                        {
                            SegundosDisponibleSalas[i]++;
                        }
                    }
                }


              }
            }

            public void onFinish()
            {
                if(TrajoEstados)
                {
                  for(int i=0;i<TiempoDisponibleSalas.length;i++)
                  {
                      if(TiempoDisponibleSalas[i].equals("00:00:00"))
                      {
                          JuegoPrevioSalas[i]=true;
                          String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/GetIdByNombre/salasdejuegos/"+NombresSalas[i];
                          new BuscarIdOModificarTask().execute("GET",url,"true");
                      }
                      else
                      {
                          if(TiempoDisponibleSalas[i].equals("Esperando2min"))
                          {
                              if(SegundosDisponibleSalas[i]==120)
                              {
                                  SegundosDisponibleSalas[i]=0;
                                  String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/GetIdByNombre/salasdejuegos"+NombresSalas[i];
                                  new BuscarIdOModificarTask().execute("GET",url,"false");
                              }
                              else
                              {
                                if(i==TiempoDisponibleSalas.length-1)
                                {
                                    Log.d("Debug", "Paso por el if");
                                    String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/GetSala";
                                    new BuscarDatosTask().execute(url);
                                }
                              }
                          }
                          else
                          {
                             if(i==TiempoDisponibleSalas.length-1)
                             {
                                 String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/GetSala";
                                 new BuscarDatosTask().execute(url);
                             }
                          }
                      }
                  }
                }
                else
                {
                    String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/GetSala";
                    new BuscarDatosTask().execute(url);
                }
            }
        }.start();
    }
    private class BuscarDatosTask extends AsyncTask<String, Void, ArrayList<SalasDeJuego>> {
        private OkHttpClient client= new OkHttpClient();
        @Override
        protected void onPostExecute(ArrayList<SalasDeJuego> ListaSalas) {
            CheckearDisponibilidadSalas(ListaSalas);
            TrajoEstados=true;
            TraerEstadosSalas();
        }

        @Override
        protected ArrayList<SalasDeJuego> doInBackground(String... parametros) {
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
                return new ArrayList<SalasDeJuego>();
            }
        }

        ArrayList<SalasDeJuego> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<SalasDeJuego> ListaSalas= new ArrayList<>();
            JSONArray jsonSalas= new JSONArray(JSONstr);
            for (int i=0; i<jsonSalas.length(); i++) {
                JSONObject jsonSala = jsonSalas.getJSONObject(i);
                int Id=jsonSala.getInt("Id");
                int CantJugadores=jsonSala.getInt("CantJugadores");
                int MontoAGanar= jsonSala.getInt("MontoAGanar");
                int NRonda= jsonSala.getInt("NRonda");
                boolean Disponible= jsonSala.getBoolean("Disponible");
                String Nombre= jsonSala.getString("Nombre");
                SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                MiSalaDeJuego.LlenarDatos(Id,CantJugadores,MontoAGanar,NRonda,Disponible,Nombre);
                ListaSalas.add(MiSalaDeJuego);
            }
            return ListaSalas;
        }
    }
    private class BuscarIdOModificarTask extends AsyncTask<String, Void, Integer> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        @Override
        protected void onPostExecute(Integer Id) {
            if(Id!=0 && Id!=-1)
            {
                SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                MiSalaDeJuego.LlenarDisponibilidad(EstadoACambiar);
                String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/ModificarSalaDeJuego/"+Id;
                new BuscarIdOModificarTask().execute("PUT",url,gson.toJson(MiSalaDeJuego));
            }
            if(Id==-1)
            {

                String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/GetSala";
                new BuscarDatosTask().execute(url);

            }
        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url= parametros[1];
            if(method.equals("GET"))
            {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    EstadoACambiar=Boolean.parseBoolean(parametros[2]);
                    Response response = client.newCall(request).execute();
                    String jsonStr = response.body().string();
                    return Integer.parseInt(jsonStr);

                } catch (IOException e) {
                    Log.d("Error :", e.getMessage());
                    return 0;
                }
            }
            else
            {
                String json = parametros[2];
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.d("Put", "Puteo");
                    return  -1;

                } catch (IOException e) {
                    Log.d("Error :", e.getMessage());
                    return 0;

                }

            }
        }
    }


    private void CheckearDisponibilidadSalas(ArrayList<SalasDeJuego> ListaSalas)
    {
        for(int i=0; i<ListaSalas.size();i++)
        {
            SalasDeJuego MiSalaDeJuego=ListaSalas.get(i);
            if(MiSalaDeJuego.Disponible)
            {
                VecEstadosSalas[i].setText("Disponible");
                VecEstadosSalas[i].setTextColor(Color.parseColor("#8ef686"));
                VecBotones[i].setEnabled(true);
                TiempoDisponibleSalas[i]="Esperando2min";
                SegundosDisponibleSalas[i]=0;
                //EjecutarTimerParaReclutarJugadores(NombresSalas[i]);
            }
            else
            {
                if(JuegoPrevioSalas[i])
                {
                    VecEstadosSalas[i].setText("00:04:15");
                    VecEstadosSalas[i].setTextColor(Color.parseColor("#f61525"));
                    VecBotones[i].setEnabled(false);
                }
                else
                {
                    VecEstadosSalas[i].setText(TiempoDisponibleSalas[i]);
                    VecEstadosSalas[i].setTextColor(Color.parseColor("#f61525"));
                    VecBotones[i].setEnabled(false);
                }
            }
        }
    }

   /* private void EjecutarTimerParaReclutarJugadores(final String Sala) {
        Segundos=0;
        final Timer timer=new Timer();
        TimerTask timertask= new TimerTask() {
            @Override
            public void run() {
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                    Segundos++;
                    if(Segundos==120)
                    {
                      switch (Sala)
                      {
                          case "A":
                              TimersSalasTerminados[0]=true;
                              break;
                          case "B":
                              TimersSalasTerminados[1]=true;
                              break;
                          case "C":
                              TimersSalasTerminados[2]=true;
                              break;
                          case "D":
                              TimersSalasTerminados[3]=true;
                              break;
                          case "E":
                              TimersSalasTerminados[4]=true;
                              break;
                          case "F":
                              TimersSalasTerminados[5]=true;
                              break;
                      }
                      timer.cancel();
                    }
                 }
             });
            }
        };
        timer.schedule(timertask,0,1000);
    }*/

    /*private void SetearTimerA()
    {
        EstadosSalas[0]=true;
        CambiarTextViewsSalasPantalla(0,-1);
        SetearTimerB();
    }

    private void SetearTimerB()
    {
        CountDownTimer Timer=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                VecBotones[0].setOnClickListener(btnEntrar_click);
                VecBotones[1].setOnClickListener(btnEntrar_click);
            }

            public void onFinish() {
                EstadosSalas[1]=true;
                CambiarTextViewsSalasPantalla(1,-1);
                AdelantarTiempoDisponibleSalas("-1","-1","1","2","3","4");
                SetearTimerC();
            }
        }.start();
    }
    private void SetearTimerC()
    {
        CountDownTimer Timer=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                VecBotones[0].setOnClickListener(btnEntrar_click);
                VecBotones[1].setOnClickListener(btnEntrar_click);
                VecBotones[2].setOnClickListener(btnEntrar_click);
            }

            public void onFinish() {
                EstadosSalas[2]=true;
                EstadosSalas[0]=false;
                CambiarTextViewsSalasPantalla(2,0);
                AdelantarTiempoDisponibleSalas("-1","-1","-1","1","2","3");
                SetearTimerD();
            }
        }.start();
    }
    private void SetearTimerD()
    {
        CountDownTimer Timer=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                EstadosSalas[3]=true;
                EstadosSalas[1]=false;
                CambiarTextViewsSalasPantalla(3,1);
                AdelantarTiempoDisponibleSalas("-1","-1","-1","-1","1","2");
                SetearTimerE();
            }
        }.start();
    }
    private void SetearTimerE()
    {
        CountDownTimer Timer=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                EstadosSalas[4]=true;
                EstadosSalas[2]=false;
                CambiarTextViewsSalasPantalla(4,-2);
                AdelantarTiempoDisponibleSalas("-1","-1","-1","-1","-1","1");
                SetearTimerF();
            }
        }.start();
    }
    private void SetearTimerF()
    {
        CountDownTimer Timer=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                EstadosSalas[5]=true;
                EstadosSalas[3]=false;
                CambiarTextViewsSalasPantalla(2,-1);

            }
        }.start();
    }
    private void CambiarTextViewsSalasPantalla(int IndiceActivado, int IndiceDesactivado)
    {
        VecEstadosSalas[IndiceActivado].setText("Disponible");
        VecTiempoDisponibleSalas[IndiceActivado].setText("Disponible por 2min");
        VecEstadosSalas[IndiceActivado].setTextColor(Color.parseColor("#8ef686"));
        VecBotones[IndiceActivado].setEnabled(true);
        VecBotones[IndiceActivado].setVisibility(View.VISIBLE);
        if(IndiceDesactivado!=-1)
        {
            VecEstadosSalas[IndiceDesactivado].setText("En juego");
            VecTiempoDisponibleSalas[IndiceDesactivado].setText("Disponible en 4'15 min");
            VecEstadosSalas[IndiceDesactivado].setTextColor(Color.parseColor("#f61525"));
            VecBotones[IndiceDesactivado].setEnabled(false);
            VecBotones[IndiceDesactivado].setVisibility(View.INVISIBLE);
        }

    }
    private void AdelantarTiempoDisponibleSalas(String TiempoSalaA,String TiempoSalaB,String TiempoSalaC,String TiempoSalaD,String TiempoSalaE,String TiempoSalaF)
    {
       for(int i=0; i<6; i++)
       {
         switch (i)
         {
             case 0:
                 if(TiempoSalaA.equals("-1")==false)
                 {
                     VecTiempoDisponibleSalas[i].setText("Disponible en "+TiempoSalaA+" min");
                 }
                 break;
             case 1:
                 if(TiempoSalaB.equals("-1")==false)
                 {
                     VecTiempoDisponibleSalas[i].setText("Disponible en "+TiempoSalaB+" min");
                 }
                 break;
             case 2:
                 if(TiempoSalaC.equals("-1")==false)
                 {
                     VecTiempoDisponibleSalas[i].setText("Disponible en "+TiempoSalaC+" min");
                 }
                 break;
             case 3:
                 if(TiempoSalaD.equals("-1")==false)
                 {
                     VecTiempoDisponibleSalas[i].setText("Disponible en "+TiempoSalaD+" min");
                 }
                 break;
             case 4:
                 if(TiempoSalaE.equals("-1")==false)
                 {
                     VecTiempoDisponibleSalas[i].setText("Disponible en "+TiempoSalaE+" min");
                 }
                 break;
             case 5:
                 if(TiempoSalaF.equals("-1")==false)
                 {
                     VecTiempoDisponibleSalas[i].setText("Disponible en "+TiempoSalaF+" min");
                 }
                 break;
         }
       }
    }
    private  View.OnClickListener btnEntrar_click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };*/


    }
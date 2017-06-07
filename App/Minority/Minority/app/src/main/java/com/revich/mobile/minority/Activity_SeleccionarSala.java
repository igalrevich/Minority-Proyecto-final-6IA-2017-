package com.revich.mobile.minority;

import android.content.Intent;
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
    Boolean[] ContandoSegundosDisponiblesSala= new Boolean[] {false,false,false,false,false,false};
    ArrayList<SalasDeJuego> ListaSalasDeJuego= new ArrayList<>();
    int IndiceVecBotonesAPasar=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__seleccionar_sala);
        getSupportActionBar().hide();
        ObtenerReferencias();
        TraerEstadosSalas();


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
        if(TrajoEstados)
        {
            CountDownTimer Timer=new CountDownTimer(15000, 1000) {

                public void onTick(long millisUntilFinished) {

                    Log.d("TrajoEstados", "Trajo estados");
                        SetearListeners();
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
                                ContandoSegundosDisponiblesSala[i]=true;
                                if(SegundosDisponibleSalas[i]<120)
                                {
                                    SegundosDisponibleSalas[i]++;
                                }
                            }
                        }


                    }

                public void onFinish()
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
                                Toast msg= Toast.makeText(getApplicationContext(),String.valueOf(SegundosDisponibleSalas[i])+ " "+NombresSalas[i],Toast.LENGTH_SHORT);
                                msg.show();
                                if(SegundosDisponibleSalas[i]==120)
                                {
                                    Toast msg2= Toast.makeText(getApplicationContext(),String.valueOf(SegundosDisponibleSalas[i])+ " "+NombresSalas[i],Toast.LENGTH_SHORT);
                                    msg.show();
                                    ContandoSegundosDisponiblesSala[i]=false;
                                    String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/GetIdByNombre/salasdejuegos/"+NombresSalas[i];
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
            }.start();
        }
        else
        {
            String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/GetSala";
            new BuscarDatosTask().execute(url);
        }
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
                Log.d("PostExecute", "DevuelveId");
                String url ="http://apiminorityproyecto.azurewebsites.net/api/rest/ModificarSalaDeJuego/"+Id;
                gson=new Gson();
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
                if(ContandoSegundosDisponiblesSala[i]==false)
                {
                    SegundosDisponibleSalas[i]=0;
                }
                //EjecutarTimerParaReclutarJugadores(NombresSalas[i]);
            }
            else
            {
                if(JuegoPrevioSalas[i])
                {
                    VecEstadosSalas[i].setText("00:04:15");
                    TiempoDisponibleSalas[i]=VecEstadosSalas[i].getText().toString();
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

    private void SetearListeners()
    {
        for(int i=0;i<VecBotones.length;i++)
        {
           VecBotones[i].setOnClickListener(btnSala_click);
        }
    }

    private View.OnClickListener btnSala_click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         int IdBoton=view.getId();
         boolean EncontroElId=false;
         int IndiceVecBotones=0;
         while(EncontroElId==false)
         {
             if(IdBoton==VecBotones[IndiceVecBotones].getId())
             {
                EncontroElId=true;
             }
             else
             {
                 IndiceVecBotones++;
             }
         }
            String url="http://apiminorityproyecto.azurewebsites.net/api/Rest/GetIdByNombre/salasdejuegos/"+NombresSalas[IndiceVecBotones];
            new BuscarIdAPasarTask().execute(url,String.valueOf(IndiceVecBotones));

        }
    };

    private class BuscarIdAPasarTask extends AsyncTask<String, Void, Integer> {
        private  OkHttpClient client= new OkHttpClient();
        @Override
        protected void onPostExecute(Integer Id) {
         IrAActivityJugabilidad(Id,SegundosDisponibleSalas[IndiceVecBotonesAPasar]);
        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String url = parametros[0];
            IndiceVecBotonesAPasar=Integer.parseInt(parametros[1]);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String jsonStr= response.body().string();
                return Integer.parseInt(jsonStr);

            } catch (IOException  e) {
                Log.d("Error", e.getMessage());
                return 0;
            }
        }

    }

    private void IrAActivityJugabilidad(int IdSala, int IndiceSegundosSala)
    {
        Intent ElIntent= new Intent(this,Activity_Jugabilidad.class);
        Bundle ElBundle= new Bundle();
        ElBundle.putInt("IdSala",IdSala);
        ElBundle.putInt("IndiceSegundosSala",IndiceSegundosSala);
        ElIntent.putExtras(ElBundle);
        startActivity(ElIntent);
    }
}


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
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_SeleccionarSala extends AppCompatActivity {
    TextView tvEstadoSalaA,tvEstadoSalaB, tvEstadoSalaC, tvEstadoSalaD,tvEstadoSalaE,tvEstadoSalaF, tvUsuario, tvMonedas;
    TextView [] VecEstadosSalas= new TextView[]{tvEstadoSalaA,tvEstadoSalaB,tvEstadoSalaC,tvEstadoSalaD,tvEstadoSalaE,tvEstadoSalaF};
    Button  btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF;
    Boolean [] JuegoPrevioSalas= new Boolean[] {true,false,false,false,false,false};
    Boolean TrajoEstados=false, EstadoACambiar, BuscaIdSala;
    String[] TiempoDisponibleSalas= new String[] {"00:00:00","00:01:00","00:02:00","00:03:00","00:04:00","00:05:00"};
    String[] NombresSalas= new String[]{"A","B","C","D","E","F"};
    Button [] VecBotones=new Button[]{btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF};
    Gson gson;
    int [] SegundosDisponibleSalas= new int [] {0,0,0,0,0,0};
    int [] IdsSalas = new int [] {0,0,0,0,0,0};
    Boolean[] ContandoSegundosDisponiblesSala= new Boolean[] {false,false,false,false,false,false};
    int  IndiceVecBotonesAPasar= 0;
    Date [] TiempoALlegar= new Date[] {null,null,null,null,null,null};
    boolean [] DisponibilidadSalas= new boolean[] {false,false,false,false,false,false};
    boolean [] DisponibilidadSalasRecienTerminada= new boolean[] {false,false,false,false,false,false};
    SimpleDateFormat dateFormat= new SimpleDateFormat("hh:mm:ss");
    Date HoraActual,DosMin= null,QuinceSeg= null,HoraComienzoSalaDateTime=null;
    Calendar cal;

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
        tvUsuario= (TextView) findViewById(R.id.tvUsuario);
        tvMonedas = (TextView) findViewById(R.id.tvMonedas);
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
                        for(int i=0; i<DisponibilidadSalas.length;i++)
                        {
                            SetearListeners();
                            if(DisponibilidadSalas[i])
                            {
                               cal= Calendar.getInstance();
                               HoraActual=cal.getTime();
                               if(HoraActual==TiempoALlegar[i])
                               {
                                   DisponibilidadSalasRecienTerminada[i]=true;
                               }
                            }
                            else
                            {
                              if(VecEstadosSalas[i].equals("00:00:00")==false)
                              {
                                 Date TiempoDisponibleSala= null;
                                 Date UnSegundo= null;
                                  try {
                                      TiempoDisponibleSala = dateFormat.parse(VecEstadosSalas[i].getText().toString());
                                      UnSegundo = dateFormat.parse("00:00:01");
                                  } catch (ParseException e) {
                                      e.printStackTrace();
                                  }
                                  long NuevoTiempoDisponible= TiempoDisponibleSala.getTime() - UnSegundo.getTime();
                                  VecEstadosSalas[i].setText(String.valueOf(NuevoTiempoDisponible));
                              }
                            }
                            /*if(TiempoDisponibleSalas[i].equals("Esperando2min")==false)
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
                            }*/
                        }


                    }

                public void onFinish()
                {
                    for(int i=0;i<DisponibilidadSalas.length;i++)
                    {
                        if(DisponibilidadSalas[i])
                        {
                            if(DisponibilidadSalasRecienTerminada[i])
                            {
                                DisponibilidadSalasRecienTerminada[i]=false;
                                DisponibilidadSalas[i]=false;
                                String url ="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/salasdejuegos/"+NombresSalas[i];
                                new BuscarIdOModificarTask().execute("GET",url,"false");
                            }
                            else
                            {
                                if(i==DisponibilidadSalas.length-1)
                                {
                                   TraerEstadosSalas();
                                }
                            }
                        }
                        else
                        {
                            String EstadoSala= VecEstadosSalas[i].getText().toString();
                            if(EstadoSala.equals("00:00:00"))
                            {
                                JuegoPrevioSalas[i]=true;
                                String url ="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/salasdejuegos/"+NombresSalas[i];
                                new BuscarIdOModificarTask().execute("GET",url,"true");
                            }
                            else
                            {
                                if(i==DisponibilidadSalas.length-1)
                                {
                                    TraerEstadosSalas();
                                }
                            }
                        }

                        /*if(TiempoDisponibleSalas[i].equals("00:00:00"))
                        {
                                JuegoPrevioSalas[i]=true;
                                String url ="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/salasdejuegos/"+NombresSalas[i];
                                new BuscarIdOModificarTask().execute("GET",url,"true");
                        }
                        else
                        {
                            if(TiempoDisponibleSalas[i].equals("Esperando2min"))
                            {
                                if(SegundosDisponibleSalas[i]==120)
                                {
                                    ContandoSegundosDisponiblesSala[i]=false;
                                    String url ="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/salasdejuegos/"+NombresSalas[i];
                                    new BuscarIdOModificarTask().execute("GET",url,"false");
                                }
                                else
                                {
                                    if(i==TiempoDisponibleSalas.length-1)
                                    {
                                        Log.d("Debug", "Paso por el if");
                                        String url ="http://apiminorityproyecto.azurewebsites.net/sala/rest/Get";
                                        new BuscarDatosTask().execute(url);
                                    }
                                }
                            }
                            else
                            {
                                if(i==TiempoDisponibleSalas.length-1)
                                {
                                    String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/Get";
                                    new BuscarDatosTask().execute(url);
                                }
                            }
                        }*/
                    }

                }
            }.start();
        }
        else
        {
            String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/Get";
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
                String HoraComienzo= jsonSala.getString("HoraComienzo");
                SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                MiSalaDeJuego.LlenarDatos(Id,CantJugadores,MontoAGanar,NRonda,Disponible,Nombre,HoraComienzo);
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
                String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarSalaDeJuego/"+Id;
                gson=new Gson();
                new BuscarIdOModificarTask().execute("PUT",url,gson.toJson(MiSalaDeJuego));
            }
            if(Id==-1)
            {

                String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/Get";
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
        dateFormat= new SimpleDateFormat("hh:mm:ss");
        for(int i=0; i<ListaSalas.size();i++)
        {
            SalasDeJuego MiSalaDeJuego=ListaSalas.get(i);
            try
            {
                HoraComienzoSalaDateTime= dateFormat.parse(MiSalaDeJuego.HoraComienzo);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            if(MiSalaDeJuego.Disponible)
            {
                VecEstadosSalas[i].setText("Disponible");
                VecEstadosSalas[i].setTextColor(Color.parseColor("#8ef686"));
                VecBotones[i].setEnabled(true);
                DisponibilidadSalas[i]=true;
                CalcularTiempoDisponibleSalas(MiSalaDeJuego.Disponible,i);
                //TiempoDisponibleSalas[i]="Esperando2min";
                if(ContandoSegundosDisponiblesSala[i]==false)
                {
                    SegundosDisponibleSalas[i]=0;
                }
                //EjecutarTimerParaReclutarJugadores(NombresSalas[i]);
            }
            else
            {
                DisponibilidadSalas[i]=false;
                CalcularTiempoDisponibleSalas(MiSalaDeJuego.Disponible,i);
                /*if(JuegoPrevioSalas[i])
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
                }*/
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

    private void CalcularTiempoDisponibleSalas (boolean DisponibilidadSala, int IndiceVectores) {
        if (DisponibilidadSala)
        {
            try {
                DosMin = dateFormat.parse("00:02:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long SumaTiempoDisponible = DosMin.getTime() + HoraComienzoSalaDateTime.getTime();
            Date SumaTiempoDisponibleDateTime = new Date(SumaTiempoDisponible);
            TiempoALlegar[IndiceVectores] = SumaTiempoDisponibleDateTime;
        }
        else
        {
            try {
                QuinceSeg = dateFormat.parse("00:00:15");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal = Calendar.getInstance();
            HoraActual = cal.getTime();
            long SumaTiempoDisponible = QuinceSeg.getTime() + HoraComienzoSalaDateTime.getTime();
            Date SumaTiempoDisponibleDateTime = new Date(SumaTiempoDisponible);
            TiempoALlegar[IndiceVectores] = SumaTiempoDisponibleDateTime;
            long TiempoHastaQueEsteDisponibleSala = SumaTiempoDisponible - HoraActual.getTime();
            VecEstadosSalas[IndiceVectores].setText(String.valueOf(TiempoHastaQueEsteDisponibleSala));
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
            String url="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/salasdejuegos/"+NombresSalas[IndiceVecBotones];
            new BuscarIdAPasarTaskOActualizarUsuario().execute("GET",url,String.valueOf(IndiceVecBotones),"true");

        }
    };

    private class BuscarIdAPasarTaskOActualizarUsuario extends AsyncTask<String, Void, Integer> {
        private  OkHttpClient client= new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        @Override
        protected void onPostExecute(Integer Id)
        {
            if(Id!=0 && Id!=-1)
            {
                if(BuscaIdSala)
                {
                    IdsSalas[IndiceVecBotonesAPasar]= Id;
                    String Usuario= tvUsuario.getText().toString();
                    String url="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/usuarios/"+Usuario;
                    new BuscarIdAPasarTaskOActualizarUsuario().execute("GET",url,String.valueOf(IndiceVecBotonesAPasar),"false");
                }
                else
                {
                    gson= new Gson();
                    Usuario MiUsuario= new Usuario();
                    String MonedasUsuarioString= tvMonedas.getText().toString();
                    int MonedasUsuario= Integer.parseInt(MonedasUsuarioString);
                    MiUsuario.LlenarDatos(Id,MonedasUsuario,IdsSalas[IndiceVecBotonesAPasar]);
                    String url="http://apiminorityproyecto.azurewebsites.net/api/usuario/ModificarUsuario/"+Id;
                    new BuscarIdAPasarTaskOActualizarUsuario().execute("PUT",url,gson.toJson(MiUsuario));
                }
            }
            else
            {
                if(Id==-1)
                {
                    IrAActivityJugabilidad(IdsSalas[IndiceVecBotonesAPasar],TiempoALlegar[IndiceVecBotonesAPasar]);
                }
            }

        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];
            if(method.equals("GET"))
            {
                IndiceVecBotonesAPasar =Integer.parseInt(parametros[2]);
                BuscaIdSala= Boolean.parseBoolean(parametros[3]);
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonStr= response.body().string();
                    return Integer.parseInt(jsonStr);

                }
                catch (IOException  e)
                {
                    Log.d("Error", e.getMessage());
                    return 0;
                }
            }
            else
            {
                String json = parametros[2];
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .put(body)
                        .build();
                try
                {
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

    }

    private void IrAActivityJugabilidad(int IdSala, Date TiempoALlegar)
    {
        Intent ElIntent= new Intent(this,Activity_Jugabilidad.class);
        Bundle ElBundle= new Bundle();
        ElBundle.putInt("IdSala",IdSala);
        String TiempoALlegarString= dateFormat.format(TiempoALlegar);
        ElBundle.putString("TiempoALlegarSala",TiempoALlegarString);
        ElIntent.putExtras(ElBundle);
        startActivity(ElIntent);
    }
}


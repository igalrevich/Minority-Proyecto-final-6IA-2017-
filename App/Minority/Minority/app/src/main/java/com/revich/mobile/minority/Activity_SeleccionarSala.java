package com.revich.mobile.minority;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
import java.util.MissingFormatArgumentException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_SeleccionarSala extends AppCompatActivity {
    TextView tvEstadoSalaA,tvEstadoSalaB, tvEstadoSalaC, tvEstadoSalaD,tvEstadoSalaE,tvEstadoSalaF, tvUsuario, tvMonedas,tvTiempoEnJuegoSalaA,tvTiempoEnJuegoSalaB, tvTiempoEnJuegoSalaC, tvTiempoEnJuegoSalaD,tvTiempoEnJuegoSalaE,tvTiempoEnJuegoSalaF;
    TextView [] VecEstadosSalas= new TextView[]{tvEstadoSalaA,tvEstadoSalaB,tvEstadoSalaC,tvEstadoSalaD,tvEstadoSalaE,tvEstadoSalaF};
    TextView [] VecTiempoEnJuegoSalas= new TextView[]{tvTiempoEnJuegoSalaA,tvTiempoEnJuegoSalaB,tvTiempoEnJuegoSalaC,tvTiempoEnJuegoSalaD,tvTiempoEnJuegoSalaE,tvTiempoEnJuegoSalaF};
    Button  btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF;
    Boolean [] JuegoPrevioSalas= new Boolean[] {true,false,false,false,false,false};
    Boolean TrajoEstados=false, EstadoACambiar, BuscaIdSala;
    String[] NombresSalas= new String[]{"A","B","C","D","E","F"};
    Button [] VecBotones=new Button[]{btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF};
    Gson gson;
    int [] IdsSalas = new int [] {0,0,0,0,0,0};
    Boolean[] ApretoBotonesSalas= new Boolean[] {false,false,false,false,false,false};
    int  IndiceVecBotonesAPasar= 0,MonedasUsuario;
    Date [] TiempoALlegar= new Date[] {null,null,null,null,null,null};
    boolean [] DisponibilidadSalas= new boolean[] {false,false,false,false,false,false};
    boolean [] DisponibilidadSalasRecienTerminada= new boolean[] {false,false,false,false,false,false};
    boolean [] EnJuegoSalasRecienTerminada= new boolean[] {false,false,false,false,false,false};
    SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
    SimpleDateFormat dateFormatSoloHora= new SimpleDateFormat("KK:mm:ss");
    Date HoraActual,HoraComienzoSalaDateTime=null, HoraComienzo;
    Calendar cal;
    String Usuario;
    MiContador mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__seleccionar_sala);
        getSupportActionBar().hide();
        ObtenerReferencias();
        tvUsuario.setText(DatosImportantesApp.GetNombreUsuario());
        tvMonedas.setText(String.valueOf(DatosImportantesApp.GetMonedasUsuario()));
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
                  //VecTiempoEnJuegoSalas[i]=(TextView) findViewById(R.id.tvTiempoEnJuegoSalaA);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaA);
                  break;
              case 1:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaB);
                  //VecTiempoEnJuegoSalas[i]=(TextView) findViewById(R.id.tvTiempoEnJuegoSalaB);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaB);
                  break;
              case 2:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaC);
                  // VecTiempoEnJuegoSalas[i]=(TextView) findViewById(R.id.tvTiempoEnJuegoSalaC);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaC);
                  break;
              case 3:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaD);
                  //VecTiempoEnJuegoSalas[i]=(TextView) findViewById(R.id.tvTiempoEnJuegoSalaD);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaD);
                  break;
              case 4:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaE);
                  //VecTiempoEnJuegoSalas[i]=(TextView) findViewById(R.id.tvTiempoEnJuegoSalaE);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaE);
                  break;
              case 5:
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaF);
                  //VecTiempoEnJuegoSalas[i]=(TextView) findViewById(R.id.tvTiempoEnJuegoSalaF);
                  VecBotones[i]=(Button) findViewById(R.id.btnSalaF);
                  break;
          }
        }
    }
    private void TraerEstadosSalas()
    {
        if(TrajoEstados)
        {
            SetearListeners();
            mc = new MiContador(15000,1000);
            mc.start();

        }
        else
        {
            Toast msg= Toast.makeText(getApplicationContext(),"Trayendo horas de comienzo salas",Toast.LENGTH_SHORT);
            msg.show();
            String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/Get";
            new BuscarDatosTask().execute(url);
        }
    }

    private class MiContador extends CountDownTimer {

        public MiContador(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {

            for(int i=0; i<DisponibilidadSalas.length;i++)
            {

                cal= Calendar.getInstance();
                HoraActual=cal.getTime();
                if(DisponibilidadSalas[i])
                {
                    String EstadoSala= VecEstadosSalas[i].getText().toString();
                    if(EstadoSala.equals("00:00:00"))
                    {
                        VecBotones[i].setEnabled(false);
                        DisponibilidadSalasRecienTerminada[i]=true;
                    }
                    else
                    {
                        long TiempoDiferenciaSala=TiempoALlegar[i].getTime()-HoraActual.getTime();
                        VecEstadosSalas[i].setText(GenerarDiferenciaHorario(TiempoDiferenciaSala));
                    }
                }
                else
                {
                    if(HoraActual==TiempoALlegar[i])
                    {
                        EnJuegoSalasRecienTerminada[i]=true;
                    }
                    else
                    {
                        cal.setTime(TiempoALlegar[i]);
                        cal.add(Calendar.SECOND,-255); //15Seg+4Minutos=255 seg
                        HoraComienzo=cal.getTime();
                        long TiempoDiferenciaSala=HoraActual.getTime() - HoraComienzo.getTime();
                        VecBotones[i].setText("Sala "+NombresSalas[i]+ Html.fromHtml("<br />") +GenerarDiferenciaHorario(TiempoDiferenciaSala) );
                    }
                }
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
                    if(EnJuegoSalasRecienTerminada[i])
                    {
                        EnJuegoSalasRecienTerminada[i]=false;
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
            }

        }

    }
    private class BuscarDatosTask extends AsyncTask<String, String , ArrayList<SalasDeJuego>> {
        private OkHttpClient client= new OkHttpClient();
        @Override
        protected void onPostExecute(ArrayList<SalasDeJuego> ListaSalas) {
            CheckearDisponibilidadSalas(ListaSalas);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            VecEstadosSalas[0].setText(values[0]);
            super.onProgressUpdate(values);
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
                String err = (e.getMessage()==null)?"Fallo api/sala/Get":e.getMessage();
                Log.d("Error", err);
                return new ArrayList<SalasDeJuego>();
            }
        }

        ArrayList<SalasDeJuego> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<SalasDeJuego> ListaSalas= new ArrayList<>();
            JSONArray jsonSalas= new JSONArray(JSONstr);
            for (int i=0; i<jsonSalas.length(); i++) {
                publishProgress(MensajeTraerSalas(i));
                Log.d("Parsea", "parsearResultado: ");
                JSONObject jsonSala = jsonSalas.getJSONObject(i);
                int Id=jsonSala.getInt("Id");
                int CantJugadores=jsonSala.getInt("CantJugadores");
                int MontoAGanar= jsonSala.getInt("MontoAGanar");
                int NRonda= jsonSala.getInt("NRonda");
                boolean Disponible= jsonSala.getBoolean("Disponible");
                String Nombre= jsonSala.getString("Nombre");
                String HoraComienzo= jsonSala.getString("HoraComienzo");
                boolean ModificarHComienzo= jsonSala.getBoolean("ModificarHComienzo");
                SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                MiSalaDeJuego.LlenarDatos(Id,CantJugadores,MontoAGanar,NRonda,Disponible,Nombre,HoraComienzo,ModificarHComienzo);
                ListaSalas.add(MiSalaDeJuego);
            }
            return ListaSalas;
        }
    }

    private String MensajeTraerSalas(int i)
    {
        String msg="";
        switch (i)
        {
            case 0:
                msg="Trayendo Horario sala A";
                break;
            case 1:
                msg= "Trayendo Horario sala B";
            break;
            case 2:
                msg="Trayendo Horario sala C";
            break;
            case 3:
                msg= "Trayendo Horario sala D";
            break;
            case 4:
                msg= "Trayendo Horario sala E";
            break;
            case 5:
                msg= "Trayendo Horario sala F";
            break;

        }
        return msg;
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

    private class ActualizarMHCSala extends AsyncTask<String, Void, Integer> {
        private OkHttpClient client = new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        @Override
        protected void onPostExecute(Integer Id)
        {
            if(Id!=0 )
            {
                TrajoEstados=true;
                TraerEstadosSalas();
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



    private void CheckearDisponibilidadSalas(ArrayList<SalasDeJuego> ListaSalas)
    {
        for(int i=0; i<ListaSalas.size();i++)
        {
            SalasDeJuego MiSalaDeJuego=ListaSalas.get(i);
            IdsSalas[i]=MiSalaDeJuego.Id;
            try
            {
                HoraComienzoSalaDateTime= dateFormat.parse(MiSalaDeJuego.HoraComienzo);
                cal=Calendar.getInstance();
                HoraActual= cal.getTime();
                cal.setTime(HoraComienzoSalaDateTime);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            if(MiSalaDeJuego.Disponible)
            {
                HoraComienzo= cal.getTime();
                long TiempoParaTerminarDisponibilidad= HoraComienzo.getTime() - HoraActual.getTime();
                String TiempoDiferenciaSala= GenerarDiferenciaHorario(TiempoParaTerminarDisponibilidad);
                /*String HoraComienzoSala= MiSalaDeJuego.HoraComienzo.trim();
                String [] HoraComienzoSalaDividida= HoraComienzoSala.split(" ");
                String TiempoComienzoSala= HoraComienzoSalaDividida[1];*/
                VecEstadosSalas[i].setText(TiempoDiferenciaSala);
                //VecTiempoEnJuegoSalas[i].setText("");
                VecEstadosSalas[i].setTextColor(Color.parseColor("#8ef686"));
                VecBotones[i].setText("Sala "+NombresSalas[i]);
                VecBotones[i].setEnabled(true);
                DisponibilidadSalas[i]=true;
                TiempoALlegar[i]=HoraComienzoSalaDateTime;
            }
            else
            {
                VecEstadosSalas[i].setText("En juego");
                DisponibilidadSalas[i]=false;
                cal.add(Calendar.MINUTE,-4);
                HoraComienzo=cal.getTime();
                long TiempoParaTerminarDisponibilidad= HoraActual.getTime() - HoraComienzo.getTime();
                String TiempoDiferenciaSala= GenerarDiferenciaHorario(TiempoParaTerminarDisponibilidad);
                VecEstadosSalas[i].setTextColor(Color.parseColor("#f61525"));
                /*VecTiempoEnJuegoSalas[i].setTextColor(Color.parseColor("#f61525"));
                VecTiempoEnJuegoSalas[i].setText(TiempoDiferenciaSala);*/
                VecBotones[i].setText("Sala "+NombresSalas[i]+ Html.fromHtml("<br />") +GenerarDiferenciaHorario(TiempoParaTerminarDisponibilidad) );
                VecBotones[i].setTextColor(Color.parseColor("#f61525"));
                VecBotones[i].setEnabled(false);
                Calendar HoraComienzoMas15Seg= Calendar.getInstance();
                HoraComienzoMas15Seg.setTime(HoraComienzoSalaDateTime);
                HoraComienzoMas15Seg.add(Calendar.SECOND,15);
                TiempoALlegar[i]= HoraComienzoMas15Seg.getTime();
            }
            //CambiarMHCSalaDeJuego(MiSalaDeJuego,false);
        }
        TrajoEstados=true;
        TraerEstadosSalas();
    }

    private void SetearListeners()
    {
        for(int i=0;i<VecBotones.length;i++)
        {
           VecBotones[i].setOnClickListener(btnSala_click);
        }
    }

    private String GenerarDiferenciaHorario( long Time)
    {
       // int DiferenciaTiempoSegundos= Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Time)));
        String NuevoTiempo="";
        if(Time>=0)
        {

            int diffSecondsint = Integer.parseInt(String.valueOf((Time/1000)%60));
            String diffMinutes="",diffHours="",diffSeconds="";
            if(diffSecondsint<10)
            {
                diffSeconds= "0"+String.valueOf(diffSecondsint);
            }
            else
            {
                diffSeconds= String.valueOf(diffSecondsint);
            }

            diffMinutes=String.valueOf(Time/ (60 * 1000));
            if(Integer.parseInt(diffMinutes)<10)
            {
              diffMinutes="0"+ diffMinutes;
            }
            diffHours=String.valueOf(Time/ (60 * 60 * 1000));
            if(Integer.parseInt(diffHours)<10)
            {
                diffHours="0"+ diffHours;
            }
            NuevoTiempo= diffHours+":"+diffMinutes+":"+diffSeconds;
        }
        else
        {
          NuevoTiempo="00:00:00";
        }
        return NuevoTiempo;
    }

    private void CambiarMHCSalaDeJuego (SalasDeJuego MiSalaDeJuego ,boolean CambiarMHC) {

            SalasDeJuego ObjetoSalaDeJuego= new SalasDeJuego();
            ObjetoSalaDeJuego.LlenarDisponibilidad(CambiarMHC);
            String url ="http://apiminorityproyecto.azurewebsites.net/api/sala/ModificarSalaDeJuegoMHC/"+String.valueOf(MiSalaDeJuego.Id);
            gson=new Gson();
            new ActualizarMHCSala().execute("PUT",url,gson.toJson(ObjetoSalaDeJuego));
    }


    private View.OnClickListener btnSala_click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Toast msg= Toast.makeText(getApplicationContext(),"Yendo a la Sala",Toast.LENGTH_SHORT);
            msg.show();
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
            IndiceVecBotonesAPasar=IndiceVecBotones;
            Usuariosxsala MiUsuariosxsala= new Usuariosxsala();
            MiUsuariosxsala.LlenarDatos(DatosImportantesApp.GetIdUsuario(),IdsSalas[IndiceVecBotones]);
            gson=new Gson();
            String url="http://apiminorityproyecto.azurewebsites.net/api/usuario/IngresarUserSala";
            new IngresarUserSala().execute("POST",url,gson.toJson(MiUsuariosxsala));
            /*String url="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/salasdejuegos/"+NombresSalas[IndiceVecBotones];
            new BuscarIdAPasarTaskOActualizarUsuario().execute("GET",url,String.valueOf(IndiceVecBotones),"true");*/

        }
    };

    private class IngresarUserSala extends AsyncTask<String, Void, Integer> {
        private  OkHttpClient client= new OkHttpClient();
        String MensajeEntraSala;
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        @Override
        protected void onPostExecute(Integer Id)
        {
            if(Id!=0)
            {
              if(MensajeEntraSala.equals("\"Ingreso a sala restando moneda\""))
              {
                  DatosImportantesApp.SetMonedasUsuario(DatosImportantesApp.GetMonedasUsuario()-1);
                  IrAActivityJugabilidad(IdsSalas[IndiceVecBotonesAPasar],TiempoALlegar[IndiceVecBotonesAPasar]);
              }

              else
              {
                  if(MensajeEntraSala.equals("\"Ingreso a sala\"")==false)
                  {
                      Toast msg= Toast.makeText(getApplicationContext(),MensajeEntraSala,Toast.LENGTH_SHORT) ;
                      msg.show();
                  }
                  else
                  {
                      IrAActivityJugabilidad(IdsSalas[IndiceVecBotonesAPasar],TiempoALlegar[IndiceVecBotonesAPasar]);
                  }

              }
            }

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
                    Response response = client.newCall(request).execute();
                    MensajeEntraSala=response.body().string();
                    return  1;

                }
                catch (IOException e)
                {
                    String err = (e.getMessage()==null)?"Fallo IngresarUserSala":e.getMessage();
                    Log.d("Error :", err);
                    return 0;

                }
            }

            else
            {
                return  0;
            }
        }
            /*if(method.equals("GET"))
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

        }*/

    }

    private class AnadirJugadorSalaDeJuegoUObtenerCantJugadores extends AsyncTask<String, Void, Integer> {
        private  OkHttpClient client= new OkHttpClient();
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        @Override
        protected void onPostExecute(Integer CantJugadores)
        {
            if(CantJugadores!=-2)
            {
                if(CantJugadores!=-1)
                {
                    if(CantJugadores<50)
                    {
                        SalasDeJuego MiSalaDeJuego= new SalasDeJuego();
                        MiSalaDeJuego.LlenarDisponibilidadConId(true,IdsSalas[IndiceVecBotonesAPasar]);
                        gson=new Gson();
                        String url="http://apiminorityproyecto.azurewebsites.net/api/sala/AnadirJugadorSalaDeJuego/"+IdsSalas[IndiceVecBotonesAPasar];
                        new AnadirJugadorSalaDeJuegoUObtenerCantJugadores().execute("PUT",url,gson.toJson(MiSalaDeJuego));
                    }
                    else
                    {
                        Toast msg= Toast.makeText(getApplicationContext(),"La cantidad de jugadores excede a 50",Toast.LENGTH_SHORT);
                        msg.show();
                    }
                }
                else
                {
                    IrAActivityJugabilidad(IdsSalas[IndiceVecBotonesAPasar],TiempoALlegar[IndiceVecBotonesAPasar]);
                }
            }

        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String method = parametros[0];
            String url = parametros[1];
            if(method.equals("PUT"))
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
                    return -2;

                }

            }
            else
            {
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
                    return -2;
                }
            }



        }

    }

    private void IrAActivityJugabilidad(int IdSala, Date TiempoALlegar)
    {
        Intent ElIntent= new Intent(this,Activity_Jugabilidad.class);
        Bundle ElBundle= new Bundle();
        ElBundle.putInt("IdSala",IdSala);
        ElBundle.putString("Usuario",Usuario);
        cal= Calendar.getInstance();
        HoraActual= cal.getTime();
        cal.setTime(TiempoALlegar);
        Date HoraComienzo= cal.getTime();
        long TiempoParaReclutarJugadores= HoraComienzo.getTime() - HoraActual.getTime();
        int TiempoParaReclutarJugadoresSegundos= Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(TiempoParaReclutarJugadores)));
        ElBundle.putInt("SegundosParaReclutarJugadores",TiempoParaReclutarJugadoresSegundos);
        ElBundle.putBoolean("PrimeraVezQueJuegaSala",true);
        /*MonedasUsuario=Integer.parseInt(tvMonedas.getText().toString());
        ElBundle.putInt("Monedas",MonedasUsuario);*/
        ElIntent.putExtras(ElBundle);
        startActivity(ElIntent);
    }
}

            /*CountDownTimer Timer=new CountDownTimer(15000, 1000) {

                public void onTick(long millisUntilFinished) {

                    Log.d("TrajoEstados", "Trajo estados");
                        SetearListeners();
                        for(int i=0; i<DisponibilidadSalas.length;i++)
                        {

                            cal= Calendar.getInstance();
                            HoraActual=cal.getTime();
                            if(DisponibilidadSalas[i])
                               {
                                   String EstadoSala= VecEstadosSalas[i].getText().toString();
                                   if(EstadoSala.equals("00:00:00"))
                                   {
                                       VecBotones[i].setEnabled(false);
                                       DisponibilidadSalasRecienTerminada[i]=true;
                                   }
                                   else
                                   {
                                       try {
                                           DiferenciaTiempoDisponibilidad = dateFormatSoloHora.parse(EstadoSala);
                                           cal.setTime(DiferenciaTiempoDisponibilidad);
                                           cal.add(Calendar.SECOND,-1);
                                           DiferenciaTiempoDisponibilidad=cal.getTime();
                                           VecEstadosSalas[i].setText(dateFormatSoloHora.format(DiferenciaTiempoDisponibilidad));
                                       } catch (ParseException e) {
                                           e.printStackTrace();
                                       }
                                   }
                               }
                               else
                               {
                                   if(HoraActual==TiempoALlegar[i])
                                   {
                                       EnJuegoSalasRecienTerminada[i]=true;
                                   }
                               }
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
                            if(EnJuegoSalasRecienTerminada[i])
                            {
                                EnJuegoSalasRecienTerminada[i]=false;
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
                    }

                }
            }.start();
            */

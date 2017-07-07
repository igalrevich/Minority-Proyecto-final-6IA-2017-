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
    TextView tvEstadoSalaA,tvEstadoSalaB, tvEstadoSalaC, tvEstadoSalaD,tvEstadoSalaE,tvEstadoSalaF, tvUsuario, tvMonedas;
    TextView [] VecEstadosSalas= new TextView[]{tvEstadoSalaA,tvEstadoSalaB,tvEstadoSalaC,tvEstadoSalaD,tvEstadoSalaE,tvEstadoSalaF};
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
    Date HoraActual,HoraComienzoSalaDateTime=null, DiferenciaTiempoDisponibilidad;
    Calendar cal;
    String Usuario;

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
            SetearListeners();
            MiContador mc = new MiContador(15000,1000);
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
                Log.d("Error", e.getMessage());
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
                cal=Calendar.getInstance();
                HoraActual= cal.getTime();
                cal.setTime(HoraComienzoSalaDateTime);
                Date HoraComienzo= cal.getTime();
                long TiempoParaTerminarDisponibilidad= HoraComienzo.getTime() - HoraActual.getTime();
                String TiempoDiferenciaSala= GenerarDiferenciaHorario(TiempoParaTerminarDisponibilidad);
                /*String HoraComienzoSala= MiSalaDeJuego.HoraComienzo.trim();
                String [] HoraComienzoSalaDividida= HoraComienzoSala.split(" ");
                String TiempoComienzoSala= HoraComienzoSalaDividida[1];*/
                VecEstadosSalas[i].setText(TiempoDiferenciaSala);
                VecEstadosSalas[i].setTextColor(Color.parseColor("#8ef686"));
                VecBotones[i].setEnabled(true);
                DisponibilidadSalas[i]=true;
                TiempoALlegar[i]=HoraComienzoSalaDateTime;
            }
            else
            {
                VecEstadosSalas[i].setText("En juego");
                DisponibilidadSalas[i]=false;
                VecEstadosSalas[i].setTextColor(Color.parseColor("#f61525"));
                VecBotones[i].setEnabled(false);
                Calendar HoraComienzoMas15Seg= Calendar.getInstance();
                HoraComienzoMas15Seg.setTime(HoraComienzoSalaDateTime);
                HoraComienzoMas15Seg.add(Calendar.SECOND,15);
                TiempoALlegar[i]= HoraComienzoMas15Seg.getTime();
            }
            CambiarMHCSalaDeJuego(MiSalaDeJuego,false);
        }
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
            diffMinutes="0"+String.valueOf(Time/ (60 * 1000));
            diffHours="0"+String.valueOf(Time/ (60 * 60 * 1000));
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
                    Usuario= tvUsuario.getText().toString();
                    Usuario=Usuario.trim();
                    String url="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetIdByNombre/usuarios/"+Usuario;
                    Log.d("url", url);
                    new BuscarIdAPasarTaskOActualizarUsuario().execute("GET",url,String.valueOf(IndiceVecBotonesAPasar),"false");
                }
                else
                {

                    if (DatosImportantesApp.GetEntroSala(IndiceVecBotonesAPasar)==false)
                    {
                        DatosImportantesApp.SetEntroSala(IndiceVecBotonesAPasar,true);
                        gson= new Gson();
                        Usuario MiUsuario= new Usuario();
                        String MonedasUsuarioString= tvMonedas.getText().toString();
                        MonedasUsuario= Integer.parseInt(MonedasUsuarioString);
                        MiUsuario.LlenarDatos(Id,MonedasUsuario,IdsSalas[IndiceVecBotonesAPasar]);
                        String url="http://apiminorityproyecto.azurewebsites.net/api/usuario/ModificarUsuario/"+Id;
                        new BuscarIdAPasarTaskOActualizarUsuario().execute("PUT",url,gson.toJson(MiUsuario));
                    }
                    else
                    {
                        IrAActivityJugabilidad(IdsSalas[IndiceVecBotonesAPasar],TiempoALlegar[IndiceVecBotonesAPasar]);
                    }
                }
            }
            else
            {
                if(Id==-1)
                {
                    String url="http://apiminorityproyecto.azurewebsites.net/api/sala/GetCantJugadoresSala/"+IdsSalas[IndiceVecBotonesAPasar];
                    new AnadirJugadorSalaDeJuegoUObtenerCantJugadores().execute("GET",url);
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

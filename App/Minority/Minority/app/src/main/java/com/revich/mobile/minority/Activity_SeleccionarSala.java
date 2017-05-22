package com.revich.mobile.minority;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_SeleccionarSala extends AppCompatActivity {
    TextView tvSalaA,tvEstadoSalaA,tvTiempoDisponibleSalaA;
    TextView tvSalaB,tvEstadoSalaB,tvTiempoDisponibleSalaB;
    TextView tvSalaC,tvEstadoSalaC,tvTiempoDisponibleSalaC;
    TextView tvSalaD,tvEstadoSalaD,tvTiempoDisponibleSalaD;
    TextView tvSalaE,tvEstadoSalaE,tvTiempoDisponibleSalaE;
    TextView tvSalaF,tvEstadoSalaF,tvTiempoDisponibleSalaF;
    Button  btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF;
    Boolean [] EstadosSalas= new Boolean[] {false,false,false,false,false,false};
    Button [] VecBotones=new Button[]{btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__seleccionar_sala);
        getSupportActionBar().hide();
        ObtenerReferencias();
        Calendar MiCalendar=Calendar.getInstance();
        int Hora= MiCalendar.get(Calendar.HOUR);
        int Minutos=MiCalendar.get(Calendar.MINUTE);
        String HoraActual= String.valueOf(Hora)+":"+String.valueOf(Minutos);
        Date HoraDateTime= Date.valueOf(HoraActual);

    }
    private void ObtenerReferencias()
    {
        tvSalaA=(TextView) findViewById(R.id.tvSalaA);
    }
    private void SetearTimerA()
    {
        CountDownTimer Timer=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                EstadosSalas[0]=true;
                SetearTimerB();
            }
        }.start();
    }

    private void SetearTimerB()
    {
        CountDownTimer Timer=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                EstadosSalas[1]=true;
                SetearTimerC();
            }
        }.start();
    }
    private void SetearTimerC()
    {
        CountDownTimer Timer=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                EstadosSalas[2]=true;
                EstadosSalas[0]=false;
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
            }
        }.start();
    }


}

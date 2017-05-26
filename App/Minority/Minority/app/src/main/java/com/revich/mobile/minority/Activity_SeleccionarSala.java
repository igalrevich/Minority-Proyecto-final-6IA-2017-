package com.revich.mobile.minority;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    TextView [] VecTvSalas= new TextView[]{tvSalaA,tvSalaB,tvSalaC,tvSalaD,tvSalaE,tvSalaF};
    TextView [] VecEstadosSalas= new TextView[]{tvEstadoSalaA,tvEstadoSalaB,tvEstadoSalaC,tvEstadoSalaD,tvEstadoSalaE,tvEstadoSalaF};
    TextView [] VecTiempoDisponibleSalas= new TextView[]{tvTiempoDisponibleSalaA,tvTiempoDisponibleSalaB,tvTiempoDisponibleSalaC,tvTiempoDisponibleSalaD,tvTiempoDisponibleSalaE,tvTiempoDisponibleSalaF};
    Button  btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF;
    Boolean [] EstadosSalas= new Boolean[] {false,false,false,false,false,false};
    Button [] VecBotones=new Button[]{btnEntrarSalaA,btnEntrarSalaB,btnEntrarSalaC,btnEntrarSalaD,btnEntrarSalaE,btnEntrarSalaF};
    Date HoraDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__seleccionar_sala);
        getSupportActionBar().hide();
        ObtenerReferencias();
        /*Calendar MiCalendar=Calendar.getInstance();
        int Hora= MiCalendar.get(Calendar.HOUR);
        int Minutos=MiCalendar.get(Calendar.MINUTE);
        String HoraActual= String.valueOf(Hora)+":"+String.valueOf(Minutos);
        HoraDateTime= Date.valueOf(HoraActual);*/
        SetearTimerA();

    }
    private void ObtenerReferencias()
    {
        for(int i=0;i<6;i++)
        {
          switch (i)
          {
              case 0:
                  VecTvSalas[i] = (TextView) findViewById(R.id.tvSalaA);
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaA);
                  VecTiempoDisponibleSalas[i]=(TextView) findViewById(R.id.tvTiempoDisponibleSalaA);
                  VecBotones[i]=(Button) findViewById(R.id.btnEntrarSalaA);
                  break;
              case 1:
                  VecTvSalas[i] = (TextView) findViewById(R.id.tvSalaB);
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaB);
                  VecTiempoDisponibleSalas[i]=(TextView) findViewById(R.id.tvTiempoDisponibleSalaB);
                  VecBotones[i]=(Button) findViewById(R.id.btnEntrarSalaB);
                  break;
              case 2:
                  VecTvSalas[i] = (TextView) findViewById(R.id.tvSalaC);
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaC);
                  VecTiempoDisponibleSalas[i]=(TextView) findViewById(R.id.tvTiempoDisponibleSalaC);
                  VecBotones[i]=(Button) findViewById(R.id.btnEntrarSalaC);
                  break;
              case 3:
                  VecTvSalas[i] = (TextView) findViewById(R.id.tvSalaD);
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaD);
                  VecTiempoDisponibleSalas[i]=(TextView) findViewById(R.id.tvTiempoDisponibleSalaD);
                  VecBotones[i]=(Button) findViewById(R.id.btnEntrarSalaD);
                  break;
              case 4:
                  VecTvSalas[i] = (TextView) findViewById(R.id.tvSalaE);
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaE);
                  VecTiempoDisponibleSalas[i]=(TextView) findViewById(R.id.tvTiempoDisponibleSalaE);
                  VecBotones[i]=(Button) findViewById(R.id.btnEntrarSalaE);
                  break;
              case 5:
                  VecTvSalas[i] = (TextView) findViewById(R.id.tvSalaF);
                  VecEstadosSalas[i]=(TextView) findViewById(R.id.tvEstadoSalaF);
                  VecTiempoDisponibleSalas[i]=(TextView) findViewById(R.id.tvTiempoDisponibleSalaF);
                  VecBotones[i]=(Button) findViewById(R.id.btnEntrarSalaF);
                  break;
          }
        }
    }
    private void SetearTimerA()
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


}

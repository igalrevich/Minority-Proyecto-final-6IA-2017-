package com.revich.mobile.minority;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

public class Activity_Jugabilidad extends AppCompatActivity {
    Button btnOpcion1,btnOpcion2, btnVotar;
    TextView tvSegundosTimer,tvVotoFinal;
    boolean VotoOpcion1=false;
    boolean VotoOpcion2=false;
    boolean VotoFinalmente=false;
    String VotoFinal,NombreBoton,SegundosTimer;
    int Idbtn;
    CountDownTimer Timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jugabilidad);
        getSupportActionBar().hide();
        ObtenerReferencias();
        SetearTimer();





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
            setContentView(R.layout.layout_jugabilidad);
            ReestablecerCondicionesLayout();
        }
    }

    private void ReestablecerCondicionesLayout()
    {
        ObtenerReferencias();
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
            DeterminarVotoFinal();
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
        tvVotoFinal.setText(VotoFinal);


    }
}

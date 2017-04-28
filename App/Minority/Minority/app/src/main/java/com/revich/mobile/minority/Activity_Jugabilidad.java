package com.revich.mobile.minority;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Activity_Jugabilidad extends AppCompatActivity {
    Button btnOpcion1,btnOpcion2, btnVotar;
    TextView tvSegundosTimer;
    boolean VotoOpcion1=false;
    boolean VotoOpcion2=false;
    String VotoFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jugabilidad);
        getSupportActionBar().hide();
        ObtenerReferencias();
        SetearTimer();





    }
    private void SetearTimer()
    {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvSegundosTimer.setText(String.valueOf(millisUntilFinished/1000));
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
            int Id= view.getId();
            String NombreBoton=getResources().getResourceName(Id);

            if(NombreBoton=="btnOpcion1")
            {
                CambiarColorBotonOpcion(btnOpcion1,VotoOpcion1);
                if(VotoOpcion1)
                {
                  VotoOpcion1=false;
                }
                else
                {
                    VotoOpcion1=true;
                }
            }
            else
            {
                CambiarColorBotonOpcion(btnOpcion2,VotoOpcion2);
                if(VotoOpcion2)
                {
                    VotoOpcion2=false;
                }
                else
                {
                    VotoOpcion2=true;
                }
            }
         }
    };

    private void CambiarColorBotonOpcion(Button btn, boolean Voto)
    {
       if(Voto==false)
       {
           btn.setBackgroundColor(Color.parseColor("#FF000000"));
           btn.setTextColor(Color.parseColor("#FFFFFFFF"));
       }
        else
       {
           btn.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
           btn.setTextColor(Color.parseColor("#FF000000"));
       }
    }
    private View.OnClickListener btnVotar_click= new View.OnClickListener(){
        @Override
        public void onClick(View view)
        {
           DeterminarVotoFinal();
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
    }
}

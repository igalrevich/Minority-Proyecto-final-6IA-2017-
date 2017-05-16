package com.revich.mobile.minority;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class Activity_Resultados extends AppCompatActivity {
     TextView tvOpcion1,tvOpcion2,tvVotosOpcion1,tvVotosOpcion2,tvGanastePerdiste,tvIndicacion1,tvIndicacion2;
     String Opcion1,Opcion2,ResultadoUsuario;
     boolean MayoriaOpcion1=false;
     int CantVotosOpcion1=0,CantVotosOpcion2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__resultados);
        getSupportActionBar().hide();
        ObtenerReferencias();
        Opcion1=tvOpcion1.getText().toString();
        Opcion2=tvOpcion2.getText().toString();
        Intent ELIntentQueVino= getIntent();
        Bundle ElBundle= ELIntentQueVino.getExtras();
        String VotoJugador= ElBundle.getString("Voto");
        Random rand = new Random();
        int CantVotosEnBlanco= rand.nextInt(50);
        int CantVotosOpciones=49-CantVotosEnBlanco;
        CantVotosOpcion1=rand.nextInt(CantVotosOpciones+1);
        CantVotosOpcion2=CantVotosOpciones-CantVotosOpcion1;
        if(VotoJugador!="")
        {
            if(CantVotosOpcion1!=CantVotosOpcion2)
            {
                if(CantVotosOpcion1>CantVotosOpcion2)
                {
                  MayoriaOpcion1=true;
                }
                ResultadoUsuario=CheckearResultados(MayoriaOpcion1,VotoJugador);

            }
            else
            {
              ResultadoUsuario="Empato";
            }
        }
        else
        {
            ResultadoUsuario="Perdio";
            GanoOPerdio(ResultadoUsuario);
        }

    }
    private void ObtenerReferencias()
    {
        tvOpcion1= (TextView) findViewById(R.id.tvOpcion1);
        tvOpcion2= (TextView) findViewById(R.id.tvOpcion2);
        tvVotosOpcion1= (TextView) findViewById(R.id.tvVotosOpcion1);
        tvVotosOpcion2= (TextView) findViewById(R.id.tvVotosOpcion2);
        tvGanastePerdiste= (TextView) findViewById(R.id.tvGanastePerdiste);
        tvIndicacion1= (TextView) findViewById(R.id.tvIndicacion1);
        tvIndicacion2= (TextView) findViewById(R.id.tvIndicacion2);
    }

    private String CheckearResultados(boolean MayoriaOpcion1,String VotoJugador)
    {   String Resultado="";
        if(MayoriaOpcion1)
        {
            if(VotoJugador==Opcion1)
            {
                Resultado="Perdio";
            }
            else
            {
                Resultado="Gano";
            }
        }
        else
        {
            if(VotoJugador==Opcion1)
            {
                Resultado="Gano";
            }
            else
            {
                Resultado="Perdio";
            }
        }
        return  Resultado;
    }

    private void ImprimirResultadosPantalla(String Resultado)
    {
        tvVotosOpcion1.setText(String.valueOf(CantVotosOpcion1));
        tvVotosOpcion2.setText(String.valueOf(CantVotosOpcion2));
        if(MayoriaOpcion1)
       {
           tvOpcion2.setTextColor(Color.parseColor("#8ef686"));
           tvVotosOpcion2.setTextColor(Color.parseColor("#8ef686"));
           tvOpcion1.setTextColor(Color.parseColor("#f61525"));
           tvVotosOpcion1.setTextColor(Color.parseColor("#f61525"));
           GanoOPerdio(Resultado);
       }
       else
        {
          if(Resultado=="Empato")
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
              tvOpcion2.setTextColor(Color.parseColor("#f61525"));
              tvVotosOpcion2.setTextColor(Color.parseColor("#f61525"));
              tvOpcion1.setTextColor(Color.parseColor("#8ef686"));
              tvVotosOpcion1.setTextColor(Color.parseColor("#8ef686"));
              GanoOPerdio(Resultado);
          }
        }

    }

    private void GanoOPerdio(String Resultado)
    {
        if(Resultado=="Gano")
        {
            tvGanastePerdiste.setTextColor(Color.parseColor("#8ef686"));
            tvIndicacion1.setTextColor(Color.parseColor("#8ef686"));
            tvIndicacion2.setTextColor(Color.parseColor("#8ef686"));
        }
        if(Resultado=="Perdio")
        {
            tvGanastePerdiste.setTextColor(Color.parseColor("#f61525"));
            tvIndicacion1.setTextColor(Color.parseColor("#f61525"));
            tvIndicacion2.setTextColor(Color.parseColor("#f61525"));
            tvGanastePerdiste.setText("Perdiste!!");
            tvIndicacion1.setText("Sos parte de la mayoria");
            tvIndicacion2.setText("Quedaste eliminado!!!");
        }
    }

}

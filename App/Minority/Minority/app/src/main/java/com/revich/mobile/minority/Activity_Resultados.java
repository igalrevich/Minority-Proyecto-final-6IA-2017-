package com.revich.mobile.minority;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class Activity_Resultados extends AppCompatActivity {
     TextView tvOpcion1,tvOpcion2,tvVotosOpcion1,tvVotosOpcion2,tvGanastePerdiste,tvIndicacion1,tvIndicacion2;
     String Opcion1,Opcion2;
     boolean MayoriaOpcion1=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__resultados);
        ObtenerReferencias();
        Opcion1=tvOpcion1.getText().toString();
        Opcion2=tvOpcion2.getText().toString();
        Intent ELIntentQueVino= getIntent();
        Bundle ElBundle= ELIntentQueVino.getExtras();
        String VotoJugador= ElBundle.getString("Voto");
        Random rand = new Random();
        int CantVotosEnBlanco= rand.nextInt(50);
        int CantVotosOpciones=49-CantVotosEnBlanco;
        if(VotoJugador!="")
        {
            int CantVotosOpcion1=rand.nextInt(CantVotosOpciones+1);
            int CantVotosOpcion2=CantVotosOpciones-CantVotosOpcion1;
            if(CantVotosOpcion1!=CantVotosOpcion2)
            {
                if(CantVotosOpcion1>CantVotosOpcion2)
                {
                  MayoriaOpcion1=true;
                }

            }

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

}

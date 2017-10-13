package com.revich.mobile.minority;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Activity_Premiacion extends AppCompatActivity {
    TextView tvStatusGanador, tvCuantasMonedasGano;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__premiacion);
        getSupportActionBar().hide();
        Intent ElIntentQueVino= getIntent();
        Bundle ElBundleQueVino= ElIntentQueVino.getExtras();
        int CantJugadores= ElBundleQueVino.getInt("CantJugadores");
        int MontoGanador= ElBundleQueVino.getInt("MontoAGanar");
        ObtenerReferencias();
        if(CantJugadores==1)
        {
            tvStatusGanador.setText("Sos el ganador de la sala");
            tvCuantasMonedasGano.setText("Ganaste "+String.valueOf(MontoGanador)+" monedas");
        }
        else
        {
            tvStatusGanador.setText("Sos uno de los 2 ganadores de la sala");
            tvCuantasMonedasGano.setText("Ganaste "+String.valueOf(MontoGanador/2)+" monedas");
        }
        Esperar15SegundosAntesDeActualizar();

    }

    private void ObtenerReferencias()
    {
        tvStatusGanador=(TextView) findViewById(R.id.tvStatusGanador);
        tvCuantasMonedasGano=(TextView) findViewById(R.id.tvCuantasMonedasGano);
    }

    private void Esperar15SegundosAntesDeActualizar()
    {
        CountDownTimer Timer=new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish()
            {
                IniciarActivitySeleccionarSalas();
            }
        }
        .start();
    }

    private void IniciarActivitySeleccionarSalas() {
        Intent MiIntent = new Intent(Activity_Premiacion.this, Activity_SeleccionarSala.class);
        startActivity(MiIntent);
        finish();
    }

}

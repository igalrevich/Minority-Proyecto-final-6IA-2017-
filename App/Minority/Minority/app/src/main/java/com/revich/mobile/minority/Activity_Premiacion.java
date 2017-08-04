package com.revich.mobile.minority;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Activity_Premiacion extends AppCompatActivity {
    TextView tvStatusGanador, tvCuantasMonedasGano;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent ElIntentQueVino= getIntent();
        Bundle ElBundleQueVino= ElIntentQueVino.getExtras();
        int CantJugadores= ElBundleQueVino.getInt("CantJugadores");
        int MontoGanador= ElBundleQueVino.getInt("MontoAGanar");
        ObtenerReferencias();
        if(CantJugadores==1)
        setContentView(R.layout.activity__premiacion);
        getSupportActionBar().hide();

    }

    private void ObtenerReferencias()
    {
        tvStatusGanador=(TextView) findViewById(R.id.tvStatusGanador);
        tvCuantasMonedasGano=(TextView) findViewById(R.id.tvCuantasMonedasGano);
    }
}

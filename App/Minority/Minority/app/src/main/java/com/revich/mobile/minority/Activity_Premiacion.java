package com.revich.mobile.minority;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_Premiacion extends AppCompatActivity {
    TextView tvStatusGanador, tvCuantasMonedasGano;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__premiacion);
        getSupportActionBar().hide();
        ObtenerReferencias();
        new BuscarMonedasUsuario().execute("http://apiminorityproyecto.azurewebsites.net/api/usuario/GetMonedas/"+DatosImportantesApp.GetIdUsuario());
        /*
        Intent ElIntentQueVino= getIntent();
        Bundle ElBundleQueVino= ElIntentQueVino.getExtras();
        int CantJugadores= ElBundleQueVino.getInt("CantJugadores");
        int MontoGanador= ElBundleQueVino.getInt("MontoAGanar");
        if(CantJugadores==1)
        {
            tvStatusGanador.setText("Sos el ganador de la sala");
            tvCuantasMonedasGano.setText("Ganaste "+String.valueOf(MontoGanador)+" monedas");
        }
        else
        {
            tvStatusGanador.setText("Sos uno de los 2 ganadores de la sala");
            tvCuantasMonedasGano.setText("Ganaste "+String.valueOf(MontoGanador/2)+" monedas");
        }*/
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
                //IniciarActivitySeleccionarSalas();
            }
        }
        .start();
    }

    private void IniciarActivitySeleccionarSalas() {
        Intent MiIntent = new Intent(Activity_Premiacion.this, Activity_SeleccionarSala.class);
        startActivity(MiIntent);
        finish();
    }

    private class BuscarMonedasUsuario extends AsyncTask<String, Void, Integer> {
        private OkHttpClient client= new OkHttpClient();
        @Override
        protected void onPostExecute(Integer MonedasActual) {
            super.onPostExecute(MonedasActual);
            if(MonedasActual!=-1)
            {
                Log.d("Monedas que tenia antes", String.valueOf(DatosImportantesApp.GetMonedasUsuario()));
                Log.d("Monedas que tiene ahora", String.valueOf(MonedasActual));
                int MonedasGanadas= MonedasActual - DatosImportantesApp.GetMonedasUsuario();
                tvCuantasMonedasGano.setText("Ganaste "+String.valueOf(MonedasGanadas)+ " monedas");
                DatosImportantesApp.SetMonedasUsuario(MonedasActual);
            }

        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String url = parametros[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String jsonStr= response.body().string();
                return Integer.parseInt(jsonStr);

            } catch (IOException e) {
                String err = (e.getMessage()==null)?"Fallo ParsearSalas":e.getMessage();
                Log.d("Error :", err);
                return -1;
            }
        }


    }

}

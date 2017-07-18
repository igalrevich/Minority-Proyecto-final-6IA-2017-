package com.revich.mobile.minority;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_Login extends AppCompatActivity {
    Button btnLogin,btnOlvideMiContrasena;
    EditText txtMail,txtContrasena;
    Gson gson;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);
        getSupportActionBar().hide();
        ObtenerReferencias();
        SetearListeners();

    }

    private void ObtenerReferencias()
    {
       btnLogin= (Button) findViewById(R.id.btnLogin);
       btnOlvideMiContrasena = (Button) findViewById(R.id.btnOlvideMiContrasena);
       txtMail=(EditText) findViewById(R.id.txtMail);
       txtContrasena= (EditText) findViewById(R.id.txtContrasena);
    }

    private void SetearListeners()
    {
        btnLogin.setOnClickListener(btnLogin_Click);
        btnOlvideMiContrasena.setOnClickListener(btnOlvideMiContrasena_Click);
        txtMail.setText("igalrevich@hotmail.com");
        txtContrasena.setText("atlantacampeon");
    }

    private View.OnClickListener btnLogin_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            btnLogin.setEnabled(false);
            String Mail= txtMail.getText().toString();
            String Password= txtContrasena.getText().toString();
            if(Mail.equals("") || Password.equals(""))
            {
             Toast msg= Toast.makeText(getApplicationContext(),"Datos en blanco. Por favor completelos",Toast.LENGTH_SHORT);
             msg.show();
            }
            else
            {
                Toast msg= Toast.makeText(getApplicationContext(),"Verificando cuenta",Toast.LENGTH_SHORT);
                msg.show();
                url="http://apiminorityproyecto.azurewebsites.net/api/usuario/GetExisteUsuario/"+Mail+"/"+Password;
                new BuscarDatosTask().execute(url);
            }
        }
    };

    private View.OnClickListener btnOlvideMiContrasena_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private class BuscarDatosTask extends AsyncTask<String, Void, Usuario> {
        private OkHttpClient client= new OkHttpClient();
        @Override
        protected void onPostExecute(Usuario MiUsuario) {
            super.onPostExecute(MiUsuario);
            int IdUsuario= MiUsuario.Id;
            if(IdUsuario==0)
            {
                Toast msg= Toast.makeText(getApplicationContext(),"No existe un usuario con dichos datos. Por favor vuelva a loguearse",Toast.LENGTH_SHORT);
                msg.show();
                btnLogin.setEnabled(true);
            }
            else
            {
                DatosImportantesApp.SetNombreUsuario(MiUsuario.Nombre);
                DatosImportantesApp.SetMonedasUsuario(MiUsuario.Monedas);
                IniciarActivitySeleccionarSalas();
            }
        }

        @Override
        protected Usuario doInBackground(String... parametros) {
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
                return new Usuario();
            }
        }

        Usuario parsearResultado(String JSONstr) throws JSONException {
            gson= new Gson();
            Usuario MiUsuario= gson.fromJson(JSONstr,Usuario.class);
            return MiUsuario;
        }
    }

    private void IniciarActivitySeleccionarSalas() {
        Intent MiIntent = new Intent(Activity_Login.this, Activity_SeleccionarSala.class);
        startActivity(MiIntent);
    }
}

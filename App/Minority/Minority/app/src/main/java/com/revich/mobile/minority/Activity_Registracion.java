package com.revich.mobile.minority;

import android.app.ProgressDialog;
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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_Registracion extends AppCompatActivity {
    EditText txtMailR, txtContrasenaR, txtNombreUsuario, txtConfirmacionContrasena;
    Button btnRegistracion;
    ProgressDialog MiProgressDialog = new ProgressDialog(this);
    Toast msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__registracion);
        getSupportActionBar().hide();
        ObtenerReferencias();
    }

    private void ObtenerReferencias()
    {
        txtMailR= (EditText) findViewById(R.id.txtMailR);
        txtContrasenaR = (EditText) findViewById(R.id.txtContrasenaR);
        txtConfirmacionContrasena= (EditText) findViewById(R.id.txtConfirmacionContrasena);
        txtNombreUsuario = (EditText) findViewById(R.id.txtNombreUsuario);
        btnRegistracion = (Button) findViewById(R.id.btnRegistracion);
        btnRegistracion.setOnClickListener(btnRegistracion_click);
    }

    private View.OnClickListener btnRegistracion_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           if( !(txtMailR.getText().toString().isEmpty() || txtNombreUsuario.getText().toString().isEmpty() || txtConfirmacionContrasena.getText().toString().isEmpty() || txtContrasenaR.getText().toString().isEmpty() ) )
           {
               String Contrasena= txtContrasenaR.getText().toString();
               String ConfirmacionContrasena= txtConfirmacionContrasena.getText().toString();
               if(Contrasena.equals(ConfirmacionContrasena))
               {
                   Usuario MiUsuario= new Usuario();
                   MiUsuario.Nombre= txtNombreUsuario.getText().toString();
                   MiUsuario.Mail= txtMailR.getText().toString();
                   MiUsuario.Password= Contrasena;
                   MiUsuario.Monedas= 20;
                   Gson gson= new Gson();
                   new AgregarUsuario().execute("http://apiminorityproyecto.azurewebsites.net/api/usuario/AgregarUsuario",gson.toJson(MiUsuario));
               }
               else
               {
                   msg= Toast.makeText(getApplicationContext(),"La contrasena no coincide con su confirmacion",Toast.LENGTH_SHORT);
                   msg.show();
               }
           }
           else
           {
               msg = Toast.makeText(getApplicationContext(),"Complete los datos en blanco",Toast.LENGTH_SHORT);
               msg.show();
           }
        }
    };

    private class AgregarUsuario extends AsyncTask<String, String, Integer> {
        private OkHttpClient client= new OkHttpClient();
        String MensajeDevuelto="";
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        @Override
        protected void onPostExecute(Integer NumeroRetornado) {
            super.onPostExecute(NumeroRetornado);
            MiProgressDialog.cancel();
            msg=Toast.makeText(getApplicationContext(),MensajeDevuelto,Toast.LENGTH_SHORT);
            msg.show();
            if(NumeroRetornado==1)
            {
                IniciarActivity_Login();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            MiProgressDialog.setMessage(values[0]);
            MiProgressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... parametros) {
            String url= parametros[0];
            String json = parametros[1];
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try
            {
                publishProgress("Verificando datos de usuario");
                Response response = client.newCall(request).execute();
                MensajeDevuelto=response.body().string();
                return  1;

            }
            catch (IOException e)
            {
                String err = (e.getMessage()==null)?"Fallo IngresarUserSala":e.getMessage();
                Log.d("Error :", err);
                return 0;

            }
        }
    }

    private void IniciarActivity_Login ()
    {
        Intent intent = new Intent(Activity_Registracion.this,Activity_Login.class);
        finish();
        startActivity(intent);
    }
}

package com.revich.mobile.minority;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_OlvideMiContrasena extends AppCompatActivity {
    TextView txtContrasena, txtConfirmacionContrasena;
    Button btnCambiarMiContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity__olvide_mi_contrasena);
        ObtenerReferencias();
    }

    private void ObtenerReferencias()
    {
      txtContrasena=(TextView) findViewById(R.id.txtContrasena);
      txtConfirmacionContrasena= (TextView) findViewById(R.id.txtConfirmacionContrasena);
      btnCambiarMiContrasena= (Button) findViewById(R.id.btnCambiarMiContrasena);
    }

    private void SetearListeners()
    {
        btnCambiarMiContrasena.setOnClickListener(btnCambiarMiContrasena_Click);
    }

    private View.OnClickListener btnCambiarMiContrasena_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String Contrasena= txtContrasena.getText().toString();
            String ConfirmacionContrasena= txtConfirmacionContrasena.getText().toString();
            if(Contrasena.equals("") || ConfirmacionContrasena.equals(""))
            {
                Toast msg= Toast.makeText(getApplicationContext(),"Datos en blanco. Por favor completelos",Toast.LENGTH_SHORT);
                msg.show();
            }

        }
    };

}

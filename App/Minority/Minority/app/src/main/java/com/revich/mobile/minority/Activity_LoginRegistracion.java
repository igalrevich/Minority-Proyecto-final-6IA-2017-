package com.revich.mobile.minority;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_LoginRegistracion extends AppCompatActivity {
    Button btnLoginLR, btnRegistracionLR;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login_registracion);
        getSupportActionBar().hide();
        ObtenerReferenciasYSetearListeners();
    }

    private void ObtenerReferenciasYSetearListeners()
    {
        btnLoginLR= (Button) findViewById(R.id.btnLoginLR);
        btnRegistracionLR= (Button) findViewById(R.id.btnRegistracionLR);
        btnLoginLR.setOnClickListener(btnLoginLR_click);
        btnRegistracionLR.setOnClickListener(btnRegistracionLR_click);
    }

    private View.OnClickListener btnLoginLR_click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            IrAActivityLoginORegistracion(true);
        }
    };

    private View.OnClickListener btnRegistracionLR_click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            IrAActivityLoginORegistracion(false);
        }
    };

    private  void IrAActivityLoginORegistracion( boolean Login)
    {
        if(Login)
        {
            intent= new Intent(Activity_LoginRegistracion.this,Activity_Login.class);
            finish();
        }
        else
        {
            intent= new Intent(Activity_LoginRegistracion.this,Activity_Registracion.class);
            finish();
        }
        startActivity(intent);
    }
}

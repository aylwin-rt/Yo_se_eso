package com.aylwin.yo_se_eso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.aylwin.yo_se_eso.R;

public class PrincipalActivity extends AppCompatActivity {
    ImageButton btn_proponer_ejercicio, btn_resolver_ejercicio, btn_mis_ejercicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        setTitle("Menú Principal");
        Init();
        InitEvents();


    }

    private void Init() {
        btn_proponer_ejercicio = findViewById(R.id.btn_proponer_ejercicio);
        btn_resolver_ejercicio = findViewById(R.id.btn_resolver_ejercicio);
        btn_mis_ejercicios=findViewById(R.id.btn_mis_ejercicios);
    }

    private void InitEvents() {
        btn_proponer_ejercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, RegistrarPreguntaActivity.class);
                startActivity(intent);
            }
        });


        btn_resolver_ejercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, PreguntasActivity.class);
                startActivity(intent);
            }
        });

        btn_mis_ejercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, PreguntasPorCodigoActivity.class);
                startActivity(intent);
            }
        });
    }

}
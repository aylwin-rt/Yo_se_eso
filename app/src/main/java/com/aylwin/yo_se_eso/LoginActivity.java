package com.aylwin.yo_se_eso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    Button btn_registrese, btn_ingresar;
    EditText edt_email, edt_contrasenya;
    CheckBox chk_recuerdame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();
        InitEvents();
        recuperarPreferencia();

    }

    private void InitEvents() {
        btn_registrese = findViewById(R.id.btn_registrese);
        btn_registrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,RegistroActivity.class));
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        btn_ingresar = findViewById(R.id.btn_ingresar);
        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,RegistroActivity.class));
                Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
                startActivity(intent);


                String email = edt_email.getText().toString();
                String contrasenya = edt_contrasenya.getText().toString();

                if (chk_recuerdame.isChecked()) {
                    guardarPreferencia(email, contrasenya, "1");
                } else {
                    guardarPreferencia(email, contrasenya, "0");
                }
            }
        });
    }

    private void Init() {
        edt_email = findViewById(R.id.edt_email);
        edt_contrasenya = findViewById(R.id.edt_contrasenya);
        btn_ingresar = findViewById(R.id.btn_ingresar);
        chk_recuerdame = findViewById(R.id.chk_recuerdame);
    }

    public void recuperarPreferencia(){
        SharedPreferences preferences = getSharedPreferences("Preferencia_loguin",0);
        String email = preferences.getString("email","");
        String contrasenya = preferences.getString("contrasenya","");
        String estado_activo = preferences.getString("estado_activo","0");



        // Verificar si existe en la BD

        if (estado_activo.equals("1")){
            chk_recuerdame.setChecked(true);

            edt_email.setText(email);
            edt_contrasenya.setText(contrasenya);

        }else{
            chk_recuerdame.setChecked(false);
        }

        // Intent al menú

    }

    public void guardarPreferencia(String email, String contrasenya, String estado_activo) {
        SharedPreferences.Editor preferences = getSharedPreferences("Preferencia_loguin",0).edit();
        preferences.putString("email",email);
        preferences.putString("contrasenya",contrasenya);
        preferences.putString("estado_activo",estado_activo);
        preferences.commit();
    }

}

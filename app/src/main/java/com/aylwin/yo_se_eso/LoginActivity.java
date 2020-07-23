package com.aylwin.yo_se_eso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.aylwin.yo_se_eso.modelo.Usuario;
import com.aylwin.yo_se_eso.modelo.request.LoguinRequest;
import com.aylwin.yo_se_eso.networking.EndPoint;
import com.aylwin.yo_se_eso.networking.HelperWs;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btn_registrese, btn_ingresar;
    EditText edt_email, edt_contrasenya;
    CheckBox chk_recuerdame;
    SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();
        InitEvents();
        recuperarPreferencia();

    }

    private void InitEvents() {

        btn_registrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,RegistroActivity.class));
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });


        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,RegistroActivity.class));

                pd = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText("Por favor, espere");
                pd.setCancelable(false);
                pd.show();



                LoguinRequest loguinRequest = new LoguinRequest();
                loguinRequest.setEmail(edt_email.getText().toString());
                loguinRequest.setContrasenya(edt_contrasenya.getText().toString());

                EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
                Call<Usuario> response = endPoint.autenticarCredenciales(loguinRequest);
                response.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                        if(response.isSuccessful())
                        {
                            Usuario usuario = response.body();

                            if(usuario.getMensajeCodigo()==200)
                            {
                                //Autenticación exitosa
                                //Guardar en la preferencia
                                guardarPreferenciaUsuario(usuario.getIdUsuario(),usuario.getToken());
                                pd.dismiss();
                                irMenuPrincipal();

                            }else{
                                //Error de autenticación
                                pd.dismiss();

                                pd = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                                pd.setContentText(usuario.getMensajeResultado());
                                pd.setCancelable(false);
                                pd.show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        //Error de autenticación
                        pd.dismiss();

                        pd = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                        pd.setContentText(t.getMessage());
                        pd.setCancelable(false);
                        pd.show();
                    }
                });


                //Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
                //startActivity(intent);


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
        btn_registrese = findViewById(R.id.btn_registrese);
        btn_ingresar = findViewById(R.id.btn_ingresar);
    }

    public void recuperarPreferencia() {
        SharedPreferences preferences = getSharedPreferences("Preferencia_loguin", 0);
        String email = preferences.getString("email", "");
        String contrasenya = preferences.getString("contrasenya", "");
        String estado_activo = preferences.getString("estado_activo", "0");


        // Verificar si existe en la BD

        if (estado_activo.equals("1")) {
            chk_recuerdame.setChecked(true);

            edt_email.setText(email);
            edt_contrasenya.setText(contrasenya);

        } else {
            chk_recuerdame.setChecked(false);
        }

        // Intent al menú

    }

    public void guardarPreferencia(String email, String contrasenya, String estado_activo) {
        SharedPreferences.Editor preferences = getSharedPreferences("Preferencia_loguin", 0).edit();
        preferences.putString("email", email);
        preferences.putString("contrasenya", contrasenya);
        preferences.putString("estado_activo", estado_activo);
        preferences.commit();
    }

    private void guardarPreferenciaUsuario(int codigoUsuario, String token){

        SharedPreferences.Editor preferences = getSharedPreferences("PREFERENCIA_USUARIO",0).edit();
        preferences.putInt("codigoUsuario",codigoUsuario);
        preferences.putString("token",token);
        preferences.commit();
    }

    public void irMenuPrincipal(){

        Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
        startActivity(i);
    }
}

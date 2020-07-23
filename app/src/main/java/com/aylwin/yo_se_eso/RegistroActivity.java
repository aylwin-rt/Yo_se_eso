package com.aylwin.yo_se_eso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aylwin.yo_se_eso.modelo.Usuario;
import com.aylwin.yo_se_eso.modelo.request.LoguinRequest;
import com.aylwin.yo_se_eso.modelo.response.Respuesta;
import com.aylwin.yo_se_eso.networking.EndPoint;
import com.aylwin.yo_se_eso.networking.HelperWs;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    Button btn_registrar_usuario;
    EditText edt_nombres, edt_apellidos, edt_email, edt_contrasenya, edt_confirmar_contrasenya;
    SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Init();
        InitEvents();
    }

    private void InitEvents() {
        //btn_registrar_usuario = findViewById(R.id.btn_registrar_usuario);
        btn_registrar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Le diste clic en el botón registrar xd",Toast.LENGTH_LONG).show();

                pd = new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText("Por favor, espere");
                pd.setCancelable(false);
                pd.show();


                final Usuario usuario = new Usuario();

                usuario.setNombres(edt_nombres.getText().toString());
                usuario.setApellidos(edt_apellidos.getText().toString());
                usuario.setEmail(edt_email.getText().toString());
                usuario.setContrasenya(edt_contrasenya.getText().toString());

/*
                usuario.setNombres("Franklin");
                usuario.setApellidos("Mendoza");
                usuario.setEmail("fmendoza@gmail.com");
                usuario.setContrasenya("trooo");

 */


                EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
                Call<Respuesta> response = endPoint.grabarUsuario(usuario);
                response.enqueue(new Callback<Respuesta>() {
                    @Override
                    public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                        if (response.isSuccessful()) {
                            Respuesta respuesta = response.body();

                            if (respuesta.getMensajeCodigo() == 200) {
                                //Grabó correctamente
                                pd.dismiss();
                                new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informativo")
                                        .setContentText("Usuario registrado")
                                        .setConfirmText("Continuar")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        }).show();
                                /*
                                Toast.makeText(getApplicationContext(),"Grabado correctamente",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegistroActivity.this, InicioActivity.class);
                                startActivity(intent);

                                 */
                            } else {
                                //Algun tipo de error o validacion
                                pd.dismiss();

                                pd = new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.ERROR_TYPE);
                                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                                pd.setContentText(usuario.getMensajeResultado());
                                pd.setCancelable(false);
                                pd.show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Respuesta> call, Throwable t) {
                        pd.dismiss();

                        pd = new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.ERROR_TYPE);
                        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                        pd.setContentText(t.getMessage());
                        pd.setCancelable(false);
                        pd.show();
                    }
                });


            }
        });
    }

    private void Init() {

        edt_nombres = findViewById(R.id.edt_nombres);
        edt_apellidos = findViewById(R.id.edt_apellidos);
        edt_email = findViewById(R.id.edt_email);
        edt_contrasenya = findViewById(R.id.edt_contrasenya);
        edt_confirmar_contrasenya = findViewById(R.id.edt_confirmar_contrasenya);
        btn_registrar_usuario = findViewById(R.id.btn_registrar_usuario);

    }
}

package com.aylwin.yo_se_eso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aylwin.yo_se_eso.modelo.response.Pregunta;
import com.aylwin.yo_se_eso.modelo.response.Respuesta;
import com.aylwin.yo_se_eso.networking.EndPoint;
import com.aylwin.yo_se_eso.networking.HelperWs;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import com.mobsandgeeks.saripaar.Validator;

public class RegistrarPreguntaActivity extends AppCompatActivity {

    //Validator validator;

    SweetAlertDialog pd;

    int valor;

    Pregunta pregunta;

    EditText edt_nombre, edt_tema;

    Button btn_adjuntar, btn_publicar;

    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_pregunta);
        setTitle("Proponer Ejercicio");

        Init();
        InitEvents();
        recuperarPreferencia();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            //Editar nuevo libro
            setTitle("Edición de Ejercicio");

            pregunta = (Pregunta) bundle.getSerializable("pregunta");

            edt_nombre.setText(pregunta.getNombre());
            edt_tema.setText(pregunta.getTema());

            //String idioma = libro.getIdioma();

            valor = 1;

        } else {
            //Crear nuevo libro
            setTitle("Registro de Ejercicio");

            edt_nombre.setText("");
            edt_tema.setText("");

            valor = 0;
        }

        //validator = new Validator(this);
        //validator.setValidationListener(this);

    }

    private void InitEvents() {
        btn_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (valor == 0) {
                    //Creando
                    grabarLibro();
                } else {
                    //Editando
                    editarLibro();
                }

            }
        });
    }

    private void Init() {
        edt_nombre = findViewById(R.id.edt_nombre);
        edt_tema = findViewById(R.id.edt_tema);
        btn_adjuntar = findViewById(R.id.btn_adjuntar);
        btn_publicar = findViewById(R.id.btn_publicar);

    }

    public void editarLibro() {

        pd = new SweetAlertDialog(RegistrarPreguntaActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
        pd.setContentText("Por favor, espere");
        pd.setCancelable(false);
        pd.show();

        Pregunta preguntaRequest = new Pregunta();
        preguntaRequest.setNombre(edt_nombre.getText().toString());
        preguntaRequest.setNombre(edt_tema.getText().toString());

        EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
        Call<Respuesta> response = endPoint.actualizarPregunta(preguntaRequest);
        response.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {

                if (response.isSuccessful()) {

                    Respuesta respuesta = response.body();

                    if (respuesta.getMensajeCodigo() == 200) {

                        pd.dismiss();

                        new SweetAlertDialog(RegistrarPreguntaActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Informativo")
                                .setContentText("Ejercicio registrado")
                                .setConfirmText("Continuar")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        onBackPressed();
                                    }
                                }).show();
                    } else {

                        pd.dismiss();

                        pd = new SweetAlertDialog(RegistrarPreguntaActivity.this, SweetAlertDialog.WARNING_TYPE);
                        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                        pd.setContentText(respuesta.getMensajeResultado());
                        pd.setCancelable(false);
                        pd.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

                pd.dismiss();

                pd = new SweetAlertDialog(RegistrarPreguntaActivity.this, SweetAlertDialog.ERROR_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText(t.getMessage());
                pd.setCancelable(false);
                pd.show();
            }
        });

    }


    public void grabarLibro() {

        pd = new SweetAlertDialog(RegistrarPreguntaActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
        pd.setContentText("Por favor, espere");
        pd.setCancelable(false);
        pd.show();

        Pregunta pregunta = new Pregunta();
        pregunta.setNombre(edt_nombre.getText().toString());
        pregunta.setTema(edt_tema.getText().toString());
        pregunta.setIdUsuario(idUsuario);
        pregunta.setRutaImagen("");


        EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
        Call<Respuesta> response = endPoint.grabarPregunta(pregunta);
        response.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {

                if (response.isSuccessful()) {

                    Respuesta respuesta = response.body();

                    if (respuesta.getMensajeCodigo() == 200) {

                        pd.dismiss();

                        new SweetAlertDialog(RegistrarPreguntaActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Informativo")
                                .setContentText("Ejercicio registrado")
                                .setConfirmText("Continuar")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        onBackPressed();
                                    }
                                }).show();
                    } else {

                        pd.dismiss();

                        pd = new SweetAlertDialog(RegistrarPreguntaActivity.this, SweetAlertDialog.WARNING_TYPE);
                        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                        pd.setContentText(respuesta.getMensajeResultado());
                        pd.setCancelable(false);
                        pd.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

                pd.dismiss();

                pd = new SweetAlertDialog(RegistrarPreguntaActivity.this, SweetAlertDialog.ERROR_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText(t.getMessage());
                pd.setCancelable(false);
                pd.show();
            }
        });
    }

    private void recuperarPreferencia(){

        SharedPreferences preferences = getSharedPreferences("PREFERENCIA_USUARIO",0);
        idUsuario = preferences.getInt("idUsuario",-1);
        //token = preferences.getString("token","");
    }



/*
    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                EditText failed = (EditText) view;
                failed.requestFocus();
                failed.setError(message);
                //((EditText) view).setError(message);
            }

        }

 */
}
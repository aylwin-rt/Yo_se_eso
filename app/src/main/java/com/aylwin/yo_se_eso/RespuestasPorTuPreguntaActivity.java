package com.aylwin.yo_se_eso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aylwin.yo_se_eso.adapter.RespuestaPorTuPreguntaAdapter;
import com.aylwin.yo_se_eso.modelo.response.Pregunta;
import com.aylwin.yo_se_eso.modelo.response.Respuesta;
import com.aylwin.yo_se_eso.networking.EndPoint;
import com.aylwin.yo_se_eso.networking.HelperWs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RespuestasPorTuPreguntaActivity extends AppCompatActivity {

    RecyclerView recycler_respuestas;

    RespuestaPorTuPreguntaAdapter adapter;

    int idPregunta;
    String token;

    SweetAlertDialog pd;

    FloatingActionButton fabAgregarRespuesta;

    Pregunta pregunta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuestas_por_tu_pregunta);

        Init();
        InitEvents();
        setTitle("Respuestas de la pregunta");

        Bundle bundle = getIntent().getExtras();

        //Recuperamos el Bundle
        pregunta = (Pregunta) bundle.getSerializable("pregunta");

        //edt_nombre.setText(pregunta.getNombre());
        //edt_tema.setText(pregunta.getTema());
        idPregunta= pregunta.getIdPregunta();

    }

    private void InitEvents() {

        fabAgregarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RespuestasPorTuPreguntaActivity.this, RegistrarRespuestaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Init() {
        fabAgregarRespuesta = findViewById(R.id.fabAgregarRespuesta);
        recycler_respuestas = findViewById(R.id.recycler_respuestas);
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarPreferencia();

        pd = new SweetAlertDialog(RespuestasPorTuPreguntaActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
        pd.setContentText("Por favor, espere LISTA");
        pd.setCancelable(false);
        pd.show();

        EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
        Call<ArrayList<Respuesta>> response = endPoint.obtenerRespuestasPorPregunta("Bearer" + " " + token,idPregunta);
        response.enqueue(new Callback<ArrayList<Respuesta>>() {
            @Override
            public void onResponse(Call<ArrayList<Respuesta>> call, Response<ArrayList<Respuesta>> response) {

                if (response.isSuccessful()) {

                    ArrayList<Respuesta> listRespuesta = response.body();
                    configurarAdaptador(listRespuesta);
                    Toast toast = Toast.makeText(getApplicationContext(), "Respondió el llamado a la Respuesta", Toast.LENGTH_SHORT);
                    pd.dismiss();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Respuesta>> call, Throwable t) {

                pd.dismiss();

                pd = new SweetAlertDialog(RespuestasPorTuPreguntaActivity.this, SweetAlertDialog.ERROR_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText(t.getMessage());
                pd.setCancelable(false);
                pd.show();
            }
        });

    }

    private void configurarAdaptador(ArrayList<Respuesta> listRespuesta) {

        adapter = new RespuestaPorTuPreguntaAdapter(listRespuesta);

        recycler_respuestas.setAdapter(adapter);
        recycler_respuestas.setLayoutManager(new LinearLayoutManager(this));
/*
        adapter.setOnItemClickListener(new RespuestaPorTuPreguntaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Respuesta respuesta) {

                //Creamos el bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("respuesta", (Serializable) respuesta);//TODO VER ESTO

                //Intent
                Intent intent = new Intent(RespuestasPorTuPreguntaActivity.this,RespuestasPorPreguntasActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

 */

    }

    private void recuperarPreferencia() {

        SharedPreferences preferences = getSharedPreferences("PREFERENCIA_USUARIO", 0);
        //idUsuario = preferences.getInt("idUsuario", -1);
        token = preferences.getString("token", "");
    }
}
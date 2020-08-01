package com.aylwin.yo_se_eso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RespuestasPorTuPreguntasActivity extends AppCompatActivity {

    RecyclerView recycler_respuestas;

    RespuestaPorTuPreguntaAdapter adapter;

    int idPregunta;
    String token;

    Pregunta pregunta;

    SweetAlertDialog pd;

    FloatingActionButton fabAgregarRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuestas_por_tu_pregunta);

        Init();
        InitEvents();


        Bundle bundle = getIntent().getExtras();

        //Recuperamos el Bundle
        pregunta = (Pregunta) bundle.getSerializable("pregunta");

        //edt_nombre.setText(pregunta.getNombre());
        //edt_tema.setText(pregunta.getTema());
        idPregunta= pregunta.getIdPregunta();

    }

    private void InitEvents() {

        setTitle("Respuestas de tu Pregunta");//Respuestas por idPregunta



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                //viewHolder.getAdapterPosition() = Posicion donde se hace el swiped

                //Llamar a ese Layout

                Respuesta respuesta = adapter.obtenerRespuesta(viewHolder.getAdapterPosition());


                //Eliminar la respuesta
                pd = new SweetAlertDialog(RespuestasPorTuPreguntasActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText("Por favor, espere ELIMINAR");
                pd.setCancelable(false);
                pd.show();

                EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
                Call<Respuesta> response = endPoint.eliminarRespuesta(respuesta.getIdRespuesta());
                response.enqueue(new Callback<Respuesta>() {
                    @Override
                    public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {

                        if (response.isSuccessful()) {

                            Respuesta respuesta = response.body();

                            if (respuesta.getMensajeCodigo() == 200) {

                                adapter.eliminarRespuesta(viewHolder.getAdapterPosition());
                                pd.dismiss();
                            } else {

                                pd.dismiss();

                                pd = new SweetAlertDialog(RespuestasPorTuPreguntasActivity.this, SweetAlertDialog.ERROR_TYPE);
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

                        pd = new SweetAlertDialog(RespuestasPorTuPreguntasActivity.this, SweetAlertDialog.ERROR_TYPE);
                        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                        pd.setContentText(t.getMessage());
                        pd.setCancelable(false);
                        pd.show();
                    }
                });

            }
        }).attachToRecyclerView(recycler_respuestas);


/*
        fabAgregarRespuesta.setOnClickListener(new View.OnClickListener() { //TODO DUPLICADO
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RespuestasPorPreguntasActivity.this, RegistrarRespuestaActivity.class);
                startActivity(intent);
                startActivity(intent);
            }
        });

 */


        fabAgregarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creamos el bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("pregunta",pregunta);

                Intent intent = new Intent(RespuestasPorTuPreguntasActivity.this,RegistrarRespuestaActivity.class);
                intent.putExtras(bundle);
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

        pd = new SweetAlertDialog(RespuestasPorTuPreguntasActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
        pd.setContentText("Por favor, espere LISTA");
        pd.setCancelable(false);
        pd.show();

        EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
        Call<ArrayList<Respuesta>> response = endPoint.obtenerRespuestasPorPregunta("Bearer" + " " + token, idPregunta);
        response.enqueue(new Callback<ArrayList<Respuesta>>() {
            @Override
            public void onResponse(Call<ArrayList<Respuesta>> call, Response<ArrayList<Respuesta>> response) {

                if (response.isSuccessful()) {

                    ArrayList<Respuesta> listRespuesta = response.body();
                    configurarAdaptador(listRespuesta);
                    Toast toast = Toast.makeText(getApplicationContext(), "Respondió el llamado a Pregunta", Toast.LENGTH_SHORT);
                    pd.dismiss();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Respuesta>> call, Throwable t) {

                pd.dismiss();

                pd = new SweetAlertDialog(RespuestasPorTuPreguntasActivity.this, SweetAlertDialog.ERROR_TYPE);
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


    }

    private void recuperarPreferencia() {

        SharedPreferences preferences = getSharedPreferences("PREFERENCIA_USUARIO", 0);
        //idUsuario = preferences.getInt("idUsuario", -1); //TODO REVISAR ESTO: LISTAR RESPUESTAS
        token = preferences.getString("token", "");
    }


}
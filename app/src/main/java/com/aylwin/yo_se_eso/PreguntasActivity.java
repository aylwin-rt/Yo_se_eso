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
import android.widget.Toast;

import com.aylwin.yo_se_eso.adapter.PreguntaAdapter;
import com.aylwin.yo_se_eso.modelo.response.Pregunta;
import com.aylwin.yo_se_eso.modelo.response.Respuesta;
import com.aylwin.yo_se_eso.networking.EndPoint;
import com.aylwin.yo_se_eso.networking.HelperWs;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreguntasActivity extends AppCompatActivity {

    RecyclerView recycler_preguntas;

    PreguntaAdapter adapter;

    int idUsuario;
    String token;

    SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        recycler_preguntas = findViewById(R.id.recycler_preguntas);
        setTitle("Listado de Preguntas");
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarPreferencia();

        pd = new SweetAlertDialog(PreguntasActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
        pd.setContentText("Por favor, espere LISTA");
        pd.setCancelable(false);
        pd.show();

        EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
        Call<ArrayList<Pregunta>> response = endPoint.obtenerPreguntas("Bearer" + " " +token);
        response.enqueue(new Callback<ArrayList<Pregunta>>() {
            @Override
            public void onResponse(Call<ArrayList<Pregunta>> call, Response<ArrayList<Pregunta>> response) {

                if (response.isSuccessful()){

                    ArrayList<Pregunta> listPregunta = response.body();

                    configurarAdaptador(listPregunta);

                    Toast toast = Toast. makeText(getApplicationContext(), "Respondió el llamado a Pregunta", Toast. LENGTH_SHORT);

                    pd.dismiss();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Pregunta>> call, Throwable t) {

                pd.dismiss();

                pd = new SweetAlertDialog(PreguntasActivity.this, SweetAlertDialog.ERROR_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText(t.getMessage());
                pd.setCancelable(false);
                Toast toast = Toast. makeText(getApplicationContext(), t.toString(), Toast. LENGTH_SHORT);
                pd.show();
            }
        });

    }

    private void configurarAdaptador(ArrayList<Pregunta> listPregunta) {

        adapter = new PreguntaAdapter(listPregunta);

        recycler_preguntas.setAdapter(adapter);
        //recycler_preguntas.setLayoutManager(new GridLayoutManager(this,2));
        recycler_preguntas.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new PreguntaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pregunta pregunta) {

                Bundle bundle = new Bundle();
                //bundle.putSerializable("pregunta",pregunta); //TODO FALTA ESTE

                //Intent
                Intent intent = new Intent(PreguntasActivity.this,RegistrarPreguntaActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /*
    @OnClick(R.id.fabAgregarLibro)
    public void agregarLibro(){

        Intent i = new Intent(LibrosActivity.this, RegistrarLibroActivity.class);
        startActivity(i);
    }

     */

    private void recuperarPreferencia(){

        SharedPreferences preferences = getSharedPreferences("PREFERENCIA_USUARIO",0);
        idUsuario = preferences.getInt("idUsuario",-1);
        token = preferences.getString("token","");
    }

}

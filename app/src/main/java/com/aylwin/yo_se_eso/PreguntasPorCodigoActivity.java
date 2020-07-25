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
import com.aylwin.yo_se_eso.adapter.PreguntaPorCodigoAdapter;
import com.aylwin.yo_se_eso.modelo.response.Pregunta;
import com.aylwin.yo_se_eso.networking.EndPoint;
import com.aylwin.yo_se_eso.networking.HelperWs;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreguntasPorCodigoActivity extends AppCompatActivity {

    RecyclerView recycler_preguntasPorCodigo;

    PreguntaPorCodigoAdapter adapter;

    int idUsuario;
    String token;

    SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_por_codigo);
        recycler_preguntasPorCodigo = findViewById(R.id.recycler_preguntasPorCodigo);
        setTitle("Listado de tus Preguntas");

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                //viewHolder.getAdapterPosition() = Posicion donde se hace el swiped

                //Llamar a ese Layout

                Pregunta pregunta = adapter.obtenerPregunta(viewHolder.getAdapterPosition());

                /*
                //Eliminar la pregunta
                pd = new SweetAlertDialog(PreguntasActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText("Por favor, espere ELIMINAR");
                pd.setCancelable(false);
                pd.show();

                EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
                Call<Respuesta> response =  endPoint.eliminarPregunta(pregunta.getIdPregunta());
                response.enqueue(new Callback<Respuesta>() {
                    @Override
                    public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {

                        if (response.isSuccessful()){

                            Respuesta respuesta = response.body();

                            if (respuesta.getMensajeCodigo() == 200){

                                adapter.eliminarPregunta(viewHolder.getAdapterPosition());
                                pd.dismiss();
                            }
                            else{

                                pd.dismiss();

                                pd = new SweetAlertDialog(PreguntasActivity.this, SweetAlertDialog.ERROR_TYPE);
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

                        pd = new SweetAlertDialog(PreguntasActivity.this, SweetAlertDialog.ERROR_TYPE);
                        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                        pd.setContentText(t.getMessage());
                        pd.setCancelable(false);
                        pd.show();
                    }
                });

                 */


            }
        }).attachToRecyclerView(recycler_preguntasPorCodigo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarPreferencia();

        pd = new SweetAlertDialog(PreguntasPorCodigoActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
        pd.setContentText("Por favor, espere LISTA POR CODIGO");
        pd.setCancelable(false);
        pd.show();

        EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
        Call<ArrayList<Pregunta>> response = endPoint.obtenerPreguntasPorCodigo("Bearer" + " " +token,idUsuario);
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

                pd = new SweetAlertDialog(PreguntasPorCodigoActivity.this, SweetAlertDialog.ERROR_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText(t.getMessage());
                pd.setCancelable(false);
                Toast toast = Toast. makeText(getApplicationContext(), t.toString(), Toast. LENGTH_SHORT);
                pd.show();
            }
        });



    }

    private void configurarAdaptador(ArrayList<Pregunta> listPregunta) {

        adapter = new PreguntaPorCodigoAdapter(listPregunta);

        recycler_preguntasPorCodigo.setAdapter(adapter);
        //recycler_preguntas.setLayoutManager(new GridLayoutManager(this,2));
        recycler_preguntasPorCodigo.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new PreguntaPorCodigoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pregunta pregunta) {

                Bundle bundle = new Bundle();
                //bundle.putSerializable("pregunta",pregunta); //TODO FALTA ESTE

                //Intent
                Intent intent = new Intent(PreguntasPorCodigoActivity.this,RegistrarPreguntaActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                //1. Abrir la camara
                //2. Permisos
                //3. Binary
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
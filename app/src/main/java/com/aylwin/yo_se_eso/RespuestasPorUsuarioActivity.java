package com.aylwin.yo_se_eso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.aylwin.yo_se_eso.adapter.PreguntaPorCodigoAdapter;
import com.aylwin.yo_se_eso.adapter.RespuestaPorUsuarioAdapter;
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

public class RespuestasPorUsuarioActivity extends AppCompatActivity {

    RecyclerView recycler_respuestasPorUsuario;

    RespuestaPorUsuarioAdapter adapter;

    int idUsuario;
    String token;

    SweetAlertDialog pd;

    //FloatingActionButton fabAgregarPreguntaPorCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuestas_por_usuario);

        Init();
        InitEvents();

    }

    private void InitEvents() {

        setTitle("Listado de tus Respuestas");
/*
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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
                pd = new SweetAlertDialog(RespuestasPorUsuarioActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText("Por favor, espere ELIMINAR");
                pd.setCancelable(false);
                pd.show();

                EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
                Call<Respuesta> response =  endPoint.eliminarPregunta(respuesta.getIdRespuesta());
                response.enqueue(new Callback<Respuesta>() {
                    @Override
                    public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {

                        if (response.isSuccessful()){

                            Respuesta respuesta = response.body();

                            if (respuesta.getMensajeCodigo() == 200){

                                adapter.eliminarRespuesta(viewHolder.getAdapterPosition());
                                pd.dismiss();
                            }
                            else{

                                pd.dismiss();

                                pd = new SweetAlertDialog(RespuestasPorUsuarioActivity.this, SweetAlertDialog.ERROR_TYPE);
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

                        pd = new SweetAlertDialog(RespuestasPorUsuarioActivity.this, SweetAlertDialog.ERROR_TYPE);
                        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                        pd.setContentText(t.getMessage());
                        pd.setCancelable(false);
                        pd.show();
                    }
                });

            }
        }).attachToRecyclerView(recycler_respuestasPorUsuario);


        fabAgregarPreguntaPorCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RespuestasPorUsuarioActivity.this, RegistrarPreguntaActivity.class);
                startActivity(intent);
            }
        });

 */

    }

    private void Init() {

        recycler_respuestasPorUsuario = findViewById(R.id.recycler_respuestas);
        //fabAgregarPreguntaPorCodigo = findViewById(R.id.fabAgregarPreguntaPorCodigo);

    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarPreferencia();

        pd = new SweetAlertDialog(RespuestasPorUsuarioActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
        pd.setContentText("Por favor, espere LISTA POR USUARIO");
        pd.setCancelable(false);
        pd.show();

        EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
        Call<ArrayList<Respuesta>> response = endPoint.obtenerRespuestasPorUsuario("Bearer" + " " +token,idUsuario);
        response.enqueue(new Callback<ArrayList<Respuesta>>() {
            @Override
            public void onResponse(Call<ArrayList<Respuesta>> call, Response<ArrayList<Respuesta>> response) {

                if (response.isSuccessful()){

                    ArrayList<Respuesta> listRespuesta = response.body();
                    configurarAdaptador(listRespuesta);
                    //Toast toast = Toast. makeText(getApplicationContext(), "Respondió el llamado a Pregunta", Toast. LENGTH_SHORT);
                    pd.dismiss();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Respuesta>> call, Throwable t) {

                pd.dismiss();

                pd = new SweetAlertDialog(RespuestasPorUsuarioActivity.this, SweetAlertDialog.ERROR_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText(t.getMessage());
                pd.setCancelable(false);
                pd.show();
            }
        });



    }

    private void configurarAdaptador(ArrayList<Respuesta> listRespuesta) {

        adapter = new RespuestaPorUsuarioAdapter(listRespuesta);

        recycler_respuestasPorUsuario.setAdapter(adapter);
        recycler_respuestasPorUsuario.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new RespuestaPorUsuarioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Respuesta respuesta) {


/*
                final CharSequence[] opciones = {"Ver respuestas", "Editar pregunta", "Cancelar"};
                final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RespuestasPorUsuarioActivity.this);
                alertOpciones.setTitle("Seleccione una opción");
                alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (opciones[i].equals("Ver respuestas")) {

                            //TODO AQUÍ LLAMAR A RespuestasPorCódigoActivity

                            //Creamos el bundle
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("pregunta",pregunta);

                            Intent intent = new Intent(RespuestasPorUsuarioActivity.this, RespuestasPorTuPreguntasActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else {
                            if (opciones[i].equals("Editar pregunta")) {

                                //Creamos el bundle
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("pregunta",pregunta);

                                //Intent
                                Intent intent = new Intent(RespuestasPorUsuarioActivity.this,RegistrarPreguntaActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else {
                                dialogInterface.dismiss();
                            }
                        }
                    }
                });
                alertOpciones.show();


 */




            }
        });

    }

    private void recuperarPreferencia(){

        SharedPreferences preferences = getSharedPreferences("PREFERENCIA_USUARIO",0);
        idUsuario = preferences.getInt("idUsuario",-1);
        token = preferences.getString("token","");
    }
}
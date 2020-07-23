package com.aylwin.yo_se_eso.networking;

import com.aylwin.yo_se_eso.modelo.Usuario;
import com.aylwin.yo_se_eso.modelo.request.LoguinRequest;
import com.aylwin.yo_se_eso.modelo.response.Pregunta;
import com.aylwin.yo_se_eso.modelo.response.Respuesta;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface EndPoint {
    @POST("Usuario")
    @Headers("Content-Type:application/json")
    Call<Respuesta> grabarUsuario(@Body Usuario usuario);

    @POST("Loguin")
    @Headers("Content-Type:application/json")
    Call<Usuario> autenticarCredenciales(@Body LoguinRequest loguinRequest);

    //Autorizado
    @GET("Pregunta")
    Call<ArrayList<Pregunta>> obtenerPreguntas(@Header("Authorization") String token);

    @DELETE("Pregunta")
    @Headers("Content-Type:application/json")
    Call<Respuesta> eliminarPregunta(@Query("idPregunta") int idPregunta);

    @POST("Pregunta")
    @Headers("Content-Type:application/json")
    Call<Respuesta> grabarPregunta(@Body Pregunta pregunta);

    @PUT("Pregunta")
    @Headers("Content-Type:application/json")
    Call<Respuesta> actualizarPregunta(@Body Pregunta pregunta);
}
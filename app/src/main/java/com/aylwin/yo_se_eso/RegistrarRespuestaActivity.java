package com.aylwin.yo_se_eso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aylwin.yo_se_eso.modelo.response.Pregunta;
import com.aylwin.yo_se_eso.modelo.response.Respuesta;
import com.aylwin.yo_se_eso.networking.EndPoint;
import com.aylwin.yo_se_eso.networking.HelperWs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarRespuestaActivity extends AppCompatActivity {

    //Validator validator;

    SweetAlertDialog pd;

    int valor, idUsuario, idPregunta;

    Pregunta pregunta;

    EditText edt_nombre;

    Button btn_adjuntar, btn_publicar;

    ImageView img_camara;

    String path;
    String mcurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    Bitmap bitmap;

    private final String CARPETA_RAIZ = "mis/ImagenesPrueba/";
    private final String RUTA_IMAGEN = "misFotos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_respuesta);
        setTitle("Registrar Respuesta");

        Init();
        InitEvents();
        recuperarPreferencia();

        Bundle bundle = getIntent().getExtras();

        pregunta = (Pregunta) bundle.getSerializable("pregunta");
        idPregunta= pregunta.getIdPregunta();

/*
        if (bundle != null) {
            //Editar nueva pregunta
            setTitle("Editar Respuesta");


            //Recuperamos el Bundle


            edt_nombre.setText(pregunta.getNombre());
            //edt_tema.setText(pregunta.getTema());


            valor = 1;

        } else {

 */
            //Crear nueva pregunta
            setTitle("Registrar Respuesta");

            //edt_nombre.setText("");

            //valor = 0;
        //}

        //validator = new Validator(this);
        //validator.setValidationListener(this);

    }

    private void InitEvents() {
        btn_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (valor == 0) {
                    //Creando
                    grabarRespuesta();
                //} else {
                    //Editando
                    //editarRespuesta();
                //}

            }
        });


        btn_adjuntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1. Abrir la camara
                //2. Permisos
                //3. Binary


                CargarImagen();


            }
        });

    }

    private void Init() {
        edt_nombre = findViewById(R.id.edt_nombre);
        btn_adjuntar = findViewById(R.id.btn_adjuntar);
        btn_publicar = findViewById(R.id.btn_publicar);
        img_camara = findViewById(R.id.img_camara);
    }


    public void editarRespuesta() {

        pd = new SweetAlertDialog(RegistrarRespuestaActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
        pd.setContentText("Por favor, espere EDITAR");
        pd.setCancelable(false);
        pd.show();

        Respuesta respuestaRequest = new Respuesta();
        respuestaRequest.setNombre(edt_nombre.getText().toString());
        //respuestaRequest.setTema(edt_tema.getText().toString());
        respuestaRequest.setIdUsuario(idUsuario);
        respuestaRequest.setIdPregunta(idPregunta);//TODO DEBO PASAR LA IDPREGUNTA
        respuestaRequest.setRutaImagen("");

        EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
        Call<Respuesta> response = endPoint.actualizarRespuesta(respuestaRequest);
        response.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {

                if (response.isSuccessful()) {

                    Respuesta respuesta = response.body();

                    if (respuesta.getMensajeCodigo() == 200) {

                        pd.dismiss();

                        new SweetAlertDialog(RegistrarRespuestaActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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

                        pd = new SweetAlertDialog(RegistrarRespuestaActivity.this, SweetAlertDialog.WARNING_TYPE);
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

                pd = new SweetAlertDialog(RegistrarRespuestaActivity.this, SweetAlertDialog.ERROR_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText(t.getMessage());
                pd.setCancelable(false);
                pd.show();
            }
        });

    }


    public void grabarRespuesta() {

        pd = new SweetAlertDialog(RegistrarRespuestaActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
        pd.setContentText("Por favor, espere");
        pd.setCancelable(false);
        pd.show();

        /*

        Bundle bundle = getIntent().getExtras();

        //Recuperamos el Bundle
        pregunta = (Pregunta) bundle.getSerializable("pregunta");

        //edt_nombre.setText(pregunta.getNombre());
        //edt_tema.setText(pregunta.getTema());
        idPregunta= pregunta.getIdPregunta();

         */


        Respuesta respuesta = new Respuesta();
        respuesta.setNombre(edt_nombre.getText().toString());
        //respuesta.setTema(edt_tema.getText().toString());
        respuesta.setIdUsuario(idUsuario);
        respuesta.setIdPregunta(idPregunta); //TODO VER
        respuesta.setRutaImagen("");


        EndPoint endPoint = HelperWs.getConfiguration().create(EndPoint.class);
        Call<Respuesta> response = endPoint.grabarRespuesta(respuesta);
        response.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {

                if (response.isSuccessful()) {

                    Respuesta respuesta = response.body();

                    if (respuesta.getMensajeCodigo() == 200) {

                        pd.dismiss();

                        new SweetAlertDialog(RegistrarRespuestaActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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

                        pd = new SweetAlertDialog(RegistrarRespuestaActivity.this, SweetAlertDialog.WARNING_TYPE);
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

                pd = new SweetAlertDialog(RegistrarRespuestaActivity.this, SweetAlertDialog.ERROR_TYPE);
                pd.getProgressHelper().setBarColor(Color.parseColor("#102670"));
                pd.setContentText(t.getMessage());
                pd.setCancelable(false);
                pd.show();
            }
        });
    }

    private void recuperarPreferencia() {

        SharedPreferences preferences = getSharedPreferences("PREFERENCIA_USUARIO", 0);
        idUsuario = preferences.getInt("idUsuario", -1);
        //token = preferences.getString("token","");
    }

    private void CargarImagen() {

        final CharSequence[] opciones = {"Tomar foto", "Cargar imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RegistrarRespuestaActivity.this);
        alertOpciones.setTitle("Seleccione una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar foto")) {
                    //Toast.makeText(getApplicationContext(),"TOMAR FOTO",Toast.LENGTH_LONG).show();
                    TomarFotografia();
                } else {
                    if (opciones[i].equals("Cargar imagen")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }

    private void TomarFotografia() {

        if (ContextCompat.checkSelfPermission(RegistrarRespuestaActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RegistrarRespuestaActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegistrarRespuestaActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.withAppendedPath(locationForPhotos, targetFilename));
        if (intent.resolveActivity(getPackageManager()) != null) {
            //startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, COD_FOTO);
                //startActivityForResult(intent, REQUEST_TAKE_PHOTO);

            }


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri miPath = data.getData();
                    img_camara.setImageURI(miPath);


                    break;

                case COD_FOTO: //TODO REVISAR ESTA PARTE
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String s, Uri uri) {
                                    Log.i("Ruta de almacenamiento", "Path: " + path);
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    img_camara.setImageBitmap(bitmap);
                    break;
            }
        }


    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "BACKUP_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mcurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
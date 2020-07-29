package com.aylwin.yo_se_eso;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Prueba extends AppCompatActivity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        private class ConvertBitmapToString extends AsyncTask<Bitmap, Void, String> {
            @Override
            protected void onPreExecute() {
                cimgprofile.setEnabled(false);
                dialogProgress.show();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                String encodedImage = "";
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    params[0].compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    byte[] b = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    return encodedImage;
                }catch (Exception e){

                }
                return encodedImage;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.length()!=0){
                    global.getUser().setBase64String(result);
                    UpdateImage();
                }
            }
        }

    }

 */
}
package com.aylwin.yo_se_eso.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aylwin.yo_se_eso.R;
import com.aylwin.yo_se_eso.adapter.PreguntaAdapter;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class MostrarEjerciciosFragment extends Fragment {

    RecyclerView recycler_libros;

    PreguntaAdapter adapter;

    int codigoUsuario;
    String token;

    SweetAlertDialog pd;


    public MostrarEjerciciosFragment() {
        // Required empty public constructor


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mostrar_ejercicios, container, false);
    }
}
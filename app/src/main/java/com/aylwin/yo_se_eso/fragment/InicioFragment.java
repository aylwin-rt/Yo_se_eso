package com.aylwin.yo_se_eso.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aylwin.yo_se_eso.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    Button btn_proponer_ejercicio;
    View view;

    public InicioFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);

    }
}

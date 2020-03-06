package com.aylwin.yo_se_eso;

import android.os.Bundle;

import com.aylwin.yo_se_eso.fragment.ConfiguracionFragment;
import com.aylwin.yo_se_eso.fragment.InicioFragment;
import com.aylwin.yo_se_eso.fragment.PerfilFragment;
import com.aylwin.yo_se_eso.fragment.Proponer_ejercicioFragment;
import com.aylwin.yo_se_eso.fragment.Resolver_ejercicioFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;

public class InicioActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    Fragment fragment;
    Button btn_proponer_ejercicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        */

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fragment = new InicioFragment();
        InsertarFragmento();

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.nav_Proponer_Ejercicio) {

                    fragment = new Proponer_ejercicioFragment();
                    InsertarFragmento();

                } else if (id == R.id.nav_Resolver_Ejercicio) {

                    fragment = new Resolver_ejercicioFragment();
                    InsertarFragmento();

                } else if (id == R.id.nav_Perfil) {

                    fragment = new PerfilFragment();
                    InsertarFragmento();

                } else if (id == R.id.nav_Inicio_Ejercicio) {

                    fragment = new InicioFragment();
                    InsertarFragmento();
                } else {

                    fragment = new ConfiguracionFragment();
                    InsertarFragmento();

                }

                drawer.closeDrawers();

                return false;

            }
        });

/*





        btn_proponer_ejercicio = findViewById(R.id.btn_proponer_ejercicio);
        btn_proponer_ejercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new Proponer_ejercicioFragment();
                InsertarFragmento();

            }
        });
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }


    public void InsertarFragmento() {

        if (fragment != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
        }

    }

}

package cl.jfcor.fastbilling;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import base_datos.Bd;
import modelos.RutaReparto;
import modelos.Usuario;

public class MenuUsuario extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private Bd bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBarTitle("Fastbilling");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Inicializa sincronizacion con servidor
        SharedPreferences prefs = this.getSharedPreferences("cl.jfcor.fastbilling", Context.MODE_PRIVATE);
        this.bd = Bd.getInstance(this);
        this.bd.abrir();
        Usuario usuario = this.bd.buscarUsuario(prefs.getString("cl.jfcor.fastbilling.usuario", ""));
        new SincronizacionServidor(usuario, this).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks.

        Fragment fragment = null;
        Class fragmentClass;
        Bundle args = new Bundle();

        switch(menuItem.getItemId()) {
            case R.id.nav_rutas:
                args.putSerializable("tipoLectura", 1);
                fragmentClass = RutasFragment.class;
                break;

            case R.id.nav_verificacion:
                args.putSerializable("tipoLectura", 2);
                fragmentClass = RutasFragment.class;
                break;

            case R.id.nav_reimpresion:
                args.putSerializable("tipo", 1);
                fragmentClass = ListaImpresion.class;
                break;

            case R.id.nav_pendientes_facturacion:
                args.putSerializable("tipo", 2);
                fragmentClass = ListaImpresion.class;
                break;

            case R.id.nav_mapa:
                fragmentClass = MapaFragment.class;
                break;

            case R.id.nav_reparto:
                fragmentClass = RutasRepartoFragment.class;
                break;

            default:
                fragmentClass = RutasFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragment.setArguments(args);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setActionBarTitle(String title)
    {
        this.getSupportActionBar().setTitle(title);
    }
}

package cl.jfcor.fastbilling;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import base_datos.Bd;
import modelos.API;
import modelos.OrdenLectura;
import modelos.OrdenReparto;
import modelos.Usuario;

import static cl.jfcor.fastbilling.R.id.botonContinuar;

public class IngresarClienteFragment extends Fragment implements AdapterView.OnClickListener  {

    private String tag = "Registro Reparto";
    private Activity activity;
    private Bd bd;
    private Usuario usuario;
    private OrdenReparto ordenes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingresar_cliente, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("cl.jfcor.fastbilling", Context.MODE_PRIVATE);

        //Se obtienen los datos del usuario desde la base de datos.
        this.bd.abrir();
        this.usuario = this.bd.buscarUsuario(prefs.getString("cl.jfcor.fastbilling.usuario", ""));

        //Creo Boton
        Button button = (Button) activity.findViewById(R.id.botonContinuar);
        button.setOnClickListener(this);// Agrego Metodo OnClick al Boton




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ((MenuUsuario) getActivity()).setActionBarTitle(this.tag);
        }

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        //Se obtiene una instancia de la base de datos.
        this.bd = Bd.getInstance(activity);
        this.activity = activity;
    }

    @Override
    public void onClick(View view)
    {
        EditText numeroCliente = (EditText) getView ().findViewById(R.id.numeroCliente);
        String nCliente = numeroCliente.getText().toString ();

        if (nCliente != null) {
            this.bd.abrir ();
            //Todo: buscar orden de reparto por numero de cliente
            OrdenReparto orden = this.bd.buscarOrdenReparto (nCliente);

            if (orden != null) {
                //Obtiene posicion gps al momento de guardar lectura
                String locationProvider = LocationManager.NETWORK_PROVIDER;
                LocationManager location = (LocationManager) this.getActivity ().getSystemService (Context.LOCATION_SERVICE);
                Location lastKnownLocation = location.getLastKnownLocation (locationProvider);
                orden.setGpsLatitud (lastKnownLocation.getLatitude ());
                orden.setGpsLongitud (lastKnownLocation.getLongitude ());
                orden.setEstado (4);
                orden.setFechaEntrega (new Date ().getTime ());
                this.bd.actualizaOrdenReparto (orden);

                Fragment fragment = null;
                try {
                    fragment = RutasRepartoFragment.class.newInstance ();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace ();
                } catch (IllegalAccessException e) {
                    e.printStackTrace ();
                }
                android.support.v4.app.FragmentManager fragmentManager = this.getActivity ().getSupportFragmentManager ();
                fragmentManager.beginTransaction ().replace (R.id.flContent, fragment).addToBackStack (null).commit ();
            } else {
                Toast.makeText (this.getContext (), "Cliente no registra en ruta", Toast.LENGTH_LONG).show ();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}

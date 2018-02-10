package cl.jfcor.fastbilling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;

import java.util.ArrayList;
import java.util.Date;

import base_datos.Bd;
import modelos.OrdenReparto;
import modelos.Ruta;
import modelos.RutaReparto;
import modelos.Usuario;


/**
 * Fragmento donde se muestra la lista de rutas asignadas
 */
public class RutasRepartoFragment extends Fragment implements AdapterView.OnClickListener
{

    private String tag = "Ordenes de Reparto";
    private ArrayList<RutaReparto> rutas;
    private Bd bd;
    private Usuario usuario;
    private ListView lista;
    private RowRutaReparto rutasAdapter;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_rutas_reparto, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        this.lista = (ListView) this.getView().findViewById(R.id.listaRutasReparto);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("cl.jfcor.fastbilling", Context.MODE_PRIVATE);

        //Se obtienen los datos del usuario desde la base de datos.
        this.bd.abrir();
        this.usuario = this.bd.buscarUsuario(prefs.getString("cl.jfcor.fastbilling.usuario", ""));

        this.rutas = new ArrayList<>();
        this.rutasAdapter = new RowRutaReparto(this.rutas, this.getActivity());
        this.lista.setAdapter(rutasAdapter);
        Button escanear = (Button) this.getView().findViewById(R.id.escanear);
        escanear.setOnClickListener(this);

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rutasAdapter.notifyDataSetChanged();
            }
        });

        //Se setea el nombre en la barra superior.
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

    /**
     * Actualiza la lista de rutas asignadas en la vista cada 5 segundos.
     */
    @Override
    public void onStart()
    {
        super.onStart();
        final Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    cargarRutas();
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            rutasAdapter.clear();
                            for(int i = 0; i < rutas.size(); i++)
                            {
                                rutasAdapter.insert(rutas.get(i), i);
                            }
                        }
                    });
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    /**
     * Accede a la base de datos y obtiene una lista actualizada de las rutas asignadas al usuario.
     */
    private void cargarRutas()
    {
        this.bd.abrir();
        this.rutas = this.bd.leerRutasReparto(usuario.getId());
    }

    /**
     * Inicia un fragmento ListaOrdenesFragment para el objeto seleccionado en la vista.
     * @param view
     */
    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(view.getContext(), CaptureActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (data != null) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Log.d("Codigo", contents);
                String cliente = contents.substring(0,11);
                this.bd.abrir();
                //Todo: buscar orden de reparto por numero de cliente
                OrdenReparto orden = this.bd.buscarOrdenReparto(cliente);
                if(orden != null) {
                    //Obtiene posicion gps al momento de guardar lectura
                    String locationProvider = LocationManager.NETWORK_PROVIDER;
                    LocationManager location = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location lastKnownLocation = location.getLastKnownLocation(locationProvider);
                    orden.setGpsLatitud(lastKnownLocation.getLatitude());
                    orden.setGpsLongitud(lastKnownLocation.getLongitude());
                    orden.setEstado(4);
                    orden.setFechaEntrega(new Date().getTime());
                    this.bd.actualizaOrdenReparto(orden);
                }
                else{
                    Toast.makeText(this.getContext(), "Orden no encontrada", Toast.LENGTH_LONG).show();
                }
                //Todo: Verificar codigo en base de datos guardarlo como entregado y actualizar en servidor
                //Todo: Obtener posicion gps del escaneo
            }
        }
    }

}
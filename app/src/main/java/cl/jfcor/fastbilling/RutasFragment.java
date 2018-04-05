package cl.jfcor.fastbilling;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import base_datos.Bd;
import modelos.Ruta;
import modelos.Usuario;

/**
 * Fragmento donde se muestra la lista de rutas asignadas
 */
public class RutasFragment extends Fragment implements AdapterView.OnItemClickListener
{

    private String tag = "Unidades de lectura";
    private ArrayList<Ruta> rutas;
    private Bd bd;
    private Usuario usuario;
    private ListView lista;
    private RowRuta rutasAdapter;
    private Activity activity;
    private int tipoLectura;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_rutas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //Obtener argumentos
        Bundle args = getArguments();
        this.tipoLectura = args.getInt("tipoLectura");

        if( tipoLectura == 2)
            this.tag = "Verificaciones";

        this.lista = (ListView) this.getView().findViewById(R.id.listaRutas);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("cl.jfcor.fastbilling", Context.MODE_PRIVATE);

        //Se obtienen los datos del usuario desde la base de datos.
        this.bd.abrir();
        this.usuario = this.bd.buscarUsuario(prefs.getString("cl.jfcor.fastbilling.usuario", ""));

        this.rutas = new ArrayList<>();
        this.rutasAdapter = new RowRuta(this.rutas, this.getActivity());
        this.lista.setAdapter(rutasAdapter);
        this.lista.setOnItemClickListener(this);

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
		this.rutas = this.bd.leerRutas(usuario.getId(), this.tipoLectura);
    }

    /**
     * Inicia un fragmento ListaOrdenesFragment para el objeto seleccionado en la vista.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Fragment fragment = null;
        try
        {
            fragment = ListaOrdenesFragment.class.newInstance();
        }
        catch (java.lang.InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        //Parametros que se pasan al fragment
        Bundle args = new Bundle();
        args.putSerializable("ruta", this.rutas.get(position));
        args.putSerializable("usuario", this.usuario);
        args.putInt("tipoLectura", this.tipoLectura);

        fragment.setArguments(args);

        android.support.v4.app.FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
    }
}

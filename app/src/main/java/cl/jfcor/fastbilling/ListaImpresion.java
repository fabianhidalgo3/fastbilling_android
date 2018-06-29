package cl.jfcor.fastbilling;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import base_datos.Bd;
import modelos.OrdenLectura;


/**
 * Vista encargada de listar las ordenes pendientes de impresion.
 */
public class ListaImpresion extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private Spinner filtro;
    private EditText buscar;
    private Bd bd;
    private ArrayList<OrdenLectura> ordenes;
    private String tag;
    private int tipo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_lista_impresion, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //Obtener argumentos
        Bundle args = getArguments();
        //Esta variable indica si la vista lista las reimpresiones o los pendientes de facturacion.
        this.tipo = args.getInt("tipo");

        this.tag = this.tipo == 1 ? "Reimpresión" : "Facturación pendientes";

        //Se agregan filtros al spinner
        String[] arraySpinner = new String[] {"Dirección", "N° Medidor"};
        this.filtro = (Spinner) this.getView().findViewById(R.id.filtroImpresion);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        this.filtro.setAdapter(adapter);

        this.buscar = (EditText) this.getView().findViewById(R.id.txt_busquedaImpresion);
        ImageButton btnBuscar = (ImageButton) this.getView().findViewById(R.id.btn_buscarImpresion);
        btnBuscar.setOnClickListener(this);

        this.listarOrdenes();


        //Se setea el nombre en la barra superior.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ((MenuUsuario) getActivity()).setActionBarTitle(this.tag);
        }
    }

    /**
     * Metodo que obtiene la lista de ordenes y las muestra en la vista
     */
    public void listarOrdenes()
    {
        this.bd.abrir();
        //La diferenciacion de reimpresion/pendientes de facturacion se hace por el atributo tipo
        if(this.tipo == 1)
            this.ordenes = this.bd.leerOrdenesReimpresion();
        else
            this.ordenes = this.bd.leerOrdenesFacturacion();


        ListView lista = (ListView) this.getView().findViewById(R.id.listaOrdenesImpresion);
        RowOrden ordenesAdapter = new RowOrden(this.ordenes, this.getActivity());
        lista.setAdapter(ordenesAdapter);
        lista.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.bd = Bd.getInstance(activity);
    }

    @Override
    public void onStart()
    {
        super.onStart();

    }

    /**
     * Maneja los eventos al precionar un elemento de la lista, generando un fragmento para impresion.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Fragment fragment = null;
        try {
            fragment = ImpresionActivity.class.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //Parametros que se pasan al fragment
        Bundle args = new Bundle();
        args.putSerializable("orden", ordenes.get(position));

        fragment.setArguments(args);

        android.support.v4.app.FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

        //Si es tipo 2(pendiente de facturacion) debe cambiarse el estado a facturado en la base de datos
        if(this.tipo == 2)
        {
            OrdenLectura orden = this.ordenes.get(position);
            orden.setFacturado(true);
            this.bd.actualizarOrden(orden);
        }
        /**Intent intent = new Intent(this.activity, OrdenLecturaFragment.class);

         startActivityForResult(intent, INGRESAR_LECTURA);*/
    }

    /**
     * Aqui se manejan los eventos del filtro para buscar ordenes.
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        String filtro = this.filtro.getSelectedItem().toString();
        if (this.tipo == 1) {
            if (filtro.equals("N° Medidor"))
                this.ordenes = this.bd.listarOrdenesMedidor(this.buscar.getText().toString(), 2, null);
            if (filtro.equals("Dirección"))
                this.ordenes = this.bd.listarOrdenesDireccion(this.buscar.getText().toString(), 2, null);
        }
        else{
            if (filtro.equals("N° Medidor"))
                this.ordenes = this.bd.listarOrdenesMedidor(this.buscar.getText().toString(), 3, null);
            if (filtro.equals("Dirección"))
                this.ordenes = this.bd.listarOrdenesDireccion(this.buscar.getText().toString(), 3, null);
        }
        ListView lista = (ListView) this.getView().findViewById(R.id.listaOrdenesImpresion);
        RowOrden ordenesAdapter = new RowOrden(this.ordenes, this.getActivity());
        lista.setAdapter(ordenesAdapter);
        lista.setOnItemClickListener(this);
    }
}

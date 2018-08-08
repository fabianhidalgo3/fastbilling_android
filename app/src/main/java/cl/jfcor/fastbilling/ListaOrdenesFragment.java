package cl.jfcor.fastbilling;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import base_datos.Bd;
import modelos.OrdenLectura;
import modelos.Ruta;
import modelos.Usuario;

public class ListaOrdenesFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener
{
    private static final String TAG = "Ordenes de lectura";

    public final static int INGRESAR_LECTURA = 1;

    private ArrayList<OrdenLectura> ordenes;
    private Ruta ruta;
    private Usuario usuario;
    private Bd bd;
    private Spinner filtro;
    private Spinner orden;
    private EditText buscar;
    private TextView pendientes;
    private int tipoLectura;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_lista_ordenes, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
	{
        super.onActivityCreated(savedInstanceState);

        //Obtener argumentos
        Bundle args = getArguments();
        this.ruta = (Ruta) args.getSerializable("ruta");
        this.usuario = (Usuario) args.getSerializable("usuario");
        this.tipoLectura = args.getInt("tipoLectura");

        //Se agregan filtros al spinner
        String[] arraySpinner = new String[] {"Dirección", "N° Medidor"};
        String[] arrayOrderBy = new String[] {"Dirección Ascendente", "Dirección Descendente"};
        this.filtro = (Spinner) this.getView().findViewById(R.id.filtroImpresion);
        this.orden = (Spinner) this.getView().findViewById(R.id.spinnerOrden);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        adapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
        this.filtro.setAdapter(adapter);

        ArrayAdapter<String> adapterOrden = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, arrayOrderBy);
        adapterOrden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.orden.setAdapter(adapterOrden);
        this.orden.setOnItemSelectedListener(this);

        this.buscar = (EditText) this.getView().findViewById(R.id.txt_busquedaImpresion);
        ImageButton btnBuscar = (ImageButton) this.getView().findViewById(R.id.btn_buscar);
        btnBuscar.setOnClickListener(this);

		TextView txtRuta = (TextView) this.getView().findViewById(R.id.listaOrden_ruta);
		txtRuta.setText("Ruta: " + this.ruta.getCodigo());

        this.pendientes = (TextView) this.getView().findViewById(R.id.listaOrden_pendientes);
        this.listarOrdenes();
    }

    public void listarOrdenes()
    {
        this.bd.abrir();
        this.ordenes = this.bd.leerOrdenes(this.ruta.getId(), this.tipoLectura, getOrderBy());

        ListView lista = (ListView) this.getView().findViewById(R.id.listaOrdenes);
        RowOrden ordenesAdapter = new RowOrden(this.ordenes, this.getActivity());
        lista.setAdapter(ordenesAdapter);
        lista.setOnItemClickListener(this);
        this.pendientes.setText("Pendientes: " + this.ordenes.size());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Fragment fragment = null;
        try {
            fragment = OrdenLecturaFragment.class.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //Parametros que se pasan al fragment
        Bundle args = new Bundle();
        args.putSerializable("orden", ordenes.get(position));
        args.putSerializable("ruta",ruta.getCodigo());
        args.putSerializable("usuario", usuario);

        fragment.setArguments(args);

        android.support.v4.app.FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View v)
    {
        this.filtrar();
    }

    private String getOrderBy()
    {
        String orden = this.orden.getSelectedItem().toString();
        String orderBy = null;

        if (orden.equals("Dirección Ascendente"))
            orderBy = "direccion ASC";
        if (orden.equals("Dirección Descendente"))
            orderBy = "direccion DESC";

        return orderBy;
    }

    private void filtrar()
    {
        String filtro = this.filtro.getSelectedItem().toString();

        if (filtro.equals("N° Medidor"))
            this.ordenes = this.bd.listarOrdenesMedidor(this.buscar.getText().toString(), 1, this.getOrderBy());
        if (filtro.equals("Dirección"))
            this.ordenes = this.bd.listarOrdenesDireccion(this.buscar.getText().toString(), 1, this.getOrderBy());

        ListView lista = (ListView) this.getView().findViewById(R.id.listaOrdenes);
        RowOrden ordenesAdapter = new RowOrden(this.ordenes, this.getActivity());
        lista.setAdapter(ordenesAdapter);
        lista.setOnItemClickListener(this);
        this.pendientes.setText("Pendientes: " + this.ordenes.size());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        if(this.buscar.getText().toString() != "")
            this.listarOrdenes();
        else
            this.filtrar();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        return;
    }
}

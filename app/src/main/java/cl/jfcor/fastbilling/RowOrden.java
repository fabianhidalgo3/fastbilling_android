package cl.jfcor.fastbilling;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import base_datos.Bd;
import modelos.Cliente;
import modelos.Instalacion;
import modelos.Medidor;
import modelos.OrdenLectura;

/**
 * Una fila de la lista de ordenes de lectura
 * Created by brayan on 12/6/16.
 */
public class RowOrden extends ArrayAdapter<OrdenLectura>
{
    private Activity context;
    private ArrayList<OrdenLectura> ordenes;
    private Bd bd = Bd.getInstance(this.getContext());

    static class ViewHolder
    {
        public TextView nombre;
        public TextView direccion;
        public TextView instalacion;
        public TextView medidor;
    }

    public RowOrden(ArrayList<OrdenLectura> ordenes, Activity context)
    {
        super(context, R.layout.row_orden, ordenes);
        this.context = context;
        this.ordenes = ordenes;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if (row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.row_orden, null);
            // configure view holder
            RowOrden.ViewHolder viewHolder = new RowOrden.ViewHolder();
            viewHolder.direccion = (TextView) row.findViewById(R.id.row_orden_dir);
            viewHolder.instalacion =  (TextView) row.findViewById(R.id.row_orden_instalacion);
            viewHolder.medidor =  (TextView) row.findViewById(R.id.row_orden_medidor);
            viewHolder.nombre =  (TextView) row.findViewById(R.id.row_orden_nombre);
            row.setTag(viewHolder);
        }

        RowOrden.ViewHolder holder = (RowOrden.ViewHolder) row.getTag();
        OrdenLectura orden = this.ordenes.get(position);
        this.bd.abrir();

        //Se obtienen datos a mostrar y se setean en las variables
        Cliente cliente = this.bd.buscarCliente(orden.getClienteId());
        Medidor medidor = this.bd.buscarMedidor(orden.getMedidorId());

        Instalacion instalacion = this.bd.buscarInstalacion(orden.getInstalacionId());
        holder.direccion.setText(orden.getDireccion());

        // Se validan algunos campos que podrian ser nulos
        if( cliente != null)
            holder.nombre.setText(cliente.getNombre());
        else
            holder.nombre.setText("");

        if(medidor != null)
            holder.medidor.setText(medidor.getNumeroMedidor());
        else
            holder.medidor.setText("");

        if(instalacion != null)
            holder.instalacion.setText(instalacion.getCodigo());
        else
            holder.instalacion.setText("");

        return row;
    }
}

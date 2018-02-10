package cl.jfcor.fastbilling;

/**
 * Created brayan on 26-12-17.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modelos.RutaReparto;

/**
 * Una fila de la lista de rutas
 * Created by brayan on 11/28/16.
 */
public class RowRutaReparto extends ArrayAdapter<RutaReparto>
{

    private Activity context;
    private ArrayList<RutaReparto> rutas;

    static class ViewHolder
    {
        public TextView ul;
        public TextView cantidad;
    }

    public RowRutaReparto(ArrayList<RutaReparto> rutas, Activity context)
    {
        super(context, R.layout.row_ruta, rutas);
        this.rutas = rutas;
        this.context = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if (row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.row_ruta, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.ul = (TextView) row.findViewById(R.id.row_ul);
            viewHolder.cantidad =  (TextView) row.findViewById(R.id.row_cantidad);
            row.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) row.getTag();
        RutaReparto ruta = this.rutas.get(position);
        holder.ul.setText(ruta.getCodigo());
        holder.cantidad.setText(Integer.toString(ruta.getNumeroOrdenes()));
        return row;
    }


}
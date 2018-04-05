package cl.jfcor.fastbilling;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modelos.Ruta;

/**
 * Una fila de la lista de rutas
 * Creat
 */
public class RowRuta extends ArrayAdapter<Ruta>
{

    private Activity context;
    private ArrayList<Ruta> rutas;

    static class ViewHolder
    {
        public TextView ul;
        public TextView cantidad;
    }

    public RowRuta(ArrayList<Ruta> rutas, Activity context)
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
        Ruta ruta = this.rutas.get(position);
        holder.ul.setText(ruta.getCodigo());
        holder.cantidad.setText(Integer.toString(ruta.getLecturas().size()));
        return row;
    }


}

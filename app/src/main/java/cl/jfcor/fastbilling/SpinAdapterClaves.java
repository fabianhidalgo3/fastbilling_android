package cl.jfcor.fastbilling;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modelos.ClaveLectura;

/**
 * Muestra Lista de claves
 * Created by brayan on 4/26/17.
 */

public class SpinAdapterClaves extends ArrayAdapter<ClaveLectura>
{
    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<ClaveLectura> values;

    public SpinAdapterClaves(Context context, int textViewResourceId, ArrayList values)
    {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount()
    {
        return values.size();
    }

    public ClaveLectura getItem(int position)
    {
        return values.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getClave());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setGravity (Gravity.CENTER);
        label.setTextSize(18);
        label.setPadding(16, 16, 16, 16);
        label.setText(values.get(position).getClave());

        return label;
    }
}

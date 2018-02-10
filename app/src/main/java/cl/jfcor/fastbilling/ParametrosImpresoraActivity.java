package cl.jfcor.fastbilling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import base_datos.Bd;
import modelos.ParametrosImpresora;

/**
 * Activity utilizado para ingresar los datos necesarios de la impresora para la conexion con el equipo
 */
public class ParametrosImpresoraActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros_impresora);

        ParametrosImpresora parametros;
        Bd bd = Bd.getInstance(this);
        bd.abrir();
        parametros = bd.buscarParametrosImpresora(1);

        Switch habilitarImpresion = (Switch) findViewById(R.id.habilitar_impresion);
        EditText mac = (EditText) findViewById(R.id.mac_edit_text);
        habilitarImpresion.setChecked(parametros.isImpresionHabilitada());
        mac.setText(parametros.getMac());

        Button guardar = (Button) findViewById(R.id.btn_guardar_params_impresora);
        guardar.setOnClickListener(this);
    }

    /**
     * Aqui se manejan los eventos sobre el boton guardar
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        Switch habilitarImpresion = (Switch) findViewById(R.id.habilitar_impresion);
        EditText mac = (EditText) findViewById(R.id.mac_edit_text);
        ParametrosImpresora parametros = new ParametrosImpresora(1, mac.getText().toString(), habilitarImpresion.isChecked());

        Bd bd = Bd.getInstance(this);
        bd.abrir();
        bd.actualizarParametrosImpresora(parametros);

        finish();
    }
}

package cl.jfcor.fastbilling;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import base_datos.Bd;
import modelos.API;
import modelos.ParametrosServidor;

/**
 * Activity utilizada para setear la configuracion del servidor desde el que se consumiran los endpoints.
 */
public class ParametrosServidorActivity extends AppCompatActivity implements View.OnClickListener
{
    private ParametrosServidor parametros;
    private Bd bd;
    private Button btnGuardar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros_servidor);

        this.bd = Bd.getInstance(this);

        SharedPreferences prefs = this.getSharedPreferences("cl.jfcor.fastbilling", Context.MODE_PRIVATE);
        EditText usuario = (EditText) findViewById(R.id.nombre_usuario);
        EditText pass = (EditText) findViewById(R.id.pass_usuario);
        usuario.setText(prefs.getString("cl.jfcor.fastbilling.usuario",""));
        pass.setText(prefs.getString("cl.jfcor.fastbilling.password",""));

        //Se realiza la busqueda de los parametros almacenados en la base de datos.
        this.bd.abrir();
        this.parametros = this.bd.buscarParametros(1);

        ImageView imageView = (ImageView) findViewById(R.id.imageView3);
        imageView.setImageAlpha(30);

        if(this.parametros != null)
        {
            EditText ip = (EditText) findViewById(R.id.direccion_servidor);
            EditText intervalo = (EditText) findViewById(R.id.intervalo_actualizacion);
            ip.setText(this.parametros.getIp());
            intervalo.setText(Integer.toString(this.parametros.getIntervalo()));
        }

        this.btnGuardar = (Button) findViewById(R.id.btn_guardar_parametros_servidor);
        this.btnGuardar.setOnClickListener(this);
    }

    /**
     * Aqui se manejan los eventos del boton guardar.
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        //Se obtienen datos
        EditText usuario = (EditText) findViewById(R.id.nombre_usuario);
        EditText pass = (EditText) findViewById(R.id.pass_usuario);
        EditText ip = (EditText) findViewById(R.id.direccion_servidor);
        EditText intervalo = (EditText) findViewById(R.id.intervalo_actualizacion);
        SharedPreferences prefs = this.getSharedPreferences("cl.jfcor.fastbilling", Context.MODE_PRIVATE);
        prefs.edit().putString("cl.jfcor.fastbilling.usuario", usuario.getText().toString()).apply();
        prefs.edit().putString("cl.jfcor.fastbilling.password", pass.getText().toString()).apply();

        //Se abre la base de datos para guardar los parametros
        this.bd.abrir();

        //Se eliminan datos de las tablas
        this.bd.eliminarPerfiles();
        this.bd.eliminarClavesLectura();
        this.bd.eliminarEquipos();

        //Se define si es una actualizacion o una creacion del registro
        if(this.parametros == null)
        {
            this.parametros = new ParametrosServidor(1, ip.getText().toString(),Integer.parseInt(intervalo.getText().toString()));
            this.bd.insertarParametros(this.parametros);
        }
        else
        {
            this.parametros.setIp(ip.getText().toString());
            this.parametros.setIntervalo(Integer.parseInt(intervalo.getText().toString()));
            this.bd.actualizarParametros(this.parametros);
        }

        Toast.makeText(getBaseContext(),"Datos guardados",Toast.LENGTH_SHORT).show();
        API carga = new API(this);
        carga.login(usuario.getText().toString(), pass.getText().toString());
        finish();
    }
}

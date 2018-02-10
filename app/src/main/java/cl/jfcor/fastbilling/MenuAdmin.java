package cl.jfcor.fastbilling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import base_datos.Bd;
import modelos.API;
import modelos.Mac;

/**
 * Menu de administrador utilizado para
 */
public class MenuAdmin extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        //Setea imagen de fondo de la vista.
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageAlpha(30);

        Button paramServidor = (Button) findViewById(R.id.btn_conf_parametros_servidor);
        paramServidor.setOnClickListener(this);
        Button carga = (Button) findViewById(R.id.btn_carga_parametros_iniciales);
        carga.setOnClickListener(this);
        Button paramImpresora = (Button) findViewById(R.id.btn_conf_parametros_impresora);
        paramImpresora.setOnClickListener(this);
    }

    /**
     * Maneja los eventos sobre los botones que hay en la vista.
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_conf_parametros_servidor:
                Intent parametros = new Intent(MenuAdmin.this, ParametrosServidorActivity.class);
                startActivity(parametros);
                break;

            case R.id.btn_conf_parametros_impresora:
                Intent impresora = new Intent(MenuAdmin.this, ParametrosImpresoraActivity.class);
                startActivity(impresora);
                break;

            case R.id.btn_carga_parametros_iniciales:
                Bd bd = Bd.getInstance(this);
                bd.abrir();

                if(bd.buscarEquipoMac(new Mac().getMac().toLowerCase()))
                    this.cargaDatos();
                else
                    Toast.makeText(getBaseContext(),"Equipo no registrado en servidor" ,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Realiza carga de parametros iniciales.
     */
    public void cargaDatos()
    {
        API carga = new API(this);
        SharedPreferences prefs = this.getSharedPreferences("cl.jfcor.fastbilling", Context.MODE_PRIVATE);
        String usuario = prefs.getString("cl.jfcor.fastbilling.usuario","");
        carga.cargaClaves(usuario);
        carga.cargaObservaciones(usuario);
        carga.cargaPerfiles(usuario);
    }
}

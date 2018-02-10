package cl.jfcor.fastbilling;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import base_datos.Bd;
import modelos.API;
import modelos.OrdenLectura;
import modelos.Usuario;

/**
 * Clase encargada de acceder a los metodos de la api de forma asincrona segun intervalo fijado en
 * configuración de la aplicación.
 * Created by brayan on 08-09-17.
 */
public class SincronizacionServidor extends AsyncTask<String, Void, Void>
{
    private Bd bd;
    private Usuario usuario;
    private Context context;
    private int intervaloActualizacion;

    /**
     * Constructor
     * @param usuario usuario que inicio sesion en la aplicacion
     * @param context contexto de la aplicacion
     */
    public SincronizacionServidor(Usuario usuario, Context context)
    {
        this.usuario = usuario;
        this.context = context;
        this.bd = Bd.getInstance(this.context);
        this.intervaloActualizacion = this.bd.buscarParametros(1).getIntervalo() * 1000;
    }

    /**
     * Metodo asincrono que se encarga de realizar las consultas a los distintos endpoints
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(String... params)
    {
        Thread thread = new Thread(){
            @Override
            public void run()
            {
                while (true)
                {
                    Log.d("carga masiva", "asdf");
                    try
                    {
                        Thread.sleep(10000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    API carga = new API(context);
                    carga.cargaAsignacionesReparto(usuario.getEmail());

                    //Se consulta por desasignaciones
                    carga.cargaDesasignaciones(usuario.getEmail());

                    //Se obtienen datos desde el servidor
                    carga.cargaAsignaciones(usuario.getEmail());
                    //Se envian datos locales al servidor
                    carga.actualizaLecturas(usuario.getEmail());

                    try
                    {
                        Thread.sleep(intervaloActualizacion);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.run();
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
    }
}

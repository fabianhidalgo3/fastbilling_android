package modelos;

import android.content.Context;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import base_datos.Bd;

import static com.android.volley.VolleyLog.TAG;

/**
 * Creado por Brayan Gonzalez el 08-03-17.
 * Clase para manejar todas las comunicaciones con la API Web
 */

public class API
{
    private Bd baseDatos;
    private Context contexto;
    private RequestQueue queue;
    private String urlLogin;
    private String urlClaveLectura;
    private String urlObservacionLectura;
    private String urlPerfiles;
    private String urlEquipos;
    private String urlUsuarios;
    private String urlOrdenLecturas;
    private String urlAsignaciones;
    private String urlRutas;
    private String urlDetalOrdenLectura;
    private String urlClientes;
    private String urlInstalacion;
    private String urlMedidor;
	private String urlIntentos;
	private String urlFotografias;
    private String urlAsignacionesReparto;
    private String urlOrdenReparto;
    int contador = 0;


    public API(Context contexto)
    {
        this.contexto = contexto;
        this.baseDatos = Bd.getInstance(this.contexto);
        this.queue = Volley.newRequestQueue(this.contexto);
        this.baseDatos.abrir();
        ParametrosServidor parametros = this.baseDatos.buscarParametros(1);


        //Se definen las url a consumir desde la API.
        this.urlLogin = "http://" + parametros.getIp() + "/api/usuario/sign_in";
        this.urlClaveLectura = "http://" + parametros.getIp() + "/api/clave_lecturas";
        this.urlObservacionLectura = "http://" + parametros.getIp() + "/api/observaciones";
        this.urlPerfiles = "http://" + parametros.getIp() + "/api/perfils";
        this.urlEquipos = "http://" + parametros.getIp() + "/api/equipos";
        this.urlUsuarios = "http://" + parametros.getIp() + "/api/usuarios";
        this.urlOrdenLecturas = "http://" + parametros.getIp() + "/api/orden_lecturas";
        this.urlRutas = "http://" + parametros.getIp() + "/api/ruta";
        this.urlAsignaciones = "http://" + parametros.getIp() + "/api/asignacions";
        this.urlDetalOrdenLectura = "http://" + parametros.getIp() + "/api/detalle_orden_lecturas";
        this.urlClientes = "http://" + parametros.getIp() + "/api/clientes";
        this.urlInstalacion = "http://" + parametros.getIp() + "/api/instalacions";
        this.urlMedidor = "http://" + parametros.getIp() + "/api/medidors";
		this.urlIntentos = "http://" + parametros.getIp() + "/api/intentos";
		this.urlFotografias = "http://" + parametros.getIp() + "/api/fotografia";
        this.urlAsignacionesReparto = "http://" + parametros.getIp() + "/api/asignacion_repartos";
        this.urlOrdenReparto = "http://" + parametros.getIp() + "/api/orden_repartos";
    }

    /**
     * Metodo encargado de verificar y obtener el login del usuario en el servicio web
     * @param usuario Nombre de usuario
     * @param clave Contraseña de inicio de sesion
     */
    public void login(final String usuario, final String clave)
    {
        Map<String, String> postParam = new HashMap<>();
        postParam.put("email", usuario);
        postParam.put("password", clave);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                this.urlLogin, new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            baseDatos.abrir();
                            if(Integer.parseInt(response.getString("perfil_id"))==1)
                            {
                                baseDatos.eliminarTablaUsuarios();
                                baseDatos.insertarUsuario(response.getInt("id"), usuario, clave,
                                                          response.getString("authentication_token"),
                                                          response.getInt("perfil_id"));
                                cargarEquipos(usuario);
                            }
                            else
                            {
                                Usuario u = baseDatos.buscarUsuario(usuario);
                                u.setPassword(clave);
                                u.setToken(response.getString("authentication_token"));
                                baseDatos.actualizarUsuario(u);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                })
                {

                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError
                    {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }


                };

        jsonObjReq.setShouldCache(false);
        this.queue.add(jsonObjReq);

    }

    /**
     * Carga claves de lectura desde la API
     * @param usuario Nombre de usuario
     */
    public void cargaClaves(String usuario)
    {
        this.baseDatos.abrir();
        this.baseDatos.eliminarClavesLectura();
        Usuario u = this.baseDatos.buscarUsuario(usuario);


        JsonArrayRequest clavesLecturaRequest = new JsonArrayRequest(Request.Method.GET,
                urlClaveLectura + "?user_email=" + u.getEmail() + "&user_token=" + u.getToken(), (String) null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response){
                Log.d("Response", response.toString());
                for(int i = 0; i < response.length(); i++)
                {
                    JSONObject claveLectura;
                    try
                    {
                        claveLectura = response.getJSONObject(i);
                        ClaveLectura clave = new ClaveLectura();
                        clave.setId(claveLectura.getInt("id"));
                        clave.setClave(claveLectura.getString("nombre"));
                        clave.setCodigo(claveLectura.getString("codigo"));
                        clave.setNumFotografias(claveLectura.getInt("num_fotografias"));
                        clave.setDescripcionCorta(claveLectura.getString("descripcion_corta"));
                        clave.setLecturaRequerida(claveLectura.getBoolean("requerido"));
                        Log.d("requerido", Boolean.toString(clave.isLecturaRequerida()));
                        if(claveLectura.getString("tipo_cobro_id").equals("null"))
                            clave.setTipoCobroId(0);
                        else
                            clave.setTipoCobroId(claveLectura.getInt("tipo_cobro_id"));

                        baseDatos.abrir();
                        baseDatos.insertarClave(clave);

                    }
                    catch (JSONException e)
                    {
                        //e.printStackTrace();
                    }
                }
                Toast.makeText(contexto,"Claves de lectura cargadas",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(contexto,"No se pudieron cargar las claves",Toast.LENGTH_SHORT).show();
            }
        });
        clavesLecturaRequest.setShouldCache(false);
        this.queue.add(clavesLecturaRequest);
    }

    /**
     * Carga observaciones de lectura desde la API
     * @param usuario
     */
    public void cargaObservaciones(String usuario)
    {
        this.baseDatos.abrir();
        this.baseDatos.eliminarObservaciones();
        Usuario u = this.baseDatos.buscarUsuario(usuario);


        JsonArrayRequest observacionesLecturaRequest = new JsonArrayRequest(Request.Method.GET,
                this.urlObservacionLectura + "?user_email=" + u.getEmail() + "&user_token=" + u.getToken(), (String) null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response){
                Log.d("Response", response.toString());
                for(int i = 0; i < response.length(); i++)
                {
                    JSONObject observacionLectura;
                    try
                    {
                        observacionLectura = response.getJSONObject(i);
                        Observacion observacion = new Observacion();
                        observacion.setId(observacionLectura.getInt("id"));
                        observacion.setDescripcion(observacionLectura.getString("descripcion"));
                        observacion.setClaveLecturaId(observacionLectura.getInt("clave_lectura_id"));
                        observacion.setNumeroFotografias (observacionLectura.getInt("num_fotografias"));
                        observacion.setLecturaRequerida (observacionLectura.getBoolean ("requerido"));
                        observacion.setLecturaEfectiva (observacionLectura.getBoolean ("efectivo"));
                        observacion.setFactura (observacionLectura.getBoolean ("factura"));
                        observacion.setFolio (observacionLectura.getBoolean ("folio"));
                        baseDatos.abrir();
                        baseDatos.insertarObservacion(observacion);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(contexto,"Observaciones de lectura cargadas",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(contexto,"No se pudieron cargar las observaciones",Toast.LENGTH_SHORT).show();
            }
        });
        observacionesLecturaRequest.setShouldCache(false);
        this.queue.add(observacionesLecturaRequest);
    }

    /**
     * Carga perfiles de usuario desde la API
     * @param usuario
     */
    public void cargaPerfiles(String usuario)
    {
        this.baseDatos.abrir();
        this.baseDatos.eliminarPerfiles();
        Usuario u = this.baseDatos.buscarUsuario(usuario);


        JsonArrayRequest perfilesRequest = new JsonArrayRequest(Request.Method.GET,
                urlPerfiles+ "?user_email=" + u.getEmail() + "&user_token=" + u.getToken(), (String) null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response){
                Log.d("Response", response.toString());
                for(int i = 0; i < response.length(); i++)
                {
                    JSONObject perfil;
                    try
                    {
                        perfil = response.getJSONObject(i);

                        baseDatos.abrir();
                        baseDatos.insertarPerfil(perfil.getInt("id"), perfil.getString("nombre"),
                                                 perfil.getString("imagen"));

                    }
                    catch (JSONException e)
                    {
                        //e.printStackTrace();
                    }
                }
                Toast.makeText(contexto,"Perfiles de usuario cargados",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(contexto,"No se pudieron cargar los perfiles",Toast.LENGTH_SHORT).show();
            }
        });
        perfilesRequest.setShouldCache(false);
        this.queue.add(perfilesRequest);
    }

    /**
     * Carga datos del equipo asignado al usuario.
     * @param usuario
     */
    private void cargarEquipos(final String usuario)
    {
        this.baseDatos.abrir();
        this.baseDatos.eliminarEquipos();
        final Usuario u = this.baseDatos.buscarUsuario(usuario);


        JsonArrayRequest equiposRequest = new JsonArrayRequest(Request.Method.GET,
                urlEquipos + "?user_email=" + u.getEmail() + "&user_token=" + u.getToken() + "&mac=" + new Mac().getMac().toLowerCase()
                , (String) null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response){
                Log.d("Response", response.toString());
                for(int i = 0; i < response.length(); i++)
                {
                    JSONObject equipo;
                    try
                    {
                        equipo = response.getJSONObject(i);

                        baseDatos.abrir();
                        baseDatos.insertarEquipo(equipo.getInt("id"), equipo.getString("nombre"), equipo.getString("mac").toLowerCase());

                        cargaUsuario(u.getEmail(),equipo.getInt("empleado_id"));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(contexto,"Datos Equipos Cargados",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(contexto,"No se pudieron cargar los datos del equipo",Toast.LENGTH_SHORT).show();
            }
        });
        equiposRequest.setShouldCache(false);
        this.queue.add(equiposRequest);
    }

    private void cargaUsuario(String usuario, int id)
    {
        this.baseDatos.abrir();
        Usuario u = this.baseDatos.buscarUsuario(usuario);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlUsuarios + "?user_email=" + u.getEmail() + "&user_token=" + u.getToken() + "&id=" + id
                , (String) null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response){
                Log.d("Response", response.toString());

                baseDatos.abrir();
                try {
                    baseDatos.insertarUsuario(response.getInt("id"), response.getString("email"), " ",
                                    response.getString("authentication_token"), response.getInt("perfil_id"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Toast.makeText(contexto,"Datos Usuario cargados",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("Error usuario", error.toString());
                Toast.makeText(contexto,"No se pudo cargar datos de usuario",Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        this.queue.add(request);
    }

    /**
     * Consulta por ordenes desasignadas y las elimina de la base de datos local
     * @param usuario
     */
    public void cargaDesasignaciones(String usuario)
    {
        this.baseDatos.abrir();
        Usuario u = this.baseDatos.buscarUsuario(usuario);


        ArrayList<OrdenLectura> ordenes = this.baseDatos.leerTodasOrdenesPendientes();
        for(final OrdenLectura orden : ordenes)
        {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    this.urlAsignaciones + "/desasignacion?user_email=" + u.getEmail() + "&user_token=" + u.getToken() + "&id=" + u.getId()
                            + "&orden_id=" + orden.getId(), (String) null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    //Log.d("Response", response.toString());
                    String id = new String();

                    try
                    {
                        id = response.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if ( id.equals("null"))
                    {
                        ArrayList<DetalleOrdenLectura> numeradores =  orden.getNumeradores();
                        for(DetalleOrdenLectura numerador : numeradores)
                        {
                            baseDatos.eliminarIntentos(numerador.getId());
                        }
                        baseDatos.eliminarDetalles(orden.getId());
                        baseDatos.eliminarOrden(orden.getId());
                        //Se eliminan todos los datos de esta asignacion
                    }
                    else
                    {
                        //no se hace nada
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    //Toast.makeText(contexto, "No se pudo establecer comunicacion con el servidor", Toast.LENGTH_SHORT).show();
                }
            });
            request.setShouldCache(false);
            this.queue.add(request);
        }
    }

    /**
     * Consulta por asignaciones y guarda los datos en la base de datos local.
     * @param usuario
     */
    public void cargaAsignaciones(final String usuario)
    {
        this.baseDatos.abrir();
        Usuario u = this.baseDatos.buscarUsuario(usuario);
        //final int contador =0;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                this.urlAsignaciones + "?user_email=" + u.getEmail() + "&user_token=" + u.getToken() + "&id=" + u.getId()
                , (String) null, new Response.Listener<JSONArray>()
        {
            int contador = 0;
            @Override
            public void onResponse(JSONArray response) {
                Log.d ("Response", response.toString ());
                for (int i = 0; i < response.length (); i++) {
                    JSONObject asignacion;
                    try {
                        asignacion = response.getJSONObject (i);
                        try {
                            if (!baseDatos.existeOrdenLectura (asignacion.getInt ("orden_lectura_id")))
                                cargaOrdenLectura (usuario, asignacion.getInt ("orden_lectura_id"));
                                contador = contador +1;
                        } catch (SQLiteCantOpenDatabaseException e) {
                            e.printStackTrace ();
                        } finally {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }
                //Toast.makeText(contexto,"TOTAL ASIGNADO:" + contador,Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(contexto,"Error asignaciones",Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        this.queue.add(request);
    }

    /**
     * Carga datos de orden de lectura desde la API
     * @param usuario Nombre de usuario
     * @param ordenLecturaId Id orden de lectura a cargar
     */
    private void cargaOrdenLectura(final String usuario, final int ordenLecturaId)
    {
        this.baseDatos.abrir();
        final Usuario u = this.baseDatos.buscarUsuario(usuario);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                this.urlOrdenLecturas + "?user_email=" + u.getEmail() + "&user_token=" + u.getToken() + "&id=" + ordenLecturaId
                , (String) null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response){
                try
                {
                    JSONObject jsonOrden = response.getJSONObject("orden_lectura");
                    JSONObject jsonInstalacion = jsonOrden.getJSONObject("instalacion");
                    JSONObject jsonCliente = jsonOrden.getJSONObject("cliente");
                    JSONObject jsonMedidor = jsonOrden.getJSONObject("medidor");
                    JSONObject jsonRuta = jsonOrden.getJSONObject("rutum");
                    JSONArray jsonDetalleOrden = jsonOrden.getJSONArray("detalle_orden_lectura");

                    Instalacion instalacion = new Instalacion(jsonInstalacion.getInt("id"), jsonInstalacion.getString("codigo"));

                    Cliente cliente = new Cliente(jsonCliente.getInt("id"), jsonCliente.getString("rut"),
                                                  jsonCliente.getString("nombre"), jsonCliente.getString("apellido_paterno"),
                                                  jsonCliente.getString("apellido_materno"), jsonCliente.getString("direccion"),
                                                  jsonCliente.getString("giro"), jsonCliente.getString("telefono"),
                                                  jsonCliente.getString("memo"), jsonCliente.getString("duenorespo"),
                                                  jsonCliente.getString("numero_cliente"));

                    Medidor medidor = new Medidor(jsonMedidor.getInt("id"), jsonMedidor.getString("numero_medidor"),
                                                  jsonMedidor.getInt("modelo_id"), jsonMedidor.getBoolean("propiedad_cliente"),
                                                  jsonMedidor.getInt("nro_digitos"), jsonMedidor.getInt("diametro"));

                    Ruta ruta = new Ruta(jsonRuta.getInt("id"), jsonRuta.getString("codigo"), jsonRuta.getString("nombre"),
                                         u.getId(), 0);

                    ArrayList<DetalleOrdenLectura> detalleOrden = new ArrayList<>();
                    for(int i = 0; i < jsonDetalleOrden.length(); i++)
                    {
                        DetalleOrdenLectura detalle = new DetalleOrdenLectura(jsonDetalleOrden.getJSONObject(i).getInt("id"),
                                                                              jsonDetalleOrden.getJSONObject(i).getInt("orden_lectura_id"),
                                                                              jsonDetalleOrden.getJSONObject(i).getInt("numerador_id"),
                                                                              jsonDetalleOrden.getJSONObject(i).getDouble("lectura_anterior"),
                                                                              jsonDetalleOrden.getJSONObject(i).getDouble("lectura_promedio"),
                                                                              jsonDetalleOrden.getJSONObject(i).getDouble("rango_superior"),
                                                                              jsonDetalleOrden.getJSONObject(i).getDouble("rango_inferior"),
                                                                              jsonDetalleOrden.getJSONObject(i).getString ("clave_lectura_anterior"),
                                                                              jsonDetalleOrden.getJSONObject(i).getString ("clave_lectura_anterior_2"),
                                                                              jsonDetalleOrden.getJSONObject(i).getString ("clave_lectura_anterior_3"),
                                                                              jsonDetalleOrden.getJSONObject(i).getDouble("m3_acumulados"));
                        detalleOrden.add(detalle);
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");

                    OrdenLectura orden = new OrdenLectura(jsonOrden.getInt("id"), jsonOrden.getInt("codigo"), jsonOrden.getInt("secuencia_lector"),
                                                          jsonOrden.getString("direccion"), jsonOrden.getString("direccion_entrega"), jsonOrden.getString("numero_poste"),
                                                          sdf.parse(jsonOrden.getString("fecha_carga")).getTime(), sdf.parse(jsonOrden.getString("fecha_propuesta")).getTime(),
                                                          sdf.parse(jsonOrden.getString("fecha_asignacion")).getTime(), jsonOrden.getDouble("ajuste_sencillo_anterior"),
                                                          instalacion.getId(), medidor.getId(), cliente.getId(), ruta.getId(), jsonOrden.getInt("tipo_lectura_id"), 3,
                                                          jsonOrden.getInt("tipo_tarifa_id"), jsonOrden.getInt("tipo_entrega_id"), jsonOrden.getInt("tipo_establecimiento_id"),
                                                          jsonOrden.getInt("nro_municipal"), jsonOrden.getInt("factor_cobro_id"), jsonOrden.getBoolean("autorizado_facturacion"),
                                                          jsonOrden.getBoolean("facturado"), 0, detalleOrden);

                    //TODO: implementar validaciones e insertar datos.
                    baseDatos.insertarInstalacion(instalacion);
                    baseDatos.insertarCliente(cliente);
                    baseDatos.insertarMedidor(medidor);
                    baseDatos.insertarRuta(ruta);
                    if(baseDatos.insertarOrdenlectura(orden))
                        actualizaEstadoOrden(u,orden.getId());
                }
                catch (JSONException e)
                {
					e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally
                {

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(contexto,"Error orden lectura",Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        this.queue.add(request);
    }

    /**
     * Actualiza estado de una orden de lectura en el servidor.
     * @param usuario Nombre de usuario
     * @param id Id orden a actualizar
     */
    public void actualizaEstadoOrden(Usuario usuario, int id)
    {
        Map<String, String> postParam = new HashMap<>();
        postParam.put("user_email", usuario.getEmail());
        postParam.put("user_token", usuario.getToken());
        postParam.put("orden_lectura_id", Integer.toString(id));
        postParam.put("estado_id", "3");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                this.urlOrdenLecturas + "/" + id, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };
        jsonObjReq.setShouldCache(false);
        this.queue.add(jsonObjReq);
    }

    /**
     * Obtiene una lista de ordenes sin actualizar en servidor y las actualiza
     * @param usuario Nombre de usuario
     */
    public void actualizaLecturas(String usuario)
    {
        this.baseDatos.abrir();
		Usuario u = this.baseDatos.buscarUsuario(usuario);

        //Se obtiene lista de ordenes sin actualizar en servidor
		ArrayList<OrdenLectura> ordenLecturas = this.baseDatos.listaOrdenesSinEnviar();

	
		for(OrdenLectura orden : ordenLecturas)
		{
			Map<String, String> postParam = new HashMap<>();
			postParam.put("user_email", u.getEmail());
			postParam.put("user_token", u.getToken());
			postParam.put("orden_lectura_id", Integer.toString(orden.getId()));
			postParam.put("estado_id", Integer.toString(orden.getEstadoLecturaId()));
			postParam.put("gps_latitud", Double.toString(orden.getGpsLatitud()));
			postParam.put("gps_longitud", Double.toString(orden.getGpsLongitud()));
            postParam.put("folio_casa_cerrada", Integer.toString(orden.getFolioCasaCerrada()));
            postParam.put("autorizado_facturacion", Boolean.toString(orden.isAutorizadoFacturacion()));
            postParam.put("facturado", Boolean.toString(orden.isFacturado()));
			
			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
					this.urlOrdenLecturas + "/" + orden.getId(), new JSONObject(postParam),
					new Response.Listener<JSONObject>() {
						
						@Override
						public void onResponse(JSONObject response) {
							Log.d(TAG, response.toString());
							
						}
					}, new Response.ErrorListener() {
				
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());
				}
			}) {
				
				/**
				 * Passing some request headers
				 */
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> headers = new HashMap<>();
					headers.put("Content-Type", "application/json; charset=utf-8");
					return headers;
				}
				
				
			};

			this.baseDatos.abrir();
			Instalacion i = this.baseDatos.buscarInstalacion(orden.getInstalacionId());

            //Se actualiza cada numerador de la orden.
			this.actualizaDetalleOrdenes( u, orden.getNumeradores(), i.getCodigo());
            jsonObjReq.setShouldCache(false);
			this.queue.add(jsonObjReq);
		}
		
		
    }

    /**
     * Atualiza detalle de una orden de lectura.
     * @param u
     * @param numeradores
     * @param instalacion
     */
    private void actualizaDetalleOrdenes(Usuario u, ArrayList<DetalleOrdenLectura> numeradores, String instalacion)
	{
		for(DetalleOrdenLectura numerador : numeradores)
		{
			Map<String, String> postParam = new HashMap<>();
			postParam.put("user_email", u.getEmail());
			postParam.put("user_token", u.getToken());
			postParam.put("detalle_orden_lectura_id", Integer.toString(numerador.getId()));
			postParam.put("lectura_actual", Double.toString(numerador.getLecturaActual()));
			postParam.put("fecha_ejecucion", numerador.parseDate());
			postParam.put("clave_lectura_id", Integer.toString(numerador.getClaveLecturaId()));
			postParam.put("observacion_id", Integer.toString(numerador.getObservacionId()));
			Log.e("salida", Integer.toString(numerador.getObservacionId()));
			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
					this.urlDetalOrdenLectura + "/" + numerador.getId(), new JSONObject(postParam),
					new Response.Listener<JSONObject>() {
						
						@Override
						public void onResponse(JSONObject response) {
							Log.d(TAG, response.toString());
							
						}
					}, new Response.ErrorListener() {
				
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());
				}
			}) {
				
				/**
				 * Passing some request headers
				 */
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> headers = new HashMap<>();
					headers.put("Content-Type", "application/json; charset=utf-8");
					return headers;
				}
				
				
			};
			this.guardaFotografias(u, numerador.getFotografias(), instalacion, numerador.parseDate());
			this.guardaIntentos(u, numerador.getListaIntentos());
			jsonObjReq.setShouldCache(false);
			this.queue.add(jsonObjReq);
		}
	}

    /**
     * Actualiza cantidad de intentos para un detalle de orden indicando la lectura ingresada
     * por cada intento
     * @param u
     * @param intentos
     */
	private void guardaIntentos(Usuario u, ArrayList<Intento> intentos)
	{
		for(final Intento intento : intentos)
		{
            if (intento.getFlagEnvio() == 0)
            {
                Map<String, String> postParam = new HashMap<>();
                postParam.put("user_email", u.getEmail());
                postParam.put("user_token", u.getToken());
                postParam.put("detalle_orden_lectura_id", Integer.toString(intento.getIdDetalleOrden()));
                postParam.put("lectura", Double.toString(intento.getLectura()));

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        this.urlIntentos, new JSONObject(postParam),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                intento.setFlagEnvio(1);
                                baseDatos.actualizarIntento(intento);

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }) {

                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };
                jsonObjReq.setShouldCache(false);
                this.queue.add(jsonObjReq);
            }
		}
	}

    /**
     * Envia fotografias tomadas a servidor
     * @param u Nombre de usuario
     * @param fotografias Lista de fotografias tomadas
     * @param instalacion Se utiliza para nombrar el archivo
     * @param fecha Se utiliza para nombrar el archivo
     */
	private void guardaFotografias(Usuario u, ArrayList<Fotografia> fotografias, String instalacion, String fecha)
	{
		int i = 1;
		for(final Fotografia fotografia : fotografias)
		{
            if (fotografia.getFlagEnvio() == 0)
            {
                //Se prepara el archivo para ser enviado al servidor

                Log.d("asdf", fotografia.getRuta());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap resizedBitmap = fotografia.getImagen();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                String fotografiaBase64 = Base64.encodeToString(b, Base64.DEFAULT);
                //fin
                resizedBitmap.recycle();
                //"data:image/jpeg;base64," +

                Map<String, String> postParam = new HashMap<>();
                postParam.put("user_email", u.getEmail());
                postParam.put("user_token", u.getToken());
                postParam.put("detalle_orden_lectura_id", Integer.toString(fotografia.getDetalleOrdenLecturaId()));
                postParam.put("nombre_archivo", instalacion + "_" + i + "_" + fecha);
                postParam.put("fotografia", fotografiaBase64);
                postParam.put("observacion", fotografia.getObservacion());

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        this.urlFotografias, new JSONObject(postParam),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                fotografia.setFlagEnvio(1);
                                baseDatos.actualizarFotografia(fotografia);

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d(TAG, "Error: " + error.getStackTrace().toString());
                    }
                }) {

                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };
                jsonObjReq.setShouldCache(false);
                this.queue.add(jsonObjReq);
                i++;
            }
		}
	}


    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Consulta por asignaciones y guarda los datos en la base de datos local.
     * @param usuario
     */
    public void cargaAsignacionesReparto(final String usuario)
    {
        this.baseDatos.abrir();
        Usuario u = this.baseDatos.buscarUsuario(usuario);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                this.urlAsignacionesReparto + "?user_email=" + u.getEmail() + "&user_token=" + u.getToken() + "&id=" + u.getId()
                , (String) null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response){
                Log.d("Response", response.toString());
                for(int i = 0; i < response.length(); i++)
                {
                    JSONObject asignacion;
                    try
                    {
                        asignacion = response.getJSONObject(i);
                        Log.d("AsignacionReparto", asignacion.toString());
                        try
                        {
                            baseDatos.abrir();
                            if(!baseDatos.existeOrdenReparto(asignacion.getInt("orden_reparto_id")))
                                cargaOrdenReparto(usuario, asignacion.getInt("orden_reparto_id"));

                        }
                        catch (SQLiteCantOpenDatabaseException e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {

                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(contexto,"Error asignaciones",Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        this.queue.add(request);
    }

    /**
     * Metodo para obtener los datos de una orden de reparto desde la API
     * @param usuario
     * @param ordenRepartoId
     */
    private void cargaOrdenReparto(final String usuario, final int ordenRepartoId)
    {
        this.baseDatos.abrir();
        final Usuario u = this.baseDatos.buscarUsuario(usuario);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                this.urlOrdenReparto + "?user_email=" + u.getEmail() + "&user_token=" + u.getToken() + "&id=" + ordenRepartoId
                , (String) null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response){
                try
                {
                    Log.e("OrdenReparto", response.toString());
                    JSONObject jsonOrden = response.getJSONObject("orden_reparto");
                    JSONObject jsonInstalacion = jsonOrden.getJSONObject("instalacion");
                    JSONObject jsonCliente = jsonOrden.getJSONObject("cliente");
                    JSONObject jsonRuta = jsonOrden.getJSONObject("ruta_reparto");

                    Instalacion instalacion = new Instalacion(jsonInstalacion.getInt("id"), jsonInstalacion.getString("codigo"));

                    Cliente cliente = new Cliente(jsonCliente.getInt("id"), jsonCliente.getString("rut"),
                            jsonCliente.getString("nombre"), jsonCliente.getString("apellido_paterno"),
                            jsonCliente.getString("apellido_materno"), jsonCliente.getString("direccion"),
                            jsonCliente.getString("giro"), jsonCliente.getString("telefono"),
                            jsonCliente.getString("memo"), jsonCliente.getString("duenorespo"),
                            jsonCliente.getString("numero_cliente"));

                    RutaReparto ruta = new RutaReparto(jsonRuta.getInt("id"), jsonRuta.getString("codigo"), jsonRuta.getString("nombre"),
                            u.getId(), 0);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

                    OrdenReparto orden = new OrdenReparto(jsonOrden.getInt("id"), jsonOrden.getInt("numero_interno"), jsonOrden.getInt("numero_boleta"),
                                                         baseDatos.buscaTipoDocumento(jsonOrden.getInt("tipo_documento_id")), sdf2.parse(jsonOrden.getString("fecha_emision")).getTime(),
                                                         sdf2.parse(jsonOrden.getString("fecha_vencimiento")).getTime(), jsonOrden.getInt("total_pago"),
                                                         jsonOrden.getInt("orden_ruta"), jsonOrden.getInt("correlativo_impresion"), jsonOrden.getString("direccion_entrega"),
                                                         jsonOrden.getInt("comuna_id"), cliente, ruta, 3, jsonOrden.getInt("tipo_reparto_id"), jsonOrden.getInt("tipo_entrega_id"),
                                                         instalacion, sdf.parse(jsonOrden.getString("fecha_asignacion")).getTime());

                    baseDatos.insertarInstalacion(instalacion);
                    baseDatos.insertarCliente(cliente);
                    baseDatos.insertarRutaReparto(ruta);
                    if(baseDatos.insertarOrdenReparto(orden))
                        actualizaEstadoOrdenReparto(u,orden.getId());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally
                {

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(contexto,"Error orden lectura",Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        this.queue.add(request);
    }

    /**
     * Actualiza estado de una orden de reparto en el servidor.
     * @param usuario Nombre de usuario
     * @param id Id orden a actualizar
     */
    public void actualizaEstadoOrdenReparto(Usuario usuario, int id)
    {
        Map<String, String> postParam = new HashMap<>();
        postParam.put("user_email", usuario.getEmail());
        postParam.put("user_token", usuario.getToken());
        postParam.put("orden_reparto_id", Integer.toString(id));
        postParam.put("estado_reparto_id", "3");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                this.urlOrdenReparto + "/" + id, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };
        jsonObjReq.setShouldCache(false);
        this.queue.add(jsonObjReq);
    }

    /**
     * Actualiza datos de ordenes entregadas
     * @param usuario
     */
    public void actualizaEntregas(String usuario)
    {
        this.baseDatos.abrir();
        Usuario u = this.baseDatos.buscarUsuario(usuario);

        //Se obtiene lista de ordenes sin actualizar en servidor
        ArrayList<OrdenReparto> ordenes = this.baseDatos.listaOrdenesRepartoSinEnviar();


        for(OrdenReparto orden : ordenes)
        {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("user_email", u.getEmail());
            postParam.put("user_token", u.getToken());
            postParam.put("orden_reparto_id", Integer.toString(orden.getId()));
            postParam.put("estado_id", Integer.toString(orden.getEstado()));
            postParam.put("gps_latitud", Double.toString(orden.getGpsLatitud()));
            postParam.put("gps_longitud", Double.toString(orden.getGpsLongitud()));
            postParam.put("fecha_entrega", orden.parseFechaEntrega());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    this.urlOrdenReparto + "/" + orden.getId(), new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }


            };

            jsonObjReq.setShouldCache(false);
            this.queue.add(jsonObjReq);
        }


    }
}

package base_datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import modelos.ClaveLectura;
import modelos.Cliente;
import modelos.DetalleOrdenLectura;
import modelos.EstadoReparto;
import modelos.Fotografia;
import modelos.Instalacion;
import modelos.Intento;
import modelos.Medidor;
import modelos.Observacion;
import modelos.OrdenLectura;
import modelos.OrdenReparto;
import modelos.ParametrosImpresora;
import modelos.ParametrosServidor;
import modelos.Ruta;
import modelos.RutaReparto;
import modelos.TipoDocumento;
import modelos.TipoEntrega;
import modelos.TipoReparto;
import modelos.Usuario;
import static android.provider.BaseColumns._ID;

/**
 * Base de datos embebida
 * Creada por brayan el 22/11/2016.
 * @version 02/11/2017
 */
public class Bd extends SQLiteOpenHelper
{
    private static final String NOMBRE_BD = "BaseDatos";
    private static final int VERSION_ANTERIOR_BD = 6; //Version anterior de la base de datos.
    private static final int VERSION_BD = 7; //Version actual / Se agregan tablas de reparto

    private static Bd instanciaBd;

    /**
     * Se obtiene instancia de la base de datos
     * @param context
     * @return
     */
    public static synchronized Bd getInstance(Context context)
    {
        //Se utiliza un patron de diseño singleton, por lo cual siempre existira una sola instancia
        //de la base de datos en tiempo de ejecucion.
        if (instanciaBd == null) {
            instanciaBd = new Bd(context.getApplicationContext());
        }
        return instanciaBd;
    }

    /**
     * Crea una nueva instancia de la base de datos
     *
     * @param ctx Contexto
     */
    private Bd(Context ctx)
    {
        super(ctx, NOMBRE_BD, null, VERSION_BD);
    }

    /**
     * Metodo donde se definen las tablas de la base de datos y luego se crean.
     * @param db Base de datos
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String  usuario = "CREATE TABLE usuarios (" +
                           _ID + " INTEGER PRIMARY KEY," +
                           "email TEXT,"+
                           "password TEXT,"+
                           "token TEXT," +
                           "perfil_id INTEGER)";

        String ruta = "CREATE TABLE ruta(" +
                       _ID+" INTEGER PRIMARY KEY,"+
                       "codigo STRING," +
                       "nombre STRING," +
                       "usuario INTEGER)";

        String tipo_aparato = "CREATE TABLE tipo_aparatos("+
                _ID+" INTEGER PRIMARY KEY,"+
                "nombre STRING,"+
                "descripcion STRING" + ")";
        
        String ordenLectura = "CREATE TABLE ordenlectura(" +
                               _ID+" INTEGER PRIMARY KEY," +
                               "codigo INTEGER," +
                               "posicion INTEGER," +
                               "direccion TEXT," +
                               "direccion_entrega TEXT," +
                               "numero_poste TEXT," +
                               "fecha_carga TEXT," +
                               "fecha_propuesta TEXT," +
                               "fecha_asignacion TEXT," +
                               "gps_latitud REAL," +
                               "gps_longitud REAL," +
                               "ajuste_sencillo_anterior REAL," +
                               "ajuste_sencillo_actual REAL," +
                               "instalacion_id INTEGER," +
                               "medidor_id INTEGER," +
                               "cliente_id INTEGER," +
                               "ruta_id INTEGER," +
                               "tipo_lectura_id INTEGER," +
                               "estado_lectura_id INTEGER," +
                               "tipo_tarifa_id INTEGER," +
                               "tipo_entrega_id INTEGER," +
                               "tipo_establecimiento_id INTEGER," +
                               "nro_municipal TEXT," +
                               "factor_cobro_id INTEGER, " +
                               "folio_casa_cerrada INTEGER," +
                               "autorizado_facturacion INTEGER," +
                               "facturado INTEGER," +
                               "flag_envio INTEGER)";


        String detalleOrdenLectura = "CREATE TABLE detalleordenlectura(" +
                                      _ID+" INTEGER PRIMARY KEY," +
                                      "orden_lectura_id INTEGER," +
                                      "numerador_id INTEGER," +
                                      "lectura_anterior REAL," +
                                      "lectura_promedio REAL," +
                                      "lectura_actual REAL," +
                                      "rango_superior REAL," +
                                      "rango_inferior REAL," +
                                      "fecha_ejecucion TEXT," +
                                      "clave_lectura_id INTEGER," +
                                      "observacion_id INTEGER," +
                                      "clave_lectura_anterior TEXT," +
                                      "clave_lectura_anterior_2 TEXT," +
                                       "clave_lectura_anterior_3 TEXT," +
                                      "m3_acumulados REAL)";

        String claveLectura = "CREATE TABLE clavelectura(" +
                               _ID + " INTEGER PRIMARY KEY," +
                               "clave TEXT," +
                               "codigo TEXT," +
                               "num_fotografias INTEGER," +
                               "descripcion_corta TEXT," +
                               "tipo_cobro_id INTEGER," +
                               "lectura_requerida INTEGER)";

        String observacion = "CREATE TABLE observacion(" +
                             _ID + " INTEGER PRIMARY KEY," +
                             "num_fotografias INTEGER," +
                             "requerido INTEGER," +
                             "efectivo INTEGER," +
                             "folio INTEGER," +
                             "factura INTEGER," +
                             "clave_lectura_id INTEGER)";

        String perfiles = "CREATE TABLE perfiles(" +
                           _ID + " INTEGER PRIMARY KEY," +
                           "nombre TEXT," +
                           "imagen TEXT)";

        String equipos = "CREATE TABLE equipos(" +
                          _ID + " INTEGER PRIMARY KEY," +
                          "nombre TEXT," +
                          "mac TEXT)";

        String estadoLectura = "CREATE TABLE estado_lectura(" +
                               _ID + " INTEGER PRIMARY KEY," +
                               "nombre TEXT)";

        String instalacion = "CREATE TABLE instalacion(" +
                             _ID + " INTEGER PRIMARY KEY," +
                             "codigo TEXT)";

        String fotografia = "CREATE TABLE fotografia(" +
                            _ID + " INTEGER PRIMARY KEY," +
                            "detalle_orden_lectura_id INTEGER," +
                            "archivo TEXT," +
                            "descripcion TEXT," +
                            "flag_envio INTEGER)";

        String tipoLectura = "CREATE TABLE tipo_lectura(" +
                             _ID + " INTEGER PRIMARY KEY," +
                             "nombre TEXT)";

        String tipoTarifa = "CREATE TABLE tipo_tarifa(" +
                            _ID + " INTEGER PRIMARY KEY," +
                            "nombre TEXT," +
                            "facturacion_en_terreno INTEGER)";

        String tipoEntrega = "CREATE TABLE tipo_entrega(" +
                             _ID + " INTEGER PRIMARY KEY," +
                             "codigo TEXT," +
                             "nombre  TEXT," +
                             "facturacion_en_terreno INTEGER)";

        String tipoEstablecimiento = "CREATE TABLE tipo_establecimiento(" +
                                     _ID + " INTEGER PRIMARY KEY," +
                                     "codigo TEXT," +
                                     "nombre TEXT," +
                                     "facturacion_en_terreno INTEGER," +
                                     "excento INTEGER)";

        String factorCobro = "CREATE TABLE factor_cobro(" +
                             _ID + " INTEGER PRIMARY KEY," +
                             "subempresa_id INTEGER," +
                             "sector_id INTEGER," +
                             "tipo_sector_id INTEGER," +
                             "tipo_tarifa_id INTEGER," +
                             "cargo_fijo REAL," +
                             "cargo_unico REAL," +
                             "cargo_energia_base REAL," +
                             "cargo_energia_adicional REAL," +
                             "cargo_energia_inyectada REAL)";


        String tipoCobro = "CREATE TABLE tipo_cobro(" +
                           _ID + " INTEGER PRIMARY KEY," +
                           "descripcion TEXT)";

        String cliente = "CREATE TABLE cliente(" +
                         _ID + " INTEGER PRIMARY KEY," +
                         "rut TEXT," +
                         "nombre TEXT," +
                         "apellido_paterno TEXT," +
                         "apellido_materno TEXT," +
                         "direccion TEXT," +
                         "giro TEXT," +
                         "telefono TEXT," +
                         "memo TEXT," +
                         "duenorespo TEXT," +
                         "numero_cliente TEXT)";

        String descuento = "CREATE TABLE descuento(" +
                           _ID + " INTEGER PRIMARY KEY," +
                           "descripcion TEXT," +
                           "monto REAL," +
                           "instalacion_id INTEGER)";

        String medidor = "CREATE TABLE medidor(" +
                         _ID + " INTEGER PRIMARY KEY," +
                         "numero_medidor TEXT," +
                         "modelo_id INTEGER," +
                         "propiedad_cliente INTEGER," +
                         "nro_digitos INTEGER," +
                         "diametro INTEGER)";

        String intentos = "CREATE TABLE intento(" +
                          _ID + " INTEGER PRIMARY KEY," +
                          "detalle_orden_lectura_id INTEGER," +
                          "lectura REAL," +
                          "flag_envio INTEGER)";

        String documentos = "CREATE TABLE documentos(" +
                            _ID + " INTEGER PRIMARY KEY," +
                            "numero_documento INTEGER," +
                            "monto_total REAL," +
                            "tipo_documento_id INTEGER," +
                            "orden_lectura_id INTEGER)";

        String detalleDocumentos = "CREATE TABLE detalle_documentos(" +
                                   _ID + " INTEGER PRIMARY KEY," +
                                   "detalle TEXT," +
                                   "monto REAL," +
                                   "documento_id INTEGER)";

        String tipoDocumentos = "CREATE TABLE tipo_documentos(" +
                                _ID + " INTEGER PRIMARY KEY," +
                                "codigo_sii TEXT," +
                                "nombre TEXT)";

        String parametros = "CREATE TABLE parametros(" +
                            _ID + " INTEGER PRIMARY KEY," +
                            "ip TEXT,"+
                            "intervalo INTEGER)";

        String parametrosImpresora = "CREATE TABLE parametrosimpresora("+
                                    _ID + " INTEGER PRYMARY KEY," +
                                    "mac TEXT," +
                                    "flag_facturacion INTEGER)";

        String ordenReparto = "CREATE TABLE ordenreparto(" +
                                _ID + " INTEGER PRYMARY KEY," +
                                "numero_interno INTEGER," +
                                "numero_boleta INTEGER," +
                                "tipo_documento_id INTEGER," +
                                "fecha_emision TEXT," +
                                "fecha_vencimiento TEXT," +
                                "fecha_entrega TEXT," +
                                "total_pago INTEGER," +
                                "orden_ruta INTEGER," +
                                "correlativo_impresion INTEGER," +
                                "direccion_entrega TEXT," +
                                "comuna_id INTEGER," +
                                "cliente_id INTEGER," +
                                "ruta_reparto_id INTEGER," +
                                "estado_reparto_id INTEGER," +
                                "tipo_reparto_id INTEGER," +
                                "tipo_entrega_id INTEGER," +
                                "instalacion_id INTEGER," +
                                "fecha_asignacion TEXT," +
                                "gps_longitud REAL," +
                                "gps_latitud REAL," +
                                "flag_envio INTEGER)";

        String estadoReparto = "CREATE TABLE estadoreparto(" +
                                _ID + " INTEGER PRIMARY KEY," +
                                "estado TEXT)";

        String tipoReparto = "CREATE TABLE tiporeparto(" +
                              _ID + " INTEGER PRIMARY KEY," +
                              "descripcion TEXT)";

        String rutaReparto = "CREATE TABLE rutareparto(" +
                              _ID + " INTEGER PRIMARY KEY," +
                              "codigo TEXT," +
                              "nombre TEXT," +
                              "usuario INTEGER)";




        //TODO: Falta tabla de tipos de documentos ????

        // Ejecucion de sentencias SQL definidas anteriormente
        db.execSQL(usuario);
        db.execSQL(ruta);
        db.execSQL(ordenLectura);
        db.execSQL(detalleOrdenLectura);
        db.execSQL(claveLectura);
        db.execSQL(observacion);
        db.execSQL(perfiles);
        db.execSQL(equipos);
        db.execSQL(estadoLectura);
        db.execSQL(instalacion);
        db.execSQL(fotografia);
        db.execSQL(tipoLectura);
        db.execSQL(tipoTarifa);
        db.execSQL(tipoEntrega);
        db.execSQL(tipoEstablecimiento);
        db.execSQL(factorCobro);
        db.execSQL(tipoCobro);
        db.execSQL(cliente);
        db.execSQL(descuento);
        db.execSQL(medidor);
        db.execSQL(intentos);
        db.execSQL(documentos);
        db.execSQL(detalleDocumentos);
        db.execSQL(tipoDocumentos);
        db.execSQL(parametros);
        db.execSQL(parametrosImpresora);
        db.execSQL(ordenReparto);
        db.execSQL(estadoReparto);
        db.execSQL(tipoReparto);
        db.execSQL(rutaReparto);

        //Insersion de usuario por defecto en la bd.
        db.execSQL("INSERT INTO usuarios (" + _ID + ", email, password, token, perfil_id) values(1,'admin','admin','',1)");
        db.execSQL("INSERT INTO parametrosimpresora(" + _ID + ", mac, flag_facturacion) values(1, '', 0)");
    }

    /**
     * Actualiza la base de datos local cuando hay cambios
     * @param db Base de datos
     * @param i Version anterior
     * @param i1 version Actual
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS ruta");
        db.execSQL("DROP TABLE IF EXISTS ordenlectura");
        db.execSQL("DROP TABLE IF EXISTS detalleordenlectura");
        db.execSQL("DROP TABLE IF EXISTS clavelectura");
        db.execSQL("DROP TABLE IF EXISTS observacion");
        db.execSQL("DROP TABLE IF EXISTS perfiles");
        db.execSQL("DROP TABLE IF EXISTS equipos");
        db.execSQL("DROP TABLE IF EXISTS estado_lectura");
        db.execSQL("DROP TABLE IF EXISTS instalacion");
        db.execSQL("DROP TABLE IF EXISTS fotografia");
        db.execSQL("DROP TABLE IF EXISTS tipo_lectura");
        db.execSQL("DROP TABLE IF EXISTS tipo_tarifa");
        db.execSQL("DROP TABLE IF EXISTS tipo_entrega");
        db.execSQL("DROP TABLE IF EXISTS tipo_establecimiento");
        db.execSQL("DROP TABLE IF EXISTS factor_cobro");
        db.execSQL("DROP TABLE IF EXISTS tipo_cobro");
        db.execSQL("DROP TABLE IF EXISTS cliente");
        db.execSQL("DROP TABLE IF EXISTS descuento");
        db.execSQL("DROP TABLE IF EXISTS medidor");
        db.execSQL("DROP TABLE IF EXISTS intento");
        db.execSQL("DROP TABLE IF EXISTS documentos");
        db.execSQL("DROP TABLE IF EXISTS detalle_documentos");
        db.execSQL("DROP TABLE IF EXISTS tipo_documentos");
        db.execSQL("DROP TABLE IF EXISTS parametros");
        db.execSQL("DROP TABLE IF EXISTS parametrosimpresora");
        db.execSQL("DROP TABLE IF EXISTS ordenreparto");
        db.execSQL("DROP TABLE IF EXISTS estadoreparto");
        db.execSQL("DROP TABLE IF EXISTS tiporeparto");
        db.execSQL("DROP TABLE IF EXISTS rutareparto");
        onCreate(db);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                         CLIENTES                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Inserta un cliente
     * @param cliente
     */
    public void insertarCliente(Cliente cliente)
    {
        if(!this.existeCliente(cliente)) {
            ContentValues valores = new ContentValues();
            valores.put(_ID, cliente.getId());
            valores.put("rut", cliente.getRut());
            valores.put("nombre", cliente.getNombre());
            valores.put("apellido_paterno", cliente.getApellidoPaterno());
            valores.put("apellido_materno", cliente.getApellidoMaterno());
            valores.put("direccion", cliente.getDireccion());
            valores.put("giro", cliente.getGiro());
            valores.put("telefono", cliente.getTelefono());
            valores.put("memo", cliente.getMemo());
            valores.put("duenorespo", cliente.getDuenorespo());
            valores.put("numero_cliente", cliente.getNumero_cliente());

            this.getWritableDatabase().insert("cliente", null, valores);
        }
    }

    /**
     * Busca un cliente por su id
     * @param idCliente identificador de cliente
     * @return
     */
    public Cliente buscarCliente(int idCliente)
    {
        Cliente resultado= null;
        String[] campos = new String[] {_ID,  "rut", "nombre", "apellido_paterno", "apellido_materno",
                                        "direccion", "giro", "telefono", "memo", "duenorespo", "numero_cliente"};
        String[] args = new String[] {Integer.toString(idCliente)};

        Cursor c = this.getReadableDatabase().query("cliente", campos, _ID + "=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int rut = c.getColumnIndex("rut");
        int nombre = c.getColumnIndex("nombre");
        int apellidoPaterno = c.getColumnIndex("apellido_paterno");
        int apellidoMaterno = c.getColumnIndex("apellido_materno");
        int direccion = c.getColumnIndex("direccion");
        int giro = c.getColumnIndex("giro");
        int telefono = c.getColumnIndex("telefono");
        int memo = c.getColumnIndex("memo");
        int duenorespo = c.getColumnIndex("duenorespo");
        int numeroCliente = c.getColumnIndex("numero_cliente");


        while(c.moveToNext())
        {
            resultado = new Cliente(c.getInt(id), c.getString(rut), c.getString(nombre),
                                    c.getString(apellidoPaterno), c.getString(apellidoMaterno),
                                    c.getString(direccion), c.getString(giro), c.getString(telefono),
                                    c.getString(memo), c.getString(duenorespo), c.getString(numeroCliente));
        }

        c.close();
        return resultado;
    }

    /**
     * Verifica si un cliente existe
     * @param cliente
     * @return
     */
    public boolean existeCliente(Cliente cliente){
        boolean resultado = false;
        String[] campos = new String[] {_ID};
        String[] args = new String[] {Integer.toString(cliente.getId())};

        Cursor c = this.getReadableDatabase().query("cliente", campos, _ID + "=?", args, null, null, null);
        int id;

        id= c.getColumnIndex(_ID);

        while(c.moveToNext())
        {
            if(c.getInt(id) == cliente.getId())
                resultado = true;
        }

        c.close();
        return resultado;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       Usuarios                                             //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Elimina todos los datos desde la tabla usuarios.
     */
    public void eliminarTablaUsuarios()
    {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("usuarios", null, null);
        db.execSQL("DELETE FROM usuarios" );
    }

    /**
     * Insertar usuario.
     *
     * @param id       Identificador de usuario
     * @param email    Email o nombre de usuario
     * @param password Contraseña
     * @param token    Token para comunicacion con servidor
     */
    public void insertarUsuario(int id, String email, String password, String token, int perfil_id)
    {
        ContentValues valores  = new ContentValues();
        valores.put(_ID, id);
        valores.put("email", email);
        valores.put("password", password);
        valores.put("token", token);
        valores.put("perfil_id", perfil_id);

        this.getWritableDatabase().insert("usuarios", null, valores);
    }

    /**
     * Actualiza registro de usuario
     * @param usuario Objeto del tipo usuario
     */
    public void actualizarUsuario(Usuario usuario)
    {
        ContentValues valores = new ContentValues();
        valores.put("password", usuario.getPassword());
        valores.put("token", usuario.getToken());
        valores.put("perfil_id", usuario.getPerfilId());
        this.getWritableDatabase().update("usuarios", valores, _ID + "=" + usuario.getId(), null);
    }

    /**
     * Elimina un registro de usuario.
     *
     * @param email Email o nombre de usuario
     * @return string status
     */
    public String eliminarUsuario(String email)
    {
        String[] args = new String[] {email};
        this.getReadableDatabase().execSQL("DELETE FROM usuarios WHERE email=?", args);
        return "Registro Eliminado";
    }

    /**
     * Busca registro de usuario.
     *
     * @param email Email o nombre de usuario
     * @return objeto de la clase Usuario
     */
    public Usuario buscarUsuario(String email)
    {
        Usuario resultado= null;
        String[] campos = new String[] {_ID, "email", "password","token","perfil_id"};
        String[] args = new String[] {email};

        Cursor c = this.getReadableDatabase().query("usuarios", campos, "email=?", args, null, null, null);
        int id, mail, pass, token, perfil_id;

        id = c.getColumnIndex(_ID);
        mail = c.getColumnIndex("email");
        pass = c.getColumnIndex("password");
        token = c.getColumnIndex("token");
        perfil_id =c.getColumnIndex("perfil_id");

        while(c.moveToNext())
        {
            Log.e(null, "buscarUsuario: ");
            resultado = new Usuario(Integer.parseInt(c.getString(id)),c.getString(mail),
                        c.getString(pass), c.getString(token), Integer.parseInt(c.getString(perfil_id)));
        }

        c.close();
        return resultado;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                            Rutas                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Verifica que un id de ruta exista.
     *
     * @param ruta objeto con todos los datos de la unidad de lectura
     * @return boolean que indica si la ruta existe o no
     */
    public boolean existeRuta(Ruta ruta)
    {
        boolean resultado = false;
        String[] campos = new String[] {_ID};
        String[] args = new String[] {Integer.toString(ruta.getId())};

        Cursor c = this.getReadableDatabase().query("ruta", campos, _ID + "=?", args, null, null, null);
        int id;

        id= c.getColumnIndex(_ID);
		
        while(c.moveToNext())
        {
            if(c.getInt(id) == ruta.getId())
                resultado = true;
        }

        c.close();
        return resultado;
    }

    /**
     * Busca ruta de acuerdo al ID
     * @param rutaId Id de ruta
     * @return Objeto Ruta con todos los datos del id buscado
     */
    //TODO: Revisar si este metodo aun es necesario.
    public Ruta buscarRuta(String rutaId)
    {
        Ruta resultado = null;
        String[] campos = new String[] {_ID, "codigo", "nombre", "usuario"};
        String[] args = new String[] {rutaId};

        Cursor c = this.getReadableDatabase().query("ruta", campos, _ID + "=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int nombre = c.getColumnIndex("nombre");
        int usuario = c.getColumnIndex("usuario");

        while(c.moveToNext())
        {
            if(c.getString(id).equals(rutaId))
            {
                resultado = new Ruta(c.getInt(id), c.getString(codigo), c.getString(nombre), c.getInt(usuario),
                        numeroOrdenesRuta(c.getInt(id)));
            }
        }

        c.close();
        return resultado;
    }

    /**
     * Insertar ruta.
     *
     * @param ruta
     */
    public void insertarRuta(Ruta ruta)
    {
        if(!this.existeRuta(ruta)) {
            ContentValues valores = new ContentValues();
            valores.put(_ID, ruta.getId());
            valores.put("codigo", ruta.getCodigo());
            valores.put("nombre", ruta.getNombre());
            valores.put("usuario", ruta.getUsuario());

            this.getWritableDatabase().insert("ruta", null, valores);
        }
    }

    /**
     * Leer rutas y retornar un arreglo de ellas de acuerdo al usuario logeado.
     *
     * @param usr Id usuario logeado
     * @param tipoLectura Indica tipo de ordenes a cargar | 1 -> Lectura Normal | 2 -> Lectura Control
     * @return the array list
     */
    public ArrayList<Ruta> leerRutas(int usr, int tipoLectura)
    {
        ArrayList<Ruta> resultado = new ArrayList<>();

        String filas[] = {_ID, "codigo", "nombre", "usuario"};
        String[] args = new String[] {Integer.toString(usr)};

        Cursor c = this.getReadableDatabase().query("ruta", filas,"usuario=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int nombre = c.getColumnIndex("nombre");
        int usuario = c.getColumnIndex("usuario");

        while(c.moveToNext())
        {
            Ruta ruta = new Ruta(c.getInt(id),c.getString(codigo),
                                 c.getString(nombre), c.getInt(usuario), leerOrdenes(c.getInt(id), tipoLectura, null));

            //Se muestran solo las rutas con un numero de ordenes pendientes mayor a cero.
            if (ruta.getLecturas().size() != 0)
                resultado.add(ruta);
        }

        c.close();
        return resultado;
    }

    /**
     * Obtiene numero de ordenes de lectura pendientes para una ruta.
     * @param id Id ruta
     * @return cantidad de ordenes pendientes de ruta ingresada
     */
    private int numeroOrdenesRuta(int id)
	{
		SQLiteStatement statement = this.getReadableDatabase().compileStatement(
		                            "select count(*) from ordenlectura where ruta_id='" +
                                            Integer.toString(id) + "' and estado_lectura_id = '3'");

        return (int) (long) statement.simpleQueryForLong();
	}

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                     INSTALACIONES                                          //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Inserta un nuevo registro de instalación
     * @param instalacion
     */
    public void insertarInstalacion(Instalacion instalacion)
    {
        if(!this.existeInstalacion(instalacion)) {


            ContentValues valores = new ContentValues();
            valores.put(_ID, instalacion.getId());
            valores.put("codigo", instalacion.getCodigo());

            this.getWritableDatabase().insert("instalacion", null, valores);
        }
    }

    /**
     * Verifica si una instalacion ya existe en la base de datos
     * @param instalacion
     * @return
     */
    public boolean existeInstalacion(Instalacion instalacion)
    {
        boolean resultado = false;
        String[] campos = new String[] {_ID};
        String[] args = new String[] {Integer.toString(instalacion.getId())};

        Cursor c = this.getReadableDatabase().query("instalacion", campos, _ID + "=?", args, null, null, null);
        int id;

        id= c.getColumnIndex(_ID);

        while(c.moveToNext())
        {
            if(c.getInt(id) == instalacion.getId())
                resultado = true;
        }

        c.close();
        return resultado;

    }

    /**
     * Busca un registro de instalacion de acuerdo a su ID
     * @param idInstalacion Id instalacion
     * @return Objeto del tipo instalacion correspondiente al id buscado
     */
    public Instalacion buscarInstalacion(int idInstalacion)
    {
        Instalacion resultado= null;
        String[] campos = new String[] {_ID,  "codigo"};
        String[] args = new String[] {Integer.toString(idInstalacion)};

        Cursor c = this.getReadableDatabase().query("instalacion", campos, _ID + "=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");

        while(c.moveToNext())
        {
            resultado = new Instalacion(c.getInt(id), c.getString(codigo));
        }

        c.close();
        return resultado;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                         MEDIDOR                                            //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Inserta un nuevo registro en la tabla de medidores.
     * @param medidor
     */
    public void insertarMedidor(Medidor medidor)
    {
        if(!this.existeMedidor(medidor)) {
            ContentValues valores = new ContentValues();
            valores.put(_ID, medidor.getId());
            valores.put("numero_medidor", medidor.getNumeroMedidor());
            valores.put("modelo_id", medidor.getModeloId());
            valores.put("propiedad_cliente", medidor.getPropiedadCliente());
            valores.put("nro_digitos", medidor.getNroDigitos());
            valores.put("diametro", medidor.getDiametro());

            this.getWritableDatabase().insert("medidor", null, valores);
        }
    }

    /**
     * Busca un medidor de acuerdo a su id
     * @param idMedidor id de medidor a buscar
     * @return resultado Objeto de tipo medidor
     */
    public Medidor buscarMedidor(int idMedidor)
    {
        Medidor resultado= null;
        String[] campos = new String[] {_ID,  "numero_medidor", "modelo_id", "propiedad_cliente",
                                        "nro_digitos", "diametro"};

        String[] args = new String[] {Integer.toString(idMedidor)};

        Cursor c = this.getReadableDatabase().query("medidor", campos, _ID + "=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int numeroMedidor = c.getColumnIndex("numero_medidor");
        int modeloId = c.getColumnIndex("modelo_id");
        int propiedadCliente = c.getColumnIndex("propiedad_cliente");
        int nroDigitos = c.getColumnIndex("nro_digitos");
        int diametro = c.getColumnIndex("diametro");

        while(c.moveToNext())
        {
            resultado = new Medidor(c.getInt(id), c.getString(numeroMedidor), c.getInt(modeloId),
                                    c.getInt(propiedadCliente) == 1? true: false, c.getInt(nroDigitos), c.getInt(diametro));
        }

        c.close();
        return resultado;
    }

    /**
     * Verifica existencia de un medidor en la base de datos
     * @param medidor objeto de tipo medidor
     * @return boolean
     */
    public boolean existeMedidor(Medidor medidor)
    {
        boolean resultado = false;
        String[] campos = new String[] {_ID};
        String[] args = new String[] {Integer.toString(medidor.getId())};

        Cursor c = this.getReadableDatabase().query("medidor", campos, _ID + "=?", args, null, null, null);
        int id;

        id= c.getColumnIndex(_ID);

        while(c.moveToNext())
        {
            if(c.getInt(id) == medidor.getId())
                resultado = true;
        }

        c.close();
        return resultado;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                  ORDENES DE LECTURA                                        //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Insertar ordenlectura.
     *
     * @param orden
     */
    public boolean insertarOrdenlectura(OrdenLectura orden) {
        if (!this.existeOrdenLectura(orden.getId())) {
            ContentValues valores = new ContentValues();
            valores.put(_ID, orden.getId());
            valores.put("codigo", orden.getCodigo());
            valores.put("posicion", orden.getPosicion());
            valores.put("direccion", orden.getDireccion());
            valores.put("direccion_entrega", orden.getDireccionEntrega());
            valores.put("numero_poste", orden.getNumeroPoste());
            valores.put("fecha_carga", orden.getFechaCarga());
            valores.put("fecha_propuesta", orden.getFechaPropuesta());
            valores.put("fecha_asignacion", orden.getFechaAsignacion());
            valores.put("gps_latitud", orden.getGpsLatitud());
            valores.put("gps_longitud", orden.getGpsLongitud());
            valores.put("ajuste_sencillo_anterior", orden.getAjusteSencilloAnterior());
            valores.put("ajuste_sencillo_actual", orden.getAjusteSencilloActual());
            valores.put("instalacion_id", orden.getInstalacionId());
            valores.put("medidor_id", orden.getMedidorId());
            valores.put("cliente_id", orden.getClienteId());
            valores.put("ruta_id", orden.getRutaId());
            valores.put("tipo_lectura_id", orden.getTipoLecturaId());
            valores.put("estado_lectura_id", orden.getEstadoLecturaId());
            valores.put("tipo_tarifa_id", orden.getTipoTarifaId());
            valores.put("tipo_entrega_id", orden.getTipoEntregaId());
            valores.put("tipo_establecimiento_id", orden.getTipoEstablecimientoId());
            valores.put("nro_municipal", orden.getNroMunicipal());
            //valores.put("folio_casa_cerrada", 0);
            valores.put("factor_cobro_id", orden.getFactorCobroId());
            valores.put("autorizado_facturacion", orden.isAutorizadoFacturacion());
            valores.put("facturado", orden.isFacturado());
            valores.put("flag_envio", 0);

            if(this.getWritableDatabase().insert("ordenlectura", null, valores) != -1){
                for(DetalleOrdenLectura numerador : orden.getNumeradores()){
                    this.insertarDetalleOrdenlectura(numerador);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Leer ordenes.
     *
     * @param rutaCarga Ruta de la cual se quieren obtener las ordenes de lectura
     * @param tipoLectura Indica tipo de ordenes a cargar | 1 -> Lectura Normal | 2 -> Lectura Control
     * @return arreglo con todas las ordenes de la ruta
     */
    public ArrayList<OrdenLectura> leerOrdenes(int rutaCarga, int tipoLectura, String orderBy)
    {
        ArrayList<OrdenLectura> resultado = new ArrayList<>();

        String filas[] = {_ID, "codigo", "posicion", "direccion", "direccion_entrega", "numero_poste", "fecha_carga",
                "fecha_propuesta", "fecha_asignacion","gps_latitud", "gps_longitud", "ajuste_sencillo_anterior",
                "ajuste_sencillo_actual", "instalacion_id", "medidor_id", "cliente_id", "ruta_id", "tipo_lectura_id",
                "estado_lectura_id", "tipo_tarifa_id", "tipo_entrega_id", "tipo_establecimiento_id", "nro_municipal",
                "factor_cobro_id", "folio_casa_cerrada", "autorizado_facturacion", "facturado", "flag_envio"};
        String[] args = new String[] {Integer.toString(rutaCarga), Integer.toString(tipoLectura)};

        Cursor c = this.getReadableDatabase().query("ordenlectura", filas, "ruta_id=? and tipo_lectura_id=?", args, null, null, orderBy);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int posicion = c.getColumnIndex("posicion");
        int direccion = c.getColumnIndex("direccion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int numeroPoste = c.getColumnIndex("numero_poste");
        int fechaCarga = c.getColumnIndex("fecha_carga");
        int fechaPropuesta = c.getColumnIndex("fecha_propuesta");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_latitud");
        int gpsLongitud = c.getColumnIndex("gps_longitud");
        int ajusteSencilloAnterior = c.getColumnIndex("ajuste_sencillo_anterior");
        int ajusteSencilloActual = c.getColumnIndex("ajuste_sencillo_actual");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int medidorId = c.getColumnIndex("medidor_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaId = c.getColumnIndex("ruta_id");
        int tipoLecturaId = c.getColumnIndex("tipo_lectura_id");
        int estadoLecturaId = c.getColumnIndex("estado_lectura_id");
        int tipoTarifaId = c.getColumnIndex("tipo_tarifa_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int tipoEstablecimientoId = c.getColumnIndex("tipo_establecimiento_id");
        int nroMunicipal = c.getColumnIndex("nro_municipal");
        int factorCobroId = c.getColumnIndex("factor_cobro_id");
        int folioCasaCerrada = c.getColumnIndex("folio_casa_cerrada");
        int autorizadoFacturacion = c.getColumnIndex("autorizado_facturacion");
        int facturado = c.getColumnIndex("facturado");
        int flagEnvio = c.getColumnIndex("flag_envio");

        while(c.moveToNext())
        {
            if(c.getString(estadoLecturaId).equals("3"))
            {
                OrdenLectura orden = new OrdenLectura(c.getInt(id), c.getInt(codigo), c.getInt(posicion), c.getString(direccion),
                        c.getString(direccionEntrega), c.getString(numeroPoste), c.getLong(fechaCarga),
                        c.getLong(fechaPropuesta), c.getLong(fechaAsignacion), c.getDouble(gpsLatitud),
                        c.getDouble(gpsLongitud), c.getDouble(ajusteSencilloAnterior),
                        c.getDouble(ajusteSencilloActual), c.getInt(instalacionId), c.getInt(medidorId),
                        c.getInt(clienteId), c.getInt(rutaId), c.getInt(tipoLecturaId), c.getInt(estadoLecturaId),
                        c.getInt(tipoTarifaId), c.getInt(tipoEntregaId), c.getInt(tipoEstablecimientoId),
                        c.getInt(nroMunicipal), c.getInt(factorCobroId), c.getInt(folioCasaCerrada),
                        (c.getInt(autorizadoFacturacion) == 1 ? true : false), (c.getInt(facturado) == 1 ? true : false),
                        c.getInt(flagEnvio), leerDetalleOrdenLectura(c.getInt(id)));

                if(this.buscarCliente(orden.getClienteId())!= null && this.buscarInstalacion(orden.getInstalacionId()) != null
                        && this.buscarMedidor(orden.getMedidorId()) != null && orden.getNumeradores().size() > 0)
                {
                    resultado.add(orden);
                }
            }
        }

        c.close();
        return resultado;
    }

    /**
     * Lista todas las ordenes ya facturadas, para ser reimpresas
     * @return arreglo con todas las ordenes facturadas
     */
    public ArrayList<OrdenLectura> leerOrdenesReimpresion()
    {
        ArrayList<OrdenLectura> resultado = new ArrayList<>();

        String filas[] = {_ID, "codigo", "posicion", "direccion", "direccion_entrega", "numero_poste", "fecha_carga",
                "fecha_propuesta", "fecha_asignacion","gps_latitud", "gps_longitud", "ajuste_sencillo_anterior",
                "ajuste_sencillo_actual", "instalacion_id", "medidor_id", "cliente_id", "ruta_id", "tipo_lectura_id",
                "estado_lectura_id", "tipo_tarifa_id", "tipo_entrega_id", "tipo_establecimiento_id", "nro_municipal",
                "factor_cobro_id", "folio_casa_cerrada", "autorizado_facturacion", "facturado", "flag_envio"};
        String[] args = new String[] {Integer.toString(1)};

        Cursor c = this.getReadableDatabase().query("ordenlectura", filas, "facturado=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int posicion = c.getColumnIndex("posicion");
        int direccion = c.getColumnIndex("direccion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int numeroPoste = c.getColumnIndex("numero_poste");
        int fechaCarga = c.getColumnIndex("fecha_carga");
        int fechaPropuesta = c.getColumnIndex("fecha_propuesta");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_latitud");
        int gpsLongitud = c.getColumnIndex("gps_longitud");
        int ajusteSencilloAnterior = c.getColumnIndex("ajuste_sencillo_anterior");
        int ajusteSencilloActual = c.getColumnIndex("ajuste_sencillo_actual");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int medidorId = c.getColumnIndex("medidor_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaId = c.getColumnIndex("ruta_id");
        int tipoLecturaId = c.getColumnIndex("tipo_lectura_id");
        int estadoLecturaId = c.getColumnIndex("estado_lectura_id");
        int tipoTarifaId = c.getColumnIndex("tipo_tarifa_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int tipoEstablecimientoId = c.getColumnIndex("tipo_establecimiento_id");
        int nroMunicipal = c.getColumnIndex("nro_municipal");
        int factorCobroId = c.getColumnIndex("factor_cobro_id");
        int folioCasaCerrada = c.getColumnIndex("folio_casa_cerrada");
        int autorizadoFacturacion = c.getColumnIndex("autorizado_facturacion");
        int facturado = c.getColumnIndex("facturado");
        int flagEnvio = c.getColumnIndex("flag_envio");

        while(c.moveToNext())
        {
            OrdenLectura orden = new OrdenLectura(c.getInt(id), c.getInt(codigo), c.getInt(posicion), c.getString(direccion),
                        c.getString(direccionEntrega), c.getString(numeroPoste), c.getLong(fechaCarga),
                        c.getLong(fechaPropuesta), c.getLong(fechaAsignacion), c.getDouble(gpsLatitud),
                        c.getDouble(gpsLongitud), c.getDouble(ajusteSencilloAnterior),
                        c.getDouble(ajusteSencilloActual), c.getInt(instalacionId), c.getInt(medidorId),
                        c.getInt(clienteId), c.getInt(rutaId), c.getInt(tipoLecturaId), c.getInt(estadoLecturaId),
                        c.getInt(tipoTarifaId), c.getInt(tipoEntregaId), c.getInt(tipoEstablecimientoId),
                        c.getInt(nroMunicipal), c.getInt(factorCobroId), c.getInt(folioCasaCerrada),
                        (c.getInt(autorizadoFacturacion) == 1 ? true : false), (c.getInt(facturado) == 1 ? true: false),
                        c.getInt(flagEnvio), leerDetalleOrdenLectura(c.getInt(id)));

            if(this.buscarCliente(orden.getClienteId())!= null && this.buscarInstalacion(orden.getInstalacionId()) != null
               && this.buscarMedidor(orden.getMedidorId()) != null && orden.getNumeradores().size() > 0)
            {
                resultado.add(orden);
            }
        }

        c.close();
        return resultado;
    }

    /**
     * Lee todas las ordenes pendientes de facturacion
     * @return Arreglo con las ordenes pendientes de facturacion
     */
    public ArrayList<OrdenLectura> leerOrdenesFacturacion()
    {
        ArrayList<OrdenLectura> resultado = new ArrayList<>();

        String filas[] = {_ID, "codigo", "posicion", "direccion", "direccion_entrega", "numero_poste", "fecha_carga",
                "fecha_propuesta", "fecha_asignacion","gps_latitud", "gps_longitud", "ajuste_sencillo_anterior",
                "ajuste_sencillo_actual", "instalacion_id", "medidor_id", "cliente_id", "ruta_id", "tipo_lectura_id",
                "estado_lectura_id", "tipo_tarifa_id", "tipo_entrega_id", "tipo_establecimiento_id", "nro_municipal",
                "factor_cobro_id", "folio_casa_cerrada", "autorizado_facturacion", "facturado", "flag_envio"};
        String[] args = new String[] {Integer.toString(1), Integer.toString(0)};

        Cursor c = this.getReadableDatabase().query("ordenlectura", filas, "autorizado_facturacion=? and facturado=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int posicion = c.getColumnIndex("posicion");
        int direccion = c.getColumnIndex("direccion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int numeroPoste = c.getColumnIndex("numero_poste");
        int fechaCarga = c.getColumnIndex("fecha_carga");
        int fechaPropuesta = c.getColumnIndex("fecha_propuesta");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_latitud");
        int gpsLongitud = c.getColumnIndex("gps_longitud");
        int ajusteSencilloAnterior = c.getColumnIndex("ajuste_sencillo_anterior");
        int ajusteSencilloActual = c.getColumnIndex("ajuste_sencillo_actual");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int medidorId = c.getColumnIndex("medidor_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaId = c.getColumnIndex("ruta_id");
        int tipoLecturaId = c.getColumnIndex("tipo_lectura_id");
        int estadoLecturaId = c.getColumnIndex("estado_lectura_id");
        int tipoTarifaId = c.getColumnIndex("tipo_tarifa_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int tipoEstablecimientoId = c.getColumnIndex("tipo_establecimiento_id");
        int nroMunicipal = c.getColumnIndex("nro_municipal");
        int factorCobroId = c.getColumnIndex("factor_cobro_id");
        int folioCasaCerrada = c.getColumnIndex("folio_casa_cerrada");
        int autorizadoFacturacion = c.getColumnIndex("autorizado_facturacion");
        int facturado = c.getColumnIndex("facturado");
        int flagEnvio = c.getColumnIndex("flag_envio");

        while(c.moveToNext())
        {
            OrdenLectura orden = new OrdenLectura(c.getInt(id), c.getInt(codigo), c.getInt(posicion), c.getString(direccion),
                    c.getString(direccionEntrega), c.getString(numeroPoste), c.getLong(fechaCarga),
                    c.getLong(fechaPropuesta), c.getLong(fechaAsignacion), c.getDouble(gpsLatitud),
                    c.getDouble(gpsLongitud), c.getDouble(ajusteSencilloAnterior),
                    c.getDouble(ajusteSencilloActual), c.getInt(instalacionId), c.getInt(medidorId),
                    c.getInt(clienteId), c.getInt(rutaId), c.getInt(tipoLecturaId), c.getInt(estadoLecturaId),
                    c.getInt(tipoTarifaId), c.getInt(tipoEntregaId), c.getInt(tipoEstablecimientoId),
                    c.getInt(nroMunicipal), c.getInt(factorCobroId), c.getInt(folioCasaCerrada),
                    (c.getInt(autorizadoFacturacion) == 1 ? true : false), (c.getInt(facturado) == 1 ? true: false),
                    c.getInt(flagEnvio), leerDetalleOrdenLectura(c.getInt(id)));

            if(this.buscarCliente(orden.getClienteId())!= null && this.buscarInstalacion(orden.getInstalacionId()) != null
                    && this.buscarMedidor(orden.getMedidorId()) != null && orden.getNumeradores().size() > 0)
            {
                resultado.add(orden);
            }
        }

        c.close();
        return resultado;
    }

    /**
     * Retorna un arreglo con todas las ordenes de lectura pendientes
     * @return Arreglo con todas las ordenes de lectura pendientes
     */
    public ArrayList<OrdenLectura> leerTodasOrdenesPendientes()
    {
        ArrayList<OrdenLectura> resultado = new ArrayList<>();

        String filas[] = {_ID, "codigo", "posicion", "direccion", "direccion_entrega", "numero_poste", "fecha_carga",
                "fecha_propuesta", "fecha_asignacion","gps_latitud", "gps_longitud", "ajuste_sencillo_anterior",
                "ajuste_sencillo_actual", "instalacion_id", "medidor_id", "cliente_id", "ruta_id", "tipo_lectura_id",
                "estado_lectura_id", "tipo_tarifa_id", "tipo_entrega_id", "tipo_establecimiento_id", "nro_municipal",
                "folio_casa_cerrada", "factor_cobro_id", "autorizado_facturacion", "facturado", "flag_envio"};

        //Parametro de ordenes pendientes estado_lectura_id = 3.
        String[] args = new String[] {Integer.toString(3)};

        Cursor c = this.getReadableDatabase().query("ordenlectura", filas, "estado_lectura_id=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int posicion = c.getColumnIndex("posicion");
        int direccion = c.getColumnIndex("direccion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int numeroPoste = c.getColumnIndex("numero_poste");
        int fechaCarga = c.getColumnIndex("fecha_carga");
        int fechaPropuesta = c.getColumnIndex("fecha_propuesta");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_latitud");
        int gpsLongitud = c.getColumnIndex("gps_longitud");
        int ajusteSencilloAnterior = c.getColumnIndex("ajuste_sencillo_anterior");
        int ajusteSencilloActual = c.getColumnIndex("ajuste_sencillo_actual");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int medidorId = c.getColumnIndex("medidor_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaId = c.getColumnIndex("ruta_id");
        int tipoLecturaId = c.getColumnIndex("tipo_lectura_id");
        int estadoLecturaId = c.getColumnIndex("estado_lectura_id");
        int tipoTarifaId = c.getColumnIndex("tipo_tarifa_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int tipoEstablecimientoId = c.getColumnIndex("tipo_establecimiento_id");
        int nroMunicipal = c.getColumnIndex("nro_municipal");
        int folioCasaCerrada = c.getColumnIndex("folio_casa_cerrada");
        int factorCobroId = c.getColumnIndex("factor_cobro_id");
        int autorizadoFacturacion = c.getColumnIndex("autorizado_facturacion");
        int facturado = c.getColumnIndex("facturado");
        int flagEnvio = c.getColumnIndex("flag_envio");

        while(c.moveToNext())
        {
            resultado.add(new OrdenLectura(c.getInt(id), c.getInt(codigo), c.getInt(posicion), c.getString(direccion),
                        c.getString(direccionEntrega), c.getString(numeroPoste), c.getLong(fechaCarga),
                        c.getLong(fechaPropuesta), c.getLong(fechaAsignacion), c.getDouble(gpsLatitud),
                        c.getDouble(gpsLongitud), c.getDouble(ajusteSencilloAnterior), c.getDouble(ajusteSencilloActual),
                        c.getInt(instalacionId), c.getInt(medidorId), c.getInt(clienteId), c.getInt(rutaId), c.getInt(tipoLecturaId),
                        c.getInt(estadoLecturaId), c.getInt(tipoTarifaId), c.getInt(tipoEntregaId), c.getInt(tipoEstablecimientoId),
                        c.getInt(nroMunicipal), c.getInt(factorCobroId), c.getInt(folioCasaCerrada),
                        (c.getInt(autorizadoFacturacion) == 1 ? true : false), (c.getInt(facturado) == 1 ? true: false),
                        c.getInt(flagEnvio), leerDetalleOrdenLectura(c.getInt(id))));
        }

        c.close();
        return resultado;
    }

    /**
     * Busca una orden de lectura
     * @param idOrdenLectura id de orden a buscar
     * @return objeto de tipo orden de lectura
     */
    public OrdenLectura buscarOrden(int idOrdenLectura)
    {
        OrdenLectura orden = new OrdenLectura();

        String filas[] = {_ID, "codigo", "posicion", "direccion", "direccion_entrega", "numero_poste", "fecha_carga",
                "fecha_propuesta", "fecha_asignacion","gps_latitud", "gps_longitud", "ajuste_sencillo_anterior",
                "ajuste_sencillo_actual", "instalacion_id", "medidor_id", "cliente_id", "ruta_id", "tipo_lectura_id",
                "estado_lectura_id", "tipo_tarifa_id", "tipo_entrega_id", "tipo_establecimiento_id", "nro_municipal",
                "factor_cobro_id", "folio_casa_cerrada", "autorizado_facturacion", "facturado", "flag_envio"};
        String[] args = new String[] {Integer.toString(idOrdenLectura)};

        Cursor c = this.getReadableDatabase().query("ordenlectura", filas, _ID + "=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int posicion = c.getColumnIndex("posicion");
        int direccion = c.getColumnIndex("direccion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int numeroPoste = c.getColumnIndex("numero_poste");
        int fechaCarga = c.getColumnIndex("fecha_carga");
        int fechaPropuesta = c.getColumnIndex("fecha_propuesta");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_latitud");
        int gpsLongitud = c.getColumnIndex("gps_longitud");
        int ajusteSencilloAnterior = c.getColumnIndex("ajuste_sencillo_anterior");
        int ajusteSencilloActual = c.getColumnIndex("ajuste_sencillo_actual");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int medidorId = c.getColumnIndex("medidor_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaId = c.getColumnIndex("ruta_id");
        int tipoLecturaId = c.getColumnIndex("tipo_lectura_id");
        int estadoLecturaId = c.getColumnIndex("estado_lectura_id");
        int tipoTarifaId = c.getColumnIndex("tipo_tarifa_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int tipoEstablecimientoId = c.getColumnIndex("tipo_establecimiento_id");
        int nroMunicipal = c.getColumnIndex("nro_municipal");
        int factorCobroId = c.getColumnIndex("factor_cobro_id");
        int folioCasaCerrada = c.getColumnIndex("folio_casa_cerrada");
        int autorizadoFacturacion = c.getColumnIndex("autorizado_facturacion");
        int facturado = c.getColumnIndex("facturado");
        int flagEnvio = c.getColumnIndex("flag_envio");

        if(c.moveToNext())
        {
            orden.setId(c.getInt(id));
            orden.setCodigo(c.getInt(codigo));
            orden.setPosicion(c.getInt(posicion));
            orden.setDireccion(c.getString(direccion));
            orden.setDireccionEntrega(c.getString(direccionEntrega));
            orden.setNumeroPoste(c.getString(numeroPoste));
            orden.setFechaCarga(c.getLong(fechaCarga));
            orden.setFechaPropuesta(c.getLong(fechaPropuesta));
            orden.setFechaAsignacion(c.getLong(fechaAsignacion));
            orden.setGpsLatitud(c.getDouble(gpsLatitud));
            orden.setGpsLongitud(c.getDouble(gpsLongitud));
            orden.setAjusteSencilloAnterior(c.getDouble(ajusteSencilloAnterior));
            orden.setAjusteSencilloActual(c.getDouble(ajusteSencilloActual));
            orden.setInstalacionId(c.getInt(instalacionId));
            orden.setMedidorId(c.getInt(medidorId));
            orden.setClienteId(c.getInt(clienteId));
            orden.setRutaId(c.getInt(rutaId));
            orden.setTipoLecturaId(c.getInt(tipoLecturaId));
            orden.setEstadoLecturaId(c.getInt(estadoLecturaId));
            orden.setTipoTarifaId(c.getInt(tipoTarifaId));
            orden.setTipoEntregaId(c.getInt(tipoEntregaId));
            orden.setTipoEstablecimientoId(c.getInt(tipoEstablecimientoId));
            orden.setNroMunicipal(c.getInt(nroMunicipal));
            orden.setFactorCobroId(c.getInt(factorCobroId));
            orden.setFolioCasaCerrada(c.getInt(folioCasaCerrada));
            orden.setAutorizadoFacturacion(c.getInt(autorizadoFacturacion) == 1 ? true : false);
            orden.setFacturado(c.getInt(facturado) == 1 ? true: false);
            orden.setFlagEnvio(c.getInt(flagEnvio));
            orden.setNumeradores(leerDetalleOrdenLectura(c.getInt(id)));
        }

        c.close();
        return orden;
    }

    /**
     * Retorna un arreglo con una lista de ordenes que aun no se actualizan en el servidor.
     * @return Arreglo con una lista de ordenes que aun no se actualizan en el servidor.
     */
    public ArrayList<OrdenLectura> listaOrdenesSinEnviar()
    {
        ArrayList<OrdenLectura> resultado = new ArrayList<>();
        
        String filas[] = {_ID, "codigo", "posicion", "direccion", "direccion_entrega", "numero_poste", "fecha_carga",
                "fecha_propuesta", "fecha_asignacion","gps_latitud", "gps_longitud", "ajuste_sencillo_anterior",
                "ajuste_sencillo_actual", "instalacion_id", "medidor_id", "cliente_id", "ruta_id", "tipo_lectura_id",
                "estado_lectura_id", "tipo_tarifa_id", "tipo_entrega_id", "tipo_establecimiento_id", "nro_municipal",
                "folio_casa_cerrada", "factor_cobro_id", "autorizado_facturacion", "facturado", "flag_envio"};

        //Ordenes leidas sin enviar deben estar en estado_lectura_id = 4.
        String[] args = new String[] {"4"};
        
        Cursor c = this.getReadableDatabase().query("ordenlectura", filas, "estado_lectura_id=?", args, null, null, null);
        
        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int posicion = c.getColumnIndex("posicion");
        int direccion = c.getColumnIndex("direccion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int numeroPoste = c.getColumnIndex("numero_poste");
        int fechaCarga = c.getColumnIndex("fecha_carga");
        int fechaPropuesta = c.getColumnIndex("fecha_propuesta");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_latitud");
        int gpsLongitud = c.getColumnIndex("gps_longitud");
        int ajusteSencilloAnterior = c.getColumnIndex("ajuste_sencillo_anterior");
        int ajusteSencilloActual = c.getColumnIndex("ajuste_sencillo_actual");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int medidorId = c.getColumnIndex("medidor_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaId = c.getColumnIndex("ruta_id");
        int tipoLecturaId = c.getColumnIndex("tipo_lectura_id");
        int estadoLecturaId = c.getColumnIndex("estado_lectura_id");
        int tipoTarifaId = c.getColumnIndex("tipo_tarifa_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int tipoEstablecimientoId = c.getColumnIndex("tipo_establecimiento_id");
        int nroMunicipal = c.getColumnIndex("nro_municipal");
        int folioCasaCerrada = c.getColumnIndex("folio_casa_cerrada");
        int factorCobroId = c.getColumnIndex("factor_cobro_id");
        int autorizadoFacturacion = c.getColumnIndex("autorizado_facturacion");
        int facturado = c.getColumnIndex("facturado");
        int flagEnvio = c.getColumnIndex("flag_envio");
        
        while(c.moveToNext())
        {
            //El flag de envio indica si la orden esta pendiente(0) o enviada (1)
            if(c.getString(flagEnvio).equals("0"))
            {
                resultado.add(new OrdenLectura(c.getInt(id), c.getInt(codigo), c.getInt(posicion), c.getString(direccion),
                        c.getString(direccionEntrega), c.getString(numeroPoste), c.getLong(fechaCarga),
                        c.getLong(fechaPropuesta), c.getLong(fechaAsignacion), c.getDouble(gpsLatitud),
                        c.getDouble(gpsLongitud), c.getDouble(ajusteSencilloAnterior), c.getDouble(ajusteSencilloActual),
                        c.getInt(instalacionId), c.getInt(medidorId), c.getInt(clienteId), c.getInt(rutaId), c.getInt(tipoLecturaId),
                        c.getInt(estadoLecturaId), c.getInt(tipoTarifaId), c.getInt(tipoEntregaId), c.getInt(tipoEstablecimientoId),
                        c.getInt(nroMunicipal), c.getInt(factorCobroId), c.getInt(folioCasaCerrada),
                        (c.getInt(autorizadoFacturacion) == 1 ? true : false), (c.getInt(facturado) == 1 ? true: false),
                        c.getInt(flagEnvio),leerDetalleOrdenLectura(c.getInt(id))));
            }
        }
		
        c.close();
        return resultado;
    }

    /**
     * Retorna un arreglo con una lista de ordenes que aun no se ha ingresado lectura.
     * @return Arreglo con una lista de ordenes que aun no se actualizan en el servidor.
     */
    public ArrayList<OrdenLectura> listaOrdenesPendientes()
    {
        ArrayList<OrdenLectura> resultado = new ArrayList<>();

        String filas[] = {_ID, "codigo", "posicion", "direccion", "direccion_entrega", "numero_poste", "fecha_carga",
                "fecha_propuesta", "fecha_asignacion","gps_latitud", "gps_longitud", "ajuste_sencillo_anterior",
                "ajuste_sencillo_actual", "instalacion_id", "medidor_id", "cliente_id", "ruta_id", "tipo_lectura_id",
                "estado_lectura_id", "tipo_tarifa_id", "tipo_entrega_id", "tipo_establecimiento_id", "nro_municipal",
                "folio_casa_cerrada", "factor_cobro_id", "autorizado_facturacion", "facturado", "flag_envio"};

        //Ordenes sin lectura deben estar en estado_lectura_id = 3.
        String[] args = new String[] {"3"};

        Cursor c = this.getReadableDatabase().query("ordenlectura", filas, "estado_lectura_id=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int posicion = c.getColumnIndex("posicion");
        int direccion = c.getColumnIndex("direccion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int numeroPoste = c.getColumnIndex("numero_poste");
        int fechaCarga = c.getColumnIndex("fecha_carga");
        int fechaPropuesta = c.getColumnIndex("fecha_propuesta");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_latitud");
        int gpsLongitud = c.getColumnIndex("gps_longitud");
        int ajusteSencilloAnterior = c.getColumnIndex("ajuste_sencillo_anterior");
        int ajusteSencilloActual = c.getColumnIndex("ajuste_sencillo_actual");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int medidorId = c.getColumnIndex("medidor_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaId = c.getColumnIndex("ruta_id");
        int tipoLecturaId = c.getColumnIndex("tipo_lectura_id");
        int estadoLecturaId = c.getColumnIndex("estado_lectura_id");
        int tipoTarifaId = c.getColumnIndex("tipo_tarifa_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int tipoEstablecimientoId = c.getColumnIndex("tipo_establecimiento_id");
        int nroMunicipal = c.getColumnIndex("nro_municipal");
        int folioCasaCerrada = c.getColumnIndex("folio_casa_cerrada");
        int factorCobroId = c.getColumnIndex("factor_cobro_id");
        int autorizadoFacturacion = c.getColumnIndex("autorizado_facturacion");
        int facturado = c.getColumnIndex("facturado");
        int flagEnvio = c.getColumnIndex("flag_envio");

        while(c.moveToNext())
        {
            if(c.getString(flagEnvio).equals("0"))
            {
                resultado.add(new OrdenLectura(c.getInt(id), c.getInt(codigo), c.getInt(posicion), c.getString(direccion),
                        c.getString(direccionEntrega), c.getString(numeroPoste), c.getLong(fechaCarga),
                        c.getLong(fechaPropuesta), c.getLong(fechaAsignacion), c.getDouble(gpsLatitud),
                        c.getDouble(gpsLongitud), c.getDouble(ajusteSencilloAnterior), c.getDouble(ajusteSencilloActual),
                        c.getInt(instalacionId), c.getInt(medidorId), c.getInt(clienteId), c.getInt(rutaId), c.getInt(tipoLecturaId),
                        c.getInt(estadoLecturaId), c.getInt(tipoTarifaId), c.getInt(tipoEntregaId), c.getInt(tipoEstablecimientoId),
                        c.getInt(nroMunicipal), c.getInt(factorCobroId), c.getInt(folioCasaCerrada),
                        (c.getInt(autorizadoFacturacion) == 1 ? true : false), (c.getInt(facturado) == 1 ? true: false),
                        c.getInt(flagEnvio),leerDetalleOrdenLectura(c.getInt(id))));
            }
        }

        c.close();
        return resultado;
    }

    /**
     * Verifica si es que existe una orden de acuerdo a su id.
     * @param ordenId id de orden de lectura
     * @return boolean que indica si la orden existe dentro de la BD
     */
    public boolean existeOrdenLectura(int ordenId)
	{
		String filas[] = {_ID};
		String[] args = new String[] {Integer.toString(ordenId)};

		Cursor c = this.getReadableDatabase().query("ordenlectura", filas, _ID +"=?", args, null, null, null);
		if(c.moveToNext())
			return true;

		c.close();
		return false;
	}

    /**
     * Actualizar orden.
     *
     * @param orden Objeto de tipo OrdenLectura.
     */
    public void actualizarOrden(OrdenLectura orden)
    {
        ContentValues valores = new ContentValues();
        valores.put("estado_lectura_id", orden.getEstadoLecturaId());
        valores.put("gps_latitud", orden.getGpsLatitud());
        valores.put("gps_longitud", orden.getGpsLongitud());
        valores.put("folio_casa_cerrada", orden.getFolioCasaCerrada());
        valores.put("autorizado_facturacion", orden.getAutorizadoFacturacion());
        valores.put("facturado", orden.getFacturado());
        this.getWritableDatabase().update("ordenlectura", valores, _ID + "=" + orden.getId(), null);
    }

    /**
     * Elimina una orden de lectura dado un id
     * @param id id de orden a eliminar
     */
    public void eliminarOrden(int id)
    {
        this.getReadableDatabase().execSQL("DELETE FROM ordenlectura WHERE " + _ID + "=" + Integer.toString(id));
    }

    /**
     * Buscar ordenes por numero de medidor
     * modo busqueda -> 1 = busqueda de ordenes en estado 3
     *               -> 2 = busqueda de ordenes en estado 4 y facturadas
     *               -> 3 = busqueda de ordenes en estado 4 autorizadas a facturar
     */
    public ArrayList<OrdenLectura> listarOrdenesMedidor(String medidor, int modoBusqueda, String orderBy)
    {
        ArrayList<OrdenLectura> resultado = new ArrayList<>();

        //String filas[] = {_ID, "codigo", "posicion", "direccion", "direccion_entrega", "numero_poste", "fecha_carga",
        //"fecha_propuesta", "fecha_asignacion","gps_latitud", "gps_longitud", "ajuste_sencillo_anterior",
        //"ajuste_sencillo_actual", "instalacion_id", "medidor_id", "cliente_id", "ruta_id", "tipo_lectura_id",
        //"estado_lectura_id", "tipo_tarifa_id", "tipo_entrega_id", "tipo_establecimiento_id", "nro_municipal",
        //"factor_cobro_id", "flag_envio"};
        String[] args = new String[] {"%" + medidor + "%"};
        String query = "";
        switch (modoBusqueda) {
            case 1:
                query = "SELECT * FROM ordenlectura INNER JOIN medidor ON ordenlectura.medidor_id = medidor." +
                        _ID + " WHERE medidor.numero_medidor LIKE ? and ordenlectura.estado_lectura_id = 3 " + "ORDER BY " + orderBy;
                break;
            case 2:
                query = "SELECT * FROM ordenlectura INNER JOIN medidor ON ordenlectura.medidor_id = medidor." +
                        _ID + " WHERE medidor.numero_medidor LIKE ? and ordenlectura.facturado=1 " + "ORDER BY " + orderBy;
                break;
            case 3:
                query = "SELECT * FROM ordenlectura INNER JOIN medidor ON ordenlectura.medidor_id = medidor." +
                        _ID + " WHERE medidor.numero_medidor LIKE ? and ordenlectura.autorizado_facturacion = 1 and ordenlectura.facturado=0 "
                        + "ORDER BY " + orderBy;
                break;

        }

        Cursor c = this.getReadableDatabase().rawQuery(query, args);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int posicion = c.getColumnIndex("posicion");
        int direccion = c.getColumnIndex("direccion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int numeroPoste = c.getColumnIndex("numero_poste");
        int fechaCarga = c.getColumnIndex("fecha_carga");
        int fechaPropuesta = c.getColumnIndex("fecha_propuesta");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_latitud");
        int gpsLongitud = c.getColumnIndex("gps_longitud");
        int ajusteSencilloAnterior = c.getColumnIndex("ajuste_sencillo_anterior");
        int ajusteSencilloActual = c.getColumnIndex("ajuste_sencillo_actual");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int medidorId = c.getColumnIndex("medidor_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaId = c.getColumnIndex("ruta_id");
        int tipoLecturaId = c.getColumnIndex("tipo_lectura_id");
        int estadoLecturaId = c.getColumnIndex("estado_lectura_id");
        int tipoTarifaId = c.getColumnIndex("tipo_tarifa_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int tipoEstablecimientoId = c.getColumnIndex("tipo_establecimiento_id");
        int nroMunicipal = c.getColumnIndex("nro_municipal");
        int folioCasaCerrada = c.getColumnIndex("folio_casa_cerrada");
        int factorCobroId = c.getColumnIndex("factor_cobro_id");
        int autorizadoFacturacion = c.getColumnIndex("autorizado_facturacion");
        int facturado = c.getColumnIndex("facturado");
        int flagEnvio = c.getColumnIndex("flag_envio");

        while(c.moveToNext())
        {
            resultado.add(new OrdenLectura(c.getInt(id), c.getInt(codigo), c.getInt(posicion), c.getString(direccion),
                                           c.getString(direccionEntrega), c.getString(numeroPoste), c.getLong(fechaCarga),
                                           c.getLong(fechaPropuesta), c.getLong(fechaAsignacion), c.getDouble(gpsLatitud),
                                           c.getDouble(gpsLongitud), c.getDouble(ajusteSencilloAnterior),
                                           c.getDouble(ajusteSencilloActual), c.getInt(instalacionId), c.getInt(medidorId),
                                           c.getInt(clienteId), c.getInt(rutaId), c.getInt(tipoLecturaId), c.getInt(estadoLecturaId),
                                           c.getInt(tipoTarifaId), c.getInt(tipoEntregaId), c.getInt(tipoEstablecimientoId),
                                           c.getInt(nroMunicipal), c.getInt(factorCobroId), c.getInt(folioCasaCerrada),
                                           (c.getInt(autorizadoFacturacion) == 1 ? true : false),
                                           (c.getInt(facturado) == 1 ? true: false),
                                           c.getInt(flagEnvio), this.leerDetalleOrdenLectura(c.getInt(id))));
        }

        c.close();
        return resultado;
    }

    /**
     * Busca ordenes de lectura por la direccion de la orden
     * @param direccionBusqueda Direccion busqueda
     * @return Arreglo con todas las ordenes que contengan direcciones similares a lo buscado
     */
    public ArrayList<OrdenLectura> listarOrdenesDireccion(String direccionBusqueda, int modoBusqueda, String orderBy)
    {
        ArrayList<OrdenLectura> resultado = new ArrayList<>();

        String[] args = new String[] {"%" + direccionBusqueda + "%"};
        String query = "";
        switch (modoBusqueda) {
            case 1:
                query = "SELECT * FROM ordenlectura WHERE direccion LIKE ? and ordenlectura.estado_lectura_id = 3 " + "ORDER BY " + orderBy;
                break;
            case 2:
                query = "SELECT * FROM ordenlectura WHERE direccion LIKE ? and ordenlectura.facturado=1 " + "ORDER BY " + orderBy;
                break;
            case 3:
                query = "SELECT * FROM ordenlectura WHERE direccion LIKE ? and ordenlectura.autorizado_facturacion = 1 and ordenlectura.facturado=0 " + "ORDER BY " + orderBy;
                break;

        }

        Cursor c = this.getReadableDatabase().rawQuery(query, args);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int posicion = c.getColumnIndex("posicion");
        int direccion = c.getColumnIndex("direccion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int numeroPoste = c.getColumnIndex("numero_poste");
        int fechaCarga = c.getColumnIndex("fecha_carga");
        int fechaPropuesta = c.getColumnIndex("fecha_propuesta");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_latitud");
        int gpsLongitud = c.getColumnIndex("gps_longitud");
        int ajusteSencilloAnterior = c.getColumnIndex("ajuste_sencillo_anterior");
        int ajusteSencilloActual = c.getColumnIndex("ajuste_sencillo_actual");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int medidorId = c.getColumnIndex("medidor_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaId = c.getColumnIndex("ruta_id");
        int tipoLecturaId = c.getColumnIndex("tipo_lectura_id");
        int estadoLecturaId = c.getColumnIndex("estado_lectura_id");
        int tipoTarifaId = c.getColumnIndex("tipo_tarifa_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int tipoEstablecimientoId = c.getColumnIndex("tipo_establecimiento_id");
        int nroMunicipal = c.getColumnIndex("nro_municipal");
        int folioCasaCerrada = c.getColumnIndex("folio_casa_cerrada");
        int factorCobroId = c.getColumnIndex("factor_cobro_id");
        int autorizadoFacturacion = c.getColumnIndex("autorizado_facturacion");
        int facturado = c.getColumnIndex("facturado");
        int flagEnvio = c.getColumnIndex("flag_envio");

        while(c.moveToNext())
        {
            resultado.add(new OrdenLectura(c.getInt(id), c.getInt(codigo), c.getInt(posicion), c.getString(direccion),
                                           c.getString(direccionEntrega), c.getString(numeroPoste), c.getLong(fechaCarga),
                                           c.getLong(fechaPropuesta), c.getLong(fechaAsignacion), c.getDouble(gpsLatitud),
                                           c.getDouble(gpsLongitud), c.getDouble(ajusteSencilloAnterior),
                                           c.getDouble(ajusteSencilloActual), c.getInt(instalacionId), c.getInt(medidorId),
                                           c.getInt(clienteId), c.getInt(rutaId), c.getInt(tipoLecturaId), c.getInt(estadoLecturaId),
                                           c.getInt(tipoTarifaId), c.getInt(tipoEntregaId), c.getInt(tipoEstablecimientoId),
                                           c.getInt(nroMunicipal), c.getInt(factorCobroId), c.getInt(folioCasaCerrada),
                                           (c.getInt(autorizadoFacturacion) == 1 ? true : false),
                                           (c.getInt(facturado) == 1 ? true: false),
                                           c.getInt(flagEnvio), this.leerDetalleOrdenLectura(c.getInt(id))));
        }

        c.close();
        return resultado;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                               DETALLE ORDENE DE LECTURA                                    //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Inserta un detalle de orden en la base de datos.
     * @param detalle
     */
    public void insertarDetalleOrdenlectura(DetalleOrdenLectura detalle)
    {
        if(!this.existeDetalleOrdenLectura(detalle)) {
            ContentValues valores = new ContentValues();
            valores.put(_ID, detalle.getId());
            valores.put("orden_lectura_id", detalle.getOrdenLecturaId());
            valores.put("numerador_id", detalle.getNumeradorId());
            valores.put("lectura_anterior", detalle.getLecturaAnterior());
            valores.put("lectura_promedio", detalle.getLecturaPromedio());
            valores.put("lectura_actual", detalle.getLecturaActual());
            valores.put("rango_superior", detalle.getRangoSuperior());
            valores.put("rango_inferior", detalle.getRangoInferior());
            valores.put("fecha_ejecucion", detalle.getFechaEjecucion());
            valores.put("clave_lectura_id", detalle.getClaveLecturaId());
            valores.put("observacion_id", detalle.getObservacionId());
            valores.put("clave_lectura_anterior", detalle.getClaveLecturaAnterior());
            valores.put("m3_acumulados", detalle.getM3Acumulados());

            this.getWritableDatabase().insert("detalleordenlectura", null, valores);
        }
    }

    /**
     * Verifica la existencia de un detalle de orden de lectura en la base de datos.
     * @param detalle objeto de tipo detalle
     * @return boolean
     */
    public boolean existeDetalleOrdenLectura(DetalleOrdenLectura detalle){
        String filas[] = {_ID};
        String[] args = new String[] {Integer.toString(detalle.getId())};

        Cursor c = this.getReadableDatabase().query("detalleordenlectura", filas, _ID +"=?", args, null, null, null);
        if(c.moveToNext())
            return true;

        c.close();
        return false;
    }

    /**
     * Retorna todos los detalles(Numeradores) de una orden de lectura
     * @param ordenId id de orden de la que se quieren obtener los detalles.
     * @return
     */
    public ArrayList<DetalleOrdenLectura> leerDetalleOrdenLectura(int ordenId)
    {
        ArrayList<DetalleOrdenLectura> resultado = new ArrayList<>();

        String filas[] = {_ID, "orden_lectura_id", "numerador_id", "lectura_anterior", "lectura_promedio",
                          "lectura_actual", "rango_superior", "rango_inferior", "fecha_ejecucion",
                          "clave_lectura_id","observacion_id", "clave_lectura_anterior",
                           "clave_lectura_anterior_2", "clave_lectura_anterior_3",  "m3_acumulados"};

        String[] args = new String[] {Integer.toString(ordenId)};

        Cursor c = this.getReadableDatabase().query("detalleordenlectura", filas, "orden_lectura_id=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int ordenLecturaId = c.getColumnIndex("orden_lectura_id");
        int numeradorId = c.getColumnIndex("numerador_id");
        int lecturaAnterior = c.getColumnIndex("lectura_anterior");
        int lecturaPromedio = c.getColumnIndex("lectura_promedio");
        int lecturaActual = c.getColumnIndex("lectura_actual");
        int rangoSuperior = c.getColumnIndex("rango_superior");
        int rangoInferior = c.getColumnIndex("rango_inferior");
        int fechaEjecucion = c.getColumnIndex("fecha_ejecucion");
        int claveLecturaId = c.getColumnIndex("clave_lectura_id");
        int observacionId = c.getColumnIndex("observacion_id");
        int claveLecturaAnteriorId = c.getColumnIndex("clave_lectura_anterior");
        int claveLecturaAnteriorId2 = c.getColumnIndex("clave_lectura_anterior_2");
        int claveLecturaAnteriorId3 = c.getColumnIndex("clave_lectura_anterior_3");
        int m3Acumulados = c.getColumnIndex("m3_acumulados");

        while(c.moveToNext())
        {
            resultado.add(new DetalleOrdenLectura(c.getInt(id), c.getInt(ordenLecturaId), c.getInt(numeradorId),
                                                  c.getDouble(lecturaAnterior), c.getDouble(lecturaPromedio),
                                                  c.getDouble(lecturaActual), c.getDouble(rangoSuperior),
                                                  c.getDouble(rangoInferior), c.getLong(fechaEjecucion),
                                                  c.getInt(claveLecturaId), c.getInt(observacionId),
                                                  c.getString (claveLecturaAnteriorId), c.getString (claveLecturaAnteriorId2),
                                                  c.getString (claveLecturaAnteriorId3),  c.getDouble(m3Acumulados),
                                                  this.verIntentos(c.getInt(id)), this.verFotografias(c.getInt(id))));
        }

        c.close();
        return resultado;
    }

    /**
     * Elimina detalle dado un id
     * @param id id de detalle a eliminar
     */
    public void eliminarDetalles(int id)
    {
        this.getReadableDatabase().execSQL("DELETE FROM detalleordenlectura WHERE orden_lectura_id=" + Integer.toString(id));
    }

    /**
     * Actualiza un registro de detalle de lectura.
     * @param detalle Objeto del tipo DetalleOrdenLectura
     */
    public void actualizarDetalleOrden(DetalleOrdenLectura detalle)
    {
        ContentValues valores = new ContentValues();
        valores.put("lectura_actual", detalle.getLecturaActual());
        valores.put("fecha_ejecucion", detalle.getFechaEjecucion());
        valores.put("clave_lectura_id", detalle.getClaveLecturaId());
        System.out.println ("Observacion BD: " + detalle.getObservacionId ());
        valores.put("observacion_id", detalle.getObservacionId());
        this.getWritableDatabase().update("detalleordenlectura", valores, _ID + "=" + detalle.getId(), null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                      CLAVES DE LECTURA                                     //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Insertar clave.
     *
     * @param claveLectura Objeto del tipo ClaveLectura
     */
    public void insertarClave(ClaveLectura claveLectura)
    {
        ContentValues valores  = new ContentValues();
        valores.put(_ID, claveLectura.getId());
        valores.put("clave", claveLectura.getClave());
        valores.put("codigo", claveLectura.getCodigo());
        valores.put("num_fotografias", claveLectura.getNumFotografias());
        valores.put("descripcion_corta", claveLectura.getDescripcionCorta());
        valores.put("tipo_cobro_id", claveLectura.getTipoCobroId());
        valores.put("lectura_requerida", claveLectura.getLecturaRequerida());

        this.getWritableDatabase().insert("clavelectura", null, valores);
    }

    /**
     * Retorna arreglo con las claves de lectura existentes.
     *
     * @return the array list
     */
    public ArrayList<ClaveLectura> leerClaves()
    {
        ArrayList<ClaveLectura> resultado = new ArrayList<>();

        String filas[] = {_ID, "clave", "codigo", "num_fotografias", "descripcion_corta", "tipo_cobro_id",
                         "lectura_requerida"};

        Cursor c = this.getReadableDatabase().query("clavelectura", filas,null, null, null, null, null);

        int id= c.getColumnIndex(_ID);
        int clave= c.getColumnIndex("clave");
        int codigo = c.getColumnIndex("codigo");
        int numFotografias = c.getColumnIndex("num_fotografias");
        int descripcionCorta = c.getColumnIndex("descripcion_corta");
        int tipoCobroId = c.getColumnIndex("tipo_cobro_id");
        int lecturaRequerida = c.getColumnIndex("lectura_requerida");

        boolean requerida = false;

        while(c.moveToNext())
        {
            if(c.getInt(lecturaRequerida) == 1)
                requerida = true;

            resultado.add(new ClaveLectura(c.getInt(id),c.getString(clave),c.getString(codigo),
                                           c.getInt(numFotografias), c.getString(descripcionCorta),
                                           c.getInt(tipoCobroId), requerida,this.leerObservaciones(c.getInt(id))));

            requerida = false;
        }

        c.close();
        return resultado;
    }

    /**
     * Eliminar claves lectura.
     */
    public void eliminarClavesLectura()
    {
        this.getReadableDatabase().execSQL("DELETE FROM clavelectura");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                    OBSERVACIONES DE LECTURA                                //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Insertar observacion.
     *
     * @param observacion Objeto del tipo Observacion de lectura
     */
    public void insertarObservacion(Observacion observacion)
    {
        ContentValues valores  = new ContentValues();
        valores.put(_ID, observacion.getId());
        valores.put("descripcion", observacion.getDescripcion());
        valores.put("clave_lectura_id", observacion.getClaveLecturaId());

        this.getWritableDatabase().insert("observacion", null, valores);
    }

    /**
     * Retorna arreglo con lista de observaciones de lectura que pertenecen a una clave.
     *
     * @return the array list
     */
    private ArrayList<Observacion> leerObservaciones(int idBusqueda)
    {
        ArrayList<Observacion> resultado = new ArrayList<>();

        String filas[] = {_ID, "descripcion", "clave_lectura_id"};

        Cursor c = this.getReadableDatabase().query("observacion", filas, "clave_lectura_id = " + idBusqueda, null, null, null, null);

        int id= c.getColumnIndex(_ID);
        int descripcion = c.getColumnIndex("descripcion");
        int clave_lectura = c.getColumnIndex("clave_lectura_id");

        //resultado.add(new Observacion(0, "-- Seleccione Observación --", idBusqueda));
        while(c.moveToNext())
        {
            resultado.add(new Observacion(c.getInt(id),c.getString(descripcion), c.getInt(clave_lectura)));
        }

        c.close();
        return resultado;
    }

    /**
     * Elimina todas las observaciones de lectura en la base de datos.
     */
    public void eliminarObservaciones()
    {
        this.getReadableDatabase().execSQL("DELETE FROM observacion");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                  PARAMETROS SERVIDOR                                       //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Insertar parametros.
     *
     * @param parametros the parametros
     */
    public void insertarParametros(ParametrosServidor parametros)
    {
        ContentValues valores = new ContentValues();
        valores.put(_ID, parametros.getId());
        valores.put("ip", parametros.getIp());
        valores.put("intervalo", parametros.getIntervalo());
        this.getWritableDatabase().insert("parametros", null, valores);
    }

    /**
     * Actualizar parametros.
     *
     * @param parametros the parametros
     */
    public void actualizarParametros(ParametrosServidor parametros)
    {
        ContentValues valores = new ContentValues();
        valores.put("ip", parametros.getIp());
        valores.put("intervalo", parametros.getIntervalo());

        this.getWritableDatabase().update("parametros", valores, _ID + "=" + parametros.getId(), null);
    }

    /**
     * Buscar parametros parametros servidor.
     *
     * @param idBusqueda the id busqueda
     * @return the parametros servidor
     */
    public ParametrosServidor buscarParametros(int idBusqueda)
    {
        ParametrosServidor resultado= null;
        String[] campos = new String[] {_ID, "ip", "intervalo"};

        Cursor c = this.getReadableDatabase().query("parametros", campos, _ID + "=" + idBusqueda, null, null, null, null);
        int id, ip, intervalo;

        id= c.getColumnIndex(_ID);
        ip= c.getColumnIndex("ip");
        intervalo= c.getColumnIndex("intervalo");

        while(c.moveToNext())
        {
            resultado = new ParametrosServidor(Integer.parseInt(c.getString(id)),c.getString(ip),Integer.parseInt(c.getString(intervalo)));
        }

        c.close();

        return resultado;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                   PERFILES DE USUARIO                                      //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Insertar perfil.
     *
     * @param id     the id
     * @param nombre the nombre
     * @param imagen the imagen
     */
    public void insertarPerfil(int id, String nombre, String imagen)
    {
        ContentValues valores = new ContentValues();
        valores.put(_ID, id);
        valores.put("nombre", nombre);
        valores.put("imagen", imagen);
        this.getWritableDatabase().insert("perfiles", null, valores);
    }

    /**
     * Elimina todos los perfiles en la base de datos.
     */
    public void eliminarPerfiles()
    {
        this.getReadableDatabase().execSQL("DELETE FROM perfiles");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                         DATOS EQUIPO                                       //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Insertar equipo.
     *
     * @param id     the id
     * @param nombre the nombre
     * @param mac    the mac
     */
    public void insertarEquipo(int id, String nombre, String mac)
    {
        ContentValues valores = new ContentValues();
        valores.put(_ID, id);
        valores.put("nombre", nombre);
        valores.put("mac", mac);
        this.getWritableDatabase().insert("equipos", null, valores);
    }

    /**
     * Elimina equipos.
     */
    public void eliminarEquipos()
    {
        this.getReadableDatabase().execSQL("DELETE FROM equipos");
    }

    /**
     * Busca un equipo en la base de datos de acuerdo a la mac
     * @param macEquipo
     * @return
     */
    public boolean buscarEquipoMac(String macEquipo)
    {
        String[] campos = new String[] {_ID, "nombre", "mac"};
        String[] args = new String[] {macEquipo};

        Cursor c = this.getReadableDatabase().query("equipos", campos, "mac=?", args, null, null, null);

        int mac = c.getColumnIndex("mac");


        while(c.moveToNext())
        {
            if(c.getString(mac).equals(macEquipo))
            {
                c.close();
                return true;
            }

        }

        c.close();
        return false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                           FOTOGRAFIA                                       //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Guarda registro en tabla de fotografias.
     * @param fotografia Objeto del tipo fotografia.
     */
	public void insertarFotografia(Fotografia fotografia)
	{
		ContentValues valores = new ContentValues();
		valores.put("detalle_orden_lectura_id", fotografia.getDetalleOrdenLecturaId());
		valores.put("archivo", fotografia.getRuta());
		valores.put("descripcion", fotografia.getObservacion());
        valores.put("flag_envio", fotografia.getFlagEnvio());
		this.getWritableDatabase().insert("fotografia", null, valores);
		
	}

    /**
     * Actualiza registro en tabla de fotografias.
     * @param fotografia Objeto del tipo fotografia
     */
    public void actualizarFotografia(Fotografia fotografia)
    {
        ContentValues valores = new ContentValues();
        valores.put("detalle_orden_lectura_id", fotografia.getDetalleOrdenLecturaId());
        valores.put("archivo", fotografia.getRuta());
        valores.put("descripcion", fotografia.getObservacion());
        valores.put("flag_envio", fotografia.getFlagEnvio());

        this.getWritableDatabase().update("fotografia", valores, _ID + "=" + fotografia.getId(), null);
    }

    /**
     * Retorna todas las fotografias correspondientes a un detalle de lectura.
     * @param idDetalle Id Detalle orden
     * @return Retorna arreglo de fotografias correspondiente a un Detalle de orden de lectura
     */
	private ArrayList<Fotografia> verFotografias(int idDetalle)
	{
		ArrayList<Fotografia> resultado = new ArrayList<>();
		
		String campos[] = {_ID, "detalle_orden_lectura_id", "archivo", "descripcion", "flag_envio"};
		String[] args = new String[] {Integer.toString(idDetalle)};
		
		Cursor c = this.getReadableDatabase().query("fotografia", campos, "detalle_orden_lectura_id=?", args, null, null, null);

		int id = c.getColumnIndex(_ID);
		int detalleOrdenLecturaId = c.getColumnIndex("detalle_orden_lectura_id");
		int archivo = c.getColumnIndex("archivo");
		int descripcion = c.getColumnIndex("descripcion");
        int flagEnvio = c.getColumnIndex("flag_envio");
		
		while(c.moveToNext())
		{
			resultado.add(new Fotografia(c.getInt(id), c.getInt(detalleOrdenLecturaId), c.getString(archivo),
                                         c.getString(descripcion), c.getInt(flagEnvio)));
		}

		c.close();
		return resultado;
	}


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                             INTENTOS                                       //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Inserta registro en tabla intentos.
     * @param intento Objeto del tipo Intento.
     */
    public void insertarIntento(Intento intento)
    {
        ContentValues valores = new ContentValues();
        valores.put("detalle_orden_lectura_id", intento.getIdDetalleOrden());
        valores.put("lectura", intento.getLectura());
        valores.put("flag_envio", intento.getFlagEnvio());
        this.getWritableDatabase().insert("intento", null, valores);

    }

    /**
     * Actualiza registro de intento.
     * @param intento Objeto del tipo intento.
     */
    public void actualizarIntento(Intento intento)
    {
        ContentValues valores = new ContentValues();
        valores.put("lectura", intento.getLectura());
        valores.put("flag_envio", intento.getFlagEnvio());
        this.getWritableDatabase().update("intento", valores, _ID + "=" + intento.getId(), null);
    }

    /**
     * Retorna arreglo con intentos para un detalle de orden de lectura.
     * @param idDetalleOrdenLectura Id detalle orden de lectura
     * @return Arreglo con intentos ingresados para ese detalle de orden.
     */
    private ArrayList<Intento> verIntentos(int idDetalleOrdenLectura)
    {
        ArrayList<Intento> resultado = new ArrayList<>();
		
        String campos[] = {_ID, "detalle_orden_lectura_id", "lectura", "flag_envio"};
		String[] args = new String[] {Integer.toString(idDetalleOrdenLectura)};
    
        Cursor c = this.getReadableDatabase().query("intento", campos, "detalle_orden_lectura_id=?", args, null, null, null);
    
        int id = c.getColumnIndex(_ID);
        int detalleOrdenLecturaId = c.getColumnIndex("detalle_orden_lectura_id");
        int lectura = c.getColumnIndex("lectura");
        int flagEnvio = c.getColumnIndex("flag_envio");
    
        while(c.moveToNext())
        {
            resultado.add(new Intento(c.getInt(id), c.getInt(detalleOrdenLecturaId), c.getDouble(lectura), c.getInt(flagEnvio)));
        }
    
        c.close();
        return resultado;
    }

    /**
     * Elimina intentos de un numerador.
     * @param id id de numerador al cual se le eliminaran los intentos
     */
    public void eliminarIntentos(int id)
    {
        this.getReadableDatabase().execSQL("DELETE FROM intento WHERE detalle_orden_lectura_id=" + Integer.toString(id));
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Registra los parametros de la impresora
     * @param parametros
     */
    public void insertarParametrosImpresora(ParametrosImpresora parametros)
    {
        ContentValues valores = new ContentValues();
        valores.put("mac", parametros.getMac());
        valores.put("flag_facturacion", parametros.getFlagImpresion());
        this.getWritableDatabase().insert("parametrosimpresora", null, valores);

    }

    /**
     * Actualiza los parametros de la impresora
     * @param parametros
     */
    public void actualizarParametrosImpresora(ParametrosImpresora parametros)
    {
        ContentValues valores = new ContentValues();
        valores.put("mac", parametros.getMac());
        valores.put("flag_facturacion", parametros.getFlagImpresion());

        this.getWritableDatabase().update("parametrosimpresora", valores, _ID + "=" + parametros.getId(), null);
    }

    /**
     * Retorna parametros de impresora dado un id
     * @param idBusqueda id a buscar(El valor siempre debe ser 1)
     * @return objeto de tipo Parametros Impresora
     */
    public ParametrosImpresora buscarParametrosImpresora(int idBusqueda)
    {
        ParametrosImpresora resultado= null;
        String[] campos = new String[] {_ID, "mac", "flag_facturacion"};

        Cursor c = this.getReadableDatabase().query("parametrosimpresora", campos, _ID + "=" + idBusqueda, null, null, null, null);

        int id = c.getColumnIndex(_ID);
        int mac = c.getColumnIndex("mac");
        int flagImpresion = c.getColumnIndex("flag_facturacion");

        while(c.moveToNext())
        {
            if (c.getInt(flagImpresion) == 1)
                resultado = new ParametrosImpresora(c.getInt(id),c.getString(mac), true);

            else
                resultado = new ParametrosImpresora(c.getInt(id),c.getString(mac), false);
        }

        c.close();

        return resultado;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                      TABLAS REPARTO                                        //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Inserta una ruta de reparto a la base de datos
     * @param ruta
     */
    public void insertarRutaReparto(RutaReparto ruta)
    {
        ContentValues valores = new ContentValues();
        valores.put(_ID, ruta.getId());
        valores.put("codigo", ruta.getCodigo());
        valores.put("nombre", ruta.getNombre());
        valores.put("usuario", ruta.getUsuario());
        this.getReadableDatabase().insert("rutareparto", null, valores);
    }

    /**
     * Inserta una orden de reparto a la base de datos
     * @param orden
     */
    public boolean insertarOrdenReparto(OrdenReparto orden)
    {
        ContentValues valores = new ContentValues();
        valores.put(_ID, orden.getId());
        valores.put("numero_interno", orden.getNumero_interno());
        valores.put("numero_boleta", orden.getNumero_boleta());
        valores.put("tipo_documento_id", orden.getTipoDocumento().getId());
        valores.put("fecha_emision", orden.getFechaEmision());
        valores.put("fecha_vencimiento", orden.getFechaVencimiento());
        valores.put("fecha_entrega", orden.getFechaEntrega());
        valores.put("total_pago", orden.getTotalPago());
        valores.put("orden_ruta", orden.getOrdenRuta());
        valores.put("correlativo_impresion", orden.getCorrelativoImpresion());
        valores.put("direccion_entrega", orden.getDireccionEntrega());
        valores.put("comuna_id", orden.getComuna());
        valores.put("cliente_id", orden.getCliente().getId());
        valores.put("ruta_reparto_id", orden.getRuta().getId());
        valores.put("estado_reparto_id", orden.getEstado());
        valores.put("tipo_reparto_id", orden.getTipoReparto());
        valores.put("tipo_entrega_id", orden.getTipoEntrega());
        valores.put("instalacion_id", orden.getInstalacion().getId());
        valores.put("fecha_asignacion", orden.getFechaAsignacion());
        valores.put("gps_longitud", orden.getGpsLongitud());
        valores.put("gps_latitud", orden.getGpsLatitud());
        valores.put("flag_envio", 0);
        if(this.getReadableDatabase().insert("ordenreparto", null, valores) != -1)
            return true;

        return false;
    }

    /**
     * Inserta un estado de reparto a la base de datos
     * @param estado
     */
    public void insertarEstadoReparto(EstadoReparto estado)
    {
        ContentValues valores = new ContentValues();
        valores.put(_ID, estado.getId());
        valores.put("estado", estado.getEstado());
        this.getReadableDatabase().insert("estadoreparto", null, valores);
    }

    /**
     * Inserta un tipo de reparto a la base de datos
     * @param tipo
     */
    public void insertarTipoReparto(TipoReparto tipo)
    {
        ContentValues valores = new ContentValues();
        valores.put(_ID, tipo.getId());
        valores.put("descripcion", tipo.getDescripcion());
        this.getReadableDatabase().insert("tiporeparto", null, valores);
    }

    /**
     * Inserta un tipo de entrega a la base de datos
     * @param tipo
     */
    public void insertarTipoEntrega(TipoEntrega tipo)
    {
        ContentValues valores = new ContentValues();
        valores.put(_ID, tipo.getId());
        valores.put("codigo", tipo.getCodigo());
        valores.put("nombre", tipo.getNombre());
        valores.put("facturacion_en_terreno", tipo.isFacturacionEnTerreno() ? 1 : 0);
        this.getReadableDatabase().insert("tipo_entrega", null, valores);
    }

    /**
     * Verifica existencia de una orden de reparto dado su id
     * @param ordenId
     * @return
     */
    public boolean existeOrdenReparto(int ordenId)
    {
        String filas[] = {_ID};
        String[] args = new String[] {Integer.toString(ordenId)};

        Cursor c = this.getReadableDatabase().query("ordenreparto", filas, _ID +"=?", args, null, null, null);
        if(c.moveToNext())
            return true;

        c.close();
        return false;
    }

    public TipoDocumento buscaTipoDocumento(int idTipoDocumento)
    {
        TipoDocumento tipoDocumento = new TipoDocumento();
        String filas[] = {_ID};
        String[] args = new String[] {Integer.toString(idTipoDocumento)};

        Cursor c = this.getReadableDatabase().query("tipo_documentos", filas, _ID +"=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigoSii = c.getColumnIndex("codigo_sii");
        int nombre = c.getColumnIndex("nombre");

        if(c.moveToNext())
        {
            tipoDocumento.setId(c.getInt(id));
            tipoDocumento.setCodigoSii(c.getString(codigoSii));
            tipoDocumento.setNombre(c.getString(nombre));
        }
        c.close();

        return tipoDocumento;
    }

    public ArrayList<RutaReparto> leerRutasReparto(int usr)
    {
        ArrayList<RutaReparto> resultado = new ArrayList<>();

        String filas[] = {_ID, "codigo", "nombre", "usuario"};
        String[] args = new String[] {Integer.toString(usr)};

        Cursor c = this.getReadableDatabase().query("rutareparto", filas,"usuario=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int nombre = c.getColumnIndex("nombre");
        int usuario = c.getColumnIndex("usuario");

        while(c.moveToNext())
        {
            RutaReparto ruta = new RutaReparto(c.getInt(id),c.getString(codigo),
                    c.getString(nombre), c.getInt(usuario), contarOrdenesReparto(c.getInt(id)));

            //Se muestran solo las rutas con un numero de ordenes pendientes mayor a cero.
            if (ruta.getNumeroOrdenes() != 0)
                resultado.add(ruta);
        }

        c.close();
        return resultado;
    }

    private int contarOrdenesReparto(int id){
        SQLiteStatement statement = this.getReadableDatabase().compileStatement(
                  "select count(*) from ordenreparto where ruta_reparto_id='" +
                            Integer.toString(id) + "' and estado_reparto_id = '3'");

        return (int) (long) statement.simpleQueryForLong();
    }

    public OrdenReparto buscarOrdenReparto(String codigoCliente)
    {
        OrdenReparto resultado= null;
        String[] campos = new String[] {_ID, "numero_interno", "numero_boleta", "tipo_documento_id",
                                       "fecha_emision", "fecha_vencimiento", "fecha_entrega",
                                       "total_pago", "orden_ruta", "correlativo_impresion",
                                       "direccion_entrega", "comuna_id", "cliente_id",
                                       "ruta_reparto_id", "estado_reparto_id", "tipo_reparto_id",
                                       "tipo_entrega_id", "instalacion_id", "fecha_asignacion",
                                       "gps_longitud", "gps_latitud", "flag_envio"};

        String[] args = new String[] {codigoCliente};

        String query = query = "SELECT * FROM ordenreparto INNER JOIN cliente ON ordenreparto.cliente_id = cliente." +
                _ID + " WHERE cliente.numero_cliente = ? and ordenreparto.flag_envio = 0";
        Cursor c = this.getReadableDatabase().rawQuery(query, args);

        int id = c.getColumnIndex(_ID);
        int numeroInterno = c.getColumnIndex("numero_interno");
        int numeroBoleta = c.getColumnIndex("numero_boleta");
        int tipoDocumentoId = c.getColumnIndex("tipo_documento_id");
        int fechaEmision = c.getColumnIndex("fecha_emision");
        int fechaVencimiento = c.getColumnIndex("fecha_vencimiento");
        int fechaEntrega = c.getColumnIndex("fecha_entrega");
        int totalPago = c.getColumnIndex("total_pago");
        int ordenRuta = c.getColumnIndex("orden_ruta");
        int correlativoImpresion = c.getColumnIndex("correlativo_impresion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int comunaId = c.getColumnIndex("comuna_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaRepartoId = c.getColumnIndex("ruta_reparto_id");
        int estadoRepartoId = c.getColumnIndex("estado_reparto_id");
        int tipoRepartoId = c.getColumnIndex("tipo_reparto_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_longitud");
        int gpsLongitud = c.getColumnIndex("gps_latitud");
        int flagEnvio = c.getColumnIndex("flag_envio");


        while(c.moveToNext())
        {
            resultado = new OrdenReparto(c.getInt(id), c.getInt(numeroInterno), c.getInt(numeroBoleta),
                    this.buscaTipoDocumento(c.getInt(tipoDocumentoId)), c.getLong(fechaEmision),
                    c.getLong(fechaVencimiento), c.getLong(fechaEntrega), c.getInt(totalPago),
                    c.getInt(ordenRuta), c.getInt(correlativoImpresion), c.getString(direccionEntrega),
                    c.getInt(comunaId), this.buscarCliente(c.getInt(clienteId)),
                    this.buscaRutaReparto(c.getString(rutaRepartoId)), c.getInt(estadoRepartoId),
                    c.getInt(tipoRepartoId), c.getInt(tipoEntregaId), this.buscarInstalacion(c.getInt(instalacionId)),
                    c.getLong(fechaAsignacion), c.getDouble(gpsLatitud), c.getDouble(gpsLongitud), c.getInt(flagEnvio));
        }

        c.close();
        return resultado;
    }

    /**
     * Busca ruta de acuerdo al ID
     * @param rutaId Id de ruta
     * @return Objeto Ruta con todos los datos del id buscado
     */
    public RutaReparto buscaRutaReparto(String rutaId)
    {
        RutaReparto resultado = null;
        String[] campos = new String[] {_ID, "codigo", "nombre", "usuario"};
        String[] args = new String[] {rutaId};

        Cursor c = this.getReadableDatabase().query("rutareparto", campos, _ID + "=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int codigo = c.getColumnIndex("codigo");
        int nombre = c.getColumnIndex("nombre");
        int usuario = c.getColumnIndex("usuario");

        while(c.moveToNext())
        {
            if(c.getString(id).equals(rutaId))
            {
                resultado = new RutaReparto(c.getInt(id), c.getString(codigo), c.getString(nombre), c.getInt(usuario),
                        numeroOrdenesRuta(c.getInt(id)));
            }
        }

        c.close();
        return resultado;
    }

    /**
     * Actualizar orden.
     *
     * @param orden Objeto de tipo OrdenReparto.
     */
    public void actualizaOrdenReparto(OrdenReparto orden)
    {
        ContentValues valores = new ContentValues();
        valores.put("estado_reparto_id", orden.getEstado());
        valores.put("gps_latitud", orden.getGpsLatitud());
        valores.put("gps_longitud", orden.getGpsLongitud());
        valores.put("fecha_entrega", orden.getFechaEntrega());
        this.getWritableDatabase().update("ordenreparto", valores, _ID + "=" + orden.getId(), null);
    }

    /**
     * Retorna un arreglo con una lista de ordenes que aun no se actualizan en el servidor.
     * @return Arreglo con una lista de ordenes que aun no se actualizan en el servidor.
     */
    public ArrayList<OrdenReparto> listaOrdenesRepartoSinEnviar()
    {
        ArrayList<OrdenReparto> resultado = new ArrayList<>();

        String[] filas = new String[] {_ID, "numero_interno", "numero_boleta", "tipo_documento_id",
                "fecha_emision", "fecha_vencimiento", "fecha_entrega",
                "total_pago", "orden_ruta", "correlativo_impresion",
                "direccion_entrega", "comuna_id", "cliente_id",
                "ruta_reparto_id", "estado_reparto_id", "tipo_reparto_id",
                "tipo_entrega_id", "instalacion_id", "fecha_asignacion",
                "gps_longitud", "gps_latitud", "flag_envio"};

        //Ordenes leidas sin enviar deben estar en estado_lectura_id = 4.
        String[] args = new String[] {"4"};

        Cursor c = this.getReadableDatabase().query("ordenreparto", filas, "estado_reparto_id=?", args, null, null, null);

        int id = c.getColumnIndex(_ID);
        int numeroInterno = c.getColumnIndex("numero_interno");
        int numeroBoleta = c.getColumnIndex("numero_boleta");
        int tipoDocumentoId = c.getColumnIndex("tipo_documento_id");
        int fechaEmision = c.getColumnIndex("fecha_emision");
        int fechaVencimiento = c.getColumnIndex("fecha_vencimiento");
        int fechaEntrega = c.getColumnIndex("fecha_entrega");
        int totalPago = c.getColumnIndex("total_pago");
        int ordenRuta = c.getColumnIndex("orden_ruta");
        int correlativoImpresion = c.getColumnIndex("correlativo_impresion");
        int direccionEntrega = c.getColumnIndex("direccion_entrega");
        int comunaId = c.getColumnIndex("comuna_id");
        int clienteId = c.getColumnIndex("cliente_id");
        int rutaRepartoId = c.getColumnIndex("ruta_reparto_id");
        int estadoRepartoId = c.getColumnIndex("estado_reparto_id");
        int tipoRepartoId = c.getColumnIndex("tipo_reparto_id");
        int tipoEntregaId = c.getColumnIndex("tipo_entrega_id");
        int instalacionId = c.getColumnIndex("instalacion_id");
        int fechaAsignacion = c.getColumnIndex("fecha_asignacion");
        int gpsLatitud = c.getColumnIndex("gps_longitud");
        int gpsLongitud = c.getColumnIndex("gps_latitud");
        int flagEnvio = c.getColumnIndex("flag_envio");

        while(c.moveToNext())
        {
            //El flag de envio indica si la orden esta pendiente(0) o enviada (1)
            if(c.getString(flagEnvio).equals("0"))
            {
                resultado.add(new OrdenReparto(c.getInt(id), c.getInt(numeroInterno), c.getInt(numeroBoleta),
                        this.buscaTipoDocumento(c.getInt(tipoDocumentoId)), c.getLong(fechaEmision),
                        c.getLong(fechaVencimiento), c.getLong(fechaEntrega), c.getInt(totalPago),
                        c.getInt(ordenRuta), c.getInt(correlativoImpresion), c.getString(direccionEntrega),
                        c.getInt(comunaId), this.buscarCliente(c.getInt(clienteId)),
                        this.buscaRutaReparto(c.getString(rutaRepartoId)), c.getInt(estadoRepartoId),
                        c.getInt(tipoRepartoId), c.getInt(tipoEntregaId), this.buscarInstalacion(c.getInt(instalacionId)),
                        c.getLong(fechaAsignacion), c.getDouble(gpsLatitud), c.getDouble(gpsLongitud), c.getInt(flagEnvio)));
            }
        }

        c.close();
        return resultado;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                           METODOS PARA MANEJAR LA BASE DE DATOS                            //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Abre conexion a la base de datos SQLite.
     */
    public void abrir()
    {
		try
        {
			this.getWritableDatabase();
		}
		catch (SQLiteCantOpenDatabaseException e)
		{
			e.printStackTrace();
		}
    }

    /**
     * Cierra conexion a la base de datos SQLite.
     */
    public void cerrar()
    {
        this.close();
    }


}
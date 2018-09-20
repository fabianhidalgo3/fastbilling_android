package cl.jfcor.fastbilling;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.ArrayList;
import java.util.Date;

import base_datos.Bd;
import modelos.ClaveLectura;
import modelos.Cliente;
import modelos.Instalacion;
import modelos.Intento;
import modelos.Medidor;
import modelos.Observacion;
import modelos.OrdenLectura;
import modelos.Ruta;
import modelos.Usuario;

public class OrdenLecturaFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public final static int CAMARA = 1;
    public final static int FACTURA = 2;
    private EditText txtLectura;
    private ArrayList<OrdenLectura> ordenes;
    private Spinner claves;
    private Spinner observaciones;
    private OrdenLectura ordenLectura;
    private int tipoLectura;
    private Ruta ruta;
    private int numerador = 0;
    private Bd bd;
    private int posicion = 0;
    private static boolean disponiblepGPS, disponibleRED;
    private static LocationManager locManager;
    private static String provider;
    private static final String TAG = "Clientes Pendientes";
    private static int contador;
    public static boolean fueraRango;
    public static boolean observacionFotografia = false;
    private int pantalla;



    //TODO: Mover funciones de impresion a otra vista.
    //TODO: Revisar intent result en fragment.
    //TODO: Refactorizar, Documentar.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate (R.layout.activity_orden_lectura, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        //Obtener argumentos
        Bundle args = getArguments ();

        this.ordenLectura = (OrdenLectura) args.getSerializable ("orden");
        this.posicion = args.getInt ("posicion");
        this.ruta = (Ruta) args.getSerializable("ruta");
        this.tipoLectura = args.getInt("tipoLectura");
        String orden = args.getString ("ordendireccion" );

        this.bd.abrir ();
        this.ordenes = this.bd.leerOrdenes(this.ruta.getId(), 1, orden);

        Cliente cliente = this.bd.buscarCliente (this.ordenLectura.getClienteId ());
        Medidor medidor = this.bd.buscarMedidor (this.ordenLectura.getMedidorId ());
        Instalacion instalacion = this.bd.buscarInstalacion (this.ordenLectura.getInstalacionId ());

        TextView txtRuta = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_ruta);
        TextView txtNumCliente = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numCliente);
        TextView txtNomCliente = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_nomCliente);
        TextView txtdireccion = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_direccion);
        TextView txtNumMedidor = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numMedidor);
        TextView txtNotaLectura = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_notaLectura);
        TextView txtNumerador = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numerador);
        TextView txtTipoCliente = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_tipoCliente);

        this.txtLectura = (EditText) this.getActivity ().findViewById (R.id.ordenLectura_lectura);

        ImageButton grabar = (ImageButton) this.getActivity ().findViewById (R.id.ordenLectura_grabar);
        grabar.setOnClickListener (this);

        ImageButton btnNext = (ImageButton) this.getView().findViewById(R.id.ordenLectura_siguiente);
        btnNext.setOnClickListener (this);

        ImageButton btnPre = (ImageButton) this.getView().findViewById(R.id.ordenLectura_volver);
        btnPre.setOnClickListener(this);

        txtRuta.setText (ruta.getCodigo ());
        txtNumCliente.setText (cliente.getNumero_cliente ());
        txtNomCliente.setText (cliente.getNombreCompleto ());
        txtdireccion.setText (cliente.getDireccion ());
        if (medidor != null)
            txtNumMedidor.setText (medidor.getNumeroMedidor ());

        // txtNumInstalacion.setText(instalacion.getCodigo());

        //Creo Array con listado de claves de lectura
        ArrayList<ClaveLectura> claves = this.bd.leerClaves ();
        this.claves = (Spinner) this.getActivity ().findViewById (R.id.spinner_claves);
        this.claves.setAdapter (new SpinAdapterClaves (this.getContext (), android.R.layout.simple_spinner_dropdown_item, claves));
        this.claves.setOnItemSelectedListener (this);
        //Selecciona una clave
        ClaveLectura clave = (ClaveLectura) this.claves.getSelectedItem ();
        this.observaciones = (Spinner) this.getActivity ().findViewById (R.id.spinner_observaciones);
        this.observaciones.setAdapter (new SpinAdapterObservaciones (this.getContext (), android.R.layout.simple_spinner_dropdown_item, clave.getObservaciones ()));
        contador = 0;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach (activity);
        this.bd = Bd.getInstance (activity);
    }

    @Override
    public void onClick(View view) {
        this.bd.abrir ();
        // Switch Botones de Orden Lectura..
        switch (view.getId()) {
            // Boton Siguiente Lectura..
            case R.id.ordenLectura_siguiente:
                this.posicion ++;
                if (ordenes.size () > this.posicion){
                    this.ordenLectura = (OrdenLectura) ordenes.get (this.posicion);
                }else {
                    this.posicion = ordenes.size () - 1;
                    this.ordenLectura = (OrdenLectura) ordenes.get (this.posicion);

                }
                Cliente cliente = this.bd.buscarCliente (this.ordenLectura.getClienteId ());
                Medidor medidor = this.bd.buscarMedidor (this.ordenLectura.getMedidorId ());
                Instalacion instalacion = this.bd.buscarInstalacion (this.ordenLectura.getInstalacionId ());

                TextView txtRuta = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_ruta);
                TextView txtNumCliente = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numCliente);
                TextView txtNomCliente = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_nomCliente);
                TextView txtdireccion = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_direccion);
                TextView txtNumMedidor = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numMedidor);
                TextView txtNotaLectura = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_notaLectura);
                TextView txtNumerador = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numerador);
                TextView txtTipoCliente = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_tipoCliente);

                this.txtLectura = (EditText) this.getActivity ().findViewById (R.id.ordenLectura_lectura);

                ImageButton grabar = (ImageButton) this.getActivity ().findViewById (R.id.ordenLectura_grabar);
                grabar.setOnClickListener (this);

                ImageButton btnNext = (ImageButton) this.getView().findViewById(R.id.ordenLectura_siguiente);
                btnNext.setOnClickListener (this);

                ImageButton btnPre = (ImageButton) this.getView().findViewById(R.id.ordenLectura_volver);
                btnPre.setOnClickListener(this);

                txtRuta.setText (this.ruta.getCodigo ());
                txtNumCliente.setText (cliente.getNumero_cliente ());
                txtNomCliente.setText (cliente.getNombreCompleto ());
                txtdireccion.setText (cliente.getDireccion ());
                if (medidor != null)
                    txtNumMedidor.setText (medidor.getNumeroMedidor ());

                //Creo Array con listado de claves de lectura
                ArrayList<ClaveLectura> claves = this.bd.leerClaves ();
                this.claves = (Spinner) this.getActivity ().findViewById (R.id.spinner_claves);
                this.claves.setAdapter (new SpinAdapterClaves (this.getContext (), android.R.layout.simple_spinner_dropdown_item, claves));
                this.claves.setOnItemSelectedListener (this);
                //Selecciona una clave
                ClaveLectura claveS = (ClaveLectura) this.claves.getSelectedItem ();
                this.observaciones = (Spinner) this.getActivity ().findViewById (R.id.spinner_observaciones);
                this.observaciones.setAdapter (new SpinAdapterObservaciones (this.getContext (), android.R.layout.simple_spinner_dropdown_item, claveS.getObservaciones ()));
                contador = 0;
                break;
            // Boton Lectura Anterior
            case R.id.ordenLectura_volver:
                Bundle params = getArguments ();

                if (this.posicion < 0){
                    this.posicion = 0;
                    this.ordenLectura = (OrdenLectura) ordenes.get (this.posicion);
                }else{
                    this.ordenLectura= (OrdenLectura) ordenes.get (this.posicion --);
                }

                Cliente clientePre = this.bd.buscarCliente (this.ordenLectura.getClienteId ());
                Medidor medidorPre = this.bd.buscarMedidor (this.ordenLectura.getMedidorId ());
                Instalacion instalacionPre = this.bd.buscarInstalacion (this.ordenLectura.getInstalacionId ());
                TextView txtRutaPre = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_ruta);
                TextView txtNumClientePre = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numCliente);
                TextView txtNomClientePre = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_nomCliente);
                TextView txtdireccionPre = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_direccion);
                TextView txtNumMedidorPre = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numMedidor);
                TextView txtNotaLecturaPre = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_notaLectura);
                TextView txtNumeradorPre = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numerador);
                TextView txtTipoClientePre = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_tipoCliente);

                this.txtLectura = (EditText) this.getActivity ().findViewById (R.id.ordenLectura_lectura);

                ImageButton grabarPre = (ImageButton) this.getActivity ().findViewById (R.id.ordenLectura_grabar);
                grabarPre.setOnClickListener (this);

                ImageButton btnNextPre = (ImageButton) this.getView().findViewById(R.id.ordenLectura_siguiente);
                btnNextPre.setOnClickListener (this);

                ImageButton btnPrePre = (ImageButton) this.getView().findViewById(R.id.ordenLectura_volver);
                btnPrePre.setOnClickListener(this);

                txtRutaPre.setText (this.ruta.getCodigo ());
                txtNumClientePre.setText (clientePre.getNumero_cliente ());
                txtNomClientePre.setText (clientePre.getNombreCompleto ());
                txtdireccionPre.setText (clientePre.getDireccion ());
                if (medidorPre != null)
                    txtNumMedidorPre.setText (medidorPre.getNumeroMedidor ());

                //Creo Array con listado de claves de lectura..
                ArrayList<ClaveLectura> clavesL = this.bd.leerClaves ();
                this.claves = (Spinner) this.getActivity ().findViewById (R.id.spinner_claves);
                this.claves.setAdapter (new SpinAdapterClaves (this.getContext (), android.R.layout.simple_spinner_dropdown_item, clavesL));
                this.claves.setOnItemSelectedListener (this);
                //Selecciona una clave...
                ClaveLectura claveSp = (ClaveLectura) this.claves.getSelectedItem ();
                this.observaciones = (Spinner) this.getActivity ().findViewById (R.id.spinner_observaciones);
                this.observaciones.setAdapter (new SpinAdapterObservaciones (this.getContext (), android.R.layout.simple_spinner_dropdown_item, claveSp.getObservaciones ()));
                contador = 0;
                break;

            case R.id.ordenLectura_grabar:
                final ClaveLectura clave = (ClaveLectura) this.claves.getSelectedItem ();
                final Observacion observacion = (Observacion) this.observaciones.getSelectedItem ();

                //Se valida ingreso de observacion
                if (this.validarObservacion (observacion)) {
                    //Se valida si observacion ingresada corresponde a casa cerrada o no permite.
                    if (this.validaCasaCerrada (observacion)) {
                        Intent intent = new Intent(view.getContext(), CaptureActivity.class);
                        intent.setAction("com.google.zxing.client.android.SCAN");
                        intent.putExtra("SAVE_HISTORY", false);
                        startActivityForResult(intent, 0);
                        //Solicitud de ingreso folio casa cerrada
                        /**
                        final String[] m_Text = new String[1];
                        AlertDialog.Builder builder = new AlertDialog.Builder (this.getContext ());
                        builder.setTitle ("Ingrese número de folio...");

                        final EditText input = new EditText (this.getContext ());
                        input.setInputType (InputType.TYPE_CLASS_NUMBER);
                        builder.setView (input);

                        // Set up the buttons
                        builder.setPositiveButton ("Guardar", new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text[0] = input.getText ().toString ();
                                ordenLectura.setFolioCasaCerrada (Integer.parseInt (m_Text[0]));
                                guardarLectura (0.0, clave, observacion);
                                //Guardar lectura
                                //Tomar fotografias si corresponde
                                //getActivity().finish();
                            }
                        });
                        builder.setNegativeButton ("Cancelar", new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel ();
                            }
                        });
                        builder.show ();**/
                    } else {
                        //Proceso normal de toma de lectura
                        if (validarLectura (this.txtLectura.getText ().toString (), observacion)) {
                            //Se valida que se ingreso lectura para claves en que esta es requerida
                            //Se obtiene lectura ingresada
                            //En caso de que la casilla este vacia se setea lectura = 0.0
                            double lectura;
                            try {
                                lectura = Double.parseDouble (this.txtLectura.getText ().toString ());
                            } catch (java.lang.NumberFormatException e) {
                                lectura = 0.0;
                            }

                            //Se guarda intento
                            this.bd.abrir ();
                            this.bd.insertarIntento (new Intento (0, this.ordenLectura.getNumeradores ().get (numerador).getId (), lectura, 0));

                            //Validar si lectura dentro de rango
                            if (!this.ordenLectura.getNumeradores ().get (numerador).fueraDeRango (lectura, observacion)) {
                                //Lectura dentro de rango o con 2 intentos con la misma lectura.
                                //Guardar lectura
                                //Tomar fotografias en caso de clave id != 1
                                this.guardarLectura (lectura, clave, observacion);
                            } else {
                                //Lectura fuera de rango.
                                AlertDialog.Builder dialogo = new AlertDialog.Builder (this.getContext ());
                                dialogo.setMessage ("Lectura Fuera de Rango");
                                dialogo.setMessage (this.ordenLectura.getNumeradores ().get (numerador).getMensajeFueraDeRango () + " , n° intentos " + Integer.toString (this.ordenLectura.getNumeradores ().get (numerador).getIntentos ()));
                                dialogo.setCancelable (true);
                                dialogo.setPositiveButton ("Aceptar", new DialogInterface.OnClickListener () {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel ();
                                    }
                                });
                                AlertDialog alerta = dialogo.create ();
                                alerta.show ();
                            }
                        } else {
                            AlertDialog.Builder dialogo = new AlertDialog.Builder (this.getContext ());
                            dialogo.setMessage ("Lectura invalida");
                            dialogo.setMessage ("Se debe ingresar una lectura");
                            dialogo.setCancelable (true);
                            dialogo.setPositiveButton ("Aceptar", new DialogInterface.OnClickListener () {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel ();
                                }
                            });
                            AlertDialog alerta = dialogo.create ();
                            alerta.show ();
                        }
                    }
                } else {
                    //TODO: Quizas esto se podria realizar en una funcion
                    AlertDialog.Builder dialogo = new AlertDialog.Builder (this.getContext ());
                    dialogo.setMessage ("Observación invalida");
                    dialogo.setMessage ("Se debe seleccionar una observación");
                    dialogo.setCancelable (true);
                    dialogo.setPositiveButton ("Aceptar", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel ();
                        }
                    });
                    AlertDialog alerta = dialogo.create ();
                    alerta.show ();
                }


                this.txtLectura.setText ("");
                break;
        }
    }


    /**
     * Valida si observacion ingresada corresponde a una de casa cerrada o no permite
     *
     * @param observacion Observacion seleccionada en spinner de observaciones
     * @return boolean
     */
    private boolean validaCasaCerrada(Observacion observacion) {
        if (observacion.getId () == 24 || (observacion.getId () == 25)) {
            return true;
        }else{
            return false;
        }
    }


    /**
     * Valida que se ingreso lectura para claves en que es requerida.
     *
     * @param v Valor del campo de lectura
     * @param observacion Observación de lectura seleccionada en spinner
     * @return boolean
     */
    private boolean validarLectura(String v, Observacion observacion) {
        if (!observacion.isLecturaRequerida () && v.isEmpty ())
            return true;
        else if (!v.isEmpty ()) {
            return true;
        }
        return false;
    }

    /**
     * Valida que se ingreso lectura para claves en que es requerida.
     *
     * @param v Valor del campo de lectura
     * @param observacion Observación de lectura seleccionada en spinner
     * @return boolean
     */

    private boolean validarFotografiaRequerida(Observacion observacion) {
        if (observacion.isFoto ()) {
            return true;
        }else{
            return false;
        }

    }

    /**
     * Valida que se haya ingresado observacion.
     *
     * @param observacion Observacion seleccionada en spinner
     * @return boolean
     */
    private boolean validarObservacion(Observacion observacion) {
        return true;
    }

    public void siguiente(){
       pantalla = 2;
    }

    public void volver(){
        pantalla= 0;
    }

    /**
     * Metodo encargado de guardar todos los datos de la lectura ingresada con fotografia
     *
     * @param lectura     Lectura ingresada por usuario
     * @param clave       Clave seleccionada en spinner
     * @param observacion Observacion seleccionada en spinner
     */

    private int valida = 0;
    private void guardarLectura(double lectura, ClaveLectura clave, Observacion observacion) {
        //Obtiene posicion gps al momento de guardar lectura
        String locationProvider = LocationManager.GPS_PROVIDER;
        LocationManager location = (LocationManager) this.getActivity ().getSystemService (Context.LOCATION_SERVICE);
        Location lastKnownLocation = location.getLastKnownLocation(locationProvider);
        // Si es diferente de nulo se envia la posicion si no envia 0..
        if (lastKnownLocation != null) {
            this.ordenLectura.setGpsLatitud (lastKnownLocation.getLatitude ());
            this.ordenLectura.setGpsLongitud (lastKnownLocation.getLongitude ());
        }
        else{
            this.ordenLectura.setGpsLatitud (0.0);
            this.ordenLectura.setGpsLongitud (0.0);
        }

        //Se actualizan los datos del numerador
        this.ordenLectura.getNumeradores ().get (numerador).setLecturaActual (lectura);
        this.ordenLectura.getNumeradores ().get (numerador).setClaveLecturaId (clave.getId ());
        this.ordenLectura.getNumeradores ().get (numerador).setObservacionId (observacion.getId ());
        this.ordenLectura.getNumeradores ().get (numerador).setFechaEjecucion (new Date ().getTime ());

        this.bd.abrir ();
        this.bd.actualizarDetalleOrden (this.ordenLectura.getNumeradores ().get (numerador));
        fueraRango = this.ordenLectura.getNumeradores ().get (numerador).fueraDeRango (lectura, observacion);
        //Guardar datos de detalle de orden en la base de datos
        this.ordenLectura.getNumeradores ().get (numerador).setClaveLecturaId (clave.getId ());
        //A este estado se debe pasar solo cuando sea el ultimo numerador el ingresado,
        //si no es el ultimo se debe pasar al siguiente numerador
        if (this.ordenLectura.getNumeradores ().size () == (numerador + 1)) {
            // Valida la Cantidad de intentos y si esta fuera de rango
            if(this.ordenLectura.getNumeradores ().get(numerador).getIntentos () >= 2 || clave.getId () != 1) {
                // se toma fotografia

                System.out.println ("Fuera de Rago:.... " + fueraRango);
                System.out.print ("Valida foto" + this.validarFotografiaRequerida (observacion));
                if(fueraRango || this.validarFotografiaRequerida (observacion)){
                    Toast.makeText (this.getContext (), "Foto", Toast.LENGTH_SHORT).show ();
                    Fragment fragment = null;
                    try {
                        fragment = CamaraFragment.class.newInstance ();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace ();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace ();
                    }

                    //Parametros que se pasan al fragment
                    Bundle args = new Bundle ();
                    args.putSerializable ("claveLectura", clave);
                    args.putSerializable ("observacion", observacion);
                    args.putSerializable ("detalle", this.ordenLectura.getNumeradores ().get (numerador));
                    args.putSerializable ("ruta", this.ruta);
                    args.putSerializable ("posicion", this.posicion ++);
                    args.putSerializable ("tipoLetura", 1);
                    String orden = args.getString ("ordendireccion" );
                    args.putSerializable ("ordendireccion", orden);

                    fragment.setArguments (args);

                    android.support.v4.app.FragmentManager fragmentManager = this.getActivity ().getSupportFragmentManager ();
                    fragmentManager.beginTransaction ().replace (R.id.flContent, fragment).commit ();
                }
                else{
                    if(valida == 0 ) {
                        valida ++;
                        this.txtLectura.setText ("");
                    }
                    else{
                        //this.getActivity().onBackPressed();
                    }


                }

            }
            this.ordenLectura.setEstadoLecturaId(4);
            this.bd.abrir();
            this.bd.actualizarOrden(this.ordenLectura);
            Bundle args = getArguments ();

            String orden = args.getString ("ordendireccion" );

            this.bd.abrir ();
            this.ordenes = this.bd.leerOrdenes(this.ruta.getId(), 1, orden);
            this.ordenLectura= (OrdenLectura) ordenes.get (this.posicion ++);
            Cliente cliente = this.bd.buscarCliente (this.ordenLectura.getClienteId ());
            Medidor medidor = this.bd.buscarMedidor (this.ordenLectura.getMedidorId ());
            Instalacion instalacion = this.bd.buscarInstalacion (this.ordenLectura.getInstalacionId ());

            TextView txtRuta = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_ruta);
            TextView txtNumCliente = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numCliente);
            TextView txtNomCliente = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_nomCliente);
            TextView txtdireccion = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_direccion);
            TextView txtNumMedidor = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numMedidor);
            TextView txtNotaLectura = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_notaLectura);
            TextView txtNumerador = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_numerador);
            TextView txtTipoCliente = (TextView) this.getActivity ().findViewById (R.id.ordenLectura_tipoCliente);

            this.txtLectura = (EditText) this.getActivity ().findViewById (R.id.ordenLectura_lectura);

            ImageButton grabar = (ImageButton) this.getActivity ().findViewById (R.id.ordenLectura_grabar);
            grabar.setOnClickListener (this);

            ImageButton btnNext = (ImageButton) this.getView().findViewById(R.id.ordenLectura_siguiente);
            btnNext.setOnClickListener (this);

            ImageButton btnPre = (ImageButton) this.getView().findViewById(R.id.ordenLectura_volver);
            btnPre.setOnClickListener(this);

            txtRuta.setText (ruta.getCodigo ());
            txtNumCliente.setText (cliente.getNumero_cliente ());
            txtNomCliente.setText (cliente.getNombreCompleto ());
            txtdireccion.setText (cliente.getDireccion ());
            if (medidor != null)
                txtNumMedidor.setText (medidor.getNumeroMedidor ());

            // txtNumInstalacion.setText(instalacion.getCodigo());

            //Creo Array con listado de claves de lectura
            ArrayList<ClaveLectura> claves = this.bd.leerClaves ();
            this.claves = (Spinner) this.getActivity ().findViewById (R.id.spinner_claves);
            this.claves.setAdapter (new SpinAdapterClaves (this.getContext (), android.R.layout.simple_spinner_dropdown_item, claves));
            this.claves.setOnItemSelectedListener (this);
            //Selecciona una clave
            ClaveLectura claveSpiner = (ClaveLectura) this.claves.getSelectedItem ();
            this.observaciones = (Spinner) this.getActivity ().findViewById (R.id.spinner_observaciones);
            this.observaciones.setAdapter (new SpinAdapterObservaciones (this.getContext (), android.R.layout.simple_spinner_dropdown_item, clave.getObservaciones ()));
            contador = 0;
        }
    }

    //Selecciona la Observación de Lectura...
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(contador == 0){
            contador ++;
        }else
        {
            ClaveLectura clave = (ClaveLectura) parent.getSelectedItem ();
            this.observaciones.setAdapter (new SpinAdapterObservaciones (this.getContext (), android.R.layout.simple_spinner_item, clave.getObservaciones ()));
            this.observaciones.performClick ();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}

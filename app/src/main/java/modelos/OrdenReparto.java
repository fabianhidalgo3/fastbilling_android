package modelos;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by brayan on 02-11-17.
 */

public class OrdenReparto implements Serializable
{
    private int id;
    private int numero_interno;
    private int numero_boleta;
    private TipoDocumento tipoDocumento;
    private long fechaEmision;
    private long fechaVencimiento;
    private long fechaEntrega;
    private int totalPago;
    private int ordenRuta;
    private int correlativoImpresion;
    private String direccionEntrega;
    private int comuna;
    private Cliente cliente;
    private RutaReparto ruta;
    private int estado;
    private int tipoReparto;
    private int tipoEntrega;
    private Instalacion instalacion;
    private long fechaAsignacion;
    private double gpsLatitud;
    private double gpsLongitud;
    private int flagEnvio;


    public OrdenReparto(int id, int numero_interno, int numero_boleta, TipoDocumento tipoDocumento,
                        long fechaEmision, long fechaVencimiento, long fechaEntrega, int totalPago,
                        int ordenRuta, int correlativoImpresion, String direccionEntrega, int comuna,
                        Cliente cliente, RutaReparto ruta, int estado, int tipoReparto,
                        int tipoEntrega, Instalacion instalacion, long fechaAsignacion, double gpsLatitud,
                        double gpsLongitud, int flagEnvio)
    {
        this.id = id;
        this.numero_interno = numero_interno;
        this.numero_boleta = numero_boleta;
        this.tipoDocumento = tipoDocumento;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaEntrega = fechaEntrega;
        this.totalPago = totalPago;
        this.ordenRuta = ordenRuta;
        this.correlativoImpresion = correlativoImpresion;
        this.direccionEntrega = direccionEntrega;
        this.comuna = comuna;
        this.cliente = cliente;
        this.ruta = ruta;
        this.estado = estado;
        this.tipoReparto = tipoReparto;
        this.tipoEntrega = tipoEntrega;
        this.instalacion = instalacion;
        this.fechaAsignacion = fechaAsignacion;
        this.gpsLatitud = gpsLatitud;
        this.gpsLongitud = gpsLongitud;
        this.flagEnvio = flagEnvio;
    }

    public OrdenReparto(int id, int numero_interno, int numero_boleta, TipoDocumento tipoDocumento,
                        long fechaEmision, long fechaVencimiento, int totalPago, int ordenRuta,
                        int correlativoImpresion, String direccionEntrega, int comuna,
                        Cliente cliente, RutaReparto ruta, int estado,
                        int tipoReparto, int tipoEntrega,
                        Instalacion instalacion, long fechaAsignacion) {
        this.id = id;
        this.numero_interno = numero_interno;
        this.numero_boleta = numero_boleta;
        this.tipoDocumento = tipoDocumento;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.totalPago = totalPago;
        this.ordenRuta = ordenRuta;
        this.correlativoImpresion = correlativoImpresion;
        this.direccionEntrega = direccionEntrega;
        this.comuna = comuna;
        this.cliente = cliente;
        this.ruta = ruta;
        this.estado = estado;
        this.tipoReparto = tipoReparto;
        this.tipoEntrega = tipoEntrega;
        this.instalacion = instalacion;
        this.fechaAsignacion = fechaAsignacion;
    }

    public String parseFechaEntrega(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = dateFormatter.format(this.fechaEntrega);
        return fecha;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getNumero_interno()
    {
        return numero_interno;
    }

    public void setNumero_interno(int numero_interno)
    {
        this.numero_interno = numero_interno;
    }

    public int getNumero_boleta()
    {
        return numero_boleta;
    }

    public void setNumero_boleta(int numero_boleta)
    {
        this.numero_boleta = numero_boleta;
    }

    public TipoDocumento getTipoDocumento()
    {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento)
    {
        this.tipoDocumento = tipoDocumento;
    }

    public long getFechaEmision()
    {
        return fechaEmision;
    }

    public void setFechaEmision(long fechaEmision)
    {
        this.fechaEmision = fechaEmision;
    }

    public long getFechaVencimiento()
    {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(long fechaVencimiento)
    {
        this.fechaVencimiento = fechaVencimiento;
    }

    public long getFechaEntrega()
    {
        return fechaEntrega;
    }

    public void setFechaEntrega(long fechaEntrega)
    {
        this.fechaEntrega = fechaEntrega;
    }

    public int getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(int totalPago)
    {
        this.totalPago = totalPago;
    }

    public int getOrdenRuta()
    {
        return ordenRuta;
    }

    public void setOrdenRuta(int ordenRuta)
    {
        this.ordenRuta = ordenRuta;
    }

    public int getCorrelativoImpresion()
    {
        return correlativoImpresion;
    }

    public void setCorrelativoImpresion(int correlativoImpresion)
    {
        this.correlativoImpresion = correlativoImpresion;
    }

    public String getDireccionEntrega()
    {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega)
    {
        this.direccionEntrega = direccionEntrega;
    }

    public int getComuna()
    {
        return comuna;
    }

    public void setComuna(int comuna)
    {
        this.comuna = comuna;
    }

    public Cliente getCliente()
    {
        return cliente;
    }

    public void setCliente(Cliente cliente)
    {
        this.cliente = cliente;
    }

    public RutaReparto getRuta()
    {
        return ruta;
    }

    public void setRuta(RutaReparto ruta)
    {
        this.ruta = ruta;
    }

    public int getEstado()
    {
        return estado;
    }

    public void setEstado(int estado)
    {
        this.estado = estado;
    }

    public int getTipoReparto()
    {
        return tipoReparto;
    }

    public void setTipoReparto(int tipoReparto)
    {
        this.tipoReparto = tipoReparto;
    }

    public int getTipoEntrega()
    {
        return tipoEntrega;
    }

    public void setTipoEntrega(int tipoEntrega)
    {
        this.tipoEntrega = tipoEntrega;
    }

    public Instalacion getInstalacion()
    {
        return instalacion;
    }

    public void setInstalacion(Instalacion instalacion)
    {
        this.instalacion = instalacion;
    }

    public long getFechaAsignacion()
    {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(long fechaAsignacion)
    {
        this.fechaAsignacion = fechaAsignacion;
    }

    public double getGpsLatitud()
    {
        return gpsLatitud;
    }

    public void setGpsLatitud(double gpsLatitud)
    {
        this.gpsLatitud = gpsLatitud;
    }

    public double getGpsLongitud()
    {
        return gpsLongitud;
    }

    public void setGpsLongitud(double gpsLongitud)
    {
        this.gpsLongitud = gpsLongitud;
    }

    public int getFlagEnvio() {
        return flagEnvio;
    }

    public void setFlagEnvio(int flagEnvio) {
        this.flagEnvio = flagEnvio;
    }
}

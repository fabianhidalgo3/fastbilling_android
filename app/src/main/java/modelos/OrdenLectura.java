package modelos;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by brayan on 24-11-16.
 */

public class OrdenLectura implements Serializable
{
    private int id;
    private int codigo;
    private int posicion;
    private String direccion;
    private String direccionEntrega;
    private String numeroPoste;
    private Long fechaCarga;
    private Long fechaPropuesta;
    private Long fechaAsignacion;
    private double gpsLatitud;
    private double gpsLongitud;
    private double ajusteSencilloAnterior;
    private double ajusteSencilloActual;
    private int instalacionId;
    private int medidorId;
    private int clienteId;
    private int rutaId;
    private int tipoLecturaId;
    private int estadoLecturaId;
    private int tipoTarifaId;
    private int tipoEntregaId;
    private int tipoEstablecimientoId;
    private int nroMunicipal;
    private int factorCobroId;
    private int folioCasaCerrada;
    private boolean autorizadoFacturacion;
    private boolean facturado;
    private int flagEnvio;
    private ArrayList<DetalleOrdenLectura> numeradores;

    public OrdenLectura(){}

    public OrdenLectura(int id, int codigo, int posicion, String direccion, String direccionEntrega,
                        String numeroPoste, Long fechaCarga, Long fechaPropuesta, Long fechaAsignacion,
                        double gpsLatitud, double gpsLongitud, double ajusteSencilloAnterior,
                        double ajusteSencilloActual, int instalacionId, int medidorId, int clienteId,
                        int rutaId, int tipoLecturaId, int estadoLecturaId, int tipoTarifaId,
                        int tipoEntregaId, int tipoEstablecimientoId, int nroMunicipal,
                        int factorCobroId, int folioCasaCerrada, boolean autorizadoFacturacion, boolean facturado,
                        int flagEnvio, ArrayList<DetalleOrdenLectura> numeradores)
    {
        this.id = id;
        this.codigo = codigo;
        this.posicion = posicion;
        this.direccion = direccion;
        this.direccionEntrega = direccionEntrega;
        this.numeroPoste = numeroPoste;
        this.fechaCarga = fechaCarga;
        this.fechaPropuesta = fechaPropuesta;
        this.fechaAsignacion = fechaAsignacion;
        this.gpsLatitud = gpsLatitud;
        this.gpsLongitud = gpsLongitud;
        this.ajusteSencilloAnterior = ajusteSencilloAnterior;
        this.ajusteSencilloActual = ajusteSencilloActual;
        this.instalacionId = instalacionId;
        this.medidorId = medidorId;
        this.clienteId = clienteId;
        this.rutaId = rutaId;
        this.tipoLecturaId = tipoLecturaId;
        this.estadoLecturaId = estadoLecturaId;
        this.tipoTarifaId = tipoTarifaId;
        this.tipoEntregaId = tipoEntregaId;
        this.tipoEstablecimientoId = tipoEstablecimientoId;
        this.nroMunicipal = nroMunicipal;
        this.factorCobroId = factorCobroId;
        this.folioCasaCerrada = folioCasaCerrada;
        this.autorizadoFacturacion = autorizadoFacturacion;
        this.facturado = facturado;
        this.flagEnvio = flagEnvio;
        this.numeradores = numeradores;
    }

    public OrdenLectura(int id, int codigo, int posicion, String direccion, String direccionEntrega, String numeroPoste, Long fechaCarga, Long fechaPropuesta, Long fechaAsignacion, double ajusteSencilloAnterior, int instalacionId, int medidorId, int clienteId, int rutaId, int tipoLecturaId, int estadoLecturaId, int tipoTarifaId, int tipoEntregaId, int tipoEstablecimientoId, int nroMunicipal, int factorCobroId, boolean autorizadoFacturacion, boolean facturado, int flagEnvio, ArrayList<DetalleOrdenLectura> numeradores) {
        this.id = id;
        this.codigo = codigo;
        this.posicion = posicion;
        this.direccion = direccion;
        this.direccionEntrega = direccionEntrega;
        this.numeroPoste = numeroPoste;
        this.fechaCarga = fechaCarga;
        this.fechaPropuesta = fechaPropuesta;
        this.fechaAsignacion = fechaAsignacion;
        this.ajusteSencilloAnterior = ajusteSencilloAnterior;
        this.instalacionId = instalacionId;
        this.medidorId = medidorId;
        this.clienteId = clienteId;
        this.rutaId = rutaId;
        this.tipoLecturaId = tipoLecturaId;
        this.estadoLecturaId = estadoLecturaId;
        this.tipoTarifaId = tipoTarifaId;
        this.tipoEntregaId = tipoEntregaId;
        this.tipoEstablecimientoId = tipoEstablecimientoId;
        this.nroMunicipal = nroMunicipal;
        this.factorCobroId = factorCobroId;
        this.autorizadoFacturacion = autorizadoFacturacion;
        this.facturado = facturado;
        this.flagEnvio = flagEnvio;
        this.numeradores = numeradores;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getNumeroPoste() {
        return numeroPoste;
    }

    public void setNumeroPoste(String numeroPoste) {
        this.numeroPoste = numeroPoste;
    }

    public Long getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Long fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public Long getFechaPropuesta() {
        return fechaPropuesta;
    }

    public void setFechaPropuesta(Long fechaPropuesta) {
        this.fechaPropuesta = fechaPropuesta;
    }

    public Long getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Long fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public double getGpsLatitud() {
        return gpsLatitud;
    }

    public void setGpsLatitud(double gpsLatitud) {
        this.gpsLatitud = gpsLatitud;
    }

    public double getGpsLongitud() {
        return gpsLongitud;
    }

    public void setGpsLongitud(double gpsLongitud) {
        this.gpsLongitud = gpsLongitud;
    }

    public double getAjusteSencilloAnterior() {
        return ajusteSencilloAnterior;
    }

    public void setAjusteSencilloAnterior(double ajusteSencilloAnterior) {
        this.ajusteSencilloAnterior = ajusteSencilloAnterior;
    }

    public double getAjusteSencilloActual() {
        return ajusteSencilloActual;
    }

    public void setAjusteSencilloActual(double ajusteSencilloActual) {
        this.ajusteSencilloActual = ajusteSencilloActual;
    }

    public int getInstalacionId() {
        return instalacionId;
    }

    public void setInstalacionId(int instalacionId) {
        this.instalacionId = instalacionId;
    }

    public int getMedidorId() {
        return medidorId;
    }

    public void setMedidorId(int medidorId) {
        this.medidorId = medidorId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getRutaId() {
        return rutaId;
    }

    public void setRutaId(int rutaId) {
        this.rutaId = rutaId;
    }

    public int getTipoLecturaId() {
        return tipoLecturaId;
    }

    public void setTipoLecturaId(int tipoLecturaId) {
        this.tipoLecturaId = tipoLecturaId;
    }

    public int getEstadoLecturaId() {
        return estadoLecturaId;
    }

    public void setEstadoLecturaId(int estadoLecturaId) {
        this.estadoLecturaId = estadoLecturaId;
    }

    public int getTipoTarifaId() {
        return tipoTarifaId;
    }

    public void setTipoTarifaId(int tipoTarifaId) {
        this.tipoTarifaId = tipoTarifaId;
    }

    public int getTipoEntregaId() {
        return tipoEntregaId;
    }

    public void setTipoEntregaId(int tipoEntregaId) {
        this.tipoEntregaId = tipoEntregaId;
    }

    public int getTipoEstablecimientoId() {
        return tipoEstablecimientoId;
    }

    public void setTipoEstablecimientoId(int tipoEstablecimientoId) {
        this.tipoEstablecimientoId = tipoEstablecimientoId;
    }

    public int getNroMunicipal() {
        return nroMunicipal;
    }

    public void setNroMunicipal(int nroMunicipal) {
        this.nroMunicipal = nroMunicipal;
    }

    public int getFactorCobroId() {
        return factorCobroId;
    }

    public void setFactorCobroId(int factorCobroId) {
        this.factorCobroId = factorCobroId;
    }

    public boolean isAutorizadoFacturacion() {
        return autorizadoFacturacion;
    }

    public void setAutorizadoFacturacion(boolean autorizadoFacturacion) {
        this.autorizadoFacturacion = autorizadoFacturacion;
    }

    public boolean isFacturado() {
        return facturado;
    }

    public void setFacturado(boolean facturado) {
        this.facturado = facturado;
    }

    public int getAutorizadoFacturacion() {

        if(this.isAutorizadoFacturacion())
            return 1;

        else return 0;
    }

    public int getFacturado() {
        if(this.isFacturado())
            return 1;

        else return 0;
    }


    public int getFlagEnvio() {
		return flagEnvio;
	}
	
	public void setFlagEnvio(int flagEnvio) {
		this.flagEnvio = flagEnvio;
	}
	
	public ArrayList<DetalleOrdenLectura> getNumeradores() {
        return numeradores;
    }

    public void setNumeradores(ArrayList<DetalleOrdenLectura> numeradores) {
        this.numeradores = numeradores;
    }

    public int getFolioCasaCerrada() {
        return folioCasaCerrada;
    }

    public void setFolioCasaCerrada(int folioCasaCerrada) {
        this.folioCasaCerrada = folioCasaCerrada;
    }
}

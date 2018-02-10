package modelos;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by brayan on 30-03-17.
 */

public class DetalleOrdenLectura implements Serializable
{
    private int id;
    private int ordenLecturaId;
    private int numeradorId;
    private double lecturaAnterior;
    private double lecturaPromedio;
    private double lecturaActual;
    private double rangoSuperior;
    private double rangoInferior;
    private long fechaEjecucion;
    private int claveLecturaId;
    private int observacionId;
    private int claveLecturaAnteriorId;
    private int claveLecturaAnteriorId2;
    private int claveLecturaAnteriorId3;
    private double m3Acumulados;
	private ArrayList<Intento> listaIntentos;
    private int intentos = 0;
	private ArrayList<Fotografia> fotografias;
	private String mensajeFueraDeRango = "";
	
	public DetalleOrdenLectura(int id, int ordenLecturaId, int numeradorId, double lecturaAnterior,
							   double lecturaPromedio, double lecturaActual, double rangoSuperior,
							   double rangoInferior, long fechaEjecucion, int claveLecturaId, int observacionId,
							   int claveLecturaAnteriorId, int claveLecturaAnteriorId2, int claveLecturaAnteriorId3, double m3Acumulados, ArrayList<Intento> intentos,
							   ArrayList<Fotografia> fotografias)
	{
		this.id = id;
		this.ordenLecturaId = ordenLecturaId;
		this.numeradorId = numeradorId;
		this.lecturaAnterior = lecturaAnterior;
		this.lecturaPromedio = lecturaPromedio;
		this.lecturaActual = lecturaActual;
		this.rangoSuperior = rangoSuperior;
		this.rangoInferior = rangoInferior;
		this.fechaEjecucion = fechaEjecucion;
		this.claveLecturaId = claveLecturaId;
		this.observacionId = observacionId;
        this.claveLecturaAnteriorId2 = claveLecturaAnteriorId2;
        this.claveLecturaAnteriorId3 = claveLecturaAnteriorId3;
		this.claveLecturaAnteriorId = claveLecturaAnteriorId;
		this.m3Acumulados = m3Acumulados;
		this.listaIntentos = intentos;
		this.fotografias = fotografias;
	}

    public DetalleOrdenLectura(int id, int ordenLecturaId, int numeradorId, double lecturaAnterior,
                               double lecturaPromedio, double rangoSuperior, double rangoInferior,
                               int claveLecturaAnteriorId, int claveLecturaAnteriorId2, int claveLecturaAnteriorId3,
                               double m3Acumulados) {
        this.id = id;
        this.ordenLecturaId = ordenLecturaId;
        this.numeradorId = numeradorId;
        this.lecturaAnterior = lecturaAnterior;
        this.lecturaPromedio = lecturaPromedio;
        this.rangoSuperior = rangoSuperior;
        this.rangoInferior = rangoInferior;
        this.claveLecturaAnteriorId = claveLecturaAnteriorId;
        this.claveLecturaAnteriorId2 = claveLecturaAnteriorId2;
        this.claveLecturaAnteriorId3 = claveLecturaAnteriorId3;
        this.m3Acumulados = m3Acumulados;
    }

    public String parseDate()
	{
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fecha = dateFormatter.format(this.fechaEjecucion);
		return fecha;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdenLecturaId() {
        return ordenLecturaId;
    }

    public void setOrdenLecturaId(int ordenLecturaId) {
        this.ordenLecturaId = ordenLecturaId;
    }

    public int getNumeradorId() {
        return numeradorId;
    }

    public void setNumeradorId(int numeradorId) {
        this.numeradorId = numeradorId;
    }

    public double getLecturaAnterior() {
        return lecturaAnterior;
    }

    public void setLecturaAnterior(float lecturaAnterior) {
        this.lecturaAnterior = lecturaAnterior;
    }

    public double getLecturaPromedio() {
        return lecturaPromedio;
    }

    public void setLecturaPromedio(double lecturaPromedio) {
        this.lecturaPromedio = lecturaPromedio;
    }

    public double getLecturaActual() {
        return lecturaActual;
    }

    public void setLecturaActual(double lecturaActual) {
        this.lecturaActual = lecturaActual;
    }

    public double getRangoSuperior() {
        return rangoSuperior;
    }

    public void setRangoSuperior(double rangoSuperior) {
        this.rangoSuperior = rangoSuperior;
    }

    public Long getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(long fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public int getClaveLecturaId() {
        return claveLecturaId;
    }

    public void setClaveLecturaId(int claveLecturaId) {
        this.claveLecturaId = claveLecturaId;
    }

    public int getObservacionId() {
        return observacionId;
    }

    public void setObservacionId(int observacionId) {
        this.observacionId = observacionId;
    }

    public int getClaveLecturaAnteriorId() {
        return claveLecturaAnteriorId;
    }

    public void setClaveLecturaAnteriorId(int claveLecturaAnteriorId) {
        this.claveLecturaAnteriorId = claveLecturaAnteriorId;
    }

    public double getM3Acumulados() {
        return m3Acumulados;
    }

    public void setM3Acumulados(double m3Acumulados) {
        this.m3Acumulados = m3Acumulados;
    }

    public void setLecturaAnterior(double lecturaAnterior) {
        this.lecturaAnterior = lecturaAnterior;
    }

    public double getRangoInferior() {
        return rangoInferior;
    }

    public void setRangoInferior(double rangoInferior) {
        this.rangoInferior = rangoInferior;
    }

    public boolean fueraDeRango(double lectura, ClaveLectura clave )
    {
        //Si la clave no requiere lectura, la lectura siempre esta dentro del rango.
		if(!clave.isLecturaRequerida())
		    return false;


        //Verifica cuantas veces se ha ingresado la misma lectura
        if( this.lecturaActual == lectura)
            this.intentos++;
        else
        {
            this.intentos = 1;
            this.lecturaActual = lectura;
        }
	
		//Verifica cantidad de veces que se repite la misma lectura para considerarla valida
		if( this.intentos == 2)
			return false;

        //Verifica si lectura actual es menor a lectura anterior
        if(this.lecturaActual < this.lecturaAnterior) {
            this.setMensajeFueraDeRango("Lectura ingresada se encuentra bajo el consumo normal");
            return true;
        }

        if(this.lecturaActual > this.rangoSuperior) {
            this.setMensajeFueraDeRango("Lectura ingresada con sobreconsumo");
            return true;
        }
        return false;
    }
	
	public ArrayList<Intento> getListaIntentos()
	{
		return listaIntentos;
	}
	
	public void setListaIntentos(ArrayList<Intento> listaIntentos)
	{
		this.listaIntentos = listaIntentos;
	}
	
	public int getIntentos() {
		return intentos;
	}
	
	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}
	
	public ArrayList<Fotografia> getFotografias() {
		return fotografias;
	}
	
	public void setFotografias(ArrayList<Fotografia> fotografias) {
		this.fotografias = fotografias;
	}

    public int getClaveLecturaAnteriorId2() {
        return claveLecturaAnteriorId2;
    }

    public void setClaveLecturaAnteriorId2(int claveLecturaAnteriorId2) {
        this.claveLecturaAnteriorId2 = claveLecturaAnteriorId2;
    }

    public int getClaveLecturaAnteriorId3() {
        return claveLecturaAnteriorId3;
    }

    public void setClaveLecturaAnteriorId3(int claveLecturaAnteriorId3) {
        this.claveLecturaAnteriorId3 = claveLecturaAnteriorId3;
    }

    public String getMensajeFueraDeRango() {
        return mensajeFueraDeRango;
    }

    public void setMensajeFueraDeRango(String mensajeFueraDeRango) {
        this.mensajeFueraDeRango = mensajeFueraDeRango;
    }
}

package modelos;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by brayan on 30-03-17.
 * Modify by Fabián Hidalgo 11-09-2018
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
    private String claveLecturaAnterior;
    private String claveLecturaAnterior2;
    private String claveLecturaAnterior3;
    private double m3Acumulados;
	private ArrayList<Intento> listaIntentos;
    private int intentos = 0;
	private ArrayList<Fotografia> fotografias;
	private String mensajeFueraDeRango = "";
	private Long hora_lector;
    private Long hora_medidor;
    private double energia_reactiva;
    private double energia_suministrada;
    private Long fecha_energia_suministrada;
    private Long hora_energia_suministrada;
    private int reset_energia_suministrada;
    private double energia_punta;
    private Long fecha_energia_punta;
    private Long hora_energia_punta;
    private int reset_energia_punta;
    private double energia_inyectada;

    public DetalleOrdenLectura(int id, int ordenLecturaId, int numeradorId, double lecturaAnterior,
							   double lecturaPromedio, double lecturaActual, double rangoSuperior,
							   double rangoInferior, long fechaEjecucion, int claveLecturaId, int observacionId,
							   String claveLecturaAnterior, String claveLecturaAnterior2, String claveLecturaAnterior3,
                               double m3Acumulados, ArrayList<Intento> intentos,
							   ArrayList<Fotografia> fotografias,
                               long hora_lector, long hora_medidor, double energia_reactiva, double energia_suministrada,
                               long fecha_energia_suministrada, long hora_energia_suministrada, int reset_energia_suministrada,





                               double energia_punta,long fecha_energia_punta,long hora_energia_punta, int reset_energia_punta, double energia_inyectada )
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
        this.claveLecturaAnterior2 = claveLecturaAnterior2;
        this.claveLecturaAnterior3 = claveLecturaAnterior3;
		this.claveLecturaAnterior = claveLecturaAnterior;
		this.m3Acumulados = m3Acumulados;
		this.listaIntentos = intentos;
		this.fotografias = fotografias;
		this.hora_lector = hora_lector;
		this.hora_medidor = hora_medidor;
		this.energia_reactiva = energia_reactiva;
		this.energia_suministrada = energia_suministrada;
		this.fecha_energia_suministrada = fecha_energia_suministrada;
		this.hora_energia_suministrada = hora_energia_suministrada;
		this.reset_energia_suministrada = reset_energia_suministrada;
		this.energia_punta = energia_punta;
		this.fecha_energia_punta = fecha_energia_punta;
		this.hora_energia_punta = hora_energia_punta;
		this.reset_energia_punta = reset_energia_punta;
		this.energia_inyectada = energia_inyectada;
	}

    public DetalleOrdenLectura(int id, int ordenLecturaId, int numeradorId, double lecturaAnterior,
                               double lecturaPromedio, double rangoSuperior, double rangoInferior,
                               String claveLecturaAnterior, String claveLecturaAnterior2, String claveLecturaAnterior3,
                               double m3Acumulados) {
        this.id = id;
        this.ordenLecturaId = ordenLecturaId;
        this.numeradorId = numeradorId;
        this.lecturaAnterior = lecturaAnterior;
        this.lecturaPromedio = lecturaPromedio;
        this.rangoSuperior = rangoSuperior;
        this.rangoInferior = rangoInferior;
        this.claveLecturaAnterior = claveLecturaAnterior;
        this.claveLecturaAnterior2 = claveLecturaAnterior2;
        this.claveLecturaAnterior3 = claveLecturaAnterior3;
        this.m3Acumulados = m3Acumulados;
    }

    public String parseDate()
	{
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fecha = dateFormatter.format(this.fechaEjecucion);
		return fecha;
	}

    public String getClaveLecturaAnterior() {
        return claveLecturaAnterior;
    }

    public void setClaveLecturaAnterior(String claveLecturaAnterior) {
        this.claveLecturaAnterior = claveLecturaAnterior;
    }

    public String getClaveLecturaAnterior2() {
        return claveLecturaAnterior2;
    }

    public void setClaveLecturaAnterior2(String claveLecturaAnterior2) {
        this.claveLecturaAnterior2 = claveLecturaAnterior2;
    }

    public String getClaveLecturaAnterior3() {
        return claveLecturaAnterior3;
    }

    public void setClaveLecturaAnterior3(String claveLecturaAnterior3) {
        this.claveLecturaAnterior3 = claveLecturaAnterior3;
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

    public boolean fueraDeRango(double lectura, Observacion observacion )
    {
        //Si la clave no requiere lectura, la lectura siempre esta dentro del rango.
		if(!observacion.isLecturaRequerida())
		    return false;
        System.out.println (observacion.isLecturaRequerida ());
        //Verifica cuantas veces se ha ingresado la misma lectura
        if( this.lecturaActual == lectura)
            this.intentos++;
        else
        {
            this.intentos = 1;
            this.lecturaActual = lectura;
        }
	
		//Verifica cantidad de veces que se repite la misma lectura para considerarla valida
		if( this.intentos == 2){
            return false;
        }


        //Verifica si lectura actual es menor a lectura anterior
        if(this.lecturaActual < this.rangoInferior) {
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



    public String getMensajeFueraDeRango() {
        return mensajeFueraDeRango;
    }

    public void setMensajeFueraDeRango(String mensajeFueraDeRango) {
        this.mensajeFueraDeRango = mensajeFueraDeRango;
    }
}

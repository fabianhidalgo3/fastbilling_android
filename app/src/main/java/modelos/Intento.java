package modelos;

import java.io.Serializable;

/**
 * Created by brayan on 17-05-17.
 */

public class Intento implements Serializable
{
    private int id;
    private int idDetalleOrden;
    private double lectura;
    private int flagEnvio;


    public Intento(int id, int idDetalleOrden, double lectura, int flagEnvio)
    {
        this.id = id;
        this.idDetalleOrden = idDetalleOrden;
        this.lectura = lectura;
        this.flagEnvio = flagEnvio;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getIdDetalleOrden()
    {
        return idDetalleOrden;
    }

    public void setIdDetalleOrden(int idDetalleOrden)
    {
        this.idDetalleOrden = idDetalleOrden;
    }

    public double getLectura()
    {
        return lectura;
    }

    public void setLectura(double lectura)
    {
        this.lectura = lectura;
    }

    public int getFlagEnvio()
    {
        return flagEnvio;
    }

    public void setFlagEnvio(int flagEnvio)
    {
        this.flagEnvio = flagEnvio;
    }
}

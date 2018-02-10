package modelos;

import java.io.Serializable;

/**
 * Created by brayan on 02-11-17.
 */

public class Comuna implements Serializable
{
    private int id;
    private String codigo;
    private String codigoSii;
    private String codigoTesoreria;
    private String nombre;


    public Comuna(int id, String codigo, String codigoSii, String codigoTesoreria, String nombre)
    {
        this.id = id;
        this.codigo = codigo;
        this.codigoSii = codigoSii;
        this.codigoTesoreria = codigoTesoreria;
        this.nombre = nombre;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getCodigoSii()
    {
        return codigoSii;
    }

    public void setCodigoSii(String codigoSii)
    {
        this.codigoSii = codigoSii;
    }

    public String getCodigoTesoreria()
    {
        return codigoTesoreria;
    }

    public void setCodigoTesoreria(String codigoTesoreria)
    {
        this.codigoTesoreria = codigoTesoreria;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }
}

package modelos;

import java.io.Serializable;

/**
 * Created by brayan on 02-11-17.
 */

public class TipoDocumento implements Serializable
{
    private int id;
    private String codigoSii;
    private String nombre;

    public TipoDocumento(){};

    public TipoDocumento(int id, String codigoSii, String nombre)
    {
        this.id = id;
        this.codigoSii = codigoSii;
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

    public String getCodigoSii()
    {
        return codigoSii;
    }

    public void setCodigoSii(String codigoSii)
    {
        this.codigoSii = codigoSii;
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

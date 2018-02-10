package modelos;

import java.io.Serializable;

/**
 * Created by brayan on 02-11-17.
 */

public class TipoReparto implements Serializable
{
    private int id;
    private String descripcion;

    public TipoReparto(int id, String descripcion)
    {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }
}

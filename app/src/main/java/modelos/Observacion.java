package modelos;

import java.io.Serializable;

import static android.provider.BaseColumns._ID;

/**
 * Created by brayan on 22-06-17.
 */

public class Observacion implements Serializable
{
    private int id;
    private String descripcion;
    private int claveLecturaId;

    public Observacion(){}

    public Observacion(int id, String descripcion, int clave_lectura_id)
    {
        this.id = id;
        this.descripcion = descripcion;
        this.claveLecturaId = clave_lectura_id;
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

    public int getClaveLecturaId()
    {
        return claveLecturaId;
    }

    public void setClaveLecturaId(int claveLecturaId)
    {
        this.claveLecturaId = claveLecturaId;
    }
}

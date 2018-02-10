package modelos;

import java.io.Serializable;

/**
 * Created by brayan on 02-11-17.
 */

public class TipoEntrega implements Serializable
{
    private int id;
    private String codigo;
    private String nombre;
    private boolean facturacionEnTerreno;

    public TipoEntrega(int id, String codigo, String nombre, boolean facturacionEnTerreno)
    {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.facturacionEnTerreno = facturacionEnTerreno;
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

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public boolean isFacturacionEnTerreno()
    {
        return facturacionEnTerreno;
    }

    public void setFacturacionEnTerreno(boolean facturacionEnTerreno)
    {
        this.facturacionEnTerreno = facturacionEnTerreno;
    }
}

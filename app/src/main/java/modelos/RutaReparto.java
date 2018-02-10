package modelos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by brayan on 02-11-17.
 */

public class RutaReparto implements Serializable
{
    private int id;
    private String codigo;
    private String nombre;
    private int usuario;
    private ArrayList<OrdenReparto> ordenes;
    private int numeroOrdenes;

    public RutaReparto(int id, String codigo, String nombre, int usuario, int numeroOrdenes)
    {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.usuario = usuario;
        this.numeroOrdenes = numeroOrdenes;
    }

    public RutaReparto(int id, String codigo, String nombre, int usuario, ArrayList<OrdenReparto> ordenes)
    {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.usuario = usuario;
        this.ordenes = ordenes;
        this.numeroOrdenes = ordenes.size();
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

    public int getUsuario()
    {
        return usuario;
    }

    public void setUsuario(int usuario)
    {
        this.usuario = usuario;
    }

    public ArrayList<OrdenReparto> getOrdenes()
    {
        return ordenes;
    }

    public void setOrdenes(ArrayList<OrdenReparto> ordenes)
    {
        this.ordenes = ordenes;
    }

    public int getNumeroOrdenes()
    {
        return numeroOrdenes;
    }

    public void setNumeroOrdenes(int numeroOrdenes)
    {
        this.numeroOrdenes = numeroOrdenes;
    }
}

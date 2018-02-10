package modelos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by brayan on 22-11-16.
 */

public class Ruta implements Serializable
{
    private int id;
    private String codigo;
    private String nombre;
    private int usuario;
    private ArrayList<OrdenLectura> lecturas;
	private int numeroOrdenes;
	
	public Ruta(int id, String codigo, String nombre, int usuario, int numeroOrdenes)
	{
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.usuario = usuario;
		this.numeroOrdenes = numeroOrdenes;
	}

    public Ruta(int id, String codigo, String nombre, int usuario, ArrayList<OrdenLectura> lecturas)
    {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.usuario = usuario;
        this.lecturas = lecturas;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCodigo()
    {
        return this.codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public int getNumeroOrdenes()
    {
        return this.numeroOrdenes;
    }

    public ArrayList<OrdenLectura> getLecturas() {
        return lecturas;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public void setLecturas(ArrayList<OrdenLectura> lecturas) {
        this.lecturas = lecturas;
    }
}

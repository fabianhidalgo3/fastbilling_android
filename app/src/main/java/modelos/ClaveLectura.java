package modelos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by brayan on 12/20/16.
 */

public class ClaveLectura implements Serializable
{
    private int id;
    private String clave;
    private String codigo;
    private int numFotografias;
    private String descripcionCorta;
    private int tipoCobroId;
    private boolean lecturaRequerida;

    private ArrayList<Observacion> observaciones;

    public ClaveLectura()
    {

    }


    public ClaveLectura(int id, String clave, String codigo, int numFotografias, String descripcionCorta, int tipoCobroId, boolean lecturaRequerida, ArrayList<Observacion> observaciones)
    {
        this.id = id;
        this.clave = clave;
        this.codigo = codigo;
        this.numFotografias = numFotografias;
        this.descripcionCorta = descripcionCorta;
        this.tipoCobroId = tipoCobroId;
        this.lecturaRequerida = lecturaRequerida;
        this.observaciones = observaciones;
    }

    public int getNumFotografias() {
        return numFotografias;
    }

    public void setNumFotografias(int numFotografias) {
        this.numFotografias = numFotografias;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getClave()
    {
        return clave;
    }

    public void setClave(String clave)
    {
        this.clave = clave;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getDescripcionCorta()
    {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta)
    {
        this.descripcionCorta = descripcionCorta;
    }

    public int getTipoCobroId()
    {
        return tipoCobroId;
    }

    public void setTipoCobroId(int tipoCobroId)
    {
        this.tipoCobroId = tipoCobroId;
    }

    public boolean isLecturaRequerida() {
        return lecturaRequerida;
    }

    public void setLecturaRequerida(boolean lecturaRequerida) {
        this.lecturaRequerida = lecturaRequerida;
    }

    public int getLecturaRequerida()
    {
        if(this.isLecturaRequerida())
            return 1;

        return 0;
    }

    public ArrayList<Observacion> getObservaciones()
    {
        return observaciones;
    }

    public void setObservaciones(ArrayList<Observacion> observaciones)
    {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return codigo;
    }
}

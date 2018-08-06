package modelos;

import java.io.Serializable;

import static android.provider.BaseColumns._ID;

/**
 * Created by brayan on 22-06-17.
 *  Modify by Fabián Hidalgo 06-08-2018
 */

public class Observacion implements Serializable
{
    private int id;
    private String descripcion;
    private int claveLecturaId;
    private int numeroFotografias;
    private boolean lecturaRequerida;
    private boolean lecturaEfectiva;
    private boolean factura;
    private boolean folio;


    public Observacion(){}

    public Observacion(int id, String descripcion, int clave_lectura_id, int numeroFotografias,
                       boolean lecturaEfectiva, boolean lecturaRequerida, boolean factura, boolean folio)
    {
        this.id = id;
        this.descripcion = descripcion;
        this.claveLecturaId = clave_lectura_id;
        this.numeroFotografias = numeroFotografias;
        this.lecturaRequerida = lecturaRequerida;
        this.lecturaEfectiva = lecturaEfectiva;
        this.factura = factura;
        this.folio = folio;
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
    public int getNumeroFotografias() { return numeroFotografias; }
    public void setNumeroFotografias(int numeroFotografias) { this.numeroFotografias = numeroFotografias; }
    public int getLecturaRequerida()
    {
        if(this.isLecturaRequerida())
            return 1;

        return 0;
    }
    public boolean isLecturaRequerida() { return lecturaRequerida; }
    public void setLecturaRequerida(boolean lecturaRequerida) { this.lecturaRequerida = lecturaRequerida; }
    public int getLecturaEfectiva()
    {
        if(this.isLecturaEfectiva ())
            return 1;

        return 0;
    }
    public boolean isLecturaEfectiva() { return lecturaEfectiva; }
    public void setLecturaEfectiva(boolean lecturaEfectiva) { this.lecturaEfectiva = lecturaEfectiva; }
    public int getFactura()
    {
        if(this.isFactura ())
            return 1;

        return 0;
    }
    public boolean isFactura() { return factura; }
    public void setFactura(boolean factura) { this.factura = factura; }
    public int getFolio()
    {
        if(this.isFolio ())
            return 1;

        return 0;
    }
    public boolean isFolio() { return folio; }
    public void setFolio(boolean folio) { this.folio = folio; }
}

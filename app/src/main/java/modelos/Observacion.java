package modelos;

import java.io.Serializable;

import static android.provider.BaseColumns._ID;

/**
 * Created by brayan on 22-06-17.
 * Modifiqued by Fabián Hidalgo
 */

public class Observacion implements Serializable
{
    private int id;
    private String descripcion;
    private int claveLecturaId;
    private int num_fotografias;
    private boolean requerido;
    private boolean efectivo;
    private boolean factura;
    private boolean folio;

    public Observacion(){}


    public Observacion(int id, String descripcion, int claveLecturaId, int num_fotografias, boolean requerido, boolean efectivo, boolean factura, boolean folio) {
        this.id = id;
        this.descripcion = descripcion;
        this.claveLecturaId = claveLecturaId;
        this.num_fotografias = num_fotografias;
        this.requerido = requerido;
        this.efectivo = efectivo;
        this.factura = factura;
        this.folio = folio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getClaveLecturaId() {
        return claveLecturaId;
    }

    public void setClaveLecturaId(int claveLecturaId) {
        this.claveLecturaId = claveLecturaId;
    }

    public int getNum_fotografias() {
        return num_fotografias;
    }

    public void setNum_fotografias(int num_fotografias) {
        this.num_fotografias = num_fotografias;
    }

    public boolean isRequerido() { return requerido; }

    public void setRequerido(boolean requerido) {
        this.requerido = requerido;
    }
    // Get > Efectivo
    public int getRequerido()
    {
        if(this.isRequerido ())
            return 1;

        return 0;
    }

    public boolean isEfectivo() { return efectivo; }

    public void setEfectivo(boolean efectivo) {
        this.efectivo = efectivo;
    }
    // Get > Efectivo
    public int getEfectivo()
    {
        if(this.isEfectivo ())
            return 1;

        return 0;
    }

    public boolean isFactura() { return factura; }

    public void setFactura(boolean factura) {
        this.factura = factura;
    }
    // Get > Efectivo
    public int getFactura()
    {
        if(this.isFactura ())
            return 1;

        return 0;
    }

    public boolean isFolio() { return folio; }

    public void setFolio(boolean folio) {
        this.folio = folio;
    }

    // Get > Folio
    public int getFolio()
    {
        if(this.isFolio ())
            return 1;

        return 0;
    }
}

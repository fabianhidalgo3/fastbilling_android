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

    public String getDescripcion() {
        return descripcion;
    }

    public int getClaveLecturaId() {
        return claveLecturaId;
    }

    public int getNum_fotografias() {
        return num_fotografias;
    }

    public boolean isRequerido() {
        return requerido;
    }

    public boolean isEfectivo() {
        return efectivo;
    }

    public boolean isFactura() {
        return factura;
    }

    public boolean isFolio() {
        return folio;
    }
}

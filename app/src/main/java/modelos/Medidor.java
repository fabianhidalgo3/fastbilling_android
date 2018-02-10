package modelos;

/**
 * Created by brayan on 4/26/17.
 */

public class Medidor
{
    private int id;
    private String numeroMedidor;
    private int modeloId;
    private boolean propiedadCliente;
    private int nroDigitos;
    private int diametro;

    public Medidor(int id, String numeroMedidor, int modeloId, boolean propiedadCliente, int nroDigitos, int diametro) {
        this.id = id;
        this.numeroMedidor = numeroMedidor;
        this.modeloId = modeloId;
        this.propiedadCliente = propiedadCliente;
        this.nroDigitos = nroDigitos;
        this.diametro = diametro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroMedidor() {
        return numeroMedidor;
    }

    public void setNumeroMedidor(String numeroMedidor) {
        this.numeroMedidor = numeroMedidor;
    }

    public int getModeloId() {
        return modeloId;
    }

    public void setModeloId(int modeloId) {
        this.modeloId = modeloId;
    }

    public boolean getPropiedadCliente() {
        return propiedadCliente;
    }

    public void setPropiedadCliente(boolean propiedadCliente) {
        this.propiedadCliente = propiedadCliente;
    }

    public int getNroDigitos() {
        return nroDigitos;
    }

    public void setNroDigitos(int nroDigitos) {
        this.nroDigitos = nroDigitos;
    }

    public int getDiametro() {
        return diametro;
    }

    public void setDiametro(int diametro) {
        this.diametro = diametro;
    }
}

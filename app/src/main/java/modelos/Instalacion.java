package modelos;

/**
 * Created by brayan on 4/26/17.
 */

public class Instalacion {

    private int id;
    private String codigo;

    public Instalacion(int id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}

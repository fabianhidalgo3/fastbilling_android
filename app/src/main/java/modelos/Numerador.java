package modelos;

/**
 * Created by Fabián Hidalgo on 09-09-2018.
 */
public class Numerador {
    private int id;
    private String codigo;
    private String nombre;
    private int secuencia;
    private String descripcion;

    public Numerador(int id, String codigo, String nombre, int secuencia, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.secuencia = secuencia;
        this.descripcion = descripcion;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

package modelos;

/**
 * Created by brayan on 03-04-17.
 */

public class Cliente
{
    private int id;
    private String rut;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String direccion;
    private String giro;
    private String telefono;
    private String memo;
    private String duenorespo;
    private String numero_cliente;

    public Cliente(int id, String rut, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion, String giro, String telefono, String memo, String duenorespo, String numero_cliente)
    {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.direccion = direccion;
        this.giro = giro;
        this.telefono = telefono;
        this.memo = memo;
        this.duenorespo = duenorespo;
        this.numero_cliente = numero_cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGiro() {
        return giro;
    }

    public void setGiro(String giro) {
        this.giro = giro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDuenorespo() {
        return duenorespo;
    }

    public void setDuenorespo(String duenorespo) {
        this.duenorespo = duenorespo;
    }

    public String getNumero_cliente() {
        return numero_cliente;
    }

    public void setNumero_cliente(String numero_cliente) {
        this.numero_cliente = numero_cliente;
    }

    public String getNombreCompleto()
    {
        String nombre = this.nombre;
        if(this.apellidoPaterno != null)
            nombre += " " + this.apellidoPaterno;

        if(this.apellidoMaterno != null)
            nombre += " " + this.apellidoMaterno;

        return nombre;
    }
}

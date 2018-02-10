package modelos;

/**
 * Created by brayan on 07-07-17.
 */

public class ParametrosImpresora
{
    private int id;
    private String mac;
    private boolean impresionHabilitada;

    public ParametrosImpresora(int id, String mac, boolean impresionHabilitada)
    {
        this.id = id;
        this.mac = mac;
        this.impresionHabilitada = impresionHabilitada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isImpresionHabilitada() {
        return impresionHabilitada;
    }

    public void setImpresionHabilitada(boolean impresionHabilitada) {
        this.impresionHabilitada = impresionHabilitada;
    }

    public int getFlagImpresion()
    {
        if (this.impresionHabilitada)
            return 1;

        return 0;
    }
}

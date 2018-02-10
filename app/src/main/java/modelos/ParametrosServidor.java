package modelos;

/**
 * Created by brayan on 06-03-17.
 */

public class ParametrosServidor
{
    private int id;
    private String ip;
    private int intervalo;

    public ParametrosServidor(int id, String ip, int intervalo)
    {
        this.id = id;
        this.ip = ip;
        this.intervalo = intervalo;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getIntervalo()
    {
        return intervalo;
    }

    public void setIntervalo(int intervalo)
    {
        this.intervalo = intervalo;
    }
}

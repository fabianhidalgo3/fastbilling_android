package modelos;

import java.io.Serializable;

/**
 * Created by brayan on 22-11-16.
 */

public class Usuario implements Serializable
{
    private int id;
    private String email;
    private String password;
    private String token;
    private int perfilId;

    public Usuario(int id, String email, String password, String token, int perfilId)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.token = token;
        this.perfilId = perfilId;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getToken()
    {
        return this.token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public int getPerfilId()
    {
        return perfilId;
    }

    public void setPerfilId(int perfilId)
    {
        this.perfilId = perfilId;
    }

    public boolean validaLogin(String email, String password)
    {
        if(this.email.equals(email) && this.password.equals(password))
            return true;

        return false;
    }


}

package modelos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.Serializable;

/**
 * Clase encargada de almacenar datos de fotografias
 */

public class Fotografia implements Serializable
{
    private int id;
	private int detalleOrdenLecturaId;
	private String ruta;
	private String observacion;
	private int flagEnvio;
	
	public Fotografia(int id, int detalleOrdenLecturaId, String ruta, String observacion, int flagEnvio)
	{
        this.id = id;
		this.detalleOrdenLecturaId = detalleOrdenLecturaId;
		this.ruta = ruta;
		this.observacion = observacion;
        this.flagEnvio = flagEnvio;
	}

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getDetalleOrdenLecturaId()
	{
		return detalleOrdenLecturaId;
	}
	
	public void setDetalleOrdenLecturaId(int detalleOrdenLecturaId)
	{
		this.detalleOrdenLecturaId = detalleOrdenLecturaId;
	}
	
	public String getRuta()
	{
		return ruta;
	}
	
	public void setRuta(String ruta)
	{
		this.ruta = ruta;
	}
	
	public String getObservacion()
	{
		return observacion;
	}
	
	public void setObservacion(String observacion)
	{
		this.observacion = observacion;
	}

    public int getFlagEnvio()
    {
        return flagEnvio;
    }

    public void setFlagEnvio(int flagEnvio)
    {
        this.flagEnvio = flagEnvio;
    }

    /**
     * Metodo que retorna la imagen redimensionada lista para visualizacion o para envio a servidor.
     * @return Mapa de bits correspondiente a la fotografia.
     */
    public Bitmap getImagen()
	{
		Matrix matrix = new Matrix();
		Bitmap bm = BitmapFactory.decodeFile(this.getRuta());
		if(bm.getWidth() > bm.getHeight())
		{
			matrix.postRotate(90);
		}
		bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

        return Bitmap.createScaledBitmap(bm, 480, 640, false);
	}
}

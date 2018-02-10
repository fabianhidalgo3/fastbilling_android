package modelos;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Clase para ver lo que muestra la camara.
 * Created by brayan on 29-06-17.
 */

public class CamaraPreview extends SurfaceView implements SurfaceHolder.Callback
{
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int zoom;
    private Double distanciaAnterior = 0.0;
    private Double distanciaActual = 0.0;

    public CamaraPreview(Context context, Camera camera)
    {
        super(context);
        this.mCamera = camera;
        this.zoom = 0; //Indica posicion en lista de zooms que se esta utilizando

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        this.mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.mHolder.addCallback(this);

    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        // The Surface has been created, now tell the camera where to draw the preview.
        try
        {
            this.mCamera.setPreviewDisplay(holder);
            this.mCamera.startPreview();
            this.mCamera.setDisplayOrientation(90);
            Camera.Parameters parameters = this.mCamera.getParameters();
            parameters.setRotation(90);
            parameters.set("orientation", "portrait");
            this.mCamera.setParameters(parameters);
        }
        catch (IOException e)
        {
            Log.d("Error", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        this.mCamera.stopPreview();
        this.mCamera.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (this.mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try
        {
            this.mCamera.stopPreview();
        }
        catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        try
        {
            Camera.Parameters params = this.mCamera.getParameters();
            params.setPictureFormat(PixelFormat.JPEG);

            //if you want the preview to be zoomed from start :
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            params.setZoom(this.zoom);


            System.out.println(params.getZoomRatios().toString());

            this.mCamera.setParameters(params);
            this.mCamera.setPreviewDisplay(this.mHolder);
            this.mCamera.setDisplayOrientation(90);
            Camera.Parameters parameters = this.mCamera.getParameters();
            parameters.setRotation(90);
            this.mCamera.setParameters(parameters);
            this.mCamera.startPreview();

        } catch (Exception e)
        {
            Log.d("Error", "Error starting camera preview: " + e.getMessage() + " " + e.getStackTrace().toString());
        }
    }

    /**
     * Se controlan los eventos sobre la pantalla para controlar zoom de la camara
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        String TAG = "Touch event";
        int action = event.getActionMasked();

        switch (action)
        {

            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:

                //Se setea la distancia anterior igual a la ultima distancia actual registrada
                this.distanciaAnterior = this.distanciaActual;
                //Se actualiza la distancia actual
                this.distanciaActual = this.obtenerDistanciaEntreDedos(event);

                //Si la distancia actual es mayor que la anterior se acerca.
                if (this.distanciaAnterior != 0 && this.distanciaActual > this.distanciaAnterior)
                    this.acercar();

                //Si la distancia actual es menor que la anterior se aleja.
                if (this.distanciaAnterior != 0 && this.distanciaActual < this.distanciaAnterior)
                    this.alejar();

                break;

            case MotionEvent.ACTION_UP:

                break;

            case MotionEvent.ACTION_CANCEL:

                break;

            case MotionEvent.ACTION_OUTSIDE:

                break;
        }

        //return super.onTouchEvent(event);
        return true;
    }

    @NonNull
    private Double obtenerDistanciaEntreDedos(MotionEvent event)
    {
        float x = 0;
        float y = 0;

        //Se valida que se este tocando con mas de un dedo la pantalla
        if (event.getPointerCount() > 1)
        {
            //Calculo de distancia entre puntos anteriores y actuales.
            x = event.getX(0) - event.getX(1);
            y = event.getY(0) - event.getY(1);
        }

        //Se retorna la distancia entre los dos puntos
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Metodo para acercar la camara al separar los dedos en la pantalla
     */
    public void acercar()
    {
        //Se obtienen los parametros de la camara
        Camera.Parameters params = this.mCamera.getParameters();

        //Se valida que el zoom actual no corresponda al zoom maximo soportado
        if (this.zoom < params.getMaxZoom())
        {
            //Se aumenta zoom y luego se setea en la camara
            this.zoom++;
            params.setZoom(this.zoom);
            this.mCamera.setParameters(params);
        }
    }

    /**
     * Metodo para alejar la camara al juntar los dedos en la pantalla.
     */
    public void alejar()
    {
        // Se obtienen los parametros de la camara
        Camera.Parameters params = this.mCamera.getParameters();

        //Se valida que el zoom actual sea mayor a cero
        if (this.zoom > 0)
        {
            //Se disminuye el zoom y se setean los nuevos parametros de la camara.
            this.zoom --;
            params.setZoom(this.zoom);
            mCamera.setParameters(params);
        }
    }
}

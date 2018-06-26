package cl.jfcor.fastbilling;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import base_datos.Bd;
import modelos.CamaraPreview;
import modelos.ClaveLectura;
import modelos.DetalleOrdenLectura;
import modelos.Fotografia;

/**
 * Created by brayan on 29-06-17.
 */
public class CamaraFragment extends Fragment implements View.OnClickListener, Camera.PictureCallback
{
    private static final int MEDIA_TYPE_IMAGE = 1;
    private Camera mCamara;
    private CamaraPreview mCamaraPreview;
    private FrameLayout preview;
    private Fotografia fotografia;
    private int cantidadFotografias;
    private int fotografiaActual;
    private TextView contador;
    private int detalleId;
    private Bd bd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_camara, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //Se obtienen datos.
        Bundle args = getArguments();
        ClaveLectura clave = (ClaveLectura) args.getSerializable("claveLectura");
        DetalleOrdenLectura detalle = (DetalleOrdenLectura) args.getSerializable("detalle");

        this.detalleId = detalle.getId();

        this.fotografiaActual = 1;


        this.contador = (TextView) this.getActivity().findViewById(R.id.numero_fotografia);

        if(clave.getId () == 1 || clave.getId () == 2 || clave.getId () == 3 || clave.getId () == 6) {
            this.cantidadFotografias = 2;
            if (this.contador != null)
                this.contador.setText ("Fotografia " + this.fotografiaActual + "/" + this.cantidadFotografias);
        }

        if(clave.getId () == 4 || clave.getId () == 5  || clave.getId () == 7){
            this.cantidadFotografias = 1;
            if (this.contador != null)
                this.contador.setText ("Fotografia " + this.fotografiaActual + "/" + this.cantidadFotografias);
        }


        // Se crea una instancia de camara
        this.mCamara = getCameraInstance();

        // Se crea la vista previa de la camara y se pone el contenido dentro del activity.
        this.mCamaraPreview = new CamaraPreview(this.getContext(), this.mCamara);
        this.preview = (FrameLayout) this.getActivity().findViewById(R.id.camera_preview);
        this.preview.addView(this.mCamaraPreview);
        Button tomarFoto = (Button) this.getActivity().findViewById(R.id.button_capture);
        Button guardar = (Button) this.getActivity().findViewById(R.id.button_guardar);
        Button volver = (Button) this.getActivity().findViewById(R.id.button_volver);
        tomarFoto.setOnClickListener(this);
        guardar.setOnClickListener(this);
        volver.setOnClickListener(this);
        guardar.setVisibility(View.INVISIBLE);
        volver.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.bd = Bd.getInstance(activity);
    }

    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_capture:
                //Aqui se controla la captura de la imagen.
                this.mCamara.takePicture(null, null, this);
                break;

            case R.id.button_volver:
                this.tomarOtraVez();
                break;

            case R.id.button_guardar:
                this.guardarFotografia();
                break;

        }

    }

    private void tomarOtraVez()
    {
        //Se elimina fotografia recien tomada.
        if (new File(this.fotografia.getRuta()).delete())
            Log.d("Fotografia", "Se elimino fotografia" + this.fotografia.getRuta());
        else
            Log.d("Fotografia", "No se elimino fotografia" + this.fotografia.getRuta());

        //Se elimina la vista previa de la fotografia
        this.preview.removeAllViews();

        //Se agregan nuevamente los elementos de la camara
        // Se crea instancia de la camara
        this.mCamara = getCameraInstance();

        this.mCamaraPreview = new CamaraPreview(this.getContext(), this.mCamara);
        this.preview = (FrameLayout) this.getActivity().findViewById(R.id.camera_preview);
        this.preview.addView(this.mCamaraPreview);

        Button tomarFoto = (Button) this.getActivity().findViewById(R.id.button_capture);
        Button guardar = (Button) this.getActivity().findViewById(R.id.button_guardar);
        Button volver = (Button) this.getActivity().findViewById(R.id.button_volver);
        tomarFoto.setVisibility(View.VISIBLE);
        guardar.setVisibility(View.INVISIBLE);
        volver.setVisibility(View.INVISIBLE);
    }

    private void guardarFotografia()
    {
        //TODO: Guardar las fotografias y actualizar lectura al aceptar la ultima fotografia
        //Se guarda la ruta de la fotografia en la base de datos.
        this.bd.abrir();
        bd.insertarFotografia(this.fotografia);

        this.fotografiaActual ++;
        if (this.fotografiaActual <= this.cantidadFotografias)
        {
            if (this.contador != null)
                this.contador.setText("Fotografia " + this.fotografiaActual + "/" + this.cantidadFotografias);

            //Se elimina la vista previa de la fotografia
            this.preview.removeAllViews();

            //Se agregan nuevamente los elementos de la camara
            // Se crea una instancia de camara
            this.mCamara = getCameraInstance();

            this.mCamaraPreview = new CamaraPreview(this.getContext(), this.mCamara);
            this.preview = (FrameLayout) this.getActivity().findViewById(R.id.camera_preview);
            this.preview.addView(this.mCamaraPreview);

            Button tomarFoto = (Button) this.getActivity().findViewById(R.id.button_capture);
            Button guardar = (Button) this.getActivity().findViewById(R.id.button_guardar);
            Button volver = (Button) this.getActivity().findViewById(R.id.button_volver);
            tomarFoto.setVisibility(View.VISIBLE);
            guardar.setVisibility(View.INVISIBLE);
            volver.setVisibility(View.INVISIBLE);
        }
        else
        {

            //Intent resultIntent = new Intent();
            //setResult(Activity.RESULT_OK, resultIntent);
            //this.getActivity().getSupportFragmentManager().popBackStack();
            this.getActivity().onBackPressed();
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera)
    {
        File archivo = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (archivo == null)
        {
            Log.d("Fotografia", "Error creando imagen, revisar permisos de almacenamiento");
            return;
        }

        try
        {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(data);
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            Log.d("Fotografia", "File not found: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.d("Fotografia", "Error accessing file: " + e.getMessage());
        }

        this.fotografia = new Fotografia(0, this.detalleId, archivo.getAbsolutePath(), "", 0);

        this.preview.removeAllViews();
        ImageView imagen = new ImageView(this.getContext());
        imagen.setImageBitmap(this.fotografia.getImagen());
        this.preview.addView(imagen);
        Button tomarFoto = (Button) this.getActivity().findViewById(R.id.button_capture);
        Button guardar = (Button) this.getActivity().findViewById(R.id.button_guardar);
        Button volver = (Button) this.getActivity().findViewById(R.id.button_volver);
        tomarFoto.setVisibility(View.INVISIBLE);
        guardar.setVisibility(View.VISIBLE);
        volver.setVisibility(View.VISIBLE);
    }

    /** Create a File for saving an image or video */
    @Nullable
    private static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Fastbilling");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists())
        {
            if (! mediaStorageDir.mkdirs())
            {
                Log.d("Fastbiling", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        else
            return null;

        return mediaFile;
    }
}

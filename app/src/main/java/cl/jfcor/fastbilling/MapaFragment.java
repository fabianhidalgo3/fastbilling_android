package cl.jfcor.fastbilling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Fragmento utilizado para mostrar el mapa con la ruta generada con los puntos gps de meses anteriores
 */
public class MapaFragment extends Fragment
{
    private static final String TAG = "Mapa";

    private MapView mapView;
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_mapa, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((MenuUsuario) getActivity()).setActionBarTitle(TAG);
        }



        mapView = (MapView) getActivity().findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);


        //bitmap
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_room_black_24dp);

        // Toma el mapa desde mapView y lo inicializa
        map = mapView.getMap();

        //Mostrar solo lecturas pendientes y que tengan ubicacion gps.
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bm))
                .anchor(0.0f, 1.0f)
                .position(new LatLng(-34.9886452, -71.2418179)));
        //////////////////////////////////////////////////////////////////////

        //Habilita la utilizacion de la posicion gps acutal del equipo
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        MapsInitializer.initialize(this.getActivity());
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(-34.9886452, -71.2418179));
        LatLngBounds bounds = builder.build();


        // Actualiza ubicacion y zoon del mapa
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 300,300, 1);
        map.moveCamera(cameraUpdate);
        map.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

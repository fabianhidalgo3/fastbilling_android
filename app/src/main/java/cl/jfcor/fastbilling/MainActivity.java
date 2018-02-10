package cl.jfcor.fastbilling;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import base_datos.Bd;
import modelos.API;
import modelos.Usuario;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Bd bd;
    private Usuario usuario;
    private CheckBox ejecutivoTerreno;
    private CheckBox admin;
	
	private Location currentLocation;

    //TODO: Refactorizar codigo.

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Base de datos
        this.bd = Bd.getInstance(this.getApplicationContext());
        Button login = (Button) findViewById(R.id.btn_login);

        this.ejecutivoTerreno = (CheckBox) findViewById(R.id.chbox_ejecutivo_terreno);
        this.admin = (CheckBox) findViewById(R.id.chbox_administrador);

        //Configuracion de listener para seleccion de tipo de login
        this.ejecutivoTerreno.setChecked(true);
        login.setOnClickListener(this);

        this.ejecutivoTerreno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                    admin.setChecked(false);

            }
        });

        this.admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                    ejecutivoTerreno.setChecked(false);

            }
        });

		// Metodo obtencion posicion GPS
        if(!checkLocationPermission())
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        else {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    currentLocation = location;

                    int Bearing = (int) currentLocation.getBearing();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("posicion", currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                            //Toast.makeText(MainActivity.this, "Network Provider update " + currentLocation.getLatitude() + " " + currentLocation.getLongitude() , Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }

            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2, 10, locationListener);
        }

        //Se revisan los permisos para usar el mapa
        if(!checkMapsPermission())
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
        }
        /*
            if(!checkCameraPermission())
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 2);
        }*/


    }

    //Verifica permisos de acceso a GPS
    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    //Verifica permisos de camara
    public boolean checkCameraPermission()
    {
        String permission = "android.permission.CAMERA";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    //Verifica permisos de acceso a MAPS
    public boolean checkMapsPermission()
    {
        String permission = "android.permission.ACCESS_COARSE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

	public boolean checkWritePermission()
    {
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				
			} else {
				
				
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23
				);
			}
		}
		return true;
	}

    //Setea permisos GPS
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode)
        {
            case 1:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (!checkCameraPermission())
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 2);
				}
                else{/*permission denied, boo! Disable the functionality that depends on this permission.*/}

                return;
            }
            case 2:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    checkWritePermission();
                }
                else{/*permission denied, boo! Disable the functionality that depends on this permission.*/}

                return;
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        //Se obtienen los datos
        final EditText email = (EditText) findViewById(R.id.nombreUsuario);
        final EditText password = (EditText) findViewById(R.id.password);
        SharedPreferences prefs = this.getSharedPreferences("cl.jfcor.fastbilling", Context.MODE_PRIVATE);
        prefs.edit().putString("cl.jfcor.fastbilling.usuario", email.getText().toString()).apply();
        prefs.edit().putString("cl.jfcor.fastbilling.password", password.getText().toString()).apply();
        prefs.edit().putString("cl.jfcor.fastbilling.login", "0").apply();

        //Se abre la bd
        this.bd.abrir();
        this.usuario = this.bd.buscarUsuario(email.getText().toString());
        //this.bd.cerrar();
        if(this.admin.isChecked())
        {
            if (this.usuario != null && usuario.validaLogin(email.getText().toString(), password.getText().toString()))
            {
                if(this.usuario.getPerfilId() == 1)
                {
                    Intent menuAdmin = new Intent(MainActivity.this, MenuAdmin.class);
                    startActivity(menuAdmin);
                }
                else
                {
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                    dialogo.setMessage("Login incorrecto");
                    dialogo.setMessage("Este usuario no es administrador");
                    dialogo.setCancelable(true);
                    dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alerta = dialogo.create();
                    alerta.show();
                }
            }
            else
            {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setMessage("Login incorrecto");
                if (this.usuario == null)
                    dialogo.setMessage("Ingrese usuario y contraseña");

                else
                {
                    if (!this.usuario.getEmail().equals(email.getText().toString()))
                        dialogo.setMessage("Usuario incorrecto");

                    if (!this.usuario.getPassword().equals(password.getText().toString()))
                        dialogo.setMessage("Contraseña incorrecta");
                }
                dialogo.setCancelable(true);
                dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alerta = dialogo.create();
                alerta.show();
            }
        }

        if( this.ejecutivoTerreno.isChecked() )
        {
            if(this.usuario != null && this.usuario.getPassword().equals(" "))
            {
                API carga = new API(this);
                carga.login(email.getText().toString(), password.getText().toString());
            }
            if(this.usuario != null && this.usuario.validaLogin(email.getText().toString(), password.getText().toString()))
            {
				API carga = new API(this);
				carga.login(email.getText().toString(), password.getText().toString());
                //Si login correcto muestra lista de UL
                if(this.usuario.getPerfilId() == 6 || this.usuario.getPerfilId() == 7) {
                    Intent mostrar = new Intent(MainActivity.this, MenuUsuario.class);//new Intent(MainActivity.this, RutasFragment.class);
                    startActivity(mostrar);
                }
            }
            else
            {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setMessage("Login incorrecto");
                if (this.usuario == null)
                    dialogo.setMessage("Ingrese usuario y contraseña");

                else
                {
                    if (!this.usuario.getEmail().equals(email.getText().toString()))
                        dialogo.setMessage("Usuario incorrecto");

                    if (!this.usuario.getPassword().equals(password.getText().toString()))
                        dialogo.setMessage("Contraseña incorrecta");
                }
                dialogo.setCancelable(true);
                dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alerta = dialogo.create();
                alerta.show();
            }
        }
    }
}

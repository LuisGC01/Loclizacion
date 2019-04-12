package com.example.usuario_06.loclizacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {
    MediaPlayer m;
    LocationManager GPS;
    TextView txtGps;
    Location l1;
    Location l2;
    ConstraintLayout fondo;
    float d;
    SmsManager sms = SmsManager.getDefault();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtGps = (TextView) findViewById(R.id.txtGps);
        fondo=(ConstraintLayout)findViewById(R.id.fondo);
        m = MediaPlayer.create(this, R.raw.musica);
        try {
            GPS = (LocationManager) getSystemService(LOCATION_SERVICE);
        } catch (Exception e) {
            txtGps.setText("El error es " + e.getMessage());
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        GPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

        l1= new Location("Su salon");
        l1.setLatitude(19.474396398290992);
        l1.setLongitude(-99.04601695016026);
        l1.setAltitude(2234.0);
        l2=new Location("Punto en el que se esta moviendo");



        new CountDownTimer(5000, 1) {

            @Override
            public void onTick(long l) {
                if (!m.isPlaying()) {
                    m.start();
                }

            }

            @Override
            public void onFinish() {
                m.stop();
            }
        }.start();
    }

    @Override
    public void onLocationChanged(Location location) {

        l2.setLongitude(location.getLongitude());
        l2.setLatitude(location.getLatitude());
        d = l1.distanceTo(l2);
        try {
            txtGps.setText("\naltitudud: " +
                    location.getAltitude() + " \nlongitud: " +
                    location.getLongitude() + " \nlatitud: " +
                    location.getLatitude() +
                    "\nvelocidad: " + location.getSpeed() +
                    "\nDistancia al punto: "+d);
        }catch (Exception e){
            txtGps.setText("El error es "+e.getMessage());
        }

        if(d>10){
            fondo.setBackgroundColor(Color.RED);
            sms.sendTextMessage("5513894675",null,"Obligame PRRO",null,null);
            txtGps.setText("\naltitudud: " +
                    location.getAltitude() + " \nlongitud: " +
                    location.getLongitude() + " \nlatitud: " +
                    location.getLatitude() +
                    "\nvelocidad: " + location.getSpeed() +
                    "\nDistancia al punto: "+d+"\nSe envio el mensaje");
        }else{
            fondo.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        txtGps.setBackgroundColor(Color.GREEN);
        txtGps.setText("Recibiendo informacion del gps");
    }

    @Override
    public void onProviderDisabled(String s) {
        txtGps.setBackgroundColor(Color.RED);
        txtGps.setText("Por favor enciende tu gps");
    }
}

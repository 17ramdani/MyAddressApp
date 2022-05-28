package com.ramdani.mylatlongdres;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude , longitude;
    Button btnShowAddress;
    TextView tvAddress;
    Location location;
    ApplicationService appLocationService;
    @Override
    @SuppressWarnings("deprecation") protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAddress = findViewById(R.id.tvAddress);
        appLocationService = new ApplicationService(MainActivity.this);
        btnShowAddress = findViewById(R.id.btnShowAddress);
        btnShowAddress.setOnClickListener(new
         View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { OnGPS();
                } else {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    } else {
                        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (locationGPS != null) {
                            double latitude = locationGPS.getLatitude();
                            double longitude = locationGPS.getLongitude();
                            LocationAddress locationAddress = new LocationAddress();
                            locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(),
                                    new GeoCodeHandler()); } else {
                            Toast toast = Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG);toast.show();
                        }
                    }
                }
            }
            class GeoCodeHandler extends Handler {
                @Override
                public void handleMessage(Message message) {
                    String locationAddress;
                    switch (message.what) {

                        case 1:
                            Bundle bundle = message.getData();
                            locationAddress = bundle.getString("address");
                            break;
                            default: locationAddress = null;
                    }
                    tvAddress.setText(locationAddress);
                }
            }
        });
    }
    private void OnGPS() {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

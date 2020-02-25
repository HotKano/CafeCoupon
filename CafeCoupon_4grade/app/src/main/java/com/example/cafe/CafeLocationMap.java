package com.example.cafe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CafeLocationMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Context mContext;
    LocationManager locationManager2;
    ImageButton btn1;
    int dialogState = 0;
    RequestQueue requestQueue;
    String x, y;
    double t1, t2;
    double lat, lon;
    ImageButton btn;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafe_location_map);
        mContext = this;

        x = getIntent().getStringExtra("x");
        y = getIntent().getStringExtra("y");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager2 = (LocationManager) getSystemService(LOCATION_SERVICE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btn1 = (ImageButton) findViewById(R.id.btn1);
        btn = (ImageButton) findViewById(R.id.backbutton);

        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn1.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        btn1.setAlpha(255);
                        try {
                            checkLocation();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return false;

                }
                return true;
            }
        });


        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        btn.setAlpha(255);
                        onBackPressed();
                        return false;

                }
                return true;
            }
        });

    }

    public void checkLocation() {

        Toast.makeText(mContext, "위치를 검색중입니다.\n지연시 새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CafeLocationMap.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


        }


        if (locationManager2.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            locationManager2.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {

                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();

                    final LatLng latLng = new LatLng(latitude, longitude);

                    try {

                        if (latLng.latitude != 0.0 & latLng.longitude != 0.0) {

                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(latLng).title("현재 위치"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                  /*          mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    Toast.makeText(mContext, "위도" + latitude + "\n" + "경도" + longitude, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });*/
                            lat = latitude;
                            lon = longitude;

                            LatLng cafe = new LatLng(Double.parseDouble(x), Double.parseDouble(y));
                            mMap.addMarker(new MarkerOptions().position(cafe).title("카페 위치"));

                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            locationManager2.removeUpdates(this);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (locationManager2.isProviderEnabled(LocationManager.GPS_PROVIDER))

        {


            locationManager2.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {


                @Override
                public void onLocationChanged(final Location location) {

                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();

                    final LatLng latLng = new LatLng(latitude, longitude);

                    try {

                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("현재 위치"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Toast.makeText(mContext, "위도" + latitude + "\n" + "경도" + longitude, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });*/

                        lat = latitude;
                        lon = longitude;

                        LatLng cafe = new LatLng(Double.parseDouble(x), Double.parseDouble(y));
                        mMap.addMarker(new MarkerOptions().position(cafe).title("카페 위치"));

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        locationManager2.removeUpdates(this);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (!locationManager2.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

        {
            showSettingsAlert();
            if (t1 != 0.0 && t2 != 0.0) {
                final LatLng latLng = new LatLng(t1, t2);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("최근 위치"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }

        } else if (!locationManager2.isProviderEnabled(LocationManager.GPS_PROVIDER))

        {
            showSettingsAlert();
            if (t1 != 0.0 && t2 != 0.0) {
                final LatLng latLng = new LatLng(t1, t2);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("최근 위치"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CafeLocationMap.this,
                            "위치 확인을 위해 새로고침을 눌러주세요. :)",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(CafeLocationMap.this,
                            "지도 기능을 이용하실 수 없습니다. ...:(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng cafe = new LatLng(Double.parseDouble(x), Double.parseDouble(y));
        mMap.addMarker(new MarkerOptions().position(cafe).title("카페 위치"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(cafe));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cafe, 16));

        Toast.makeText(mContext, "자신의 위치 확인은 새로 고침을 눌러주세요.", Toast.LENGTH_SHORT).show();

    }

    public void showSettingsAlert() {
        dialogState = 0;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("본 어플은 Gps 사용을 권장하고 있습니다.\n비 활성화시 일부 기능이 사용 불가능합니다.");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("활성화", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        // Cancel 하면 종료 합니다.
        alertDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        alertDialog.show();


    }

    protected void onStop() {
        super.onStop();
    }

    protected void onPause() {
        super.onPause();

    }

    protected void onResume() {
        super.onResume();


    }

    protected void onStart() {
        super.onStart();


    }


}

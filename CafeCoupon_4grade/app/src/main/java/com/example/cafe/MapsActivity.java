package com.example.cafe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Context mContext;
    LocationManager locationManager2;
    ImageButton btn1, btn2;
    int dialogState = 0;
    RequestQueue requestQueue;
    String currentLocationAddress, map, idByANDROID_ID;
    double t1, t2;
    double lat, lon;
    ImageButton btn;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mContext = this;
        main = new MainActivity();
        map = getIntent().getStringExtra("map");
        t1 = getIntent().getDoubleExtra("lat1", 0.0);
        t2 = getIntent().getDoubleExtra("lon1", 0.0);
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager2 = (LocationManager) getSystemService(LOCATION_SERVICE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btn1 = (ImageButton) findViewById(R.id.btn1);
        btn2 = (ImageButton) findViewById(R.id.resultBtn);
        btn = (ImageButton) findViewById(R.id.backbutton);


        if (map != null && map.equals("ddd"))
            btn2.setImageResource(R.drawable.setting_string);

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

        btn2.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                btn2.setAlpha(50);
                                                return true;

                                            case MotionEvent.ACTION_UP:
                                                btn2.setAlpha(255);
                                                if (map == null) {

                                                    if (lat != 0.0 && lon != 0.0) {
                                                        String address = currentLocationAddress.substring(5);
                                                        Intent intent = new Intent();
                                                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MainActivity");
                                                        intent.setComponent(name);
                                                        intent.putExtra("address", address);
                                                        intent.putExtra("lat", lat);
                                                        intent.putExtra("lon", lon);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(mContext, "위치 정보 확인 중입니다. \n반응이 없을 시 새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show();
                                                    }

                                                } else {
                                                    if (lat != 0.0 && lon != 0.0) {
                                                        Intent intent = new Intent();
                                                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MainActivity");
                                                        intent.putExtra("map", "ddd");
                                                        intent.setComponent(name);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        startActivity(intent);
                                                        InsertRecord.allDeleteRecord("location");
                                                        InsertRecord.insertRecord(String.valueOf(lat), String.valueOf(lon));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(mContext, "위치 정보 확인 중입니다. \n반응이 없을 시 새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    return false;
                                                }
                                        }
                                        return true;
                                    }
                                }

        );

        btn.setOnTouchListener(new View.OnTouchListener()

                               {
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
                               }

        );

    }

    public void checkLocation() {

        Toast.makeText(mContext, "위치를 검색중입니다.\n지연시 새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this,
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
                    mMap.clear();

                    try {

                        if (latLng.latitude != 0.0 & latLng.longitude != 0.0) {


                            mMap.addMarker(new MarkerOptions().position(latLng).title("현재 위치"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                 /*           mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    Toast.makeText(mContext, "위도" + latitude + "\n" + "경도" + longitude, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });*/
                            findAddress(latitude, longitude);
                            lat = latitude;
                            lon = longitude;
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


                        mMap.addMarker(new MarkerOptions().position(latLng).title("현재 위치"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                /*        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Toast.makeText(mContext, "위도" + latitude + "\n" + "경도" + longitude, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });*/
                        findAddress(latitude, longitude);
                        lat = latitude;
                        lon = longitude;
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
                    Toast.makeText(MapsActivity.this,
                            "새로고침을 눌러주세요.",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MapsActivity.this,
                            "지도 기능을 비활성화하였습니다.",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // 여수 기본 지도
        LatLng current = new LatLng(34.77364811535571, 127.70157304429677);
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16));

        try {
            checkLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    currentLocationAddress = address.get(0).getAddressLine(0).toString();


                } else {
                    address = geocoder.getFromLocation(t1, t2, 1);
                    if (address != null && address.size() > 0) {
                        // 주소
                        currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("주소 취득 실패");

            e.printStackTrace();
        }
        return bf.toString();
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

package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.cafe.MapsActivity.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static java.lang.Double.parseDouble;

/**
 * Created by Dr.kim on 2017-05-29.
 */

public class SearchActivity extends Fragment {
    ViewGroup rootView;
    TextView textView;
    ImageButton Mapbtn, refreshBtn;
    double lat, lon;
    String showUrl = "http://dpsw23.dothome.co.kr/4grade/cafeShow.php";
    RequestQueue requestQueue;
    ListView listview1;
    IconTextListAdapter adapter;
    String value, currentLocationAddress;
    LocationManager locationManager;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_search, container, false);
        Mapbtn = (ImageButton) rootView.findViewById(R.id.mapbtn);
        refreshBtn = (ImageButton) rootView.findViewById(R.id.refreshbtn);
        textView = (TextView) rootView.findViewById(R.id.txt1);
        requestQueue = Volley.newRequestQueue(getActivity());
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        showMet();
        setTextz2();

        adapter = new IconTextListAdapter(getActivity());
        listview1 = (ListView) rootView.findViewById(R.id.member);

        Mapbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Mapbtn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        Mapbtn.setAlpha(255);
                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MapsActivity");
                        intent.setComponent(name);
                        startActivity(intent);
                        return false;

                }
                return true;
            }
        });


        refreshBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        refreshBtn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        refreshBtn.setAlpha(255);
                        try {
                            checkLocation();
                            Sort();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;

                }
                return true;
            }
        });


        return rootView;
    }

    public String calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double EARTH_R, Rad, radLat1, radLat2, radDist;
        double distance, ret;
        String dis;

        EARTH_R = 6371000.0;
        Rad = Math.PI / 180;
        radLat1 = Rad * lat1;
        radLat2 = Rad * lat2;
        radDist = Rad * (lon1 - lon2);

        distance = Math.sin(radLat1) * Math.sin(radLat2);
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist);
        ret = EARTH_R * Math.acos(distance);

        double rtn = Math.round(Math.round(ret) / 1000);

        if (rtn <= 0) {
            rtn = Math.round(ret);
            dis = rtn + "m";
            return dis;
        } else {

            //result2.setText(rtn + " km");
            dis = rtn + "Km";
            return dis;
        }

    }

    protected void showMet() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                AddData(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    protected void AddData(JSONObject response) {

        adapter.mItems.clear();

        try {
            JSONArray students = response.getJSONArray("names");
            for (int i = 0; i < students.length(); i++) {
                JSONObject student = students.getJSONObject(i);

                String cafe = student.getString("cafe");
                String name = student.getString("name");
                String comment = student.getString("comment");
                String phone = student.getString("phone");
                String address = student.getString("address");

                String outside = student.getString("cafe_outside");
                String inside = student.getString("cafe_inside");

                String americano = student.getString("americano");
                String latte = student.getString("latte");
                String ade = student.getString("ade");
                String dessert = student.getString("dessert");
                String ice = student.getString("ice");

                String openclose = student.getString("openclose");

                double lat2 = student.getDouble("x");
                double lon2 = student.getDouble("y");

                if (lat != 0.0 && lon != 0.0) {
                    String test = calcDistance(lat, lon, lat2, lon2);
                    adapter.addItem(new IconTextItem(cafe, name, comment, test, phone, address, String.valueOf(lat2), String.valueOf(lon2), americano, latte, ade, dessert, ice, openclose, outside, inside));

                } else {

                    String x = InsertRecord.stateTestX();
                    String y = InsertRecord.stateTestY();
                    String test;

                    if (x != "" && y != "") {
                        test = calcDistance(parseDouble(x), parseDouble(y), lat2, lon2);
                    } else
                        test = calcDistance(34.7736233, 127.7015145, lat2, lon2);

                    adapter.addItem(new IconTextItem(cafe, name, comment, test, phone, address, String.valueOf(lat2), String.valueOf(lon2), americano, latte, ade, dessert, ice, openclose, outside, inside));

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        listview1.setAdapter(adapter);
        listview1.setFocusable(false);
        Sort();

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconTextItem curItem = (IconTextItem) adapter.getItem(position);
                String[] curData = curItem.getData();

                Intent intent = new Intent();
                ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.CafeInfoActivity");
                intent.putExtra("cafe", curData[0]);
                intent.putExtra("title", curData[1]);
                intent.putExtra("comment", curData[2]);
                intent.putExtra("phone", curData[4]);
                intent.putExtra("address", curData[5]);
                intent.putExtra("x", curData[6]);
                intent.putExtra("y", curData[7]);
                intent.putExtra("americano", curData[8]);
                intent.putExtra("latte", curData[9]);
                intent.putExtra("ade", curData[10]);
                intent.putExtra("dessert", curData[11]);
                intent.putExtra("ice", curData[12]);
                intent.putExtra("openclose", curData[13]);
                intent.putExtra("inside", curData[15]);
                intent.setComponent(name);
                startActivity(intent);

            }
        });
    }

    public void setTextz(Intent intent) {

        Bundle extras = intent.getExtras();

        if (extras != null) {
            value = extras.getString("address");
            lat = extras.getDouble("lat");
            lon = extras.getDouble("lon");

            if (value != null && lat != 0.0 && lon != 0.0) {
                textView.setText(value);
                adapter.mItems.clear();
                showMet();

            }

        }
    }

    public void setTextz2() {

        if (value != null) {
            textView.setText(value);

            try {
                Bundle args = getActivity().getIntent().getExtras();
                value = args.getString("test");
                lat = args.getDouble("lat");
                lon = args.getDouble("lon");

                if (lat == 0.0 && lon == 0.0) {
                    lat = ((MainActivity) getContext()).latM;
                    lon = ((MainActivity) getContext()).lonM;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (currentLocationAddress != null) {

            String text = currentLocationAddress.substring(5);
            textView.setText(text);
        }
    }

    public void Sort() {


        Comparator<IconTextItem> noAsc = new Comparator<IconTextItem>() {
            @Override
            public int compare(IconTextItem item1, IconTextItem item2) {
                int ret;

                String it1 = item1.getData(3);
                String it2 = item2.getData(3);

                int k1 = it1.indexOf("K");
                int k2 = it2.indexOf("K");

                int m1 = it1.indexOf("m");
                int m2 = it2.indexOf("m");


                if (k1 == -1 && k2 == -1) {
                    String o1 = it1.substring(0, m1);
                    String o2 = it2.substring(0, m2);

                    double t1 = parseDouble(o1);
                    double t2 = parseDouble(o2);

                    if (t1 < t2)
                        ret = -1;
                    else if (t1 == t2)
                        ret = 0;
                    else
                        ret = 1;

                    return ret;

                } else if (k1 != -1 && k2 == -1) {
                    ret = 1;

                    return ret;
                } else if (k1 == -1 && k2 != -1) {
                    ret = -1;

                    return ret;
                } else {
                    String o1 = it1.substring(0, k1);
                    String o2 = it2.substring(0, k2);

                    double t1 = parseDouble(o1);
                    double t2 = parseDouble(o2);

                    if (t1 < t2)
                        ret = -1;
                    else if (t1 == t2)
                        ret = 0;
                    else
                        ret = 1;

                    return ret;

                }


            }
        };

        Collections.sort(adapter.mItems, noAsc);
        adapter.notifyDataSetChanged();

    }

    private void checkLocation() {


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


        }


        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

        {


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {

                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();

                    final LatLng latLng = new LatLng(latitude, longitude);

                    try {

                        if (latLng.latitude != 0.0 & latLng.longitude != 0.0) {

                            lat = latitude;
                            lon = longitude;
                            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling

                            }

                            showMet();
                            findAddress(lat, lon);

                            locationManager.removeUpdates(this);


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
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))

        {


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {


                @Override
                public void onLocationChanged(final Location location) {

                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();

                    final LatLng latLng = new LatLng(latitude, longitude);

                    try {

                        if (latLng.latitude != 0.0 & latLng.longitude != 0.0) {

                            lat = latitude;
                            lon = longitude;
                            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling

                                return;
                            }

                            showMet();
                            findAddress(lat, lon);

                            locationManager.removeUpdates(this);


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
        } else if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

        {


        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))

        {

        }

    }

    public String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(getActivity(), Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    String text = currentLocationAddress.substring(5);
                    textView.setText(text);


                }
            }

        } catch (IOException e) {
            System.out.println("주소 취득 실패");

            e.printStackTrace();
        }
        return bf.toString();
    }

}

package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dr.kim on 2017-05-28. 123123123
 */

public class CouponActivity extends Fragment {

    CouponTextListAdapter adapter; // test
    RequestQueue requestQueue;
    String insertUrl = "http://dpsw23.dothome.co.kr/4grade/coupon_list_all.php";
    String insertUrl1 = "http://dpsw23.dothome.co.kr/4grade/coupon_list.php";
    String insertUrl2 = "http://dpsw23.dothome.co.kr/4grade/coupon_list_stamp.php";
    ListView listview1;
    ViewGroup rootView;
    String idByANDROID_ID;
    TextView emptyText;
    int state_Met;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_coupon, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        idByANDROID_ID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        emptyText = (TextView) rootView.findViewById(R.id.emptytext);
        state_Met = 0;

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        registerForContextMenu(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().openContextMenu(view);
            }
        });

        try {
            sendMet();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return rootView;
    }

    public void sendMet() {
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String state = null;

                try {
                    JSONArray jArray = new JSONArray(response);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        state = jObject.getString("state");
                    }

                    if (state.equals("success")) {
                        state_Met = 1;
                        doJSONParser(response.toString());
                        listview1.setVisibility(View.VISIBLE);
                        emptyText.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (state == null) {
                    emptyText.setVisibility(View.VISIBLE);
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", idByANDROID_ID);


                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void sendMet1() {
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String state = null;

                try {
                    JSONArray jArray = new JSONArray(response);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        state = jObject.getString("state");
                    }

                    if (state.equals("success")) {
                        state_Met = 2;
                        doJSONParser(response.toString());
                        listview1.setVisibility(View.VISIBLE);
                        emptyText.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (state == null) {
                    emptyText.setVisibility(View.VISIBLE);
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", idByANDROID_ID);


                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void sendMet2() {
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String state = null;

                try {
                    JSONArray jArray = new JSONArray(response);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        state = jObject.getString("state");
                    }

                    if (state.equals("success")) {
                        state_Met = 3;
                        doJSONParser(response.toString());
                        listview1.setVisibility(View.VISIBLE);
                        emptyText.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (state == null) {
                    emptyText.setVisibility(View.VISIBLE);
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", idByANDROID_ID);


                return parameters;
            }
        };
        requestQueue.add(request);
    }

    void doJSONParser(String response) {

        StringBuffer sb = new StringBuffer();
        listview1 = (ListView) rootView.findViewById(R.id.couponList);
        adapter = new CouponTextListAdapter(getActivity());

        adapter.mItems.clear();

        try {
            JSONArray jArray = new JSONArray(response);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String cafe = jObject.getString("cafe");
                String cou_num = String.valueOf(jObject.getInt("cou_num"));
                String coupon = jObject.getString("date");
                String used = jObject.getString("used");
                String name = jObject.getString("name");

                adapter.addItem(new CouponTextItem(cafe, cou_num, coupon, used, name));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CouponTextItem curItem = (CouponTextItem) adapter.getItem(position);
                String[] curData = curItem.getData();

                Intent intent = new Intent();
                ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.ActivityCouponInfo");
                intent.setComponent(name);
                intent.putExtra("cafe", curData[0]);
                intent.putExtra("name", curData[4]);
                startActivity(intent);

            }
        });

        listview1.setAdapter(adapter);
        listview1.setFocusable(false);


    }

    public void refresh() {
        if (state_Met == 1)
            sendMet();
        else if (state_Met == 2)
            sendMet1();
        else if (state_Met == 3)
            sendMet2();
        else
            sendMet();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 컨텍스트 메뉴가 최초로 한번만 호출되는 콜백 메서드
        Log.d("test", "onCreateContextMenu");
//        getMenuInflater().inflate(R.menu.main, menu);

        menu.setHeaderTitle("쿠폰 정렬").setHeaderIcon(R.drawable.onlylogo);
        menu.add(0, 1, 100, "모두 보기");
        menu.add(0, 2, 100, "사용 가능 순");
        menu.add(0, 3, 100, "자주 이용 순");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 롱클릭했을 때 나오는 context Menu 의 항목을 선택(클릭) 했을 때 호출

        switch (item.getItemId()) {
            case 1:
                sendMet();
                return true;
            case 2:
                sendMet1();
                return true;
            case 3:
                sendMet2();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}

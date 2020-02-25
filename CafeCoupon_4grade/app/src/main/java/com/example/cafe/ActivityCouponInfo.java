package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dr.kim on 2017-06-05.
 */

public class ActivityCouponInfo extends AppCompatActivity {


    String showUrl = "http://dpsw23.dothome.co.kr/4grade/coupon.php";
    RequestQueue requestQueue;
    private ListView listview;
    CouponInfoTextListAdapter adapter;
    String idByANDROID_ID;
    String cafeType;
    ImageButton backbtn;
    ImageView titleText2;
    String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_info);
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        requestQueue = Volley.newRequestQueue(this);
        cafeType = getIntent().getStringExtra("cafe");

        titleText2 = (ImageView) findViewById(R.id.titleText2);
        if (cafeType.equals("1"))
            Glide.with(this).load(R.drawable.subito_coupon_title).into(titleText2);
        else if (cafeType.equals("2"))
            Glide.with(this).load(R.drawable.coffeeya_coupon_title).into(titleText2);
        else if (cafeType.equals("3"))
            Glide.with(this).load(R.drawable.star_coupon_title).into(titleText2);
        else if (cafeType.equals("4"))
            Glide.with(this).load(R.drawable.soso_title).into(titleText2);

        backbtn = (ImageButton) findViewById(R.id.backbutton);

        backbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        backbtn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        backbtn.setAlpha(255);
                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MainActivity");
                        intent.setComponent(name);
                        intent.putExtra("page1", "page1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                        return false;

                }
                return true;
            }
        });

        sendMet();

    }

    protected void sendMet() {
        StringRequest request = new StringRequest(Request.Method.POST, showUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    doJSONParser(response.toString());


                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", idByANDROID_ID);
                parameters.put("cafe", cafeType);


                return parameters;
            }
        };
        requestQueue.add(request);
    }

    void doJSONParser(String response) {

        listview = (ListView) findViewById(R.id.list1);
        adapter = new CouponInfoTextListAdapter(this);

        try {
            JSONArray jArray = new JSONArray(response);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String stamp_num = jObject.getString("stamp_num");
                String cafe = jObject.getString("cafe");
                String cou_num = jObject.getString("cou_num");
                String used = jObject.getString("used");
                String date = jObject.getString("date");
                String date_used = jObject.getString("date_used");

                String cou1 = jObject.getString("cou_memo1");
                String cou2 = jObject.getString("cou_memo2");
                String cou3 = jObject.getString("cou_memo3");
                String cou4 = jObject.getString("cou_memo4");
                String cou5 = jObject.getString("cou_memo5");
                String cou6 = jObject.getString("cou_memo6");
                String cou7 = jObject.getString("cou_memo7");
                String cou8 = jObject.getString("cou_memo8");
                String cou9 = jObject.getString("cou_memo9");
                String cou10 = jObject.getString("cou_memo10");

                adapter.addItem(new CouponInfoTextItem(cafe, cou_num, date, used, date_used, stamp_num, cou1, cou2, cou3, cou4, cou5, cou6, cou7, cou8, cou9, cou10));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        listview.setAdapter(adapter);
        listview.setFocusable(false);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MainActivity");
        intent.setComponent(name);
        intent.putExtra("page1", "page1");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String cafe = extras.getString("cafe");
            cafeType = cafe;
            sendMet();

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sendMet();
    }
}

package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dr.kim on 2017-05-28.
 */

public class LogoActivity extends AppCompatActivity {

    int timer_total;
    private TimerTask second;
    Timer timer;
    String loginUrl = "http://dpsw23.dothome.co.kr/4grade/login.php";
    String idByANDROID_ID;
    RequestQueue requestQueue;
    String point;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        timer = new Timer();
        timer_total = 0;
        testStart();

        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public void testStart() {

        second = new TimerTask() {

            @Override
            public void run() {


                timer_total++;
                if (timer_total >= 3) {
                    loginMet();
                    timer.cancel();
                }
            }
        };

        timer.schedule(second, 0, 1000);
    }

    protected void loginMet() {
        StringRequest request = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    String text = JsonUtil.getStringFrom(jsonOutput, "state");
                    String pointz = JsonUtil.getStringFrom(jsonOutput, "pushState");
                    point = pointz;

                    if (point.equals("1"))
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                    else
                        FirebaseMessaging.getInstance().subscribeToTopic("news");


                    if (text.equals("fail")) {
                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.RegisterActivity");
                        intent.setComponent(name);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MainActivity");
                        intent.setComponent(name);
                        intent.putExtra("point", point);
                        startActivity(intent);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LogoActivity.this, "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
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

}

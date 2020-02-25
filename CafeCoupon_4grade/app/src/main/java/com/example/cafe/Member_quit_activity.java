package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dr.kim on 2017-06-27.
 */

public class Member_quit_activity extends AppCompatActivity {

    Button btn1;
    RequestQueue requestQueue;
    String idByANDROID_ID;
    String deleteUrl = "http://dpsw23.dothome.co.kr/4grade/member_quit.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_quit);
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btn1 = (Button) findViewById(R.id.submit);

        quitMet();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.LogoActivity");
                intent.setComponent(name);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void quitMet() {
        StringRequest request = new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    String text = JsonUtil.getStringFrom(jsonOutput, "state");

                    if (text.equals("fail")) {
                        Toast.makeText(Member_quit_activity.this, "탈퇴가 정상처리 되지 않았습니다.\n다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Member_quit_activity.this, "탈퇴가 정상적으로 처리 되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Member_quit_activity.this, "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
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

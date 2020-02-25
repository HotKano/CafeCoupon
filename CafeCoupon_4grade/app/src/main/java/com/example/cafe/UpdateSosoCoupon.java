package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
 * Created by Dr.kim on 2017-05-29.
 */

public class UpdateSosoCoupon extends AppCompatActivity {


    String insertUrl = "http://dpsw23.dothome.co.kr/4grade/Cou_Subito_update.php";
    Button update, close;
    RequestQueue requestQueue;
    String idByANDROID_ID;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_subito_coupon);
        update = (Button) findViewById(R.id.btnUpdate);
        close = (Button) findViewById(R.id.btnClose);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        textView = (TextView) findViewById(R.id.state_text);

        sendMet();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.ActivityCouponInfo");
                intent.setComponent(name);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("cafe", "4");
                startActivity(intent);
                finish();

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    protected void sendMet() {
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response.toString());
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    String text = JsonUtil.getStringFrom(jsonOutput, "state");

                    if (!text.equals("fail")) {
                        Toast.makeText(UpdateSosoCoupon.this, "적립이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        textView.setText("적립이 완료되었습니다.");
                    }


                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                textView.setText("적립에 실패하였습니다.\n인터넷 환경을 확인해주세요.");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("number", "1");
                parameters.put("id", idByANDROID_ID);
                parameters.put("cafe", "4");


                return parameters;
            }
        };
        requestQueue.add(request);
    }


}

package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
 * Created by Dr.kim on 2017-05-25.
 */

public class RegisterActivity extends AppCompatActivity {

    EditText name, email;
    ImageButton send;

    RequestQueue requestQueue;
    String insertUrl = "http://dpsw23.dothome.co.kr/4grade/insert.php";
    String loginUrl = "http://dpsw23.dothome.co.kr/4grade/login.php";
    String idByANDROID_ID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.emailedit);
        send = (ImageButton) findViewById(R.id.btnSend);

        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send.setAlpha(0);
                        return true;

                    case MotionEvent.ACTION_UP:
                        send.setAlpha(255);
                        if (name.getText().toString().equals(""))
                            Toast.makeText(RegisterActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        else if (email.getText().toString().equals(""))
                            Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                        else
                            sendMet();
                        return false;

                }
                return true;
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

                    if (text.equals("fail1")) {

                        Toast.makeText(getApplicationContext(), "회원가입 된 유저입니다. 로그인합니다.", Toast.LENGTH_SHORT).show();
                        loginMet();

                    } else if (text.equals("fail2")) {
                        Toast.makeText(getApplicationContext(), "닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MainActivity");
                        intent.setComponent(name);
                        startActivity(intent);
                        finish();
                    }


                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", idByANDROID_ID);
                parameters.put("name", name.getText().toString());
                parameters.put("mail", email.getText().toString());

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    protected void loginMet() {
        StringRequest request = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    String text = JsonUtil.getStringFrom(jsonOutput, "state");

                    if (text.equals("fail")) {
                        Toast.makeText(RegisterActivity.this, "아이디가 없습니다.", Toast.LENGTH_SHORT).show();
                    } else if (text.equals("fail1")) {
                        Toast.makeText(RegisterActivity.this, "비밀 번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MainActivity");
                        intent.setComponent(name);
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
                Toast.makeText(RegisterActivity.this, "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
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
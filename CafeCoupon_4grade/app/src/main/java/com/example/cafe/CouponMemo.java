package com.example.cafe;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
 * Created by Dr.kim on 2017-06-23.
 */

public class CouponMemo extends AppCompatActivity {


    RequestQueue requestQueue;

    TextView couDate, length;
    ImageButton send, delete;
    ImageButton backbutton;
    EditText memozone;
    String idByANDROID_ID, stamp_number, cou_number, cafe;

    String showUrl = "http://dpsw23.dothome.co.kr/4grade/Memo_show.php";
    String sendUrl = "http://dpsw23.dothome.co.kr/4grade/Memo_insert.php";
    String deleteUrl = "http://dpsw23.dothome.co.kr/4grade/Memo_delete.php";

    String edit;
    int state;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_memo);
        requestQueue = Volley.newRequestQueue(this);
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        stamp_number = getIntent().getStringExtra("stamp_number");
        cou_number = getIntent().getStringExtra("cou_number");
        cafe = getIntent().getStringExtra("cafe");

        backbutton = (ImageButton) findViewById(R.id.backbutton);
        send = (ImageButton) findViewById(R.id.btnSend);
        delete = (ImageButton) findViewById(R.id.btnDelete);
        memozone = (EditText) findViewById(R.id.memo_zone);
        couDate = (TextView) findViewById(R.id.couDate);
        length = (TextView) findViewById(R.id.length);

        edit = memozone.getText().toString();

        state = memozone.getText().toString().length();
        length.setText(state + "/255");

        memozone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                state = memozone.getText().toString().length();
                length.setText(state + "/255");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        send.setAlpha(255);
                        if (!ReviewWrite.TextCheckHelper.isBlankOrSpacing(memozone.getText().toString()))
                            SendMet();
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), ActivityCouponInfo.class);
                        intent.putExtra("cafe", cafe);
                        startActivity(intent);

                        return false;

                }
                return true;
            }
        });

        delete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        delete.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        delete.setAlpha(255);
                        if (!ReviewWrite.TextCheckHelper.isBlankOrSpacing(memozone.getText().toString()))
                            DeleteMet();
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), ActivityCouponInfo.class);
                        intent.putExtra("cafe", cafe);
                        startActivity(intent);

                        return false;

                }
                return true;
            }
        });

        backbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        backbutton.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        backbutton.setAlpha(255);

                        if (!ReviewWrite.TextCheckHelper.isBlankOrSpacing(memozone.getText().toString()))
                            SendMet();

                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), ActivityCouponInfo.class);
                        intent.putExtra("cafe", cafe);
                        startActivity(intent);

                        return false;

                }
                return true;
            }
        });

        try {
            ShowMet();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void ShowMet() {
        StringRequest request = new StringRequest(Request.Method.POST, showUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    String text = JsonUtil.getStringFrom(jsonOutput, "memo");
                    String date = JsonUtil.getStringFrom(jsonOutput, "date");

                    memozone.setText(text);
                    couDate.setText("방문일자 : " + date + " ");


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
                parameters.put("stamp_number", stamp_number);
                parameters.put("cou_number", cou_number);

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    protected void SendMet() {
        StringRequest request = new StringRequest(Request.Method.POST, sendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    String text = JsonUtil.getStringFrom(jsonOutput, "state");

                    if (text.equals("success"))
                        Toast.makeText(CouponMemo.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    else if (text.equals("fail"))
                        Toast.makeText(CouponMemo.this, "저장실패.", Toast.LENGTH_SHORT).show();

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
                parameters.put("stamp_number", stamp_number);
                parameters.put("cou_number", cou_number);
                parameters.put("memo", memozone.getText().toString());

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    protected void DeleteMet() {
        StringRequest request = new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());

                    memozone.setText("");
                    Toast.makeText(CouponMemo.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();


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
                parameters.put("stamp_number", stamp_number);
                parameters.put("cou_number", cou_number);

                return parameters;
            }
        };
        requestQueue.add(request);
    }

}

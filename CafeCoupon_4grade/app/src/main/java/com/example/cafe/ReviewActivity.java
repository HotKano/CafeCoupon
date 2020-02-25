package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
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
 * Created by Dr.kim on 2017-06-26.
 */

public class ReviewActivity extends AppCompatActivity {

    ReviewTextListAdapter adapter;
    ListView listView1;
    String cafe;
    RequestQueue requestQueue;
    ImageButton back;
    ImageButton writebtn;
    TextView emptyText;
    String showUrl = "http://dpsw23.dothome.co.kr/4grade/CafeReview.php";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafe_review_activity);

        requestQueue = Volley.newRequestQueue(this);
        adapter = new ReviewTextListAdapter(this);

        emptyText = (TextView) findViewById(R.id.emptytext);
        writebtn = (ImageButton) findViewById(R.id.writeBtn);
        cafe = getIntent().getStringExtra("cafe");
        listView1 = (ListView) findViewById(R.id.reviewList);
        back = (ImageButton) findViewById(R.id.backbutton);

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        back.setAlpha(255);
                        onBackPressed();

                        return false;

                }
                return true;
            }
        });

        writebtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        writebtn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        writebtn.setAlpha(255);
                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.ReviewWrite");
                        intent.setComponent(name);
                        intent.putExtra("cafe", cafe);
                        startActivity(intent);

                        return false;

                }
                return true;
            }
        });


        try {
            sendMet();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendMet() {
        StringRequest request = new StringRequest(Request.Method.POST, showUrl, new Response.Listener<String>() {
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
                        doJSONParser(response.toString());
                        emptyText.setVisibility(View.GONE);
                        listView1.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (state == null) {
                    emptyText.setVisibility(View.VISIBLE);
                    listView1.setVisibility(View.GONE);
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
                parameters.put("cafe", cafe);

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    void doJSONParser(String response) {

        adapter.clear();

        try {
            JSONArray jArray = new JSONArray(response);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                int no = jObject.getInt("no");
                String no_pot = String.valueOf(no);
                String id = jObject.getString("id");
                String name = jObject.getString("name");
                String comment = jObject.getString("comment");
                String date = jObject.getString("date");
                String profile = jObject.getString("profile");
                String review_img = jObject.getString("review_img");
                String img_state = jObject.getString("img_state");

                adapter.addItem(new ReviewTextItem(id, name, date, comment, profile, cafe, no_pot, review_img, img_state));


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView1.setAdapter(adapter);
        listView1.setFocusable(false);

    }

    public void reFresh() {
        adapter.clear();
        sendMet();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onRestart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sendMet();
    }
}

package com.example.cafe;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.cafe.R.layout.cafe_review_item;

/**
 * 아이템으로 보여줄 뷰 정의
 *
 * @author Mike
 */
public class ReviewTextView extends LinearLayout {

    /**
     * Icon
     */

    /**
     * TextView 01
     */
    private TextView mText01;

    /**
     * TextView 02
     */
    private TextView mText02;

    /**
     * TextView 03
     */
    private TextView mText03;

    ImageView img;

    ImageView img2;

    private ImageButton delete;

    RequestQueue requestQueue;
    String idByANDROID_ID;
    String showUrl = "http://dpsw23.dothome.co.kr/4grade/CafeReviewDelete.php";
    String loginUrl = "http://dpsw23.dothome.co.kr/4grade/login.php";
    String cafe, obj1, profile;

    private Context mContext = null;

    public ReviewTextView(final Context context, final ReviewTextItem aItem) {
        super(context);
        this.mContext = context;

        idByANDROID_ID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        // Layout Inflation
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        requestQueue = Volley.newRequestQueue(getContext());
        inflater.inflate(cafe_review_item, this, true);

        img = (ImageView) findViewById(R.id.faceImg);
        img2 = (ImageView) findViewById(R.id.reviewImg2);


        mText01 = (TextView) findViewById(R.id.nickName);
        mText01.setText(aItem.getData(1));

        mText02 = (TextView) findViewById(R.id.date);
        mText02.setText(aItem.getData(2));

        mText03 = (TextView) findViewById(R.id.comment);
        mText03.setText(aItem.getData(3));

        delete = (ImageButton) findViewById(R.id.btnDelete);
        delete.setFocusable(false);

        if (aItem.getData(0).equals(idByANDROID_ID))
            delete.setVisibility(View.VISIBLE);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cafe = aItem.getData(5);
                obj1 = aItem.getData(6);

                sendMet();
                ((ReviewActivity) mContext).adapter.mItems.clear();
                ((ReviewActivity) mContext).sendMet();
                ((ReviewActivity) mContext).reFresh();

            }
        });

        if (aItem.getData(8).equals("1")) {
            img2.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load("http://dpsw23.dothome.co.kr/4grade/ReviewImg/" + aItem.getData(7) + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img2);
        }

        if (aItem.getData(4).equals("1")) {
            Glide.with(context).load("http://dpsw23.dothome.co.kr/4grade/memberImg/" + aItem.getData(0) + ".jpg").asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });
        }


    }

    public void sendMet() {
        StringRequest request = new StringRequest(Request.Method.POST, showUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                } catch (Exception e) {

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
                parameters.put("cafe", cafe);
                parameters.put("no", obj1);


                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void loginMet(final String namez) {
        StringRequest request = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response.toString());
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    String text = JsonUtil.getStringFrom(jsonOutput, "profile");

                    profile = text;
                    System.out.println(profile + "System");

                    if (profile.equals("1"))
                        Glide.with(getContext()).load("http://dpsw23.dothome.co.kr/4grade/memberImg/" + namez + ".jpg").asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(img) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                img.setImageDrawable(circularBitmapDrawable);
                            }
                        });


                } catch (Exception e) {

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

    /**
     * set Text
     *
     * @param index
     * @param data
     */
    public void setText(int index, String data) {
        if (index == 1) {
            mText01.setText(data);
        } else if (index == 2) {
            mText02.setText(data);
        } else if (index == 3) {
            mText03.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }


}

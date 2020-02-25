package com.example.cafe;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dr.kim on 2017-05-29.
 */

public class SettingActivity extends Fragment {
    ViewGroup rootView;
    RelativeLayout member_zone, txt, txt4, map, txt5;
    ImageButton pushonoff;
    String point = "";
    String loginUrl = "http://dpsw23.dothome.co.kr/4grade/login.php";
    String updateUrl = "http://dpsw23.dothome.co.kr/4grade/login_push.php";
    RequestQueue requestQueue;
    String idByANDROID_ID, profile, nickname;
    String getProfile;
    ImageView img;
    TextView textView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.activity_setting, container, false);
        map = (RelativeLayout) rootView.findViewById(R.id.location);
        member_zone = (RelativeLayout) rootView.findViewById(R.id.member_zone);
        txt = (RelativeLayout) rootView.findViewById(R.id.txtbtn1);
        txt4 = (RelativeLayout) rootView.findViewById(R.id.txtbtn4);
        txt5 = (RelativeLayout) rootView.findViewById(R.id.txtbtn5);
        img = (ImageView) rootView.findViewById(R.id.img);
        textView = (TextView) rootView.findViewById(R.id.settingName);

        idByANDROID_ID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        requestQueue = Volley.newRequestQueue(getActivity());

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsAlert();
            }
        });

        member_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MemberImg");
                intent.setComponent(name);
                intent.putExtra("profile", profile);
                intent.putExtra("name", nickname);
                startActivity(intent);
            }
        });

        txt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.CustomerCenter");
                intent.setComponent(name);
                startActivity(intent);
            }
        });

        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.Policy_activity");
                intent.setComponent(name);
                startActivity(intent);
            }
        });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MapsActivity");
                intent.putExtra("map", "ddd");
                intent.setComponent(name);
                startActivity(intent);
            }
        });

        try {
            loginMet();
            getProfile = ((MainActivity) getContext()).profile_main;

            if (!getProfile.equals(null))
                profile = getProfile;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    public void loginMet() {

        StringRequest request = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());

                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());

                    nickname = JsonUtil.getStringFrom(jsonOutput, "name");
                    point = JsonUtil.getStringFrom(jsonOutput, "pushState");
                    profile = JsonUtil.getStringFrom(jsonOutput, "profile");
                    ((MainActivity) getContext()).profile_main = JsonUtil.getStringFrom(jsonOutput, "profile");

                    textView.setText(nickname + " 님 환영합니다.");

                    if (profile.equals("1")) {
                        Glide.with(rootView.getContext()).load("http://dpsw23.dothome.co.kr/4grade/memberImg/" + idByANDROID_ID + ".jpg").asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(img) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(rootView.getContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                img.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    } else {
                        Glide.with(rootView.getContext()).load(R.drawable.man).asBitmap().centerCrop().into(new BitmapImageViewTarget(img) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(rootView.getContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                img.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    }


                    buttonChecker(point);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
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

    public void loginMet2() {

        StringRequest request = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());

                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());

                    point = JsonUtil.getStringFrom(jsonOutput, "pushState");

                    if (profile.equals("1"))
                        Glide.with(getActivity()).load("http://dpsw23.dothome.co.kr/4grade/memberImg/" + idByANDROID_ID + ".jpg").asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(img) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                img.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    else
                        Glide.with(getActivity()).load(R.drawable.man).asBitmap().centerCrop().into(new BitmapImageViewTarget(img) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                img.setImageDrawable(circularBitmapDrawable);
                            }
                        });


                    buttonChecker(point);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
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

    protected void updateMet() {
        StringRequest request = new StringRequest(Request.Method.POST, updateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    point = JsonUtil.getStringFrom(jsonOutput, "pushState");

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "인터넷 접속 상태를 확인해주세요2.", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", idByANDROID_ID);
                parameters.put("pushState", point);

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void buttonChecker(String s) {
        pushonoff = (ImageButton) rootView.findViewById(R.id.pushonoff);

        if (s.equals("1")) {
            pushonoff.setImageResource(R.drawable.off);
        } else {
            pushonoff.setImageResource(R.drawable.on);
        }


        pushonoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (point.equals("0")) {
                    pushonoff.setImageResource(R.drawable.off);
                    point = "1";
                    updateMet();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                } else {
                    pushonoff.setImageResource(R.drawable.on);
                    point = "0";
                    updateMet();
                    FirebaseMessaging.getInstance().subscribeToTopic("news");
                }
            }
        });
    }

    public void showSettingsAlert() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("CafeCoupon").setIcon(R.drawable.onlylogo);
        alertDialog.setMessage("탈퇴를 진행하시겠습니까?");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setNegativeButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.Member_quit_activity");
                intent.setComponent(name);
                startActivity(intent);
                ((MainActivity) getContext()).finish();
            }
        });
        // Cancel 하면 종료 합니다.
        alertDialog.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        alertDialog.show();


    }

    public void refresh2(Intent intent) {

        Bundle extras = intent.getExtras();

        if (extras != null) {
            String nameFromMemberImg = extras.getString("nameFromMemberImg");
            String profileFromImg = extras.getString("profile");

            textView.setText(nameFromMemberImg + " 님 반갑습니다.");
            nickname = nameFromMemberImg;
            profile = profileFromImg;

            loginMet2();


        }


    }
}

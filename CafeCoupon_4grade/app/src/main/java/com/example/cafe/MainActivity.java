package com.example.cafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    CouponActivity fragment1;
    SearchActivity fragment2;
    SettingActivity fragment3;
    String address;
    FrameLayout frameLayout;
    boolean databaseCreated = false;
    static boolean tableCreated = false;
    public static SQLiteDatabase db;
    ImageView title;
    RequestQueue requestQueue;
    String idByANDROID_ID, page3, page1;
    TabLayout tabs;
    double latM, lonM;
    String profile_main;
    int page;
    String point;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDatabase("location");
        InsertRecord.createTable();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        try {
            point = getIntent().getStringExtra("point");

            if (point.equals("1"))
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
            else
                FirebaseMessaging.getInstance().subscribeToTopic("news");
        } catch (Exception e) {
            e.printStackTrace();
        }

        page = 1;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (ImageView) findViewById(R.id.titleText);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabs.getTabAt(0).select();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
                page = 1;
            }
        });

        frameLayout = (FrameLayout) findViewById(R.id.container);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        fragment1 = new CouponActivity();
        fragment2 = new SearchActivity();
        fragment3 = new SettingActivity();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setIcon(R.drawable.main));
        tabs.addTab(tabs.newTab().setIcon(R.drawable.map));
        tabs.addTab(tabs.newTab().setIcon(R.drawable.setting));

        address = getIntent().getStringExtra("address");
        String text1 = getIntent().getStringExtra("test1");
        page3 = getIntent().getStringExtra("page3");
        page1 = getIntent().getStringExtra("page1");


        if (page1 != null) {
            tabs.getTabAt(0).select();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
            page = 1;
        }

        if (address != null || text1 != null) {
            tabs.getTabAt(1).select();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
            page = 2;
        }

        if (page3 != null) {
            tabs.getTabAt(2).select();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
            page = 3;
        }


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = fragment1;
                    page = 1;
                } else if (position == 1) {
                    selected = fragment2;
                    page = 2;
                } else if (position == 2) {
                    selected = fragment3;
                    page = 3;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        fragment1.requestQueue = requestQueue;
        fragment1.idByANDROID_ID = idByANDROID_ID;
        fragment3.requestQueue = requestQueue;
        fragment3.idByANDROID_ID = idByANDROID_ID;

        Bundle extras = intent.getExtras();
        if (extras != null) {
            double lat1 = extras.getDouble("lat");
            double lon1 = extras.getDouble("lon");
            String page1 = extras.getString("page1");
            String name = extras.getString("nameFromMemberImg");


            if (name != null) {
                fragment3.refresh2(intent);
                page = 3;
            }

            if (lat1 != 0.0 && lon1 != 0.0) {
                fragment2.setTextz(intent);
                page = 2;
            }

            if (page1 != null) {
                fragment1.refresh();
                page = 1;
            }


        }


    }

    public void createDatabase(String name) {

        try {
            db = openOrCreateDatabase(name, MODE_PRIVATE, null);
            databaseCreated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (page == 5)
            super.onBackPressed();

        if (page == 1)
            showSettingsAlert();
        else if (page == 2 || page == 3) {
            tabs.getTabAt(0).select();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
            page = 1;
        }
    }

    public void showSettingsAlert() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("CafeCoupon").setIcon(R.drawable.onlylogo);
        alertDialog.setMessage("어플을 종료하시겠습니까?");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Cancel 하면 종료 합니다.
        alertDialog.setNegativeButton("네", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                page = 5;
                onBackPressed();


            }
        });

        alertDialog.show();


    }

/*    public void hero(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }*/
}

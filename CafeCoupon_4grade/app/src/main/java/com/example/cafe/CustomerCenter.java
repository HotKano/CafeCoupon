package com.example.cafe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Dr.kim on 2017-06-28.
 */

public class CustomerCenter extends AppCompatActivity {

    ImageButton back;
    ImageView main;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_center_activity);

        back = (ImageButton) findViewById(R.id.backbutton);
        main = (ImageView) findViewById(R.id.main);

        Glide.with(this).load(R.drawable.customer_main).into(main);

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
    }
}

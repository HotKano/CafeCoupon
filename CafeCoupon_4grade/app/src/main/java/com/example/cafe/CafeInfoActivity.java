package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Dr.kim on 2017-06-05.
 */

public class CafeInfoActivity extends AppCompatActivity {

    ImageButton backbtn, mapBtn, reviewBtn;
    ImageView titlephoto, americano_icon, latte_icon, ade_icon, dessert_icon, ice_icon;
    TextView address, americano_pot, latte_pot, ade_pot, dessert_pot, ice_pot, openclose_pot, phone_pot;
    String x, y, titlephoto_state, address_state, title;
    String americano, latte, ade, dessert, ice, openclose, phone;
    String name, comment;
    TextView title_zone, comment_zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_info);

        name = getIntent().getStringExtra("title");
        comment = getIntent().getStringExtra("comment");

        title_zone = (TextView) findViewById(R.id.title_zone);
        comment_zone = (TextView) findViewById(R.id.comment_zone);

        if (!name.equals(null) && !comment.equals(null)) {
            title_zone.setText(name);
            comment_zone.setText(comment);
        }

        americano_icon = (ImageView) findViewById(R.id.americanoicon);
        latte_icon = (ImageView) findViewById(R.id.latteicon);
        ade_icon = (ImageView) findViewById(R.id.adeicon);
        dessert_icon = (ImageView) findViewById(R.id.desserticon);
        ice_icon = (ImageView) findViewById(R.id.iceicon);

        Glide.with(this).load(R.drawable.americano).into(americano_icon);
        Glide.with(this).load(R.drawable.lattee).into(latte_icon);
        Glide.with(this).load(R.drawable.ade).into(ade_icon);
        Glide.with(this).load(R.drawable.dessert).into(dessert_icon);
        Glide.with(this).load(R.drawable.ice).into(ice_icon);

        titlephoto_state = getIntent().getStringExtra("cafe");
        title = getIntent().getStringExtra("inside");
        address_state = getIntent().getStringExtra("address");
        americano = getIntent().getStringExtra("americano");
        latte = getIntent().getStringExtra("latte");
        ade = getIntent().getStringExtra("ade");
        dessert = getIntent().getStringExtra("dessert");
        ice = getIntent().getStringExtra("ice");
        openclose = getIntent().getStringExtra("openclose");
        phone = getIntent().getStringExtra("phone");
        x = getIntent().getStringExtra("x");
        y = getIntent().getStringExtra("y");

        backbtn = (ImageButton) findViewById(R.id.backbutton);
        mapBtn = (ImageButton) findViewById(R.id.mapbtn);
        reviewBtn = (ImageButton) findViewById(R.id.reviewBtn);

        titlephoto = (ImageView) findViewById(R.id.titlephoto);

        address = (TextView) findViewById(R.id.addressPot);
        americano_pot = (TextView) findViewById(R.id.americano);
        latte_pot = (TextView) findViewById(R.id.latte);
        ade_pot = (TextView) findViewById(R.id.ade);
        dessert_pot = (TextView) findViewById(R.id.dessert);
        ice_pot = (TextView) findViewById(R.id.ice);
        openclose_pot = (TextView) findViewById(R.id.openclose);
        phone_pot = (TextView) findViewById(R.id.phoneNumber);


        Glide.with(this).load("http://dpsw23.dothome.co.kr/4grade/cafeImg/" + title + ".png").into(titlephoto);
        address.setText(address_state);
        phone_pot.setText(phone);

        americano_pot.setText(americano);
        latte_pot.setText(latte);
        ade_pot.setText(ade);
        dessert_pot.setText(dessert);
        ice_pot.setText(ice);
        openclose_pot.setText(openclose);
        phone_pot.setText(phone);

       /* if (titlephoto_state.equals("2") && !titlephoto_state.equals(null) && !address_state.equals(null)) {
            //titlephoto.setImageResource(R.drawable.coffeya);
            Glide.with(this).load(R.drawable.coffeya).into(titlephoto);
            address.setText(address_state);
            phone_pot.setText(phone);

            americano_pot.setText(americano);
            latte_pot.setText(latte);
            ade_pot.setText(ade);
            dessert_pot.setText(dessert);
            ice_pot.setText(ice);
            openclose_pot.setText(openclose);
            phone_pot.setText(phone);
        }


        if (titlephoto_state.equals("3") && !titlephoto_state.equals(null) && !address_state.equals(null)) {
            //titlephoto.setImageResource(R.drawable.star_cafe);
            Glide.with(this).load(R.drawable.star_cafe).into(titlephoto);
            address.setText(address_state);
            phone_pot.setText(phone);

            americano_pot.setText(americano);
            latte_pot.setText(latte);
            ade_pot.setText(ade);
            dessert_pot.setText(dessert);
            ice_pot.setText(ice);
            openclose_pot.setText(openclose);
            phone_pot.setText(phone);
        }
*/

        backbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        backbtn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        backbtn.setAlpha(255);
                        onBackPressed();
                        return false;

                }
                return true;
            }
        });


        reviewBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        reviewBtn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        reviewBtn.setAlpha(255);
                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.ReviewActivity");
                        intent.setComponent(name);
                        intent.putExtra("cafe", titlephoto_state);
                        startActivity(intent);
                        return false;

                }
                return true;
            }
        });

        mapBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mapBtn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        mapBtn.setAlpha(255);
                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.CafeLocationMap");
                        intent.setComponent(name);
                        intent.putExtra("x", x);
                        intent.putExtra("y", y);
                        startActivity(intent);
                        return false;

                }
                return true;
            }
        });


    }
}

package com.example.cafe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Dr.kim on 2017-06-28.
 */

public class MemberName extends AppCompatActivity {

    ImageButton back;
    EditText nickname_slot;
    Button button;
    String nickname,nickname2,profile;
    TextView length;
    int state;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);

        length = (TextView) findViewById(R.id.textlength);
        back = (ImageButton) findViewById(R.id.backbutton);
        nickname_slot = (EditText) findViewById(R.id.name);
        button = (Button) findViewById(R.id.submit);
        nickname2 = getIntent().getStringExtra("name");
        profile = getIntent().getStringExtra("profile");
        nickname_slot.setText(nickname2);

        state = nickname_slot.getText().toString().length();
        length.setText(state+"/7");

        nickname_slot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                state = nickname_slot.getText().toString().length();
                length.setText(state+"/7");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = nickname_slot.getText().toString();
                if(!nickname.equals("")) {
                    Intent intent = new Intent();
                    ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MemberImg");
                    intent.setComponent(name);
                    intent.putExtra("name", nickname);
                    intent.putExtra("profile",profile);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MemberName.this, "이름에 공백은 허용하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

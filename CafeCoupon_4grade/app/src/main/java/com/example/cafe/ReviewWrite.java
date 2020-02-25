package com.example.cafe;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dr.kim on 2017-06-26.
 */

public class ReviewWrite extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    Uri imgCaptureUri;
    Bitmap bitmap, photo;

    ImageButton writeBtn,photobtn;
    EditText editText;
    RequestQueue requestQueue;
    ImageButton back;

    String sendUrl = "http://dpsw23.dothome.co.kr/4grade/CafeReviewInsert.php";
    String loginUrl = "http://dpsw23.dothome.co.kr/4grade/login.php";
    String comment, cafe, idByANDROID_ID, name, profile;
    TextView length;
    int state = 0;
    ImageView img;
    String image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafe_review_insert);
        requestQueue = Volley.newRequestQueue(this);
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        photobtn = (ImageButton) findViewById(R.id.reviewImgBtn);
        photobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgbtn();
            }
        });

        img = (ImageView) findViewById(R.id.reviewImg);
        writeBtn = (ImageButton) findViewById(R.id.btnSend);
        editText = (EditText) findViewById(R.id.memo_zone);
        back = (ImageButton) findViewById(R.id.backbutton);
        cafe = getIntent().getStringExtra("cafe");
        length = (TextView) findViewById(R.id.length);

        state = editText.getText().toString().length();
        length.setText(state + "/255");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                state = editText.getText().toString().length();
                length.setText(state + "/255");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        writeBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        writeBtn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        writeBtn.setAlpha(255);
                        comment = editText.getText().toString();
                        if (!TextCheckHelper.isBlankOrSpacing(comment)) {
                            try {
                                sendMet();
                                Intent intent = new Intent();
                                ComponentName componentName = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.ReviewActivity");
                                intent.setComponent(componentName);
                                intent.putExtra("cafe", cafe);
                                intent.putExtra("name", name);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ReviewWrite.this, "내용이 공백입니다!", Toast.LENGTH_SHORT).show();
                        }
                        return false;

                }
                return true;
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

        try {
            loginMet();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class TextCheckHelper {

        /**
         * Check the text is blank or only spacing.
         *
         * @param text String to check
         * @return true if text is blank or only spacing, otherwise false.
         */
        public static boolean isBlankOrSpacing(String text) {
            if (text.equals("") || text.matches("\\s+"))
                return true;
            return false;
        }
    }

    public void loginMet() {


        StringRequest request = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    name = JsonUtil.getStringFrom(jsonOutput, "name");
                    profile = JsonUtil.getStringFrom(jsonOutput, "profile");

                } catch (Exception e) {
                    e.printStackTrace();
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

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    protected void sendMet() {
        StringRequest request = new StringRequest(Request.Method.POST, sendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

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
                if (photo != null) {
                    image = getStringImage(photo);
                    parameters.put("file", image);
                    parameters.put("cafe", cafe);
                    parameters.put("comment", comment);
                    parameters.put("id", idByANDROID_ID);
                    parameters.put("name", name);
                    parameters.put("profile", profile);
                } else {
                    parameters.put("cafe", cafe);
                    parameters.put("comment", comment);
                    parameters.put("id", idByANDROID_ID);
                    parameters.put("name", name);
                    parameters.put("profile", profile);
                }

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

        imgCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                imgCaptureUri = data.getData();
                Log.d("SmartWheel", imgCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imgCaptureUri, "image/*");

                intent.putExtra("outputX", 500);
                intent.putExtra("outputY", 500);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE);

                break;
            }

            case CROP_FROM_iMAGE: {
                comment = editText.getText().toString();
                if (resultCode != RESULT_OK) {
                    return;
                }

                AssetFileDescriptor afd = null;
                try {
                    afd = getApplicationContext().getContentResolver().openAssetFileDescriptor(imgCaptureUri, "r");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 4;

                try {
                    bitmap = BitmapFactory.decodeFileDescriptor(afd.getFileDescriptor(), null, opt);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final Bundle extras = data.getExtras();


                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    try {
                        photo = extras.getParcelable("data");

                        Glide.with(this).load(R.drawable.man).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(img) {

                            @Override
                            protected void setResource(Bitmap resource) {
                                img.setImageBitmap(photo);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                }

                File f = new File(imgCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
        }

    }

    public void imgbtn() {

        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePhotoAction();
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();


    }
}

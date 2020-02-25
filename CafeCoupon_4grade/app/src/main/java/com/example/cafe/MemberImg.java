package com.example.cafe;

import android.app.AlertDialog;
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
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dr.kim on 2017-06-27.
 */

public class MemberImg extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    ImageButton backbtn, editimg, editnick;
    Uri imgCaptureUri;
    Bitmap bitmap, photo;
    ImageView img;
    String id, name;
    int id_view, name_state;
    RequestQueue requestQueue;
    String idByANDROID_ID, profile, image, nicknameforwhere;
    Button submit;
    TextView nickname;
    String sendUrl = "http://dpsw23.dothome.co.kr/4grade/androidImage.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_image);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        name_state = 0;

        submit = (Button) findViewById(R.id.submit);
        backbtn = (ImageButton) findViewById(R.id.backbutton);
        editimg = (ImageButton) findViewById(R.id.editimg);
        editnick = (ImageButton) findViewById(R.id.editname);
        img = (ImageView) findViewById(R.id.faceImg);
        nickname = (TextView) findViewById(R.id.nickname);

        profile = getIntent().getStringExtra("profile");
        nicknameforwhere = getIntent().getStringExtra("name");
        nickname.setText(nicknameforwhere);

        if (profile.equals("1"))
            Glide.with(getApplicationContext()).load("http://dpsw23.dothome.co.kr/4grade/memberImg/" + idByANDROID_ID + ".jpg").asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });
        else
            Glide.with(getApplicationContext()).load(R.drawable.man).asBitmap().centerCrop().into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMet();

            }
        });


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

        editimg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        backbtn.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        backbtn.setAlpha(255);
                        imgbtn(v);
                        return false;

                }
                return true;
            }
        });

        editnick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        editnick.setAlpha(50);
                        return true;

                    case MotionEvent.ACTION_UP:
                        editnick.setAlpha(255);
                        Intent intent = new Intent();
                        ComponentName name = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MemberName");
                        intent.setComponent(name);
                        intent.putExtra("name", nicknameforwhere);
                        intent.putExtra("profile", profile);
                        startActivity(intent);
                        return false;

                }
                return true;
            }
        });

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
        File file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis()) + "jpg");
        imgCaptureUri = FileProvider.getUriForFile(this, "com.example.drkim.cafecoupon.fileprovider", file);
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

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE);

                break;
            }

            case CROP_FROM_iMAGE: {
                if (resultCode != RESULT_OK) {
                    return;
                }

                AssetFileDescriptor afd = null;
                try {
                    afd = getContentResolver().openAssetFileDescriptor(imgCaptureUri, "r");
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

                        //storeCropImage(photo, filePath);
                        //absoultePath = filePath;

                        Glide.with(this).load(R.drawable.man).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(img) {

                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), photo);
                                circularBitmapDrawable.setCircular(true);
                                img.setImageDrawable(circularBitmapDrawable);

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

    public void imgbtn(View v) {
        id_view = v.getId();
        if (v.getId() == R.id.editimg) {
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

    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if (!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void sendMet() {
        StringRequest request = new StringRequest(Request.Method.POST, sendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.toString());
                    String text = JsonUtil.getStringFrom(jsonOutput, "state");

                    if (text.equals("fail2")) {
                        Toast.makeText(MemberImg.this, "닉네임 중복입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (photo != null) {
                            Intent intent = new Intent();
                            ComponentName name2 = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MainActivity");
                            intent.setComponent(name2);
                            intent.putExtra("page3", "test");
                            intent.putExtra("nameFromMemberImg", name);
                            intent.putExtra("profile", "1");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent();
                            ComponentName name2 = new ComponentName("com.example.drkim.cafecoupon", "com.example.drkim.cafecoupon.MainActivity");
                            intent.setComponent(name2);
                            intent.putExtra("page3", "test");
                            intent.putExtra("nameFromMemberImg", name);
                            intent.putExtra("profile", profile);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MemberImg.this, "인터넷 접속 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                name = nickname.getText().toString();

                if (photo != null) {
                    image = getStringImage(photo);
                    parameters.put("file", image);
                    parameters.put("id", idByANDROID_ID);
                    parameters.put("nickname", name);
                } else {
                    parameters.put("id", idByANDROID_ID);
                    parameters.put("nickname", name);
                }

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();

        if (extras != null) {
            nicknameforwhere = extras.getString("name");
            profile = extras.getString("profile");
            nickname.setText(nicknameforwhere);
        }

    }


}

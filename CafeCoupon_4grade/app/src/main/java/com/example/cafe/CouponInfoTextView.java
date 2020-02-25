package com.example.cafe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static com.example.cafe.R.layout.activity_coupon_info_item;
import static java.lang.Integer.parseInt;

/**
 * 아이템으로 보여줄 뷰 정의
 *
 * @author Mike
 */
public class CouponInfoTextView extends LinearLayout {

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

    LinearLayout couponColor;
    ImageView[] cou;
    String[] cou_memo;


    public CouponInfoTextView(final Context context, final CouponInfoTextItem aItem) {
        super(context);

        // Layout Inflation
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(activity_coupon_info_item, this, true);
/*
        couponColor = (LinearLayout) findViewById(R.id.couponColor);

        if (aItem.getData(0).equals("1")) {
            couponColor.setBackgroundColor(Color.WHITE);
        }

        if (aItem.getData(0).equals("2")) {
            couponColor.setBackgroundColor(Color.BLUE);
        }

        if (aItem.getData(0).equals("3")) {
            couponColor.setBackgroundColor(Color.GREEN);
        }*/

        cou = new ImageView[10];
        cou[0] = (ImageView) findViewById(R.id.coupon1);
        cou[1] = (ImageView) findViewById(R.id.coupon2);
        cou[2] = (ImageView) findViewById(R.id.coupon3);
        cou[3] = (ImageView) findViewById(R.id.coupon4);
        cou[4] = (ImageView) findViewById(R.id.coupon5);
        cou[5] = (ImageView) findViewById(R.id.coupon6);
        cou[6] = (ImageView) findViewById(R.id.coupon7);
        cou[7] = (ImageView) findViewById(R.id.coupon8);
        cou[8] = (ImageView) findViewById(R.id.coupon9);
        cou[9] = (ImageView) findViewById(R.id.coupon10);

        if (aItem.getData(0).equals("1")) {
            for (int i = 0; i < 10; i++)
                Glide.with(getContext()).load(R.drawable.emptycoupon).into(cou[i]);
        }

        if (aItem.getData(0).equals("2")) {
            for (int i = 0; i < 10; i++)
                Glide.with(getContext()).load(R.drawable.coffeeya_null).into(cou[i]);
        }

        if (aItem.getData(0).equals("3")) {
            for (int i = 0; i < 10; i++)
                Glide.with(getContext()).load(R.drawable.star_null).into(cou[i]);
        }

        if (aItem.getData(0).equals("4")) {
            for (int i = 0; i < 10; i++)
                Glide.with(getContext()).load(R.drawable.soso_null).into(cou[i]);
        }


        cou_memo = new String[10];
        cou_memo[0] = aItem.getData(6);
        cou_memo[1] = aItem.getData(7);
        cou_memo[2] = aItem.getData(8);
        cou_memo[3] = aItem.getData(9);
        cou_memo[4] = aItem.getData(10);
        cou_memo[5] = aItem.getData(11);
        cou_memo[6] = aItem.getData(12);
        cou_memo[7] = aItem.getData(13);
        cou_memo[8] = aItem.getData(14);
        cou_memo[9] = aItem.getData(15);

        if (aItem.getData(3).equals("0")) {

            for (int i = 0; i < parseInt(aItem.getData(1)); i++) {

                if (!TextCheckHelper.isBlankOrSpacing(cou_memo[i])) {
                    if (aItem.getData(0).equals("1")) {
                        Glide.with(getContext()).load(R.drawable.subito_stamp_memo).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);
                    } else if (aItem.getData(0).equals("2")) {
                        Glide.with(getContext()).load(R.drawable.coffeeya_stamp_memo).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);
                    } else if (aItem.getData(0).equals("3")) {
                        Glide.with(getContext()).load(R.drawable.star_stamp_memo).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);
                    } else if (aItem.getData(0).equals("4")) {
                        Glide.with(getContext()).load(R.drawable.soso_stamp_memo).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);
                    }
                } else {
                    if (aItem.getData(0).equals("1")) {
                        Glide.with(getContext()).load(R.drawable.subito_stamp).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);
                    } else if (aItem.getData(0).equals("2")) {
                        Glide.with(getContext()).load(R.drawable.coffeeya_stamp).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);
                    } else if (aItem.getData(0).equals("3")) {
                        Glide.with(getContext()).load(R.drawable.star_stamp).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);
                    } else if (aItem.getData(0).equals("4")) {
                        Glide.with(getContext()).load(R.drawable.soso_stamp).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);
                    }
                }


                final int finalI = i;
                cou[i].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                return true;

                            case MotionEvent.ACTION_UP:
                                Intent intent = new Intent();
                                intent.setClass(getContext(), CouponMemo.class);
                                intent.putExtra("stamp_number", aItem.getData(5));
                                intent.putExtra("cou_number", String.valueOf(finalI + 1));
                                intent.putExtra("cafe", aItem.getData(0));
                                context.startActivity(intent);

                                return false;

                        }
                        return true;
                    }
                });

            }
        } else {
            for (int i = 0; i < parseInt(aItem.getData(1)); i++) {

                if (!TextCheckHelper.isBlankOrSpacing(cou_memo[i]))
                    Glide.with(getContext()).load(R.drawable.star).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);
                else
                    Glide.with(getContext()).load(R.drawable.none_star).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cou[i]);

                final int finalI1 = i;
                cou[i].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                return true;

                            case MotionEvent.ACTION_UP:
                                Intent intent = new Intent();
                                intent.setClass(getContext(), CouponMemo.class);
                                intent.putExtra("stamp_number", aItem.getData(5));
                                intent.putExtra("cou_number", String.valueOf(finalI1 + 1));
                                intent.putExtra("cafe", aItem.getData(0));
                                context.startActivity(intent);
                                return false;

                        }
                        return true;
                    }
                });
            }
        }

        mText01 = (TextView) findViewById(R.id.cou_num);
        mText01.setText(String.valueOf(aItem.getData(1)));
        mText01.setVisibility(View.GONE);

        String state = aItem.getData(3);

        mText02 = (TextView) findViewById(R.id.date);
        if (state.equals("0"))
            mText02.setText(String.valueOf(aItem.getData(2)));
        else
            mText02.setText(String.valueOf(aItem.getData(4)));

        mText03 = (TextView) findViewById(R.id.used);
        mText03.setText(String.valueOf(aItem.getData(5)));
        mText03.setVisibility(View.GONE);


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

}

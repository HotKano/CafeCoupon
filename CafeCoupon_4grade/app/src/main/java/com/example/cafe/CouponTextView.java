package com.example.cafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.example.cafe.R.layout.couponlist_item;

/**
 * 아이템으로 보여줄 뷰 정의
 *
 * @author Mike
 */
public class CouponTextView extends LinearLayout {

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

    private TextView mText04;

    private ImageView img;

    public CouponTextView(final Context context, final CouponTextItem aItem) {
        super(context);

        // Layout Inflation
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(couponlist_item, this, true);

        if (aItem.getData(0).equals("1")) {
            img = (ImageView) findViewById(R.id.imagezone);
            //img.setImageResource(R.drawable.su);
            Glide.with(context).load(R.drawable.su).into(img);
        } else if (aItem.getData(0).equals("2")) {
            img = (ImageView) findViewById(R.id.imagezone);
            //img.setImageResource(R.drawable.cafe1);
            Glide.with(context).load(R.drawable.coffeeya_conpon_cover).into(img);
        } else if (aItem.getData(0).equals("3")) {
            img = (ImageView) findViewById(R.id.imagezone);
            //img.setImageResource(R.drawable.cafe2);
            Glide.with(context).load(R.drawable.star_conpon_cover).into(img);
        } else if (aItem.getData(0).equals("4")) {
            img = (ImageView) findViewById(R.id.imagezone);
            //img.setImageResource(R.drawable.cafe2);
            Glide.with(context).load(R.drawable.soso_logo).into(img);
        }

        mText01 = (TextView) findViewById(R.id.cou_num_list);
        mText01.setVisibility(View.GONE);

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
        } else {
            throw new IllegalArgumentException();
        }
    }


}

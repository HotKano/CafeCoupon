package com.example.cafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.example.cafe.R.layout.cafelist_item;

/**
 * 아이템으로 보여줄 뷰 정의
 *
 * @author Mike
 */
public class IconTextView extends LinearLayout {

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
    String pot;

    public IconTextView(final Context context, final IconTextItem aItem) {
        super(context);

        // Layout Inflation
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(cafelist_item, this, true);

        img = (ImageView) findViewById(R.id.cafeImage);
        pot = aItem.getData(0);

        Glide.with(getContext()).load("http://dpsw23.dothome.co.kr/4grade/cafeImg/"+aItem.getData(14)+".png").into(img);

/*        if (parseInt(aItem.getData(0)) == 3) {
            //img.setImageResource(R.drawable.star_cafe);
            Glide.with(getContext()).load(R.drawable.star_cafe).into(img);
        }*/

        // Set Text 01
        mText01 = (TextView) findViewById(R.id.namePot);
        mText01.setText(aItem.getData(1));

        // Set Text 02
        mText02 = (TextView) findViewById(R.id.phonePot);
        mText02.setText(aItem.getData(2));

        mText04 = (TextView) findViewById(R.id.etcPot);
        mText04.setText(aItem.getData(3));

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
            mText04.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }


}

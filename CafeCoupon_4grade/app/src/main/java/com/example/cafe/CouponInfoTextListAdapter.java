package com.example.cafe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 어댑터 클래스 정의
 *
 * @author Mike
 */
public class CouponInfoTextListAdapter extends BaseAdapter {

    private Context mContext;

    private List<CouponInfoTextItem> mItems = new ArrayList<CouponInfoTextItem>();

    public void clear() {
        mItems.clear();
    }

    public CouponInfoTextListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(CouponInfoTextItem it) {
        mItems.add(it);
    }

    public void setListItems(List<CouponInfoTextItem> lit) {
        mItems = lit;
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        final CouponInfoTextView itemView1;


        if (convertView == null) {

            itemView1 = new CouponInfoTextView(mContext, mItems.get(position));

        } else {
            convertView = new CouponInfoTextView(mContext, mItems.get(position));
            itemView1 = (CouponInfoTextView) convertView;
        }


        return itemView1;
    }


}

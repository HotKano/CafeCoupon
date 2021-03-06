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
public class CouponTextListAdapter extends BaseAdapter {

    private Context mContext;

    public List<CouponTextItem> mItems = new ArrayList<CouponTextItem>();

    public void clear() {
        mItems.clear();
    }

    public CouponTextListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(CouponTextItem it) {
        mItems.add(it);
    }

    public void setListItems(List<CouponTextItem> lit) {
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
        final CouponTextView itemView2;

        if (convertView == null) {
            itemView2 = new CouponTextView(mContext, mItems.get(position));

        } else {
            convertView = new CouponTextView(mContext, mItems.get(position));
            itemView2 = (CouponTextView) convertView;
        }

        //itemView2.setText(1, mItems.get(position).getData(2));


        return itemView2;
    }


}

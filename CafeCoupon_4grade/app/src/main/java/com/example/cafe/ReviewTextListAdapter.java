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
public class ReviewTextListAdapter extends BaseAdapter {

    private Context mContext;

    public List<ReviewTextItem> mItems = new ArrayList<ReviewTextItem>();

    public void clear() {
        mItems.clear();
    }

    public ReviewTextListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(ReviewTextItem it) {
        mItems.add(it);
    }

    public void setListItems(List<ReviewTextItem> lit) {
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
        final ReviewTextView itemView4;


        if (convertView == null) {

            itemView4 = new ReviewTextView(mContext, mItems.get(position));

        } else {
            convertView = new ReviewTextView(mContext, mItems.get(position));
            itemView4 = (ReviewTextView) convertView;
        }


        return itemView4;
    }


}

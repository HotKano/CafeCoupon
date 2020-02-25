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
public class IconTextListAdapter extends BaseAdapter {

    private Context mContext;

    public List<IconTextItem> mItems = new ArrayList<IconTextItem>();

    public void clear() {
        mItems.clear();
    }

    public IconTextListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(IconTextItem it) {
        mItems.add(it);
    }

    public void setListItems(List<IconTextItem> lit) {
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
        final IconTextView itemView3;

        if (convertView == null) {
            itemView3 = new IconTextView(mContext, mItems.get(position));
        } else {
            convertView = new IconTextView(mContext, mItems.get(position));
            itemView3 = (IconTextView) convertView;
        }

        itemView3.setText(1, mItems.get(position).getData(1));
        itemView3.setText(2, mItems.get(position).getData(2));
        itemView3.setText(3, mItems.get(position).getData(3));


        return itemView3;
    }
    

}

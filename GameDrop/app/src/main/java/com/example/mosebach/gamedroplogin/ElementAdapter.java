package com.example.mosebach.gamedroplogin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Nipun on 10/21/2016.
 */

public class ElementAdapter extends BaseAdapter {
    private Context mContext;

    public ElementAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return mThumbIds[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    private static Integer[] mThumbIds = {
            ElementStore.elements[0],
            ElementStore.elements[1],
            ElementStore.elements[2],
            ElementStore.elements[3],
            ElementStore.elements[4],
            ElementStore.elements[5],
            ElementStore.elements[6],
            ElementStore.elements[7]
    };
}
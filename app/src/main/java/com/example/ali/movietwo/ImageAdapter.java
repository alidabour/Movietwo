package com.example.ali.movietwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private String[] urls;
        LayoutInflater inflater;

        public ImageAdapter(Context c, String[] arr) {
            mContext = c;
            urls = arr;
            inflater = LayoutInflater.from(mContext);
        }

        public int getCount() {
            return urls.length;
        }

        public Object getItem(int id) {
            return urls[id];
        }

        public long getItemId(int id) {
            return id;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int id, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = (ImageView)inflater.inflate(R.layout.list_item,null);
            } else {
                imageView = (ImageView) convertView;
            }
            Picasso.with(mContext).load((String)getItem(id)).into(imageView);
            return imageView;
        }

}

package com.example.ali.movietwo;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;



/**
 * Created by ali on 28/12/15.
 */
public class CustomBtnAdapter extends ArrayAdapter<String> {
    String[]  url;
    ArrayList<String> name;
    Context context;
    public CustomBtnAdapter(Context context,ArrayList<String> name) {
        super(context,R.layout.btn_list_item,name);
        this.context=context;
        this.name=name;
    }

    @Override
    public int getCount() {
        return name.size();
    }
    public void addUrl (String[] url){
        this.url=url;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (convertView == null ){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.btn_list_item, null);
        }
        Button btn = (Button)rowView.findViewById(R.id.btn_tv );
        btn.setText(name.get(position));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url[position])));
                Log.v("T", String.valueOf(getItemId(position)));
            }
        });

        return rowView;
    }

}

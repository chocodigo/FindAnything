package com.example.reamhae.findanything;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by reamhae on 2017-05-06.
 */

public class searchListView_Adapter extends BaseAdapter {
    private ArrayList<searchListView> listViewItemList=new ArrayList<searchListView>();

    public searchListView_Adapter(){

    }

    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_listview, parent, false);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.item_image);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.item_name);
        TextView descTextView = (TextView) convertView.findViewById(R.id.item_explain);

        searchListView search_listview = listViewItemList.get(position);

        iconImageView.setImageDrawable(search_listview.getIcon());
        titleTextView.setText(search_listview.getTitle());
        descTextView.setText(search_listview.getDesc());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList.get(position);
    }

    public void addItem(Drawable icon, String title, String desc){
        searchListView item = new searchListView();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        listViewItemList.add(item);
    }

}

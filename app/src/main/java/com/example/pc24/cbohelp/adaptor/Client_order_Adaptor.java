package com.example.pc24.cbohelp.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.pc24.cbohelp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by pc24 on 29/11/2016.
 */

public class Client_order_Adaptor extends BaseAdapter {
    private Context context;
    ArrayList<HashMap<String , String>> dataList=new ArrayList<>();
    ArrayList<HashMap<String , String>> MaindataList=new ArrayList<>();
    String type;

    public Client_order_Adaptor(Context context, ArrayList<HashMap<String , String>> adaptor_data, String type){
        this.context=context;
        dataList = adaptor_data;
        MaindataList.addAll(dataList);
        this.type=type;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.client_order_item, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView amt = (TextView) convertView.findViewById(R.id.amt);


        name.setText(dataList.get(position).get("PA_NAME"));
        if (type.equals("A")) {
            amt.setText(dataList.get(position).get("COUNT"));
        }else {
            amt.setText(dataList.get(position).get("USER_NAME"));
        }


        return convertView;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataList.clear();
        if (charText.length() == 0) {
            dataList.addAll(MaindataList);
        } else {
            for (HashMap<String , String> wp : MaindataList) {
                if (wp.get("PA_NAME").toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    dataList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}

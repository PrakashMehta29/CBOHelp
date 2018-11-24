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

/**
 * Created by pc24 on 29/11/2016.
 */

public class Order_List_Adaptor extends BaseAdapter {
    private Context context;
    HashMap<String,ArrayList> adaptor_data;

    public Order_List_Adaptor(Context context, HashMap<String,ArrayList> adaptor_data){
        this.context=context;
        this.adaptor_data=adaptor_data;
    }

    @Override
    public int getCount() {
        return adaptor_data.get("DOC_NO").size();
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
            convertView = infalInflater.inflate(R.layout.order_list_item, null);
        }

        TextView order_no = (TextView) convertView.findViewById(R.id.order_no);
        TextView amt = (TextView) convertView.findViewById(R.id.amt);
        TextView date = (TextView) convertView.findViewById(R.id.date);


        order_no.setText(adaptor_data.get("DOC_NO").get(position).toString());
        date.setText(adaptor_data.get("DOC_DATE").get(position).toString());
        amt.setText(adaptor_data.get("NET_AMT").get(position).toString());



        return convertView;
    }
}

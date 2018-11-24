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

public class New_order_confirm_Adaptor extends BaseAdapter {
    private Context context;
    HashMap<String,ArrayList> adaptor_data;

    public New_order_confirm_Adaptor(Context context, HashMap<String,ArrayList> adaptor_data){
        this.context=context;
        this.adaptor_data=adaptor_data;
    }

    @Override
    public int getCount() {
        return adaptor_data.get("particulars").size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.new_order_confirm_item, null);
        }
        TextView particulars = (TextView) convertView.findViewById(R.id.particulars);
        TextView rate = (TextView) convertView.findViewById(R.id.rate);
        TextView qty = (TextView) convertView.findViewById(R.id.qty);
        TextView amt = (TextView) convertView.findViewById(R.id.amt);

        particulars.setText(adaptor_data.get("particulars").get(position).toString());
        rate.setText(adaptor_data.get("rate").get(position).toString());
        qty.setText(adaptor_data.get("qty").get(position).toString());
        amt.setText(adaptor_data.get("amt").get(position).toString());

        return convertView;
    }
}

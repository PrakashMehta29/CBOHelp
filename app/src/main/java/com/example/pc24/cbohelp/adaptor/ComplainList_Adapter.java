package com.example.pc24.cbohelp.adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc24.cbohelp.DrRegistrationView;
import com.example.pc24.cbohelp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class ComplainList_Adapter extends BaseAdapter {

  Context context;
    LayoutInflater layoutInflater;
    ArrayList<HashMap<String , String>> dataList=new ArrayList<>();
    ArrayList<HashMap<String , String>> MaindataList=new ArrayList<>();
    String who,updated_complain;

    public ComplainList_Adapter(Context context, ArrayList<HashMap<String , String>> data,String who,String updated_complain){

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataList = data;
        MaindataList.addAll(dataList);
        this.who=who;
        this.updated_complain=updated_complain;

  }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null){
             holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.complain_row,null);
            //F5F5DC
            holder.date=(TextView) convertView.findViewById(R.id.mail_dateto);
            holder.from=(TextView) convertView.findViewById(R.id.mail_to);
            holder.subject=(TextView) convertView.findViewById(R.id.mail_subto);
            holder.attach=(ImageView) convertView.findViewById(R.id.attach);
            holder.cir=(TextView) convertView.findViewById(R.id.inbox_idto);
            holder.des=(TextView) convertView.findViewById(R.id.describtion);
            holder.complain_transfer=(TextView) convertView.findViewById(R.id.complain_transfer);
            holder.complain_type=(TextView) convertView.findViewById(R.id.complain_type);
            holder.complain_mobile=(TextView) convertView.findViewById(R.id.complain_mobile);

            convertView.setTag(holder);

        }else {
             holder = (ViewHolder) convertView.getTag();
        }
        if (dataList.get(position).get("DOC_NO").equals(updated_complain)){
            convertView.setBackgroundColor(0xFFF5F5DC);
        }else{
            convertView.setBackgroundColor(0xFFFFFFFF);
        }
        //convertView.setBackgroundColor(0xFFF5F5DC);

        if (getDate("dd/MM/yyyy").equals(dataList.get(position).get("DOC_DATE"))){
            holder.date.setText(dataList.get(position).get("TIME"));
        }else {
            holder.date.setText(dataList.get(position).get("DOC_DATE")+" ("+dataList.get(position).get("P_DAYS")+")");
        }
        if (who.equals("add")){
            holder.from.setText(dataList.get(position).get("DOC_NO")+" ("+dataList.get(position).get("COMPLAINT_TYPE")+" )");
            holder.cir.setText(dataList.get(position).get("COMPLAINT_TYPE").substring(0,1).toUpperCase());
            holder.complain_type.setVisibility(View.GONE);
            if  (!dataList.get(position).get("PRIORITY").equals("0")) {
                holder.complain_type.setText("PRIORITY : " + dataList.get(position).get("PRIORITY"));
                holder.complain_type.setVisibility(View.VISIBLE);
            }
        }else {
            holder.from.setText(dataList.get(position).get("PA_NAME"));
            holder.cir.setText(dataList.get(position).get("PA_NAME").substring(0,1).toUpperCase());
            holder.complain_type.setVisibility(View.VISIBLE);
            if  (!dataList.get(position).get("PRIORITY").equals("0")) {
                holder.complain_type.setText(dataList.get(position).get("DOC_NO") + " (" + dataList.get(position).get("COMPLAINT_TYPE") + " )" + " ( PRIORITY : " + dataList.get(position).get("PRIORITY") + " )");
            }else {
                holder.complain_type.setText(dataList.get(position).get("DOC_NO") + " (" + dataList.get(position).get("COMPLAINT_TYPE") + " )" );
            }
        }


        holder.des.setText(dataList.get(position).get("REMARK"));

        final Drawable drawable = holder.cir.getBackground();
        Random rnd = new Random();
        final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
        drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);


        if (dataList.get(position).get("COMMENT").equals("")){
            holder.subject.setVisibility(View.GONE);
        }else{
            if (dataList.get(position).get("DONE_BY").equals("")) {
                holder.subject.setText(dataList.get(position).get("COMMENT"));
            }else {
                holder.subject.setText(dataList.get(position).get("DONE_BY")+" :- "+dataList.get(position).get("COMMENT"));
            }
            holder.subject.setVisibility(View.VISIBLE);
        }

        if (dataList.get(position).get("MOBILE").equals("")){
            holder.complain_mobile.setVisibility(View.GONE);
        }else{
            holder.complain_mobile.setText(dataList.get(position).get("MOBILE"));
            holder.complain_mobile.setVisibility(View.VISIBLE);
        }

        if (dataList.get(position).get("FROM_USER").equals("") && dataList.get(position).get("TO_USER").equals("")){
            holder.complain_transfer.setVisibility(View.GONE);
        }else{
            holder.complain_transfer.setText(dataList.get(position).get("FROM_USER")+" ----> "+dataList.get(position).get("TO_USER"));
            holder.complain_transfer.setVisibility(View.VISIBLE);
        }

        if (dataList.get(position).get("STATUS").equals("COMPLETE")){
            color[0] = Color.argb(255,34,139,34);
            holder.from.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
        }else{
            color[0] = Color.argb(255,255,69,0);
            holder.from.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
        }


        holder.date.setTag(position);
        holder.from.setTag(position);
        holder.subject.setTag(position);
        holder.attach.setVisibility(View.GONE);

        String file_name=dataList.get(position).get("ATTACHMENT");
        if(file_name.equals("")){
            holder.attach.setVisibility(View.GONE);
        }else{
            holder.attach.setVisibility(View.VISIBLE);
            final String[] aT1 = {file_name};
            holder.attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!aT1[0].contains("http://")){
                        aT1[0] ="http://"+ aT1[0];
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(aT1[0]));
                    context.startActivity(browserIntent);
                   /* Intent i = new Intent(context, DrRegistrationView.class);
                    i.putExtra("A_TP1", aT1[0]);
                    i.putExtra("Menu_code", "");
                    i.putExtra("Title", "Attachment");
                    context.startActivity(i);*/
                }
            });
        }



        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public class ViewHolder{
        ImageView attach;
        TextView from,subject,date,cir,des,complain_transfer,complain_type,complain_mobile;
    }

    private String getDate(String date_format){
        SimpleDateFormat format = new SimpleDateFormat(date_format);//"yyyy.MM.dd HH:mm");
        Calendar cal = Calendar.getInstance();
        System.out.println(format.format(cal.getTime()));
        return format.format(cal.getTime());
    }


    // Filter Class
    public void filter(String charText, int from ,int to) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataList.clear();
        if (charText.length() == 0 && from == 0 && to == 0) {
            dataList.addAll(MaindataList);
        }else  if (charText.length() == 0 && from != 0 && to != 0){
            for (HashMap<String , String> wp : MaindataList) {
                int priority=Integer.parseInt (wp.get("PRIORITY"));
                if (priority <= to && priority >= from){
                    dataList.add(wp);
                }
            }
        }else {
            for (HashMap<String , String> wp : MaindataList) {
                if (wp.get("PA_NAME").toLowerCase(Locale.getDefault()).contains(charText)
                        || wp.get("DOC_NO").toLowerCase(Locale.getDefault()).contains(charText)) {
                    int priority=Integer.parseInt(wp.get("PRIORITY"));
                    if (from != 0 && to != 0){
                        if (priority <= to && priority >= from){
                            dataList.add(wp);
                        }
                    }else{
                        dataList.add(wp);
                    }

                }
            }
        }
        notifyDataSetChanged();
    }


}

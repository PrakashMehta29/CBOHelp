package com.example.pc24.cbohelp.PartyView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pc24.cbohelp.Followingup.mFollowupgrid;
import com.example.pc24.cbohelp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.MyViewHolder> {

    public List<String> data;
    Context context;
    ICallListiner ICallListiner;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener;


    public interface ICallListiner{
        void onCallClick(View view,String number);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Paname,Number;
        LinearLayout container;
        ImageView viewbrtn;

        public MyViewHolder(View view) {
            super(view);
            Paname =(TextView)view.findViewById(R.id.paname);
            Number=(TextView) view.findViewById(R.id.number);
            container=(LinearLayout)view.findViewById(R.id.container);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recycleViewOnItemClickListener != null) {
                       recycleViewOnItemClickListener.onClick(view,getAdapterPosition(),false);
                        recycleViewOnItemClickListener.onCallClick(view,Number.getText().toString());
                    }

                }

            });

        }
    }



    public CallAdapter(Context mContext, List<String> data,RecycleViewOnItemClickListener listener) {
        this.context=mContext;
        this.data=data;
        this.recycleViewOnItemClickListener=listener;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.callrow, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        //  final mPartyContact mPartyContact=data.get(position);
        final String list=data.get(position);


        holder.Number.setText(list);

      /*  holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                recycleViewOnItemClickListener =new RecycleViewOnItemClickListener() {
                    @Override
                    public void onCallClick(View view, String number) {
                        number=holder.Number.getText().toString();

                    }

                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                };
           //     recycleViewOnItemClickListener.onCallClick();

            }

});*/






    }



    @Override
    public int getItemCount() {
        return data.size();
    }
}

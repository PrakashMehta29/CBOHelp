package com.example.pc24.cbohelp.Followingup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc24.cbohelp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NewPartyActvityAdapter extends RecyclerView.Adapter<NewPartyActvityAdapter.MyViewHolder> {

    public ArrayList<mFollowupgrid> followdata=new ArrayList< mFollowupgrid>();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Followdate, Nxtfollowdate,remark;
        ImageView viewbrtn;

        public MyViewHolder(View view) {
            super(view);
            Nxtfollowdate =(TextView)view.findViewById(R.id.nextfolowdate);
            Followdate = (TextView) view.findViewById(R.id.followdate);
            remark=(TextView) view.findViewById(R.id.fremark);


        }
    }


    public NewPartyActvityAdapter(Context mContext, ArrayList<mFollowupgrid> data) {
        this.context=mContext;
        this.followdata=data;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.followingup_detail_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        mFollowupgrid mFollowupgrid=followdata.get(position);
        ConvertDate(mFollowupgrid.getfOLLOWUPDATE());

        holder.Followdate.setText(ConvertDate(mFollowupgrid.getfOLLOWUPDATE()));
        holder.Nxtfollowdate.setText(ConvertDate(mFollowupgrid.getnEXTFOLLOWUPDATE()));

      /*  try {
            holder.Nxtfollowdate.setText((CharSequence) CustomDatePicker.getDate(mFollowupgrid.getnEXTFOLLOWUPDATE(),CustomDatePicker.CommitFormat));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        holder.remark.setText(mFollowupgrid.getfREMARK());
    }

    private String ConvertDate(String ParsingDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String date = formatter.format(Date.parse(ParsingDate));

        return date;
    }


    @Override
    public int getItemCount() {
        return followdata.size();
    }
}

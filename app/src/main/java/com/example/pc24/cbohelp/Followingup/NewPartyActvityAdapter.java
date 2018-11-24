package com.example.pc24.cbohelp.Followingup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc24.cbohelp.R;

import java.util.ArrayList;

public class NewPartyActvityAdapter extends RecyclerView.Adapter<NewPartyActvityAdapter.MyViewHolder> {

    public ArrayList<mFollowupgrid> followdata=new ArrayList< mFollowupgrid>();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView followupdate,nxtfollowupdate,remark;
        ImageView viewbrtn;

        public MyViewHolder(View view) {
            super(view);
            nxtfollowupdate=(TextView)view.findViewById(R.id.nextfolowdate);
            followupdate = (TextView) view.findViewById(R.id.followdate);
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
        holder.nxtfollowupdate.setText(mFollowupgrid.getnEXTFOLLOWUPDATE());
        holder.followupdate.setText(mFollowupgrid.getfOLLOWUPDATE());
        holder.remark.setText(mFollowupgrid.getfREMARK());
    }




    @Override
    public int getItemCount() {
        return followdata.size();
    }
}

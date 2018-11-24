package com.example.pc24.cbohelp.FollowUp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc24.cbohelp.R;

import java.util.ArrayList;

public class Followadap extends RecyclerView.Adapter<Followadap. Myviewholder> {
    Context mcontext;
    ArrayList<mFollowupgrid>mfolowgrid=new ArrayList<mFollowupgrid>();




    public  class Myviewholder extends RecyclerView.ViewHolder {
        TextView name;
        public Myviewholder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.name);
        }
    }

    public Followadap(Context context, ArrayList<mFollowupgrid>mfolowgrid) {
        this.mcontext=context;
        this.mfolowgrid=mfolowgrid;
    }

    @Override
    public Followadap.Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.followup_detail_row,parent,false);

        return new Followadap.Myviewholder(itemview);
    }

    @Override
    public void onBindViewHolder(Followadap.Myviewholder holder, int position) {
       mFollowupgrid mFollowupgrid= mfolowgrid.get(position);
       holder.name.setText(mFollowupgrid.getpANAME());

    }

    @Override
    public int getItemCount() {
        return mfolowgrid.size();
    }
}

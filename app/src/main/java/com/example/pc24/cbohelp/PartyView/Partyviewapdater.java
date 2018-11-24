package com.example.pc24.cbohelp.PartyView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pc24.cbohelp.FollowUp.VM_Followup;
import com.example.pc24.cbohelp.AddParty.PartyDetail;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Partyviewapdater extends RecyclerView.Adapter<Partyviewapdater.MyViewHolder>{

    public ArrayList<mParty> partydata = new ArrayList<mParty>();
    ArrayList<mParty> partydatacopy=new ArrayList<mParty>();
    Context context;
    VM_Followup vm_followup = null;
    Shareclass shareclass = null;
    private static final int FOLLOWUP_DIALOG = 7;
    VM_Followup.OnResulyListner resulyListner = null;
    public String Paid = "";
    mParty mParty;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre, person;

        ImageView Edit, delete;
        LinearLayout   continer;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            year = (TextView) view.findViewById(R.id.year);
            person = (TextView) view.findViewById(R.id.person);
            Edit = (ImageView) view.findViewById(R.id.Editparty);
            continer=(LinearLayout)view.findViewById(R.id.container);
        }


    }

    public Partyviewapdater(Context mContext, ArrayList<mParty> data) {
        this.context = mContext;
        this.partydata = data;
        partydatacopy = (ArrayList<mParty>) data.clone();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partyview_list, parent, false);
        shareclass = new Shareclass();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder holder1 = (MyViewHolder) holder;
            final mParty rptmodel = partydatacopy.get(position);
            //set values of data here
         //   final mParty mParty = partydata.get(position);

            //String Login_user = shareclass.getValue(context,"PA_ID","0");

            holder.title.setText(rptmodel.getName());


            //holder.genre.setText(mParty.getMobile());
            holder.year.setText(rptmodel.getStatus());
            holder.person.setText(rptmodel.getPerson());
            holder.continer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   GetFollowup();
                    Context context = view.getContext();
                   /* Bundle bundle=new Bundle();
                    bundle.putString("Paid",rptmodel.getId());
                    bundle.putString("Mobile",rptmodel.getMobile());
                    bundle.putString("Name",  rptmodel.getName());
                    bundle.putString("Person",rptmodel.getPerson());
                    bundle.putString("Status",rptmodel.getStatus());*/
                    /*shareclass=new Shareclass();
                    shareclass.save(context,"Paid",mParty.getId());
                    shareclass.save(context,"Mobile",mParty.getMobile());
                    shareclass.save(context,"Name",  mParty.getName());
                    shareclass.save(context,"Person",mParty.getPerson());
                    shareclass.save(context,"Status",mParty.getStatus());*/


                  /*  Intent intent = new Intent(context, NewPartyActivity.class);


                    //intent.putExtras(bundle);
                    context.startActivity(intent);*/
                    Intent i=new Intent();
                    i.putExtra("Paid",rptmodel.getId());
                    i.putExtra("Mobile",rptmodel.getMobile());
                    i.putExtra("Name",  rptmodel.getName());
                    i.putExtra("Person",rptmodel.getPerson());
                    i.putExtra("Status",rptmodel.getStatus());
                    ((Activity) context).setResult(RESULT_OK, i);
                    ((Activity) context).finish();

                }

            });
            holder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();

                    Intent intent = new Intent(context, PartyDetail.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("Paid",rptmodel.getId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                  /*  ((Activity) context).setResult(1, intent);
                    ((Activity) context).finish();*/
                }
            });
        }


      /*  holder.followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("iId","0");
                bundle.putInt("iSrno", bundle.getInt("iSrno",0));
                bundle.putString("iPaId",mParty.getId());
                bundle.putString("header",mParty.getName());
                bundle.putString("sContactPerson",mParty.getPerson());
                bundle.putString("sContactNo",mParty.getMobile());
                bundle.putString("iUserId",mParty.getId());




                new FollowupDialog(context, bundle, FOLLOWUP_DIALOG, new FollowupDialog.IFollowupDialog() {
                    @Override
                    public void onFollowSubmit() {

                        holder.viewbrtn.performClick();

                    }
                }).show();


            }
        });*/
    }


    @Override
    public int getItemCount() {
        return partydatacopy.size();

    }
    public void filter(String Query){
        partydatacopy.clear();
        if (Query.trim().equals("")){
            partydatacopy = (ArrayList<mParty>) partydata.clone();
            notifyDataSetChanged();
            return;
        }

        for (mParty item : partydata){
            if (item.getName().toLowerCase().contains(Query.toLowerCase()) ||
                    item.getPerson().toLowerCase().contains(Query.toLowerCase())){
                partydatacopy.add(item);
            }
        }
        notifyDataSetChanged();
    }


}

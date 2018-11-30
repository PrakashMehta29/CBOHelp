package com.example.pc24.cbohelp.PartyView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.pc24.cbohelp.FollowUp.VM_Followup;
import com.example.pc24.cbohelp.AddParty.PartyDetail;
import com.example.pc24.cbohelp.Followingup.NewPartyActivity;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;

import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class Partyviewapdater extends RecyclerView.Adapter<Partyviewapdater.MyViewHolder> {

    public ArrayList<mParty> partydata = new ArrayList<mParty>();
    ArrayList<mParty> partydatacopy = new ArrayList<mParty>();
    Context context;
    VM_Followup vm_followup = null;
    Shareclass shareclass = null;
    String SelectedPaid;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre, person, Mobile, cir;

        ImageView Edit, delete;
        LinearLayout continer;


        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            year = view.findViewById(R.id.year);
            person = view.findViewById(R.id.person);
            Edit = view.findViewById(R.id.Editparty);
            continer = view.findViewById(R.id.container);
            Mobile = view.findViewById(R.id.mobile);
            cir = view.findViewById(R.id.PartyHeader);
        }


    }

    public Partyviewapdater(Context mContext, ArrayList<mParty> data, String SelectedPaid) {
        this.context = mContext;
        this.partydata = data;
        this.SelectedPaid = SelectedPaid;
        partydatacopy = (ArrayList<mParty>) data.clone();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partyview_list2, parent, false);
        shareclass = new Shareclass();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            final mParty rptmodel = partydatacopy.get(position);

            holder.title.setText(rptmodel.getName());
            holder.year.setText(rptmodel.getStatus());
            holder.person.setText(rptmodel.getPerson());
            holder.Mobile.setText(rptmodel.getMobile());
            holder.cir.setText(rptmodel.getName().substring(0, 1).toUpperCase());
            final Drawable drawable = holder.cir.getBackground();
            Random rnd = new Random();
            final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
            drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);


            if (rptmodel.getStatus().equals("Done")) {
                color[0] = Color.parseColor("#008000");//green
                holder.cir.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
            } else if (rptmodel.getStatus().equals("InProcess")) {
                color[0] = Color.parseColor("#ff1919");//red
                holder.cir.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
            } else if (rptmodel.getStatus().equals("Not Intrested")) {
                color[0] = Color.parseColor("#3232ff");//blue
                holder.cir.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
            } else {
                color[0] = Color.parseColor("#2196F3");//colorprimery
                holder.cir.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

            }
            holder.Mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + rptmodel.getMobile().toString()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(intent);
                }
            });
            holder.continer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    SelectedPaid = rptmodel.getId();
                    Intent i = new Intent(context, NewPartyActivity.class);
                    i.putExtra("Paid",rptmodel.getId());
                    i.putExtra("Mobile",rptmodel.getMobile());
                    i.putExtra("Name",  rptmodel.getName());
                    i.putExtra("Person",rptmodel.getPerson());
                    i.putExtra("Status",rptmodel.getStatus());
                    i.putExtras(i);
                    context.startActivity(i);

//                    ((Activity) context).setResult(RESULT_OK, i);
//                    ((Activity) context).finish();
                    notifyDataSetChanged();
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

            if(SelectedPaid.equals(rptmodel.getId())){
                holder.continer.setBackgroundColor(Color.parseColor(context.getString(R.string.SelectedValue)));
            } else {
                holder.continer.setBackgroundColor(Color.parseColor(context.getString(R.string.DeafultValue)));
            }
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

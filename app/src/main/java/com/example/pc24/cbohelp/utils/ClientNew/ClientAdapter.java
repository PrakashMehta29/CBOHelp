package com.example.pc24.cbohelp.utils.ClientNew;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.pc24.cbohelp.R;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.myViewholder> {

 //  public ArrayList<mClient> Rptdata=new ArrayList<mClient>();
    private List<mClient> clientlist;

    SwipeRevealLayout swipeRevealLayout;

    public ClientAdapter( List<mClient> Rptdata) {
        this.clientlist = Rptdata;

    }

    @Override
    public myViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_raw, parent, false);
        swipeRevealLayout=new SwipeRevealLayout(parent.getContext());

        return new myViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(myViewholder holder, final int position) {
        mClient mclient = clientlist.get(position);

        holder.Call.setText(clientlist.get(position).getName());
      //  holder.Call.setText(mclient.getName());
      //  holder.message.setText(mclient.getDept());
        holder.message.setText(clientlist.get(position).getDept());
        holder.Call.setTag(position);
        holder.message.setTag(position);
        holder.Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
               startActivity(v.getContext(),intent,null);
             //   startActivity(v.getContext(),intent,null);
                Toast.makeText(v.getContext(), clientlist.get(position).getName(), Toast.LENGTH_SHORT).show();

            }
        });

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), clientlist.get(position).getDept(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setType("vnd.android-dir/mms-sms");

                startActivity(v.getContext(),intent,null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return clientlist.size();
    }

    public  class myViewholder extends RecyclerView.ViewHolder{
    TextView  Call ,message;

        public myViewholder(View itemView) {
            super(itemView);

            Call= (TextView) itemView.findViewById(R.id.btncall);
            message=(TextView)itemView.findViewById(R.id.btnmessage);
        }
    }
}

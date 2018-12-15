package com.example.pc24.cbohelp.PartyView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallDialog {

    Integer response_code;
    Bundle Msg;
    Dialog dialog;
    Context context;
    Button Submit, Cancel;
    Shareclass shareclass;
    private Boolean OkBtnReq = true;
  RecycleViewOnItemClickListener listener;





    RecyclerView recyclerView;
    List<String> partyContacts;
    mPartyContact mPartyContact;



    public CallDialog(@NonNull Context context,boolean okBtnrequired, mPartyContact mPartyContact, Integer response_code) {
        this.OkBtnReq=okBtnrequired;
        this.context = context;
        this.response_code = response_code;
        this.mPartyContact=mPartyContact;

        }

    public void show() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog2, null, false);
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);

        // partyContacts.clear();
        String SplitChar="/";
        if(mPartyContact.getNumber().contains(","))
            SplitChar=",";
        partyContacts = Arrays.asList( mPartyContact.getNumber().split(SplitChar));

        recyclerView=(RecyclerView)view.findViewById(R.id.numberlist);

        TextView textView = (TextView) view.findViewById(R.id.title);
        Submit = (Button) view.findViewById(R.id.ok);
        Cancel = (Button) view.findViewById(R.id.cancel);

        if(partyContacts.size()==1){

            Context context = view.getContext();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mPartyContact.getNumber().toString()));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            context.startActivity(intent);
            return;
        }else
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            CallAdapter adapter = new CallAdapter(context, partyContacts, new RecycleViewOnItemClickListener() {
                @Override
                public void onCallClick(View view, String number) {
                    Context context = view.getContext();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + number));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    context.startActivity(intent);
                    dialog.dismiss();
                }

                @Override
                public void onClick(View view, int position, boolean isLongClick) {

                }
            });
            recyclerView.setAdapter(adapter);

            textView.setText(mPartyContact.getName());
        }

        if(OkBtnReq==false)
            Submit.setVisibility(View.GONE);
            Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }

        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        }


}

package com.example.pc24.cbohelp.FollowUp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc24.cbohelp.Followingup.CustomDatePicker;
import com.example.pc24.cbohelp.PartyView.mParty;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.services.CboServices_Old;
import com.example.pc24.cbohelp.utils.MyAPIService;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by pc24 on 28/11/2017.
 */

 public class FollowupDialog   {

    Integer response_code;
    Bundle Msg;
    Dialog dialog;
    Context context;
    Button nextfollowup_Date;
    ImageView spinner_img_nextfollowdate;
    Button Submit, Cancel;
    Shareclass shareclass;
    ArrayList<mFollowupgrid> followupdata = new ArrayList<>();
    IFollowupDialog iFollowupDialog=null;
    String Current_Date="";
    String msg="";
    private Boolean negativeBtnReqd = true;
    private Boolean Postivebtnreq   =  true;
    public interface IFollowupDialog{
        void onFollowSubmit();
    }

    public FollowupDialog(@NonNull Context context, Bundle Msg, Integer response_code,boolean postivebtnreq,boolean negativeBtnReqd,IFollowupDialog iFollowupDialog) {
        this.negativeBtnReqd = negativeBtnReqd;
        this.Postivebtnreq = postivebtnreq;
        this.context = context;
        this.iFollowupDialog = iFollowupDialog;
        this.response_code = response_code;
        this.Msg = Msg;

    }


    public FollowupDialog(@NonNull Context context, Bundle Msg, Integer response_code,boolean negativeBtnReqd,IFollowupDialog iFollowupDialog) {

        this.negativeBtnReqd = negativeBtnReqd;
        this.context = context;
        this.iFollowupDialog = iFollowupDialog;
        this.response_code = response_code;
        this.Msg = Msg;

    }
   /* public FollowupDialog newinstance(@NonNull Context context, Bundle Msg, Integer response_code,boolean negativeBtnReqd,IFollowupDialog iFollowupDialog) {
  FollowupDialog followupDialog=new FollowupDialog(context,Msg);
        this.negativeBtnReqd = negativeBtnReqd;
        this.context = context;
        this.iFollowupDialog = iFollowupDialog;
        this.response_code = response_code;
        this.Msg = Msg;

        return followupDialog;
    }*/
    public void show() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog, null, false);


        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);



        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText(Msg.getString("header"));
        final EditText remark=(EditText)view.findViewById(R.id.remark);
        Submit = (Button) view.findViewById(R.id.ok);
        Cancel = (Button) view.findViewById(R.id.cancel);
        spinner_img_nextfollowdate = (ImageView) view.findViewById(R.id.spinner_img_nextfollowdate);
        final Calendar myCalendar = Calendar.getInstance();

        nextfollowup_Date = (Button) view.findViewById(R.id.nextfollowdatebtn);
        nextfollowup_Date.setText(CustomDatePicker.currentDate( CustomDatePicker.ShowFormat));

        Current_Date=CustomDatePicker.currentDate(CustomDatePicker.CommitFormat);
        shareclass = new Shareclass();

        nextfollowup_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context, CustomDatePicker.getDate(Current_Date,CustomDatePicker.CommitFormat))
                            .Show(CustomDatePicker.getDate(nextfollowup_Date.getText().toString(),  CustomDatePicker.ShowFormat)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {
                                    nextfollowup_Date.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));


                                }
                            });

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
        spinner_img_nextfollowdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextfollowup_Date.performClick();

            }
        });

        if(Postivebtnreq==false){
            Submit.setVisibility(View.GONE);
        }
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//Extract the data…
                HashMap<String, String> request = new HashMap<>();
                request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));
                request.put("iId", Msg.getString("iId"));
                request.put("iSrno", ""+Msg.getInt("iSrno",0));
                request.put("iPaId", Msg.getString("iPaid"));
                request.put("sFollowUpdate",Current_Date );
                request.put("sRemark", remark.getText().toString());
                try {
                    request.put("sNextFollowUpdate",CustomDatePicker.formatDate( CustomDatePicker.getDate(nextfollowup_Date.getText().toString(),  CustomDatePicker.ShowFormat),CustomDatePicker.CommitFormat));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                request.put("sContactPerson", Msg.getString("sContactPerson"));
                request.put("sContactNo", Msg.getString("sContactNo"));
                request.put("sFormType", "ORDER_STATUS_FOLLOWUP");
                request.put("iUserId", Msg.getString("iUserId"));
                ArrayList<Integer> tables = new ArrayList<>();
                tables.add(0);
                    new MyAPIService(context)
                            .execute(new ResponseBuilder("FollowCommit",request)
                                    .setTables(tables).setResponse(new CBOServices.APIResponse() {
                                        @Override
                                        public void onComplete(Bundle message) {
                                            if(msg.equalsIgnoreCase("ok")){
                                                iFollowupDialog.onFollowSubmit();
                                            }
                                            else { }
                                            }
                                        @Override
                                        public void onResponse(Bundle response) {
                                            parser2(response);
                                        }
                                    })
                            );
                dialog.dismiss();
            }

        });
        if (negativeBtnReqd==false ){
            Cancel.setVisibility(View.GONE);
        }
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

    private void parser2(Bundle result) {
        {

                try {


                    String table0 = result.getString("Tables0");
                    JSONArray jsonArray1 = new JSONArray(table0);
                    followupdata.clear();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject c = jsonArray1.getJSONObject(i);
                          msg = c.getString("MSG");


                    }


                }catch (Exception e) {
                    e.printStackTrace();
                }

        }


    }
}

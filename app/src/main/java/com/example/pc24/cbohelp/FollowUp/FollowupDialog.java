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

import com.example.pc24.cbohelp.Followingup.CustomDatePicker;
import com.example.pc24.cbohelp.PartyView.mParty;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.services.CboServices_Old;

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

public class FollowupDialog {
    Handler h1;
    Integer response_code;
    Bundle Msg;
    Dialog dialog;
    Context context;
    Button nextfollowup_Date;
    ImageView spinner_img_nextfollowdate;
    Button Submit, Cancel;
    Shareclass shareclass;
    DBHelper dbHelper;
    ProgressDialog progess;
    ArrayList<mFollowupgrid> followupdata = new ArrayList<>();
    IFollowupDialog iFollowupDialog=null;
    VM_Followup vm_followup;
    mFollowupgrid mFollow;
    private static final int FOLLOWUPGRID = 1;
    String Current_Date="";
    mParty  mParty;
    String NextDate="";
    CustomDatePicker customDatePicker;

    public String getNextDate() {
        return NextDate;
    }

    public FollowupDialog setNextDate(String nextDate) {


        NextDate = nextDate;
        return this;
    }

    public mParty getmParty() {
        return mParty;
    }

    public void setmParty(mParty mParty) {
        this.mParty = mParty;
    }

    public interface IFollowupDialog{
        void onFollowSubmit();

    }

    public FollowupDialog(@NonNull Context context, Bundle Msg, Integer response_code,IFollowupDialog iFollowupDialog) {
        //super(context);
        this.context = context;
        this.iFollowupDialog = iFollowupDialog;
        this.response_code = response_code;
        this.Msg = Msg;
    }

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
       /* SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

        nextfollowup_Date.setText(currentDate());
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

            private void updateLabel() {

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                nextfollowup_Date.setText(sdf.format(myCalendar.getTime()));
            }

        };*/
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

              /*  new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/

            }
        });
        spinner_img_nextfollowdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextfollowup_Date.performClick();

            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progess = new ProgressDialog(context);
                shareclass = new Shareclass();
                dbHelper = new DBHelper(context);

                try {

//Extract the data…

                HashMap<String, String> request = new HashMap<>();
                request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));
                request.put("iId", Msg.getString("iId"));
                request.put("iSrno", ""+Msg.getInt("iSrno",0));
                request.put("iPaId", Msg.getString("iPaId"));
                request.put("sFollowUpdate",Current_Date );
                request.put("sRemark", remark.getText().toString());
              /*  request.put("sNextFollowUpdate",iFollowupDialog.Nextdate(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat).toString()));*/

                    request.put("sNextFollowUpdate",CustomDatePicker.formatDate( CustomDatePicker.getDate(nextfollowup_Date.getText().toString(),  CustomDatePicker.ShowFormat),CustomDatePicker.CommitFormat));

                request.put("sContactPerson", Msg.getString("sContactPerson"));
                request.put("sContactNo", Msg.getString("sContactNo"));
                request.put("sFormType", "ORDER_STATUS_FOLLOWUP");
                request.put("iUserId", Msg.getString("iUserId"));
                ArrayList<Integer> tables = new ArrayList<>();
                tables.add(0);


                progess.setMessage("Please Wait..\n" +
                        " Fetching data");
                progess.setCancelable(false);
                progess.show();
                new CboServices_Old(context, mHandler).customMethodForAllServices(request, "FollowCommit", FOLLOWUPGRID, tables);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

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


    }


    public String currentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        Date todayDate = new Date();
        String currentDate = dateFormat.format(todayDate);


        return currentDate("MM/dd/yyyy");
    }

    public String currentDate(String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        Date todayDate = new Date();
        String currentDate = dateFormat.format(todayDate);


        return currentDate;
    }
//////////////////////////////////////////

    public String convetDateddMMyyyy(Date date) {

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy");

        String todayDate = timeStampFormat.format(date);

        return todayDate.toString();
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FOLLOWUPGRID:
                    progess.dismiss();
                    if ((null != msg.getData())) {
                        parser2(msg.getData());
                        //  parser1(msg.getData());

                    }
                    break;
                case 99:
                    progess.dismiss();
                    if ((null != msg.getData())) {

                        //Toast.makeText(context, msg.getData().getString("Error"), Toast.LENGTH_SHORT).show();

                    }
                    break;
                default:
                    progess.dismiss();

            }
        }
    };

    private void parser2(Bundle result) {
        {
            if ((result == null)) {
                progess.dismiss();

            } else {
                try {


                    String table0 = result.getString("Tables0");
                    JSONArray jsonArray1 = new JSONArray(table0);
                    followupdata.clear();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject c = jsonArray1.getJSONObject(i);
                        String msg = c.getString("MSG");
                        if(msg.equalsIgnoreCase("ok")){

                            progess.dismiss();
                            iFollowupDialog.onFollowSubmit();
                        }else {
                            progess.dismiss();
                        }

                    }


                } catch (Exception e) {
                    progess.dismiss();
                    e.printStackTrace();
                }
            }
        }


    }
}

package com.example.pc24.cbohelp.utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.BatteryManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc24.cbohelp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pc24 on 06/01/2017.
 */
public class Custom_Variables_And_Method {

    private static Custom_Variables_And_Method ourInstance = null;



    private static Context context;
    public static String GLOBAL_LATLON = "0.0,0.0";
    public static String FMCG_PREFRENCE = "FMCG_PREFRENCE";
    public static String BATTERYLEVEL="0";

    public static String COMPANY_CODE;
    public static String DCR_ID ="0";
    public static int PA_ID=0;
    public static String VERSION = "20181030";
    public static Custom_Variables_And_Method getInstance() {
        if (ourInstance == null)
        {
            ourInstance = new Custom_Variables_And_Method();
        }
        return ourInstance;
    }

    private Custom_Variables_And_Method() {
    }

    public void snackBar(String msg, View v) {
        android.support.design.widget.Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        sbView.performClick();

        snackbar.show();
    }


    public void msgBox(Context context,String msg) {
        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toastbackground);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(0xFFFFFFFF);
        //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,150);
        toast.show();
    }


    public void getbattrypercentage(Context context) {


        BroadcastReceiver br = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int current_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (current_level >= 0 && scale > 0) {
                    level = (current_level * 100) / scale;
                }
                BATTERYLEVEL= ""+level;
            }
        };
        IntentFilter batrylevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(br, batrylevelFilter);
    }

    public void getAlert(Context context, String title, String massege) {
        getAlert(context,title,massege,null);
    }
    public void getAlert(Context context, String title, String massege,String[] table_list) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final TableLayout Alert_message_list= (TableLayout) dialogLayout.findViewById(R.id.table_view);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);
        if (table_list==null ) {
            Alert_message.setText(massege);
            Alert_message_list.setVisibility(View.GONE);
        }else{
            Alert_message.setVisibility(View.GONE);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            TableRow.LayoutParams params1 = new TableRow.LayoutParams(0, 1, 1f);
            Alert_message_list.removeAllViews();
            for (int i = 0; i < table_list.length; i++) {
                TableRow tbrow = new TableRow(context);
                TextView t1v = new TextView(context);
                t1v.setText(table_list[i]);
                t1v.setPadding(5, 5, 5, 0);
                t1v.setTextColor(Color.BLACK);
                t1v.setLayoutParams(params);
                t1v.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
                tbrow.addView(t1v);
               /* TextView t2v = new TextView(context);
                t2v.setText(table_list[i]);
                t2v.setPadding(5, 5, 5, 0);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);*/
                TableRow tbrow1 = new TableRow(context);
                TextView t3v = new TextView(context);
                t3v.setPadding(1, 1, 1, 0);
                t3v.setLayoutParams(params1);
                t3v.setBackgroundColor(0xff125688);
                tbrow1.addView(t3v);
                Alert_message_list.addView(tbrow);
                Alert_message_list.addView(tbrow1);
            }
        }


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }



    ///////////// Getting Current Date ///////////////////
    public String convetDateMMddyyyy(Date date) {

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM/dd/yyyy");
        String todayDate = timeStampFormat.format(date);
        return todayDate.toString();
    }


    public String currentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

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


    ///////////////////////////////Getting Current time ///////////////////

    public String currentTime() {

        String mytime = "";
        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        mytime = String.format("%02d.%02d", mHour, mMinute);
        return mytime;

    }

    public String get_currentTimeStamp(){
        return ""+new Date().getTime();
    }

    public String getDataFrom_FMCG_PREFRENCE(Context context,String key,String default_value) {

        SharedPreferences myPrefrence = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE);
        String value = myPrefrence.getString(key, null);
        if (value == null) {
            value = default_value;
        }

        return value;
    }
}

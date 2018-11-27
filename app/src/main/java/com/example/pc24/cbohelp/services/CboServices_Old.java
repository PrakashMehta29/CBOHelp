package com.example.pc24.cbohelp.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.utils.SendMailTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



/**
 * Created by pc24 on 01/12/2016.
 */

public class CboServices_Old {

    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL= "http://www.cboservices.com/cbochat.asmx";
    private final Handler h1;
    private Context context;
    Shareclass shareclass;




    public CboServices_Old(Context context, Handler hh) {
        h1 = hh;
        this.context=context;
        shareclass=new Shareclass();
    }

    public CboServices_Old(View.OnClickListener onClickListener, Handler mHandler,Context context) {
        h1=mHandler;
        this.context=context;
        shareclass=new Shareclass();

    }

    public void customMethodForAllServices(final HashMap<String,String> data, final String methodName, final Integer response_code, final ArrayList<Integer> table) {
        Runnable runnable = new Runnable() {

            public void run() {
                final String ACITON_NAME = NAMESPACE + methodName;
                SoapPrimitive result=null;

                //System.setProperty("http.keepAlive", "false");

                SoapObject request;
                request = new SoapObject(NAMESPACE, methodName);

                for (String key : data.keySet()) {

                    request.addProperty(key, data.get(key));

                }


                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapSerializationEnvelope.dotNet = true;
                soapSerializationEnvelope.setOutputSoapObject(request);
                HttpTransportSE httpTransportSE = new HttpTransportSE(URL,60000);

                for (int no_of_try=1;no_of_try<=3;no_of_try++) {
                    try {
                        httpTransportSE.call(ACITON_NAME, soapSerializationEnvelope);
                        result = (SoapPrimitive) soapSerializationEnvelope.getResponse();

                        threadMsg(result.toString(), response_code, table, methodName);
                        break;
                    } catch (SocketTimeoutException e) {
                        // Timed out
                        if (no_of_try==3) {
                            threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), response_code, table, methodName);
                        }

                    } catch (Exception e) {
                        threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), response_code, table, methodName);
                        break;
                    }
                }

            }

        };
        final Thread mythread1 = new Thread(runnable);
        mythread1.start();

        /*Runnable checkConection = new Runnable() {

            @Override
            public void run() {
                boolean[] connection=isConnectingToInternet(context);
                if(connection[0] && connection[1]){
                    mythread1.start();
                }else if(!connection[0]){
                    //not connected to internet
                    threadErrorMsg("Please connect to internet");
                }else{
                    //connection time out
                    threadErrorMsg("Your Conection is too Slow \n Unable to connect to Server \n Please Try again");
                }
            }
        };

        Thread mythread2 = new Thread(checkConection);
        mythread2.start();*/

    }

    private void threadErrorMsg(String errorMsg) {
        Message msgObj = h1.obtainMessage(99);
        Bundle b = new Bundle();
        b.putString("Error",errorMsg);
        msgObj.setData(b);
        h1.sendMessage(msgObj);
    }

    private void threadMsg(final String result,Integer response_code,ArrayList<Integer> tables,final String methodName) {
        if(tables.size()==0){
            /*Message msgObj = h1.obtainMessage(100);
            Bundle b = new Bundle();
            b.putString("Error","Empty Table");
            msgObj.setData(b);
            h1.sendMessage(msgObj);*/
        }else if(result!=null && !result.toUpperCase().contains("[ERROR]")) {


            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("Tables");

                Message msgObj = h1.obtainMessage(response_code);
                Bundle b = new Bundle();
                if (tables.get(0)==-2){
                    tables.set(0,-1);
                }
                if (tables.get(0)==-1){
                    tables.remove(0);
                    for (int j=0;j<jsonArray.length();j++){
                        tables.add(j);
                    }

                }
                for(int i:tables){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Tables"+i);
                    // Log.d("MYAPP", "objects are: " + jsonArray1.toString());

                    b.putString("Tables"+i,jsonArray1.toString());

                }
                Log.d("MYAPP", "result: " + result);
                msgObj.setData(b);
                h1.sendMessage(msgObj);
            } catch (final JSONException e) {
                Log.d("MYAPP", "error are: " + e.toString());

                threadErrorMsg("Technical Error");

                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        getAlert(context,"Missing table error",e.toString());
                        String subject="";
                        if (e.toString().length()>22  && e.toString().length()>=80){
                            subject=e.toString().substring(22,80);
                        }else if (e.toString().length()>22){
                            subject=e.toString().substring(22);
                        }else{
                            subject=e.toString();
                        }
                        List toEmailList = Arrays.asList("mobileapperror@gmail.com".split("\\s*,\\s*"));
                        /*new SendMailTask().execute("mobileapperror@gmail.com",
                                "Cbo12345",toEmailList , "Missing table error", e.toString());

                        List toEmailList = Arrays.asList("mobileapperror@gmail.com".split("\\s*,\\s*"));*/
                        new SendMailTask((Activity) context).execute("mobileapperror@gmail.com",
                                "Cbo12345",toEmailList , shareclass.getValue(context,"company_code","demo")+": "+subject,context.getResources().getString(R.string.app_name)+"\n Company Code :"+shareclass.getValue(context,"company_code","demo")+"\n PA ID : "+shareclass.getValue(context,"PA_ID","0")+"\n App version : "+getVersionCode(context)+"\n methodName : "+methodName+"\n Error Alert :"+ "Missing table error"+"\n"+ e.toString());

                    }
                });
            }


        }else{
            if (tables.get(0)!=-2) {
                Log.d("MYAPP", "error are: " + result);
                threadErrorMsg("Somthing Went Wrong");
                assert result != null;
                final String title;
                final String message;
                if (result.contains("service  method")) {
                    title = "Internet Error";
                    message = context.getResources().getString(R.string.Internet_error);
                } else {
                    title = "Server Error";
                    message = context.getResources().getString(R.string.try_later);
                }
                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        getAlert(context, title, message);
                        String subject = "";
                        if (result.length() > 22 && result.length() >= 80) {
                            subject = result.substring(22, 80);
                        } else if (result.length() > 22) {
                            subject = result.substring(22);
                        } else {
                            subject = result;
                        }
                        List toEmailList = Arrays.asList("mobileapperror@gmail.com".split("\\s*,\\s*"));
                        new SendMailTask((Activity) context).execute("mobileapperror@gmail.com",
                                "Cbo12345",toEmailList , shareclass.getValue(context,"company_code","demo")+": "+subject,context.getResources().getString(R.string.app_name)+"\n Company Code :"+shareclass.getValue(context,"company_code","demo")+"\n PA ID : "+shareclass.getValue(context,"PA_ID","0")+"\n App version : "+getVersionCode(context)+"\n methodName : "+methodName+ "\n Error Alert :" + title + "\n" + result);


                  /*  //getAlert(context, "Service error", result);
                    List toEmailList = Arrays.asList("mobileapperror@gmail.com".split("\\s*,\\s*"));
                    new SendMailTask().execute("mobileapperror@gmail.com",
                    "Cbo12345",toEmailList , title, result);*/
                    }
                });
            }
        }
    }

    public void getImage(String image_url,Integer response_code) {

        InputStream in;
        Bitmap myBitmap=null;
        HttpURLConnection connection = null;

        try {
            java.net.URL url = new URL(image_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(in);
            connection.disconnect();

            Message msgObj = h1.obtainMessage(response_code);
            Bundle b = new Bundle();

            b.putParcelable("BitImage",myBitmap);

            msgObj.setData(b);
            h1.sendMessage(msgObj);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getVersionCode(Context context) {
        int v = 0;
        try {
            v = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return v;
    }

    public static boolean[] isConnectingToInternet(Context mContext) {
        final boolean[] result={false,false};
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    result[0]=true;
                    result[1]=hostAvailable("www.google.com", 80,mContext);
                    return result;
                }
            }
        }else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            result[0]=true;
                            result[1]=hostAvailable("www.google.com", 80,mContext);
                            return result;
                        }
                    }
                }
            }
        }
//        Toast.makeText(mContext,"please_connect_to_internet",Toast.LENGTH_SHORT).show();
        return result;
    }




    private static boolean hostAvailable(final String host, final int port,final Context mContext) {

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 2000);
            socket.close();

            return true;
        } catch (IOException e) {
            // Either we have a timeout or unreachable host or failed DNS lookup

            //Toast.makeText(mContext, "Your Conection is too Slow", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    public static void getAlert(Context context, String title, String messege) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);
        Alert_message.setText(messege);


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

}



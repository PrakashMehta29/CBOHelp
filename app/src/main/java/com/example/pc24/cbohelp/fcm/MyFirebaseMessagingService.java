package com.example.pc24.cbohelp.fcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.pc24.cbohelp.Client_Complain_list;
import com.example.pc24.cbohelp.Company_Grid;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.up_down_ftp;
import com.example.pc24.cbohelp.utils.FloatingViewService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;


/**
 * Created by pc24 on 09/12/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Shareclass shareclass;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        shareclass=new Shareclass();
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("body"));
        /*isAppIsInBackground(this,remoteMessage.getNotification().getBody());*/
        if(!isAppIsInBackground(this,remoteMessage.getData().get("body"))){
            /*sendNotification(remoteMessage.getData().get("body"));*/
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(remoteMessage.getData().get("body"));
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String  msgtyp= jsonObject.getString("msg");
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                String  file= jsonObject1.getString("file");
                JSONObject jsonObject2 = jsonArray.getJSONObject(2);
                String  title= jsonObject2.getString("title");
                JSONObject jsonObject3 = jsonArray.getJSONObject(3);
                String  DOC_NO= jsonObject3.getString("DOC_NO");
                JSONObject jsonObject4 = jsonArray.getJSONObject(4);
                String  status= jsonObject4.getString("status");
                String show_report="P";
                if (status.toLowerCase().equals("complete")){
                    show_report="C";
                }

                /*File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO_help" + File.separator +file);

                if(file!=null && !file.equals("")){

                    new up_down_ftp().downloadSingleFile(file,file2,this,"chat");
                }*/
                String PA_ID="Admin";
                if( shareclass.getValue(this,"DESIG_ID","0").equals("100")) {
                    JSONObject jsonObject5 = jsonArray.getJSONObject(5);
                    PA_ID= jsonObject5.getString("PA_ID");
                    //PA_ID="Admin";
                }
               // new DBHelper(this).insertChat("0",msgtyp,file2.getPath(),"0",new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()),"0",PA_ID);
                show_floatingView(remoteMessage.getData().get("body"),"0");
                sendNotification(msgtyp,title,show_report,DOC_NO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private boolean isAppIsInBackground(Context context,String messageBody) {
        boolean isInBackground = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            //Log.d(TAG, "From: " +"PassMessage "+shareclass.getValue(this,"Chat","InActive"));
                            if(shareclass.getValue(this,"Chat","InActive").equals("Active")){
                                //Log.d(TAG, "From: " +"PassMessage");
                                sendMessage(messageBody);
                            }else{
                                //Calling method to generate notification
                                return false;
                            }
                            isInBackground = true;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                //Log.d(TAG, "From: " +"PassMessage "+shareclass.getValue(this,"Chat","InActive"));
                if(shareclass.getValue(this,"Chat","InActive").equals("Active")){
                    //Log.d(TAG, "From: " +"PassMessage");
                    sendMessage(messageBody);
                }else{
                    //Calling method to generate notification
                    return false;

                }
                isInBackground = true;
            }
        }

        return isInBackground;
    }


    public void sendMessage(String msg) {
        Log.d(TAG, "Broadcasting message");
        show_floatingView(msg,"1");

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(msg);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String  msgtyp= jsonObject.getString("msg");
            JSONObject jsonObject1 = jsonArray.getJSONObject(1);
            String  file= jsonObject1.getString("file");
            JSONObject jsonObject2 = jsonArray.getJSONObject(2);
            String  title= jsonObject2.getString("title");
            JSONObject jsonObject3 = jsonArray.getJSONObject(3);
            String  DOC_NO= jsonObject3.getString("DOC_NO");
            JSONObject jsonObject4 = jsonArray.getJSONObject(4);
            String  status= jsonObject4.getString("status");

            Intent intent = new Intent("Message_Recieved");
            intent.putExtra("msg",msgtyp);
            intent.putExtra("file",file);
            intent.putExtra("title",title);
            intent.putExtra("DOC_NO",DOC_NO);
            intent.putExtra("status",status);
            //intent.setAction("Message_Recieved");
            //sendBroadcast(intent);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void show_floatingView(String msg,String who){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(msg);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String  msgtyp= jsonObject.getString("msg");
            JSONObject jsonObject1 = jsonArray.getJSONObject(1);
            String  file= jsonObject1.getString("file");
            JSONObject jsonObject2 = jsonArray.getJSONObject(2);
            String  title= jsonObject2.getString("title");
            JSONObject jsonObject3 = jsonArray.getJSONObject(3);
            String  DOC_NO= jsonObject3.getString("DOC_NO");
            JSONObject jsonObject4 = jsonArray.getJSONObject(4);
            String  status= jsonObject4.getString("status");
            JSONObject jsonObject5 = jsonArray.getJSONObject(5);
            String  PA_ID= jsonObject5.getString("PA_ID");
            JSONObject jsonObject6 = jsonArray.getJSONObject(6);
            String  COMMENT= jsonObject6.getString("COMMENT");
            JSONObject jsonObject7 = jsonArray.getJSONObject(7);
            String  COMPLANE_TYPE= jsonObject7.getString("COMPLANE_TYPE");
            JSONObject jsonObject8 = jsonArray.getJSONObject(8);
            String  DONE_BY= jsonObject8.getString("DONE_BY");
            JSONObject jsonObject9 = jsonArray.getJSONObject(9);
            String  TIME= jsonObject9.getString("TIME");

            Intent intent=new Intent(this, FloatingViewService.class);
            intent.putExtra("msg",msgtyp);
            intent.putExtra("file",file);
            intent.putExtra("title",title);
            intent.putExtra("DOC_NO",DOC_NO);
            intent.putExtra("status",status);
            intent.putExtra("PA_ID",PA_ID);
            intent.putExtra("COMMENT",COMMENT);
            intent.putExtra("COMPLANE_TYPE",COMPLANE_TYPE);
            intent.putExtra("DONE_BY",DONE_BY);
            intent.putExtra("TIME",TIME);
            intent.putExtra("WHO",who);

            startService(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody,String title,String show_report,String DOC_NO) {
        Intent intent = new Intent(this, Client_Complain_list.class);
        intent.putExtra("pa_id", "");
        intent.putExtra("name","ALL");
        intent.putExtra("s1", "");
        intent.putExtra("s2","");
        intent.putExtra("who","party_list");
        intent.putExtra("show_report",show_report);
        intent.putExtra("DOC_NO",DOC_NO);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cbo_noti)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}

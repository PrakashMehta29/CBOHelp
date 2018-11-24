package com.example.pc24.cbohelp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.services.CboServices;
import com.example.pc24.cbohelp.utils.SendMailTask;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Login extends AppCompatActivity {

    Button signin;
    public final static int REQUEST_CODE = 10101;
    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    String picturePath="";
    ImageView image_capture1;
    EditText company_code,user_id,password;
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;
    Shareclass shareclass;
    boolean another_app=false,valid_user=false;
    String expend_menu="";
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin= (Button) findViewById(R.id.signin);
        image_capture1=(ImageView) findViewById(R.id.image);

        company_code= (EditText) findViewById(R.id.company_code);
        user_id= (EditText) findViewById(R.id.user_id);
        password= (EditText) findViewById(R.id.password);

        progress1 = new ProgressDialog(this);
        dbHelper=new DBHelper(this);
        shareclass=new Shareclass();



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkInput()){

                  /*  Intent intent =new Intent(Login.this, Client.class);
                    startActivity(intent);*/
                   login_user();
                }


            }
        });
    }

    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(Login.this, "Please allow the permission to Login", Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == GALLERY_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                login_user();
            }else{
                Toast.makeText(Login.this, "Please allow the permission to Login", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Boolean checkInput(){
        if (company_code.getText() !=null && !company_code.getText().toString().equals("")
        && user_id.getText() !=null && !user_id.getText().toString().equals("")
        && password.getText() !=null && !password.getText().toString().equals("")){
            shareclass.save(this,"company_code",company_code.getText().toString());
            if (checkDrawOverlayPermission()){
                if (ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    Toast.makeText(Login.this, "Please allow the permission", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(Login.this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, Login.this.GALLERY_ACTIVITY_CODE);
                    return false;
                }else {
                    return true;
                }
            }
        }
        Toast.makeText(getApplicationContext(),"Please check, fileds cannot be blank ",Toast.LENGTH_SHORT).show();
        return false;
    }

    private void login_user(){

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sDbName", company_code.getText().toString());
        request.put("USERNAME", user_id.getText().toString());
        request.put("PASSWORD", password.getText().toString());

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(-1);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(this,mHandler).customMethodForAllServices(request,"LOGIN_CHAT",MESSAGE_INTERNET,tables);

        //End of call to service
    }

    private void open_activity(){

        Intent intent = new Intent(Login.this, Client_Complain_list.class);
        intent.putExtra("pa_id", "");
        intent.putExtra("name","ALL");
        intent.putExtra("s1", "");
        intent.putExtra("s2","");
        intent.putExtra("who","party_list");
        intent.putExtra("show_report","P");
        intent.putExtra("DOC_NO","");
        startActivity(intent);
        finish();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser1(msg.getData());

                    }
                    break;
                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();

                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };
    public void parser1(Bundle result) {
        if (result!=null ) {
            //dbHelper.deleteMenu();
            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    //product_name.add(jsonObject2.getString("DEAL_NAME"));
                    if(jsonObject2.getString("STATUS").equals("Y")){
                        shareclass.save(this,"PA_ID",jsonObject2.getString("PA_ID"));
                        shareclass.save(this,"PA_NAME",jsonObject2.getString("PA_NAME"));
                        shareclass.save(this,"ADDRESS",jsonObject2.getString("ADDRESS"));
                        shareclass.save(this,"MOBILE",jsonObject2.getString("MOBILE"));
                        shareclass.save(this,"PHONE",jsonObject2.getString("PHONE"));
                        shareclass.save(this,"DESIG_ID",jsonObject2.getString("DESIG_ID"));
                        valid_user=true;
                    }else{
                        Toast.makeText(this,jsonObject2.getString("STATUS"),Toast.LENGTH_LONG).show();
                    }


                }
                if (valid_user) {

                    String table2 = result.getString("Tables1");
                    JSONArray jsonArray3 = new JSONArray(table2);

                    for (int i = 0; i < jsonArray3.length(); i++) {
                        JSONObject jsonObject2 = jsonArray3.getJSONObject(i);
                        shareclass.save(this, "WEB_IP", jsonObject2.getString("WEB_IP"));
                        shareclass.save(this, "WEB_USER", jsonObject2.getString("WEB_USER"));
                        shareclass.save(this, "WEB_PWD", jsonObject2.getString("WEB_PWD"));
                        shareclass.save(this, "WEB_ROOT_PATH", jsonObject2.getString("WEB_ROOT_PATH"));

                    }


                    String table3 = result.getString("Tables2");
                    JSONArray jsonArray4 = new JSONArray(table3);

                    dbHelper.deleteTeam();
                    for (int i = 0; i < jsonArray4.length(); i++) {
                        JSONObject jsonObject2 = jsonArray4.getJSONObject(i);
                        dbHelper.insertTeam(jsonObject2.getString("ID"),jsonObject2.getString("USER_NAME"),jsonObject2.getString("MANAGER_PA_ID"),jsonObject2.getString("MANAGER_NAME"));

                    }

                    UpdateFCM_Tocken();
                    open_activity();
                }
                progress1.dismiss();


            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this,"Missing field error",e.toString());
                List toEmailList = Arrays.asList("support@cboinfotech.com".split("\\s*,\\s*"));
                new SendMailTask(this).execute("support@cboinfotech.com",
                        "Cbo12345",toEmailList , "Missing field error", e.toString());
                //e.printStackTrace();
            }

        }
        Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }
    private void UpdateFCM_Tocken(){

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sDbName", shareclass.getValue(this,"company_code","demo"));
        request.put("iPA_ID",shareclass.getValue(this,"PA_ID","0"));
        request.put("sGCM_TOKEN", shareclass.getValue(this,"FCM_key","0"));
        request.put("sMobileId", "0");
        request.put("sVersion", "0");

        ArrayList<Integer> tables=new ArrayList<>();
        // tables.add(0);


        new CboServices(this,mHandler).customMethodForAllServices(request,"GCM_TOKEN_UPDATE",MESSAGE_INTERNET,tables);
        if( shareclass.getValue(this,"DESIG_ID","0").equals("100")){
            FirebaseMessaging.getInstance().subscribeToTopic("Admin");
        }

        //End of call to service
    }

}

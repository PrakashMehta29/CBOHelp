package com.example.pc24.cbohelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pc24.cbohelp.Followingup.NewPartyActivity;
import com.example.pc24.cbohelp.PartyView.PartyActivity;
import com.example.pc24.cbohelp.adaptor.New_order_confirm_Adaptor;
import com.example.pc24.cbohelp.adaptor.ComplainList_Adapter;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.services.CboServices_Old;
import com.example.pc24.cbohelp.utils.Custom_Variables_And_Method;
import com.example.pc24.cbohelp.utils.GalleryUtil;
import com.example.pc24.cbohelp.utils.ImageFilePath;
import com.example.pc24.cbohelp.utils.SendMailTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class Client_Complain_list extends AppCompatActivity implements SearchView.OnQueryTextListener,SwipeRefreshLayout.OnRefreshListener,up_down_ftp.AdapterCallback{

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1,MESSAGE_INTERNET_COMPLAIN_COMIT=3;
    private  static final int MESSAGE_INTERNET_main=2;
    ListView listView,listView_alert;
    Button all,complete,pending;
    New_order_confirm_Adaptor new_order_adaptor_alert;
    ComplainList_Adapter order_list_adaptor;
    ArrayList<HashMap<String,String>> adaptor_data,adaptor_data_alert;
    ArrayList<String> id,particulars,rate,qty,amt,order_no,date,amt_alert,order_id;
    String b_amt="0",LastReport="P",name;
    Shareclass shareclass;
    DBHelper dbHelper;
    FloatingActionButton add_complain;
    Context context;
    int order_position=0;
    String PA_ID,STATUS,s1,s2,who,show_report="P",updated_complain,Login_user;
    SearchView searchView;
    MenuItem searchMenuItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "MyFirebaseMsgService";
    Boolean update_list=true;
    ImageView attach_img;
    Custom_Variables_And_Method customVariablesAndMethod;

    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    String picturePath="";
    private File output=null;
    String filename="";
    String web_root_path="",Remark="",Status="";
    int who_requested=0;
    Uri mUri=null;

    HashMap<String, ArrayList<String>> team_list;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_complain_list);

        listView= (ListView) findViewById(R.id.client_order_list);
        all= (Button) findViewById(R.id.all);
        pending= (Button) findViewById(R.id.pending);
        complete= (Button) findViewById(R.id.complete);
        add_complain = (FloatingActionButton) findViewById(R.id.add_complain);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);


        progress1 = new ProgressDialog(this);
        adaptor_data=new ArrayList<>();
        adaptor_data_alert=new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(this);

        Intent intent=getIntent();
        PA_ID=intent.getStringExtra("pa_id");
        name=intent.getStringExtra("name");
        s1=intent.getStringExtra("s1");
        s2=intent.getStringExtra("s2");
        who=intent.getStringExtra("who");
        show_report=intent.getStringExtra("show_report");
        updated_complain=intent.getStringExtra("DOC_NO");

        getSupportActionBar().setTitle(name);

        shareclass=new Shareclass();
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        context=this;
        dbHelper=new DBHelper(this);
        Login_user = shareclass.getValue(this,"PA_ID","0");
        shareclass.save(context,"filter_type","");

        if (getSupportActionBar() != null && who.equals("add")){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mUri = intent.getParcelableExtra("URI");
            Add_cpmplain();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {


            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent1, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } /*else {
            Intent intent1=new Intent(Client_Complain_list.this, FloatingViewService.class);
            intent1.putExtra("Name","CBO Infotech Pvt. Ltd.");
            startService(intent1);
        }*/

        order_id=new ArrayList<>();
        order_no=new ArrayList<>();
        date=new ArrayList<>();
        amt=new ArrayList<>();

        id=new ArrayList<>();
        particulars=new ArrayList<>();
        rate=new ArrayList<>();
        qty=new ArrayList<>();
        amt_alert=new ArrayList<>();

/*
        adaptor_data.put("order_id",order_id);
        adaptor_data.put("order_no",order_no);
        adaptor_data.put("date",date);
        adaptor_data.put("amt",amt);
*/


        pending.setBackgroundResource(R.drawable.tab_selected);

        shareclass.save(this,"Chat","Active");
        web_root_path=shareclass.getValue(context,"WEB_ROOT_PATH","/DEMO/MAILFILES/CBOACCOUNTS/");
        LocalBroadcastManager.getInstance(this).registerReceiver(
                messageBroadcastReceiver,
                new IntentFilter("Message_Recieved")
        );


        getdata(show_report);

        if (show_report.equals("C")){
            LastReport="C";
            complete.setBackgroundResource(R.drawable.tab_selected);
            all.setBackgroundResource(R.drawable.tab_deselected);
            pending.setBackgroundResource(R.drawable.tab_deselected);
        }


        team_list=dbHelper.getTeam("");
        //adaptor_data=dbHelper.getComplaint("P",PA_ID);

        //order_list_adaptor=new Order_List_Adaptor(this,adaptor_data);
        //listView.setAdapter(order_list_adaptor);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LastReport.equals("")) {
                    getdata("");
                    //adaptor_data=dbHelper.getOrder("ALL",PA_ID);
                    //order_list_adaptor=new Order_List_Adaptor(context,adaptor_data);
                   // listView.setAdapter(order_list_adaptor);
                    LastReport="";
                    //adaptor_data.clear();
                    order_id.clear();
                    order_no.clear();
                    date.clear();
                    amt.clear();
                    all.setBackgroundResource(R.drawable.tab_selected);
                    complete.setBackgroundResource(R.drawable.tab_deselected);
                    pending.setBackgroundResource(R.drawable.tab_deselected);
                }
            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LastReport.equals("P")) {
                    getdata("P");
                   // adaptor_data=dbHelper.getOrder("P",PA_ID);
                   // order_list_adaptor=new Order_List_Adaptor(context,adaptor_data);
                   // listView.setAdapter(order_list_adaptor);
                    LastReport="P";
                    //adaptor_data.clear();
                    pending.setBackgroundResource(R.drawable.tab_selected);
                    complete.setBackgroundResource(R.drawable.tab_deselected);
                    all.setBackgroundResource(R.drawable.tab_deselected);
                }
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LastReport.equals("C")) {
                    getdata("C");
                   // adaptor_data=dbHelper.getOrder("C",PA_ID);
                   // order_list_adaptor=new Order_List_Adaptor(context,adaptor_data);
                   // listView.setAdapter(order_list_adaptor);
                    LastReport="C";
                    //adaptor_data.clear();
                    complete.setBackgroundResource(R.drawable.tab_selected);
                    all.setBackgroundResource(R.drawable.tab_deselected);
                    pending.setBackgroundResource(R.drawable.tab_deselected);
                }
            }
        });

        add_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent i = new Intent(Client_Complain_list.this, PartyDetail.class);
//                startActivity(i);
                if (who.equals("add")){
                    Add_cpmplain();
                }else {
                    Intent intent = new Intent(Client_Complain_list.this, Client_grid.class);
                    //intent.putExtra("title", shareclass.getValue(Client_Complain_list.this, "PA_NAME", "Guest"));
                    intent.putExtra("title", "Select...");
                    startActivity(intent);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               show_Order_alert(position,0);
            }
        });

    }



    // Initialize a new BroadcastReceiver instance
    private BroadcastReceiver messageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (update_list) {
                String msg = intent.getStringExtra("msg");
                Log.d(TAG, "Chat: " + "Broadcast Recieved");
                String status = intent.getStringExtra("status");
                if (status.toLowerCase().equals("complete")){
                    LastReport="C";
                    complete.setBackgroundResource(R.drawable.tab_selected);
                    all.setBackgroundResource(R.drawable.tab_deselected);
                    pending.setBackgroundResource(R.drawable.tab_deselected);
                }else {
                    LastReport="P";
                    pending.setBackgroundResource(R.drawable.tab_selected);
                    complete.setBackgroundResource(R.drawable.tab_deselected);
                    all.setBackgroundResource(R.drawable.tab_deselected);
                }
                updated_complain=intent.getStringExtra("DOC_NO");
                getdata(LastReport);
                //adaptor_data.clear();
            }
            Log.d(TAG, "Chat: " +"Broadcast Recieved");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareclass.save(this,"Chat","InActive");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageBroadcastReceiver);
    }

    private void getdata(String sStatus){
        if (Login_user.equals(shareclass.getValue(context,"PA_ID","0"))) {
            adaptor_data=dbHelper.getComplaint(sStatus,"",PA_ID);
        }else {
            adaptor_data=dbHelper.getComplaint(sStatus,Login_user,PA_ID);
        }


        order_list_adaptor=new ComplainList_Adapter(context,adaptor_data,who,updated_complain);
        listView.setAdapter(order_list_adaptor);

        if (shareclass.getValue(this,"filter_type","").equals("P")){
            order_list_adaptor.filter("",1,100000);
        }

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sDbName", shareclass.getValue(this,"company_code","demo"));
        request.put("iPaId",  PA_ID);
        request.put("sStatus",  sStatus);
        request.put("sDocNO",  "0");
        request.put("sGcm_Token", shareclass.getValue(this,"FCM_key","0"));
        request.put("Auserid",Login_user);
        request.put("TOCKEN_UPDATE_ID", shareclass.getValue(this,"PA_ID","0"));
        request.put("sFILTER_TYPE", shareclass.getValue(this,"filter_type",""));

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        if (adaptor_data.size() == 0)
            progress1.show();

        new CboServices_Old(this,mHandler).customMethodForAllServices(request,"ComplaintGrid",MESSAGE_INTERNET_main,tables);

        //End of call to service

        update_list=false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.logout :
                shareclass.save(this,"PA_ID","0");
                startActivity(new Intent(Client_Complain_list.this,Login.class));
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.change_view:
                break;
            case R.id.user_view:
                Intent intent1=new Intent(Client_Complain_list.this, Admin_view.class);
                intent1.putExtra("View","U");
                startActivity(intent1);
                break;
            case R.id.party_view:
                Intent intent2=new Intent(Client_Complain_list.this, Admin_view.class);
                intent2.putExtra("View","P");
                startActivity(intent2);
                break;
            case R.id.general_view:
                //Toast.makeText(context, "hello "+id, Toast.LENGTH_SHORT).show();
                break;
            case R.id.filter:
                Show_filter();
                break;
            case R.id.party_status:
                Intent intent3=new Intent(Client_Complain_list.this, PartyActivity.class);

                startActivity(intent3);

                break;



            default:
                if (id==-1){
                    Login_user = shareclass.getValue(this,"PA_ID","0");
                    getSupportActionBar().setTitle(name);
                }else{
                    Login_user = team_list.get("PA_ID").get(id);
                    getSupportActionBar().setTitle(team_list.get("PA_NAME").get(id));
                }
                //adaptor_data.clear();
                getdata(LastReport);
                //finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        if (getSupportActionBar() != null && !who.equals("add")) {
            if (team_list.get("PA_ID").size() > 0) {
                menu.add(0, -1, Menu.NONE, name);
            }

            for (int i = 0; i < team_list.get("PA_NAME").size(); i++) {
                menu.add(0, i, Menu.NONE, team_list.get("PA_NAME").get(i));
            }
        }

        MenuItem item = menu.findItem(R.id.general_view);
        item.setVisible(false);

        /*MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinner.setGravity(Gravity.LEFT);
        SpinnerAdapter adapter;
        spinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item));
        //spinner.setOnItemSelectedListener(this); // set the listener, to perform actions based on item selection
*/

        return true;
    }

    private void Show_filter(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Client_Complain_list.this);

        builder.setPositiveButton("Ok", null)
                .setCancelable(false)
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.filter_layout, null);

        final RadioGroup filter= (RadioGroup) dialogLayout.findViewById(R.id.filter_option);
        RadioButton filter_ticket= (RadioButton) dialogLayout.findViewById(R.id.ticket);
        final RadioButton filter_priority= (RadioButton) dialogLayout.findViewById(R.id.priority);
        String filter_type=shareclass.getValue(context,"filter_type","");


        dialog.setView(dialogLayout);
        dialog.setTitle("Filter");

        if (filter_type.equals("")){
            filter_ticket.setChecked(true);
        }else{
            filter_priority.setChecked(true);
        }



        dialog.setCancelable(false);
        dialog.show();


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (filter.getCheckedRadioButtonId()==filter_priority.getId()){
                    shareclass.save(context,"filter_type","P");
                }else{
                    shareclass.save(context,"filter_type","");
                }
                getdata(LastReport);
                //adaptor_data.clear();
                dialog.dismiss();
            }
        });
    }

    public void Add_cpmplain(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Client_Complain_list.this);

        builder.setPositiveButton("Send", null)
                .setCancelable(false)
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_complain, null);

        final EditText textRemark = (EditText) dialogLayout.findViewById(R.id.remark);
        final TextView suport1 = (TextView) dialogLayout.findViewById(R.id.s1);
        final TextView suport2 = (TextView) dialogLayout.findViewById(R.id.s2);
        final TextView company = (TextView) dialogLayout.findViewById(R.id.company_name);

        final CheckBox add_attachment = (CheckBox) dialogLayout.findViewById(R.id.add_attachment);
        final RadioGroup attach_option = (RadioGroup) dialogLayout.findViewById(R.id.attach_option);
        attach_img= (ImageView) dialogLayout.findViewById(R.id.attach_img);
        attach_img.setVisibility(View.GONE);
        //final String[] ext = {path};
        attach_option.setVisibility(View.GONE);

        dialog.setView(dialogLayout);
        dialog.setTitle("Add Complain");
        company.setText(name);
        suport1.setText(s1);
        suport2.setText(s2);


        attach_option.setVisibility(View.GONE);

        add_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_attachment.isChecked()) {
                    attach_option.setVisibility(View.VISIBLE);
                } else {
                    attach_option.setVisibility(View.GONE);
                }
            }
        });

        if (mUri != null){
            add_attachment.setChecked(true);
            attach_option.setVisibility(View.GONE);
            add_attachment.setEnabled(false);
            filename = Login_user+"_"+new Date().getTime()+".jpg";
           /* Cursor c = getContentResolver().query(mUri,null,null,null,null);
            c.moveToNext();
            String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
            c.close();*/
            moveFile(getRealPathFromURI(context,mUri));
        }

        attach_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id=attach_option.getCheckedRadioButtonId();
                if (id == R.id.attach) {
                    if (ContextCompat.checkSelfPermission(Client_Complain_list.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //takePictureButton.setEnabled(false);
                        Toast.makeText(Client_Complain_list.this, "Please allow the permission", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(Client_Complain_list.this, new String[] { android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, Client_Complain_list.this.GALLERY_ACTIVITY_CODE);

                    }else {
                        open_galary();

                    }

                }else if (id == R.id.cam){

                    if (ContextCompat.checkSelfPermission(Client_Complain_list.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Client_Complain_list.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //takePictureButton.setEnabled(false);
                        Toast.makeText(Client_Complain_list.this, "Please allow the permission", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(Client_Complain_list.this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, Client_Complain_list.this.REQUEST_CAMERA);

                    }else {

                        capture_Image();
                    }

                }

            }

        });

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Remark = textRemark.getText().toString();
                if (add_attachment.isChecked() && !filename.equals("")) {
                    who_requested=1;
                    progress1.setMessage("Please Wait..\nuploading Image");
                    progress1.setCancelable(false);
                    progress1.show();
                    File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO_help" + File.separator + filename);
                    new up_down_ftp().uploadFile(file2,context);
                    dialog.dismiss();
                }else{
                    if (!Remark.trim().isEmpty()) {
                        complain_commit();
                        dialog.dismiss();
                    }else{
                        customVariablesAndMethod.msgBox(context,"Please enter some remark.....");
                    }
                }

            }
        });
    }

    private void complain_commit(){

            //Start of call to service

            HashMap<String, String> request = new HashMap<>();
            request.put("sDbName", shareclass.getValue(Client_Complain_list.this, "company_code", "demo"));
            request.put("iPaId", PA_ID);
            request.put("sRemark", Remark);
            request.put("iAUSER_ID", shareclass.getValue(this, "PA_ID", "0"));
            request.put("sAttachment", filename);

            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);

            progress1.setMessage("Please Wait..");
            progress1.setCancelable(false);
            progress1.show();

            new CboServices_Old(Client_Complain_list.this, mHandler).customMethodForAllServices(request, "ComplaintCommit", MESSAGE_INTERNET_COMPLAIN_COMIT, tables);

            //End of call to service
            update_list = false;


    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_main:
                    update_list=true;
                    if ((null != msg.getData())) {
                        parser2(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET:
                    if ((null != msg.getData())) {

                        parser1(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_COMPLAIN_COMIT:
                    update_list=true;
                    if ((null != msg.getData())) {

                        parser3(msg.getData());

                    }
                    break;
                case 99:
                    update_list=true;
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();

                    }
                    break;
                default:
                    update_list=true;
                    progress1.dismiss();

            }
        }
    };
    public void parser1(Bundle result) {
        if (result!=null ) {


            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    id.add(jsonObject2.getString("ITEM_ID"));
                    particulars.add(jsonObject2.getString("ITEM_NAME"));
                    rate.add(jsonObject2.getString("RATE"));
                    qty.add(jsonObject2.getString("QTY"));
                    amt_alert.add(jsonObject2.getString("AMOUNT"));

                }

                progress1.dismiss();
                show_Order_alert(0,0);
                new_order_adaptor_alert.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices_Old.getAlert(this,"Missing field error",e.toString());
                List toEmailList = Arrays.asList("support@cboinfotech.com".split("\\s*,\\s*"));
                new SendMailTask(this).execute("support@cboinfotech.com",
                        "Cbo12345",toEmailList , "Missing field error", e.toString());
            }

        }
        Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }
    public void parser2(Bundle result) {
        if (result!=null ) {

            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                int update_index=0;
                /*if (jsonArray1.length() > 0) {
                    dbHelper.deleteComplaint();
                }*/
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);

                    dbHelper.insertComplaint(jsonObject2.getString("COMPLAINT_TYPE"),jsonObject2.getString("DOC_NO"),jsonObject2.getString("PA_ID"),jsonObject2.getString("PA_NAME"),
                            jsonObject2.getString("FROM_USER"),jsonObject2.getString("TO_USER"),jsonObject2.getString("DONE_BY"),
                            jsonObject2.getString("STATUS"),jsonObject2.getString("COMMENT"),jsonObject2.getString("REMARK"),
                            jsonObject2.getString("TIME"),jsonObject2.getString("DOC_DATE"),jsonObject2.getString("USER1"),jsonObject2.getString("USER_ID1"),jsonObject2.getString("USER2"),jsonObject2.getString("USER_ID2"),
                            jsonObject2.getString("P_DAYS"), jsonObject2.getString("MOBILE"),jsonObject2.getString("ATTACHMENT1"),jsonObject2.getString("PRIORITY"));


                    /*HashMap<String,String> data=new HashMap<>();
                    data.put("COMPLAINT_TYPE",jsonObject2.getString("COMPLAINT_TYPE"));
                    data.put("DOC_NO",jsonObject2.getString("DOC_NO"));
                    //data.put("PA_ID",jsonObject2.getString("PA_ID"));
                    data.put("PA_NAME",jsonObject2.getString("PA_NAME"));
                    data.put("FROM_USER",jsonObject2.getString("FROM_USER"));
                    data.put("TO_USER",jsonObject2.getString("TO_USER"));
                    data.put("DONE_BY",jsonObject2.getString("DONE_BY"));
                    data.put("STATUS",jsonObject2.getString("STATUS"));
                    data.put("COMMENT",jsonObject2.getString("COMMENT"));
                    data.put("REMARK",jsonObject2.getString("REMARK"));
                    data.put("TIME",jsonObject2.getString("TIME"));
                    data.put("DOC_DATE",jsonObject2.getString("DOC_DATE"));
                    data.put("USER1",jsonObject2.getString("USER1"));
                    data.put("USER2",jsonObject2.getString("USER2"));
                    data.put("P_DAYS",jsonObject2.getString("P_DAYS"));
                    data.put("MOBILE",jsonObject2.getString("MOBILE"));
                    data.put("ATTACHMENT",jsonObject2.getString("ATTACHMENT1"));
                    data.put("PRIORITY",jsonObject2.getString("PRIORITY"));
                    data.put("IS_READ","0");
                    adaptor_data.add(data);*/

                    if (updated_complain.equals(jsonObject2.getString("DOC_NO"))){
                        update_index=i;
                    }

                }

                adaptor_data=dbHelper.getComplaint(LastReport,"",PA_ID);
                if (!updated_complain.equals("") && update_index!=0){
                    //listView.smoothScrollToPosition(update_index);
                    //listView.smoothScrollToPositionFromTop(update_index,0,500);
                    HashMap<String, String> data = adaptor_data.remove(update_index);
                    adaptor_data.add(0,data);

                }
                order_list_adaptor=new ComplainList_Adapter(context,adaptor_data,who,updated_complain);
                listView.setAdapter(order_list_adaptor);
                swipeRefreshLayout.setRefreshing(false);
                progress1.dismiss();
                if (shareclass.getValue(this,"filter_type","").equals("P")){
                    order_list_adaptor.filter("",1,100000);
                }
                //order_list_adaptor.notifyDataSetChanged();
            } catch (JSONException e) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices_Old.getAlert(this,"Missing field error",e.toString());
                List toEmailList = Arrays.asList("support@cboinfotech.com".split("\\s*,\\s*"));
                new SendMailTask(this).execute("support@cboinfotech.com",
                        "Cbo12345",toEmailList , "Missing field error", e.toString());
            }



        }
        Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();



    }


    public void parser3(Bundle result) {
        if (result!=null ) {


            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    /*id.add(jsonObject2.getString("ITEM_ID"));
                    particulars.add(jsonObject2.getString("ITEM_NAME"));
                    rate.add(jsonObject2.getString("RATE"));
                    qty.add(jsonObject2.getString("QTY"));
                    amt_alert.add(jsonObject2.getString("AMOUNT"));*/
                    updated_complain=jsonObject2.getString("DOC_NO");

                }

               // Remark="";
                filename="";
                //updated_complain="";
                //Status="";
                progress1.dismiss();

                getdata(LastReport);
                // adaptor_data=dbHelper.getOrder("P",PA_ID);
                // order_list_adaptor=new Order_List_Adaptor(context,adaptor_data);
                // listView.setAdapter(order_list_adaptor);
                //LastReport="P";
                //adaptor_data.clear();

                if (LastReport.equals("P")) {
                    pending.setBackgroundResource(R.drawable.tab_selected);
                    complete.setBackgroundResource(R.drawable.tab_deselected);
                    all.setBackgroundResource(R.drawable.tab_deselected);
                }else if (LastReport.equals("C")) {
                    complete.setBackgroundResource(R.drawable.tab_selected);
                    all.setBackgroundResource(R.drawable.tab_deselected);
                    pending.setBackgroundResource(R.drawable.tab_deselected);
                }else{
                    all.setBackgroundResource(R.drawable.tab_selected);
                    complete.setBackgroundResource(R.drawable.tab_deselected);
                    pending.setBackgroundResource(R.drawable.tab_deselected);
                }


            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices_Old.getAlert(this,"Missing field error",e.toString());
                List toEmailList = Arrays.asList("support@cboinfotech.com".split("\\s*,\\s*"));
                new SendMailTask(this).execute("support@cboinfotech.com",
                        "Cbo12345",toEmailList , "Missing field error", e.toString());
            }

        }
        Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }

    private void show_Order_alert(final int position, final int who){

        AlertDialog.Builder builder = new AlertDialog.Builder(Client_Complain_list.this);

        if (who==1 && adaptor_data.get(position).get("STATUS").toLowerCase().equals("pending")){
            builder.setPositiveButton("OK", null)
                    .setNegativeButton("EDIT", null)
                    .setCancelable(false);
        }else if (who==1) {
            builder.setPositiveButton("OK", null)
                    .setCancelable(false);
        }else{
            builder.setPositiveButton("CANCEL", null)
                    .setNegativeButton("DONE", null)
                    .setCancelable(false);
        }
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.add_complain, null);

        final EditText textRemark = (EditText) dialogLayout.findViewById(R.id.remark);
        final TextView company = (TextView) dialogLayout.findViewById(R.id.company_name);
        final TextView complain_type = (TextView) dialogLayout.findViewById(R.id.complain_type);
        final TextView suport1 = (TextView) dialogLayout.findViewById(R.id.s1);
        final TextView suport2 = (TextView) dialogLayout.findViewById(R.id.s2);
        final TextView status = (TextView) dialogLayout.findViewById(R.id.status);
        final EditText comment = (EditText) dialogLayout.findViewById(R.id.comment);
        final TextView date = (TextView) dialogLayout.findViewById(R.id.date);
        final Spinner spinner1= (Spinner) dialogLayout.findViewById(R.id.status_type);
        final String[] status_type = {adaptor_data.get(position).get("STATUS")};
        ImageView attach_img_show = (ImageView) dialogLayout.findViewById(R.id.attach_img_show);

        final CheckBox add_attachment = (CheckBox) dialogLayout.findViewById(R.id.add_attachment);
        final RadioGroup attach_option = (RadioGroup) dialogLayout.findViewById(R.id.attach_option);
        attach_img= (ImageView) dialogLayout.findViewById(R.id.attach_img);
        attach_img.setVisibility(View.GONE);
        //final String[] ext = {path};
        attach_option.setVisibility(View.GONE);

        ImageView status_img= (ImageView) dialogLayout.findViewById(R.id.status_img);
        LinearLayout status_layout= (LinearLayout) dialogLayout.findViewById(R.id.status_layout);
        LinearLayout comment_layout= (LinearLayout) dialogLayout.findViewById(R.id.comment_layout);

        dialog.setView(dialogLayout);
        dialog.setTitle("Complain No. : "+adaptor_data.get(position).get("DOC_NO"));

        suport1.setText(adaptor_data.get(position).get("USER1"));
        suport2.setText (adaptor_data.get(position).get("USER2"));
        company.setText(adaptor_data.get(position).get("PA_NAME"));
        complain_type.setVisibility(View.VISIBLE);
        complain_type.setText(adaptor_data.get(position).get("COMPLAINT_TYPE"));

        textRemark.setText(adaptor_data.get(position).get("REMARK"));
        status_layout.setVisibility(View.VISIBLE);
        comment_layout.setVisibility(View.VISIBLE);
        status.setText(adaptor_data.get(position).get("STATUS"));
        comment.setText(adaptor_data.get(position).get("COMMENT"));

        if (who != 1 ) {
            comment.setEnabled(true);
            spinner1.setVisibility(View.VISIBLE);
            status.setVisibility(View.GONE);
            comment.setSelectAllOnFocus(true);
        }else{
            comment.setEnabled(false);
            spinner1.setVisibility(View.GONE);
            status.setVisibility(View.VISIBLE);
        }

        date.setVisibility(View.VISIBLE);
        date.setText(adaptor_data.get(position).get("DOC_DATE")+"-"+adaptor_data.get(position).get("TIME")+" ("+adaptor_data.get(position).get("P_DAYS")+")");
        textRemark.setEnabled(false);

        int[] color={Color.argb(255,34,139,34)};
        final Drawable drawable = status_img.getBackground();
        if (adaptor_data.get(position).get("STATUS").equals("COMPLETE")){
            color[0] = Color.argb(255,34,139,34);
            drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
        }else{
            color[0] = Color.argb(255,255,69,0);
            drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
        }

        if (!adaptor_data.get(position).get("ATTACHMENT").equals("")){
            add_attachment.setText("Change Attachment");
            attach_img_show.setVisibility(View.VISIBLE);
        }
        attach_img_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aT1=adaptor_data.get(position).get("ATTACHMENT");
                if(!aT1.contains("http://")){
                    aT1 ="http://"+ aT1;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(aT1));
                context.startActivity(browserIntent);
            }
        });

        final List<String> list = new ArrayList<String>();
        list.add("PENDING");
        list.add("UNDER PROCESS");
        list.add("COMPLETE");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);

        attach_option.setVisibility(View.GONE);

        add_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_attachment.isChecked()) {
                    attach_option.setVisibility(View.VISIBLE);
                } else {
                    attach_option.setVisibility(View.GONE);
                }
            }
        });

        attach_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id=attach_option.getCheckedRadioButtonId();
                if (id == R.id.attach) {
                    if (ContextCompat.checkSelfPermission(Client_Complain_list.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //takePictureButton.setEnabled(false);
                        Toast.makeText(Client_Complain_list.this, "Please allow the permission", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(Client_Complain_list.this, new String[] { android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, Client_Complain_list.this.GALLERY_ACTIVITY_CODE);

                    }else {
                        open_galary();

                    }

                }else if (id == R.id.cam){

                    if (ContextCompat.checkSelfPermission(Client_Complain_list.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Client_Complain_list.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //takePictureButton.setEnabled(false);
                        Toast.makeText(Client_Complain_list.this, "Please allow the permission", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(Client_Complain_list.this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, Client_Complain_list.this.REQUEST_CAMERA);

                    }else {

                        capture_Image();
                    }

                }

            }

        });

        dialog.show();

        if (who != 1 && adaptor_data.get(position).get("STATUS").toLowerCase().equals("complete")) {
            spinner1.setSelection(2);
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (who==1 && adaptor_data.get(position).get("STATUS").toLowerCase().equals("pending")){
                    show_Order_alert(position,0);
                }else  if (who !=1 ){
                    updated_complain=adaptor_data.get(position).get("DOC_NO");
                    Remark=comment.getText().toString();
                    Status=(String) spinner1.getSelectedItem();
                    if (add_attachment.isChecked() && !filename.equals("")) {
                        who_requested=2;
                        progress1.setMessage("Please Wait..\nuploading Image");
                        progress1.setCancelable(false);
                        progress1.show();
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO_help" + File.separator + filename);
                        new up_down_ftp().uploadFile(file2,context);

                    }else{
                        String path=adaptor_data.get(position).get("ATTACHMENT");
                        filename="";
                        if (!path.equals("")) {
                            filename =path.substring(path.lastIndexOf("/")+1 );
                            filename=web_root_path.substring(web_root_path.indexOf("/",1))+filename;
                        }
                       Update_Complaint();
                    }
                }
                dialog.dismiss();

            }
        });


    }

    private void Update_Complaint(){
        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(Client_Complain_list.this, "company_code", "demo"));
        request.put("iDocNo",updated_complain);
        request.put("sComment",Remark );
        request.put("sStatus",Status );
        request.put("iPA_ID", shareclass.getValue(this,"PA_ID","0"));
        request.put("sAttachment", filename);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices_Old(Client_Complain_list.this, mHandler).customMethodForAllServices(request, "Update_Complaint", MESSAGE_INTERNET_COMPLAIN_COMIT, tables);

        //End of call to service
        update_list=false;
    }

    private void open_galary(){
        Intent gallery_Intent = new Intent(Client_Complain_list.this, GalleryUtil.class);
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    private void capture_Image(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File(Environment.getExternalStorageDirectory(), "CBO_help");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                //return true;
            }
        }
        filename = Login_user+"_"+new Date().getTime()+".jpg";
        output = new File(dir, filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                filename = Login_user+"_"+new Date().getTime()+".jpg";
                Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
                /*performCrop(picturePath);*/
                moveFile(picturePath);
            }else{
                filename="";
            }
        }else if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert selectedBitmap != null;
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File dir = new File(Environment.getExternalStorageDirectory(), "CBO_help");
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                        //return true;
                    } else {
                        try {
                            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO_help" + File.separator + filename);
                            file2.createNewFile();
                            FileOutputStream fo = new FileOutputStream(file2);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                            attach_img.setImageBitmap(myBitmap);

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }else {
                    try {
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO_help" + File.separator + filename);

                        file2.createNewFile();
                        FileOutputStream fo = new FileOutputStream(file2);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                        attach_img.setImageBitmap(myBitmap);

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }else{
                filename="";
            }
        }else if (requestCode == REQUEST_CAMERA ) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO_help"+File.separator+ filename);

                if (file1.exists()){
                    /*performCrop(file1.getPath());*/
                    previewCapturedImage(file1.getPath());
                }

            } else if (resultCode == RESULT_CANCELED) {
                filename="";
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "image capture cancelled ", Toast.LENGTH_SHORT)
                        .show();
            } else {
                filename="";
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                capture_Image();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == this.GALLERY_ACTIVITY_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                open_galary();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //startService(new Intent(Client_Complain_list.this, FloatingViewService.class));
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }
    private void moveFile( String inputFileUri) {

        InputStream in = null;
        OutputStream out = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO_help");
            //create output directory if it doesn't exist
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputFileUri);
            out = new FileOutputStream(dir+File.separator + filename);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            previewCapturedImage(dir+File.separator + filename);

        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    private void previewCapturedImage(String picUri) {
        try {
            // hide video preview

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(picUri,
                    options);
            attach_img.setVisibility(View.VISIBLE);
            attach_img.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

   /* private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image*//*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }*/


    // Convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Context context, Uri contentUri) {
      /*  Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }*/

        return ImageFilePath.getPath(context, contentUri);

    }


    @Override
    public void upload_complete(final String IsCompleted) {
        progress1.dismiss();
        if (IsCompleted.equals("S")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //new UploadPhotoInBackGround().execute();
                }
            });
        }else if (IsCompleted.equals("Y")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    filename=web_root_path.substring(web_root_path.indexOf("/",1))+filename;
                    if (who_requested==1) {
                        complain_commit();
                    }else if (who_requested==2){
                        Update_Complaint();
                    }
                }
            });
        }else if (IsCompleted.contains("ERROR")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //new UploadPhotoInBackGround().execute();
                    String folder=IsCompleted.substring(6);
                    customVariablesAndMethod.getAlert(context,"Folder not found",folder+"   Invalid path \nPlease contact your administrator");
                }
            });
        }else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    customVariablesAndMethod.msgBox(context,"UPLOAD FAILED \n Please try again");
                }
            });
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (searchView.isShown()) {
            searchView.setQuery(searchView.getQuery(), false);
            searchMenuItem.collapseActionView();
            searchView.clearFocus();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        int from= Integer.parseInt(shareclass.getValue(context,"priority_from","0"));
        int to= Integer.parseInt(shareclass.getValue(context,"priority_to","0"));
        order_list_adaptor.filter(newText,from,to);

        return true;

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getdata(LastReport);
        //adaptor_data.clear();
    }
}

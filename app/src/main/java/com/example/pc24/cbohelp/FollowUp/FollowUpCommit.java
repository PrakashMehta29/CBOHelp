package com.example.pc24.cbohelp.FollowUp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc24.cbohelp.PartyView.SwipeController;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.services.CboServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public  class FollowUpCommit  extends AppCompatActivity {
    private static final int FOLLOWUPGRID = 1;
    public ProgressDialog progress1;
    ArrayList<mFollowupgrid> followupdata = new ArrayList<>();
    mFollowupgrid mFollow;

    Shareclass shareclass;
    DBHelper dbHelper;
    Menu  menu;
    EditText paname,mobile,email,person,city,website,EmplyeeNo,Referby,adress1,adress2,adress3,adress4;
    Button submit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_followcommit_grid_layout);
        paname=(EditText) findViewById(R.id.party_name);
        mobile=(EditText) findViewById(R.id.party_mobile_no);

        person=(EditText) findViewById(R.id.party_person);
        website=(EditText) findViewById(R.id.remark);
        EmplyeeNo=(EditText) findViewById(R.id.party_empolyye_no);
        Referby=(EditText) findViewById(R.id.party_Ref);
        adress1=(EditText) findViewById(R.id.party_Adrress1);
        adress2=(EditText)findViewById(R.id.party_Adress_2);
        adress3=(EditText)findViewById(R.id.party_Adress_3);
        adress4=(EditText) findViewById(R.id.party_Adress_4);
        submit=(Button)findViewById(R.id.show) ;
                progress1 = new ProgressDialog(this);
                shareclass=new Shareclass();
        dbHelper=new DBHelper(this);
      //  GetFollowCommit();

    }

    private void GetFollowCommit() {
        progress1 = new ProgressDialog(this);
        shareclass = new Shareclass();
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        dbHelper = new DBHelper(this);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();

//Extract the data…
        String venName = bundle.getString("VENUE_NAME");
        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(this, "company_code", "demo"));
        request.put("iId",  bundle.getString("iId"));
        request.put("iSrno",  bundle.getString("iSrno"));
        request.put("iPaId",  bundle.getString("iPaId"));
        request.put("sFollowUpdate",  bundle.getString("sFollowUpdate"));
        request.put("sRemark",  bundle.getString("sRemark"));
        request.put("sNextFollowUpdate",  bundle.getString("sNextFollowUpdate"));
        request.put("sContactPerson",  bundle.getString("sContactPerson"));
        request.put("sContactNo",  bundle.getString("sContactNo"));
        request.put("sFormType", "ORDER_STATUS_FOLLOWUP");
        request.put("iUserId",  bundle.getString("iUserId"));
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);



        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();
        new CboServices(this, mHandler).customMethodForAllServices(request, "FollowCommit", FOLLOWUPGRID, tables);


    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FOLLOWUPGRID:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        parser2(msg.getData());
                        //  parser1(msg.getData());

                    }
                    break;
                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        Toast.makeText(getApplicationContext(), msg.getData().getString("Error"), Toast.LENGTH_SHORT).show();

                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    private void parser2(Bundle result) {
        {
            if ((result == null)) {
                progress1.dismiss();

            } else {
                try {


                    String table0 = result.getString("Tables0");
                    JSONArray jsonArray1 = new JSONArray(table0);
                    followupdata.clear();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject c = jsonArray1.getJSONObject(i);
                        mFollow = new mFollowupgrid();
                        String msg = c.getString("MSG");
                        mFollow.setpANAME(msg);
                        String url = c.getString("URL");
                        mFollow.setsRNO(url);

                        followupdata.add(mFollow);
                       //  getSupportActionBar().setTitle(mFollow.getpANAME());

                    }

                    progress1.dismiss();
                } catch (Exception e) {
                    progress1.dismiss();
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.party_folowupcommit, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


            finish();

        return super.onOptionsItemSelected(item);
    }

}
package com.example.pc24.cbohelp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pc24.cbohelp.adaptor.Client_order_Adaptor;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.services.CboServices_Old;
import com.example.pc24.cbohelp.utils.SendMailTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Client_grid extends AppCompatActivity implements SearchView.OnQueryTextListener{
    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1;
    private  static final int MESSAGE_INTERNET_order_commit=2;
    ListView listView;
    Client_order_Adaptor new_order_adaptor;
    ArrayList< HashMap<String,String>> adaptor_data;
    ArrayList<String> name,amt,id,hq;
    Shareclass shareclass;
    String what_to_do;
    DBHelper dbHelper;
    SearchView searchView;
    MenuItem searchMenuItem;
    HashMap<String, ArrayList<String>> team_list;
    String Login_user;
    Uri mUri=null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_grid);
        listView= (ListView) findViewById(R.id.client_account_list);

        progress1 = new ProgressDialog(this);
        adaptor_data=new ArrayList<>();

        shareclass=new Shareclass();
        dbHelper=new DBHelper(this);
        context=this;

        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent=getIntent();
        what_to_do=intent.getStringExtra("what_to_do");
        getSupportActionBar().setTitle(intent.getStringExtra("title"));

      //  team_list=dbHelper.getTeam("");


        if (!shareclass.getValue(context,"CATEGORY","").equalsIgnoreCase("ADMIN")) {
            team_list = dbHelper.getManager ("");
        }else{
            team_list = dbHelper.getTeam ("");
        }

        Login_user = shareclass.getValue(this,"PA_ID","0");
        getdata(true);


        Intent mIntent = getIntent();
        String action = mIntent.getAction();
        String type = mIntent.getType();

        if(action != null && action.equals(Intent.ACTION_SEND) && type != null){
            if (shareclass.getValue(Client_grid.this,"PA_ID","0").equals("0")){

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
                final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
                final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
                final TableLayout Alert_message_list= (TableLayout) dialogLayout.findViewById(R.id.table_view);
                final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
                Alert_title.setText("LOGIN !!!");
                Alert_message.setText("Please Login and then continue..");
                Alert_message_list.setVisibility(View.GONE);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                final AlertDialog dialog = builder1.create();
                dialog.setView(dialogLayout);
                Alert_Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Client_grid.this,Login.class));
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();

            }else {
                if (type.startsWith("image/")) {
                    mUri = mIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                    //Toast.makeText(this,"from Image Share",Toast.LENGTH_SHORT).show();
                }
            }
        }




        new_order_adaptor=new Client_order_Adaptor(this,adaptor_data,"G");
        listView.setAdapter(new_order_adaptor);
        //listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(Client_grid.this, Client_Complain_list.class);
                    intent.putExtra("pa_id", adaptor_data.get(position).get("PA_ID"));
                    intent.putExtra("name",adaptor_data.get(position).get("PA_NAME"));
                    intent.putExtra("s1", adaptor_data.get(position).get("USER_NAME"));
                    intent.putExtra("s2",adaptor_data.get(position).get("USER_NAME2"));
                    intent.putExtra("who","add");
                    intent.putExtra("show_report","P");
                    intent.putExtra("DOC_NO","");
                    intent.putExtra("URI",mUri);
                    startActivity(intent);

            }
        });

    }


    private void getdata(Boolean sync){

        if (Login_user.equals(shareclass.getValue(context,"PA_ID","0"))) {
            adaptor_data = dbHelper.getParty("","");
        }else {
            adaptor_data = dbHelper.getParty("",Login_user);
        }

        new_order_adaptor=new Client_order_Adaptor(this,adaptor_data,"G");
        listView.setAdapter(new_order_adaptor);
        new_order_adaptor.notifyDataSetChanged();
        if (sync) {

            //Start of call to service

            HashMap<String, String> request = new HashMap<>();
            request.put("sDbName", shareclass.getValue(this, "company_code", "demo"));
            request.put("iPA_ID", Login_user);

            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);

            progress1.setMessage("Please Wait..");
            progress1.setCancelable(false);
            if (adaptor_data.size() == 0)
                progress1.show();

            new CboServices_Old(this, mHandler).customMethodForAllServices(request, "PARTYGRID", MESSAGE_INTERNET, tables);

            //End of call to service
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_grid, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        if (team_list.get("PA_ID").size()>0){
            menu.add(0, -1, Menu.NONE, "ALL");
        }

        for (int i=0;i<team_list.get("PA_NAME").size();i++){
            menu.add(0, i, Menu.NONE, team_list.get("PA_NAME").get(i));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            default:
                if (id==-1){
                    Login_user = shareclass.getValue(this,"PA_ID","0");
                    getSupportActionBar().setTitle("ALL");
                }else{
                    Login_user = team_list.get("PA_ID").get(id);
                    getSupportActionBar().setTitle(team_list.get("PA_NAME").get(id));
                }
                getdata(false);
                //finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    if ((null != msg.getData())) {

                        parser1(msg.getData());

                    }
                    progress1.dismiss();
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
            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                if (jsonArray1.length() > 0) {
                    dbHelper.deleteParty();
                }
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    dbHelper.insertParty(jsonObject2.getString("PA_ID"),jsonObject2.getString("PA_NAME"),jsonObject2.getString("USER_NAME"),jsonObject2.getString("ID"),jsonObject2.getString("USER_NAME2"),jsonObject2.getString("ID2"));

                   /*
                    HashMap<String,String> data=new HashMap<>();
                    data.put("PA_ID",jsonObject2.getString("PA_ID"));
                    data.put("PA_NAME",jsonObject2.getString("PA_NAME"));
                    data.put("USER_NAME",jsonObject2.getString("USER_NAME"));
                    data.put("ID",jsonObject2.getString("ID"));
                    data.put("USER_NAME2",jsonObject2.getString("USER_NAME2"));
                    data.put("ID2",jsonObject2.getString("ID2"));
                    adaptor_data.add(data);*/
                }

                progress1.dismiss();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices_Old.getAlert(this,"Missing field error",e.toString());
                List toEmailList = Arrays.asList("support@cboinfotech.com".split("\\s*,\\s*"));
                new SendMailTask(this).execute("support@cboinfotech.com",
                        "Cbo12345",toEmailList , "Missing field error", e.toString());
                //e.printStackTrace();
            }

        }
        Log.d("MYAPP", "objects are1: " + result);

        progress1.dismiss();

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
        //adaptor_data= dbHelper.getPartyFilter(newText);
        //new_order_adaptor=new Client_order_Adaptor(this,adaptor_data,"G");
        //listView.setAdapter(new_order_adaptor);
        // Toast.makeText(getApplicationContext(),"javed "+newText,Toast.LENGTH_SHORT).show();
        new_order_adaptor.filter(newText);
        return true;

    }

}
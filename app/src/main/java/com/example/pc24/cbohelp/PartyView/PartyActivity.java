package com.example.pc24.cbohelp.PartyView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pc24.cbohelp.AddParty.PartyDetail;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.services.CboServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PartyActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final int CLIENTMAINGRID = 1;
    public ProgressDialog progress1;
    ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
    ArrayList<mParty> parytdata = new ArrayList<mParty>();
    RecyclerView recyclerView;
    Partyviewapdater mAdapter;
    mParty rptModel;
    Context context;
    private Menu menu;
    SwipeController swipeController = null;
    Shareclass shareclass;
    DBHelper dbHelper;

    mParty mParty;
    String PAid="";
    Status status=Status.All;




    enum Status {


        All(0),
        Done(1),
        inProcess(2),
        NotInterested(3);


        private int value ;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(" Select Client");
        GetclientDetail();


    }

    private void GetclientDetail( ) {

        progress1 = new ProgressDialog(this);
        shareclass = new Shareclass();
        dbHelper = new DBHelper(this);




        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(this, "company_code", "demo"));
        request.put("iUserId", "140");
        request.put("iStatus", ""+status.getValue());

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(-1);


        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();
        new CboServices(this, mHandler).customMethodForAllServices(request, "ClientMainGrid", CLIENTMAINGRID, tables);

        setupRecyclerView();
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLIENTMAINGRID:

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
                    parytdata.clear();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject c = jsonArray1.getJSONObject(i);
                        parytdata.add(   rptModel = new mParty(c.getString("PA_ID"),c.getString("PA_NAME"),c.getString("MOBILE"),c.getString("PERSON"),c.getString("STATUS")));
                    }
                    mAdapter = new Partyviewapdater(context, parytdata);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);


                    progress1.dismiss();
                } catch (Exception e) {
                    progress1.dismiss();
                    e.printStackTrace();
                }
            }
        }

    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

       /* swipeController = new SwipeController(this, new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
//                mAdapter.partydata.remove(position);
//                mAdapter.notifyItemRemoved(position);
//                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            }
        });*/
        swipeController=new SwipeController(this, new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
            }

            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
            }
        });


    /*    ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);*/

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
           @Override
           public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
              // swipeController.onDraw(c);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.party_menu, menu);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);


        ///hideOption(R.id.action_info);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.all:
                status=Status.All;
                GetclientDetail();
                break;
            case R.id.done:
                status=Status.Done;
                GetclientDetail();
                break;
            case R.id.inProcess:
                status=Status.inProcess;
                GetclientDetail();
                break;
            case R.id.notInterested:
                status=Status.NotInterested;
                GetclientDetail();

                break;
            case R.id.add_party:
                Intent i = new Intent(PartyActivity.this, PartyDetail.class);
                startActivity(i);
                break;


            default:
                finish();
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            if (mAdapter != null)
                mAdapter.filter(query);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mAdapter != null)
            mAdapter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
          if (mAdapter != null)
            mAdapter.filter(newText);
        return false;
    }

}


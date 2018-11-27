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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc24.cbohelp.AddParty.PartyDetail;
import com.example.pc24.cbohelp.Followingup.CustomDatePicker;
import com.example.pc24.cbohelp.Followingup.NewPartyActivity;
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
import java.util.ArrayList;
import java.util.Date;
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
    ImageView img_followdate, img_nextfollowdate, Img_party;
    RadioGroup radioGroup;
    RadioButton entrydate, followingdate;
    Button fromdatebtn, todatebtn, Gobtn;
    LinearLayout  Lmissedtype;
    TextView PartyTxt;
     String ViewBy="";
     TextView TotalParty;

    String FDate="",Todate="";


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

        fromdatebtn = (Button) findViewById(R.id.fromdatebtn);
        todatebtn = (Button) findViewById(R.id.todatebtn);
        img_followdate = (ImageView) findViewById(R.id.spinner_img_followdate);
        img_nextfollowdate = (ImageView) findViewById(R.id.spinner_img_nextfollowdate);
        PartyTxt = (TextView) findViewById(R.id.party_name);
        Lmissedtype = (LinearLayout) findViewById(R.id.layout_party);
         TotalParty=(TextView) findViewById(R.id.Tpartycount);
        Gobtn = (Button) findViewById(R.id.btn_go);

        radioGroup = (RadioGroup) findViewById(R.id.viewby);
        entrydate = (RadioButton) findViewById(R.id.entrydate);
        followingdate = (RadioButton) findViewById(R.id.followingdate);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(" Select Client");
        context = this;
        shareclass = new Shareclass();






        fromdatebtn.setText(CustomDatePicker.currentDate( CustomDatePicker.ShowFormat));
        FDate=CustomDatePicker.currentDate(CustomDatePicker.CommitFormat);
        todatebtn.setText(CustomDatePicker.currentDate( CustomDatePicker.ShowFormat));
        Todate=CustomDatePicker.currentDate(CustomDatePicker.CommitFormat);

        Gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetAllParty();
                /*if (Validatepaname()) {

                }
                if (!PartyTxt.getText().toString().equals("")) {
                    GetAllParty();
                } else {
                    Toast.makeText(context, "Please Select Party Name First", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        fromdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context, null,
                            CustomDatePicker.getDate(todatebtn.getText().toString(), CustomDatePicker.ShowFormat)
                    ).Show(CustomDatePicker.getDate(fromdatebtn.getText().toString(),  CustomDatePicker.ShowFormat)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {
                                    fromdatebtn.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
                                    FDate=(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                    //vm_following.setFromDate(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                }
                            });

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        todatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context,
                            CustomDatePicker.getDate(fromdatebtn.getText().toString(),  CustomDatePicker.ShowFormat))
                            .Show(CustomDatePicker.getDate(todatebtn.getText().toString(),  CustomDatePicker.ShowFormat)
                                    , new CustomDatePicker.ICustomDatePicker() {
                                        @Override
                                        public void onDateSet(Date date) {
                                            todatebtn.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormat));
                                             Todate=CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat);
                                           // vm_following.setToDate(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
                                        }
                                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        img_followdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromdatebtn.performClick();

            }
        });
        img_nextfollowdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todatebtn.performClick();
            }
        });


        Lmissedtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent1 = new Intent(PartyActivity.this, PartyActivity.class);
                startActivityForResult(intent1, 0);
            }
        });

        ViewBy="F";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.entrydate) {
                    ViewBy="N";
                } else {
                    ViewBy="F";
                }
            }
        });

        /*if (PartyTxt.getText().toString().equals("")) {
            hideOption(R.id.add_remark);
        }
        */
        setupRecyclerView();
     //   GetclientDetail();



    }

    @Override
    protected void onResume() {
        super.onResume();
        GetAllParty();

    }

    private void GetAllParty( ) {

        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));
        request.put("iUserId", "140");
        request.put("iStatus", ""+status.getValue());
        request.put("sFDATE",FDate );
        request.put("sTDATE", Todate);
        request.put("sDOC_TYPE",ViewBy);


        new MyAPIService(context)
                .execute(new ResponseBuilder("ClientMainGrid",request)
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) {
                                //parser2(message);
                                mAdapter = new Partyviewapdater(context, parytdata);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);
                                TotalParty.setText(""+mAdapter.getItemCount());
                            }

                            @Override
                            public void onResponse(Bundle response) {
                                parser2(response);
                            }


                        })
                );



    }



    private void parser2(Bundle result) {
        {

                try {


                    String table0 = result.getString("Tables0");
                    JSONArray jsonArray1 = new JSONArray(table0);
                    parytdata.clear();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject c = jsonArray1.getJSONObject(i);
                        parytdata.add(   rptModel = new mParty(c.getString("PA_ID"),c.getString("PA_NAME"),c.getString("MOBILE"),c.getString("PERSON"),c.getString("STATUS")));
                    }



                } catch (Exception e) {
                    e.printStackTrace();
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
                GetAllParty();
                break;
            case R.id.done:
                status=Status.Done;
                GetAllParty();
                break;
            case R.id.inProcess:
                status=Status.inProcess;
                GetAllParty();
                break;
            case R.id.notInterested:
                status=Status.NotInterested;
                GetAllParty();

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

    public boolean Validatepaname() {
        String username = PartyTxt.getText().toString().trim();

        if (username.isEmpty()) {
            PartyTxt.setError("Please enter your name");
            return false;
        } else {
            PartyTxt.setError(null);
            return true;
        }
    }
}


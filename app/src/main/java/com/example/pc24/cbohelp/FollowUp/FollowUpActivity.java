package com.example.pc24.cbohelp.FollowUp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc24.cbohelp.PartyView.mParty;
import com.example.pc24.cbohelp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowUpActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,IFollowup  {
    private static final int FOLLOWUPGRID = 1;
    public ProgressDialog progress1;
    ArrayList<mFollowupgrid> followupdata = new ArrayList<>();
    RecyclerView recyclerView;
    FolllowUpAdapter mAdapter;
    Context context;
    TextView Conperson,Refrby,Username,User1name,textView;
    private  static final int FOLLOWUP_DIALOG=7;
    private Menu menu;
    mFollowupgrid mFollow;
    AppBarLayout appBarLayout;
    VM_Followup vm_followup=null;
    IFollowup iFollowup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_followup_grid_layout);
        Conperson=(TextView)findViewById(R.id.Conperson);
        Refrby=(TextView)findViewById(R.id.refrby);
        Username=(TextView)findViewById(R.id.username);
        User1name=(TextView)findViewById(R.id.username) ;

        textView=(TextView)findViewById(R.id.hadder_text_1);
        recyclerView = (RecyclerView) findViewById(R.id.followuplist);
        Intent intent = getIntent();
      //  getSupportActionBar().hide();

        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
          //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }
       /* if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            setSupportActionBar(toolbar);
        }*/




        vm_followup= ViewModelProviders.of(FollowUpActivity.this).get(VM_Followup.class);
        vm_followup.setListener(this);
        vm_followup.setParty(new mParty(intent.getExtras().getString("Paid"),intent.getExtras().getString("Name"),
                intent.getExtras().getString("Mobile"), intent.getExtras().getString("Person"),
                intent.getExtras().getString("Status")));
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        appBarLayout=(AppBarLayout) findViewById(R.id.app_bar) ;
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    showOption(R.id.action_info);
                    fab.setVisibility(View.GONE);
                    // collapsingToolbarLayout.setTitle("Title");
                    isShow = true;
                } else if (isShow) {
                    hideOption(R.id.action_info);
                    fab.setVisibility(View.VISIBLE);
                    // collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


        GetclientDetail(context);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vm_followup.NextFollowupDialog(context);
           //      new FollowupDialog(context,mHandler,bundle,FOLLOWUP_DIALOG).show();
                ViewCompat.setNestedScrollingEnabled(
                        recyclerView, false);
                //   appBarLayout.setExpanded(false);
                appBarLayout.setExpanded(false, true);


              /*  FollowupoDialog cdd=new FollowupoDialog(context);
                cdd.show();
                Window window = cdd.getWindow();
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);*/
            }



        });

    }


    private void GetclientDetail(final Context context) {

        //mFavViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);

        vm_followup.GETFOLLOWCALL(context, new VM_Followup.OnResulyListner() {
            @Override
            public void Sucessresult(ArrayList<mFollowupgrid> mFollowupgrids) {

                mAdapter = new FolllowUpAdapter(context, mFollowupgrids);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
             //   vm_followup.setmFollowupgrid(mFollow);


                ViewCompat.setNestedScrollingEnabled(
                        recyclerView, false);
                //   appBarLayout.setExpanded(false);
                appBarLayout.setExpanded(false, true);



            }

            @Override
            public void ErrorResult(String Error, String Title) {
            /*    AlertDialog.Builder builder1;
;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder1 = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder1 = new AlertDialog.Builder(context);
                }
                builder1.setMessage(Error);
                builder1.setTitle(Title);
                builder1.setCancelable(true);
                AlertDialog alert11 = builder1.create();
                alert11.show();*/
            }
        });


    }
/*
       private void GetclientDetail( ) {

        Intent intent = getIntent();
        String PA_ID = intent.getExtras().getString("Paid");

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


        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(this, "company_code", "demo"));
        request.put("iPaId", PA_ID);
        request.put("sFormType", "ORDER_STATUS_FOLLOWUP");

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);



        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();
        new CboServices(this, mHandler).customMethodForAllServices(request, "FollowUpGrid", FOLLOWUPGRID, tables);

        mFollow=new mFollowupgrid();
        // getSupportActionBar().setTitle(mFollow.getpANAME());
        //setupRecyclerView();
    }*/


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

                        String paid = c.getString("PA_ID");
                        mFollow.setpAID(paid);
                        String name = c.getString("PA_NAME");
                        mFollow.setpANAME(name);
                        String srno = c.getString("SRNO");
                        mFollow.setsRNO(srno);
                        String id = c.getString("ID");
                        mFollow.setiD(id);
                        String contact = c.getString("Mobile");
                        mFollow.setcONTACTNO(contact);
                        String userId = c.getString("USER_ID");
                        mFollow.setuSERID(userId);
                        String refrby = c.getString("REF_BY");
                        mFollow.setrEFBY(refrby);
                        String followdate = c.getString("FOLLOWUPDATE");
                        mFollow.setfOLLOWUPDATE(followdate);
                        String user1name = c.getString("USER1_NAME");
                        mFollow.setuSER1NAME(user1name);
                        String user = c.getString("USER_NAME");
                        mFollow.setuSERNAME(user);
                        String nxtfollow = c.getString("NEXTFOLLOWUPDATE");
                        mFollow.setnEXTFOLLOWUPDATE(nxtfollow);
                        String contactPerson = c.getString("CONTACT_PERSON");
                        mFollow.setcONTACTPERSON(contactPerson);
                        String freamrk = c.getString("FREMARK");mFollow.setfREMARK(freamrk);
                        followupdata.add(mFollow);

                    }
                    mAdapter = new FolllowUpAdapter(context, followupdata);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);


                    ViewCompat.setNestedScrollingEnabled(
                            recyclerView, false);
                    //   appBarLayout.setExpanded(false);
                    appBarLayout.setExpanded(false,true);





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
        getMenuInflater().inflate(R.menu.menu_followupactvity, menu);


        MenuItem searchItem = menu.findItem(R.id.menu_search);
        /*SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
        //searchView.setIconifiedByDefault(false);


        hideOption(R.id.action_info);

        return true;
    }



    private void hideOption(int id) {
        if (menu == null) return;
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        if (menu == null) return;
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {

            if (appBarLayout.getTop() < 0)
                appBarLayout.setExpanded(true);
            else
                appBarLayout.setExpanded(false);
            return true;
        }else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public void updateparty(mFollowupgrid party) {
        textView.setText(party.getpANAME());
        Conperson.setText(party.getcONTACTPERSON());
        Refrby.setText(party.getrEFBY());
        Username.setText(party.getuSERNAME());
        User1name.setText(party.getuSER1NAME());
    }

    @Override
    public void setPanme(String Panme) {

    }

    @Override
    public void setContactPerson(String Cname) {

    }

    @Override
    public void setusername(String user) {

    }

    @Override
    public void setusername1(String user1) {

    }

    @Override
    public void setreferby(String referby) {

    }
}

package com.example.pc24.cbohelp.PartyView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.example.pc24.cbohelp.AddParty.PartyDetail;
import com.example.pc24.cbohelp.AddParty.PartyDetail2;
import com.example.pc24.cbohelp.Followingup.CustomDatePicker;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.Spinner_Dialog;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.utils.DropDownModel;
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

public class PartyActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,IParty {
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
    ImageView img_followdate, img_nextfollowdate, Img_party,spn_user,spn_user1;
    RadioGroup radioGroup;
    RadioButton entrydate, followingdate;
    Button fromdatebtn, todatebtn, Gobtn,UserBtn,UserBtn1;
    LinearLayout  Lmissedtype;
    TextView PartyTxt;
    String ViewBy="";TextView TotalParty;
    String Paid="",SelectedPaid="";
    String userId="",userName="",userId1="",userName1="";
    Vm_Party vm_party=null;
    LinearLayout userLayout;
    String DesgId="";

    Status status=Status.All;

    @Override
    public void onUserChanged(DropDownModel user) {



        UserBtn.setText(user.getName());
        UserBtn.setPadding(1, 0, 5, 0);
    }

    @Override
    public void onUser1Changed(DropDownModel user) {


        UserBtn1.setText(user.getName());
        UserBtn1.setPadding(1, 0, 5, 0);
    }

    @Override
    public void onFromDateChanged(Date date) {
        fromdatebtn.setText(CustomDatePicker.formatDate(date, CustomDatePicker.ShowFormat));
    }

    @Override
    public void onToDateChanged(Date date) {
        todatebtn.setText(CustomDatePicker.formatDate(date, CustomDatePicker.ShowFormat));
    }

    @Override
    public void onUserListChanged(ArrayList<DropDownModel> users) {
        String id =shareclass.getValue(this,"PA_ID","0");
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                vm_party.setUSER(users.get(i));
                vm_party.setUSER1(users.get(i));
                }
        }
        GetAllParty( );
    }

    @Override
    public void onPartyListChanged(ArrayList<mParty> parties) {
        mAdapter = new Partyviewapdater(context, parties,SelectedPaid);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        TotalParty.setText(""+mAdapter.getItemCount());
    }


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
        TotalParty = (TextView) findViewById(R.id.Tpartycount);
        Gobtn = (Button) findViewById(R.id.btn_go);
        radioGroup = (RadioGroup) findViewById(R.id.viewby);
        entrydate = (RadioButton) findViewById(R.id.entrydate);
        followingdate = (RadioButton) findViewById(R.id.followingdate);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        UserBtn = (Button) findViewById(R.id.btn_usertype);
        UserBtn1 = (Button) findViewById(R.id.btn_usertype1);
        spn_user = (ImageView) findViewById(R.id.spinner_img_usertype);
        spn_user1 = (ImageView) findViewById(R.id.spinner_img_usertype1);
       userLayout = (  LinearLayout)findViewById(R.id.userLayout);


        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra("headername"));
        }

        context = this;
        shareclass = new Shareclass();
        vm_party = ViewModelProviders.of(PartyActivity.this).get(Vm_Party.class);
        vm_party.setListener(this);


        vm_party.setFadte(new Date());
        vm_party.setTodate(new Date());
          DesgId=shareclass.getValue(context, "DESIG_ID", "0");
        if (!shareclass.getValue(context,"CATEGORY","").equalsIgnoreCase("ADMIN")) {

            userLayout.setVisibility(View.GONE);

        }
        else{
            GetDDL();
        }
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                DesgId=shareclass.getValue(context, "DESIG_ID", "0");
                if (!shareclass.getValue(context,"CATEGORY","").equalsIgnoreCase("ADMIN")) {

                    userLayout.setVisibility(View.GONE);

                }
                else{

                   GetAllParty();
                }

                pullToRefresh.setRefreshing(false);
            }
        });

            Gobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    GetAllParty();

                }
            });


            fromdatebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        new CustomDatePicker(context, null,
                                CustomDatePicker.getDate(todatebtn.getText().toString(), CustomDatePicker.ShowFormat)
                        ).Show(CustomDatePicker.getDate(fromdatebtn.getText().toString(), CustomDatePicker.ShowFormat)
                                , new CustomDatePicker.ICustomDatePicker() {
                                    @Override
                                    public void onDateSet(Date date) {
                                        vm_party.setFadte(date);
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
                                CustomDatePicker.getDate(fromdatebtn.getText().toString(), CustomDatePicker.ShowFormat))
                                .Show(CustomDatePicker.getDate(todatebtn.getText().toString(), CustomDatePicker.ShowFormat)
                                        , new CustomDatePicker.ICustomDatePicker() {
                                            @Override
                                            public void onDateSet(Date date) {

                                                vm_party.setTodate(date);
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

            UserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Spinner_Dialog(PartyActivity.this, vm_party.UserData, new Spinner_Dialog.OnItemClickListener() {

                        @Override
                        public void ItemSelected(DropDownModel item) {
                            vm_party.setUSER(item);
                        }


                    }).show();
                }
            });
            UserBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Spinner_Dialog(PartyActivity.this, vm_party.UserData, new Spinner_Dialog.OnItemClickListener() {

                        @Override
                        public void ItemSelected(DropDownModel item) {
                            vm_party.setUSER1(item);
                        }


                    }).show();
                }
            });
            spn_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UserBtn.performClick();
                }
            });

            spn_user1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserBtn1.performClick();
                }
            });

            vm_party.setViewBy("N");
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == R.id.entrydate) {
                        //  ViewBy="N";
                        vm_party.setViewBy("F");
                    } else {
                        //  ViewBy="F";
                        vm_party.setViewBy("N");
                    }
                }
            });


            setupRecyclerView();
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null)
            SelectedPaid = mAdapter.SelectedPaid;

        if (!shareclass.getValue(context,"CATEGORY","").equalsIgnoreCase("ADMIN")) {


            vm_party.setUSER1(new DropDownModel("",shareclass.getValue(this,"PA_ID","0")));
            vm_party.setUSER(new DropDownModel("",shareclass.getValue(this,"PA_ID","0")));
            GetAllParty();
        }

        else{

//            GetDDL();
//            vm_party.setUSER1(new DropDownModel("",shareclass.getValue(this,"PA_ID","0")));
//            vm_party.setUSER(new DropDownModel("",shareclass.getValue(this,"PA_ID","0")));
//           // UserBtn.setText(new ArrayList<DropDownModel> user("",shareclass.getValue(this,"PA_ID","0"));
//            GetAllParty();



        }


    }

    private void GetDDL() {
        vm_party.GetUserList(context);
    }


    private void GetAllParty( ) {
        vm_party.GetAllParty(context);
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
                 vm_party.setStatusvalue("0");
                GetAllParty();
                break;
            case R.id.done:
                status=Status.Done;
                vm_party.setStatusvalue("1");

                GetAllParty();
                break;
            case R.id.inProcess:
                status=Status.inProcess;
                vm_party.setStatusvalue("2");
                GetAllParty();
                break;
            case R.id.notInterested:
                status=Status.NotInterested;
                vm_party.setStatusvalue("3");
                GetAllParty();

                break;
            case R.id.add_party:
                Intent i = new Intent(PartyActivity.this, PartyDetail2.class);
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


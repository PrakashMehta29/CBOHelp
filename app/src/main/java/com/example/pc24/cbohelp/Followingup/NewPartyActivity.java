package com.example.pc24.cbohelp.Followingup;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
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

import com.example.pc24.cbohelp.PartyView.PartyActivity;
import com.example.pc24.cbohelp.PartyView.mParty;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class NewPartyActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, IFollowingup {

    public ProgressDialog progress1;
    RecyclerView recyclerView;
    NewPartyActvityAdapter mAdapter;
    Context context;
    mFollowupgrid mFollow;
    AppBarLayout appBarLayout;
    Vm_Following vm_following = null;
    TextView tilte, nextfollowupdate, followupdate;
    ImageView img_followdate, img_nextfollowdate, Img_party;
    RadioGroup radioGroup;
    RadioButton entrydate, followingdate;
    Button fromdatebtn, todatebtn, Gobtn;
    LinearLayout  Lmissedtype;
    TextView PartyTxt;
    Shareclass shareclass;
    DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;
    private Menu menu;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actvity_party_layout);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        followupdate = (TextView) findViewById(R.id.followdate);
        nextfollowupdate = (TextView) findViewById(R.id.nextfollowdate);
        fromdatebtn = (Button) findViewById(R.id.fromdatebtn);
        todatebtn = (Button) findViewById(R.id.todatebtn);
        img_followdate = (ImageView) findViewById(R.id.spinner_img_followdate);
        img_nextfollowdate = (ImageView) findViewById(R.id.spinner_img_nextfollowdate);
        PartyTxt = (TextView) findViewById(R.id.party_name);
        Lmissedtype = (LinearLayout) findViewById(R.id.layout_party);
        recyclerView = (RecyclerView) findViewById(R.id.followinguplist);
        Gobtn = (Button) findViewById(R.id.btn_go);
        radioGroup = (RadioGroup) findViewById(R.id.viewby);
        entrydate = (RadioButton) findViewById(R.id.entrydate);
        followingdate = (RadioButton) findViewById(R.id.followingdate);


        shareclass = new Shareclass();
        context = this;
        vm_following = ViewModelProviders.of(NewPartyActivity.this).get(Vm_Following.class);
        vm_following.setListeners(context, this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        }

        Intent intent = getIntent();
        vm_following.setParty(new mParty(intent.getExtras().getString("Paid"), intent.getExtras().getString("Name"),
                intent.getExtras().getString("Mobile"), intent.getExtras().getString("Person"),
                intent.getExtras().getString("Status")
        ));

        GetclientDetail(context);
        fromdatebtn.setText(CustomDatePicker.currentDate( CustomDatePicker.ShowFormat));
        vm_following.setFromDate(CustomDatePicker.currentDate( CustomDatePicker.CommitFormat));
        todatebtn.setText(CustomDatePicker.currentDate( CustomDatePicker.ShowFormat));
        vm_following.setToDate(CustomDatePicker.currentDate( CustomDatePicker.CommitFormat));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.entrydate:
                        vm_following.setViewby("N");
                        break;
                    case R.id.followingdate:
                        vm_following.setViewby("F");
                        break;

                }
            }
        });



        Gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PartyTxt.getText().toString().equals("")) {
                    GetclientDetail(context);
                } else {
                    Toast.makeText(context, "Please Select Party Name First", Toast.LENGTH_SHORT).show();
                }
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
                                    vm_following.setFromDate(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
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
                                            vm_following.setToDate(CustomDatePicker.formatDate(date,CustomDatePicker.CommitFormat));
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
        vm_following.setViewby("F");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.entrydate) {
                    vm_following.setViewby("F");
                } else {
                    vm_following.setViewby("N");
                }
            }
        });

        if (PartyTxt.getText().toString().equals("")) {
            hideOption(R.id.add_remark);
        }





    }
    private void GetclientDetail(final Context context) {
        vm_following.GETFOLLOWCALL(context);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_followupgrid, menu);
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


        if (id == R.id.add_remark) {
            if (PartyTxt.getText().toString()!="") {
              vm_following.NextFollowupDialog(context);
            }
            else{
                Toast.makeText(context, "Please Select Party Name", Toast.LENGTH_SHORT).show();

            }
        }
        else {
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
    public void updateparty(mParty party) {
        setPanme(party.getName());
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);

    }

    @Override
    public void setPanme(String Panme) {
        PartyTxt.setText(Panme);
        getSupportActionBar().setTitle(PartyTxt.getText().toString());
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        // tilte.setText(PartyTxt.getText().toString());

    }

    @Override
    public void setFollowupdata(ArrayList Followupdata) {
        mAdapter = new NewPartyActvityAdapter(context, Followupdata);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);

        ViewCompat.setNestedScrollingEnabled(
                recyclerView, false);
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
    @Override
    protected void onPostResume() {
        super.onPostResume ();
        mShimmerViewContainer.startShimmerAnimation ();
    }

    @Override
    protected void onPause() {
        super.onPause ();
        mShimmerViewContainer.stopShimmerAnimation();
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

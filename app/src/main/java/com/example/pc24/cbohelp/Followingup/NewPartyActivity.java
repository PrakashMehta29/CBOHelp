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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc24.cbohelp.PartyView.PartyActivity;
import com.example.pc24.cbohelp.PartyView.mParty;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.SpinAdapter;
import com.example.pc24.cbohelp.SpinnerModel;
import com.example.pc24.cbohelp.appPreferences.Shareclass;

import java.text.ParseException;
import java.util.ArrayList;

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
    Button followdatebtn, nextfollowdatebtn, Gobtn;
    LinearLayout  Lmissedtype;
    TextView PartyTxt;
    Shareclass shareclass;
    DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actvity_party_layout);
        followupdate = (TextView) findViewById(R.id.followdate);
        nextfollowupdate = (TextView) findViewById(R.id.nextfollowdate);
        followdatebtn = (Button) findViewById(R.id.followdatebtn);
        nextfollowdatebtn = (Button) findViewById(R.id.nextfollowdatebtn);
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

        followdatebtn.setText(CustomDatePicker.currentDate());
        vm_following.setEnteryDate(followdatebtn.getText().toString());
        nextfollowdatebtn.setText(CustomDatePicker.currentDate());
        vm_following.setFollowingDate(nextfollowdatebtn.getText().toString());
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
        followdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context, null,
                            CustomDatePicker.getDate(nextfollowdatebtn.getText().toString(), "MM/dd/yyyy")
                    ).Show(CustomDatePicker.getDate(followdatebtn.getText().toString(), "MM/dd/yyyy")
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(int year, int month, int dayOfMonth) {
                                    followdatebtn.setText(month + "/" + dayOfMonth + "/" + year);
                                    vm_following.setEnteryDate(month + "/" + dayOfMonth + "/" + year);
                                }
                            });

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        nextfollowdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new CustomDatePicker(context,
                            CustomDatePicker.getDate(followdatebtn.getText().toString(), "MM/dd/yyyy"))
                            .Show(CustomDatePicker.getDate(nextfollowdatebtn.getText().toString(), "MM/dd/yyyy")
                                    , new CustomDatePicker.ICustomDatePicker() {
                                        @Override
                                        public void onDateSet(int year, int month, int dayOfMonth) {
                                            nextfollowdatebtn.setText(month + "/" + dayOfMonth + "/" + year);
                                            vm_following.setFollowingDate(month + "/" + dayOfMonth + "/" + year);
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
                followdatebtn.performClick();

            }
        });
        img_nextfollowdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextfollowdatebtn.performClick();
            }
        });


        Lmissedtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent1 = new Intent(NewPartyActivity.this, PartyActivity.class);
                startActivityForResult(intent1, 0);
            }
        });
        if (radioGroup.getCheckedRadioButtonId() == -1) {

        } else {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.entrydate) {
                vm_following.setViewby("F");
            } else {
                vm_following.setViewby("N");
            }
        }
        if (PartyTxt.getText().toString().equals("")) {
            hideOption(R.id.add_remark);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            vm_following.setParty(new mParty(data.getExtras().getString("Paid"), data.getExtras().getString("Name"),
                    data.getExtras().getString("Mobile"), data.getExtras().getString("Person"),
                    data.getExtras().getString("Status")
            ));
            GetclientDetail(context);
        }
    }

    private void GetclientDetail(final Context context) {


        vm_following.GETFOLLOWCALL(context, new Vm_Following.OnResultlistner() {
            @Override
            public void Sucessresult(ArrayList<mFollowupgrid> mFollowupgrids) {
                mAdapter = new NewPartyActvityAdapter(context, mFollowupgrids);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

                ViewCompat.setNestedScrollingEnabled(
                        recyclerView, false);
                //   appBarLayout.setExpanded(false);
                appBarLayout.setExpanded(true, true);
            }

            @Override
            public void ErrorResult(String Error, String Title) {

            }
        });


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
            if (!PartyTxt.getText().toString().equals("")) {

                vm_following.NextFollowupDialog(context);
            } else {
                Toast.makeText(context, "Please Select Party Name First", Toast.LENGTH_SHORT).show();
            }
        } else {
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

    }

    @Override
    public void setPanme(String Panme) {
        PartyTxt.setText(Panme);
        getSupportActionBar().setTitle(PartyTxt.getText().toString());
        // tilte.setText(PartyTxt.getText().toString());

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

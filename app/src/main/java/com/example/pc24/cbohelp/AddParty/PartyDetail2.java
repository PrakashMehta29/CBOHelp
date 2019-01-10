package com.example.pc24.cbohelp.AddParty;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.pc24.cbohelp.PartyView.mParty;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.Spinner_Dialog;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.utils.DropDownModel;

import java.util.ArrayList;

public class PartyDetail2 extends AppCompatActivity implements  Isubmit {

    public ProgressDialog progress1=null;
    String[] STATUS = {"Done", "InProcess", "Not Intrested"};
    Button ComponeyType, User, User1;
    Spinner Status;
    Context context;
    Shareclass shareclass;
    public String USERID = "", USERNAME = "";
    public String USERID1 = "";
    String userName = "", userId = "";
    String userName1 = "", userId1 = "";
    String ComponeyName = "", ComponeyId = "";
    ArrayList<DropDownModel> componeydata = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> UserData = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> UserData1 = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> UserDataCopy = new ArrayList<DropDownModel>();
    Bundle bundle;
    String Paid = "";
    EditText paname, mobile, email, person, city, website, EmplyeeNo, Referby, adress1, adress2, adress3, adress4;
    Button submit;
    LinearLayout layoutComponey, layoutUser, layoutuser1;
    Menu menu;
    ImageView CompspinImg, UserSpingImg, User1SpinImg, statsuimg;
    VM_PartyzDetail vm_partyzDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_detail);

        paname = findViewById(R.id.party_name);
        mobile = findViewById(R.id.party_mobile_no);
        email = findViewById(R.id.party_email);
        person = findViewById(R.id.party_person);
        city = findViewById(R.id.party_city);
        website = findViewById(R.id.party_website);
        EmplyeeNo = findViewById(R.id.party_empolyye_no);
        Referby = findViewById(R.id.party_Ref);
        Status = findViewById(R.id.status_type);
        statsuimg = findViewById(R.id.spinner_img_status);
        adress1 = findViewById(R.id.party_Adrress1);
        adress2 = findViewById(R.id.party_Adress_2);
        adress3 = findViewById(R.id.party_Adress_3);
        adress4 = findViewById(R.id.party_Adress_4);
        CompspinImg = findViewById(R.id.spinner_img_componey);
        UserSpingImg = findViewById(R.id.spinner_img_user);

        User1SpinImg = findViewById(R.id.spinner_img_user1);
        layoutComponey = findViewById(R.id.layout_componey_type);
        layoutUser = findViewById(R.id.layout_user);
        layoutuser1 = findViewById(R.id.layout_user1);
        submit = findViewById(R.id.show);
        ComponeyType = findViewById(R.id.componey_type);
        User = findViewById(R.id.party_user);
        User1 = findViewById(R.id.party_user1);
        context=this;
        shareclass= new Shareclass();

        vm_partyzDetail= ViewModelProviders.of(PartyDetail2.this).get(VM_PartyzDetail.class);
        vm_partyzDetail.setListener(context,this);
        vm_partyzDetail.GEtDDLResult(context);
        ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, STATUS);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        Status.setAdapter(aa);
        Status.setSelection(1);
        statsuimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Status.performClick();
            }
        });

        bundle = getIntent().getExtras();
  if( bundle!=null){
      Paid = getIntent().getStringExtra("Paid");
      vm_partyzDetail.setPartyid(Paid);
  }
        ComponeyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Spinner_Dialog(context, componeydata, new Spinner_Dialog.OnItemClickListener() {
                    @Override
                    public void ItemSelected(DropDownModel item) {
                        ComponeyId=item.getId();
                        ComponeyName=item.getName();
                        ComponeyType.setText(ComponeyName);
                        ComponeyType.setPadding(1, 0, 5, 0);

                    }
                }).show();
            }
        });
        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Spinner_Dialog(context, UserData, new Spinner_Dialog.OnItemClickListener() {
                    @Override
                    public void ItemSelected(DropDownModel item) {
                        USERID=item.getId();
                        USERNAME=item.getName();
                        User.setText(USERNAME);
                        User.setPadding(1, 0, 5, 0);
                    }
                }).show();
            }
        });
        User1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Spinner_Dialog(context, UserData1, new Spinner_Dialog.OnItemClickListener() {
                    @Override
                    public void ItemSelected(DropDownModel item) {
                        USERID1=item.getId();
                        USERNAME=item.getName();
                        User1.setText(USERNAME);
                        User1.setPadding(1, 0, 5, 0);

                    }
                }).show();
            }
        });
        CompspinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponeyType.performClick();
            }
        });
        UserSpingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.performClick();
            }
        });
        User1SpinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User1.performClick();
            }
        });


        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Party");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validatepaname() | !Validatemobileno() | !ValidatePerson() | !ValidateCity() | !ValidateEmpNo()) {

                    return;
                }
                else{
                    vm_partyzDetail.setmPartyField( new mPartyField(paname.getText().
                            toString(),mobile.getText().toString(),person.getText().toString(),city.getText().toString(),
                 USERID, shareclass.getValue(getApplicationContext(), "PA_ID", "0"),USERID1,email.getText().toString(),
                            ComponeyType.getText().toString(),website.getText().toString(),EmplyeeNo.getText().toString(),
                            Referby.getText().toString(),
                            Status.getSelectedItem().toString(),adress1.getText().toString(),
                            adress2.getText().toString(),adress3.getText().toString(),adress4.getText().toString()));
                            vm_partyzDetail.GetPartySubmit(context);
                }

            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.party_add_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        finish();

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClientPopulate(ArrayList<mPartyField> mPartyFields, String UserId, String UserId1) {

        paname.setText(mPartyFields.get(0).getPANAME());
        mobile.setText(mPartyFields.get(0).getMOBILE());
        EmplyeeNo.setText(mPartyFields.get(0).getNOOFEMPLOYEE());
        city.setText(mPartyFields.get(0).getCITY());
        mobile.setText(mPartyFields.get(0).getMOBILE());
        ComponeyType.setText(mPartyFields.get(0).getCOMPANYTYPE());
        email.setText(mPartyFields.get(0).getEMAIL());
        person.setText(mPartyFields.get(0).getPERSON());
        Referby.setText(mPartyFields.get(0).getREFBY());
        website.setText(mPartyFields.get(0).getWEBSITE());
        adress1.setText(mPartyFields.get(0).getADD1());
        adress2.setText(mPartyFields.get(0).getADD2());
        adress3.setText(mPartyFields.get(0).getADD3());
        adress4.setText(mPartyFields.get(0).getADD4());


        if (mPartyFields.get(0).getPARTYSTATUS() != "") {
            if (mPartyFields.get(0).equals("InProcess")) {

                Status.setSelection(1);
            } else if (mPartyFields.get(0).getPARTYSTATUS().equals("Done")) {
                Status.setSelection(0);
            } else if (mPartyFields.get(0).getPARTYSTATUS().equals("Not Intrested")) {

                Status.setSelection(2);
            } else {
                Status.setSelection(1);
            }


            for (int i = 0; i < UserData.size(); i++) {

                //userId = String.valueOf(UserData.get(i).getId().equals(USERID));
                if (UserData.get(i).getId().equals(UserId)) {
                    USERID = UserData.get(i).getId();
                    userName = UserData.get(i).getName();
                    User.setText(userName);
                    User.setPadding(1, 0, 5, 0);


                }
            }
            for (int j = 0; j < UserData1.size(); j++) {
                if (UserData1.get(j).getId().equals(UserId1)) {
                    USERID1 = UserData1.get(j).getId();
                    userName1 = UserData1.get(j).getName();
                    User1.setText(userName1);
                    User1.setPadding(1, 0, 5, 0);

                }
            }
        }
    }
    @Override
    public void onComponydatta(ArrayList<DropDownModel> Componydata) {
     componeydata.addAll(Componydata);
        if (componeydata.size() != 1) {
            ComponeyId = componeydata.get(0).getId();
            ComponeyName = componeydata.get(0).getName();
            ComponeyType.setText(ComponeyName);
        }
    }

    @Override
    public void UserData(ArrayList<DropDownModel> Userlist) {
        UserData.addAll(Userlist);
        UserData1.addAll(Userlist);
        if (UserData.size() != 1) {
            USERID = UserData.get(1).getId();
            USERID1 = UserData.get(1).getId();
            userName = UserData.get(1).getName();
            User.setText(userName);
            User1.setText(userName);

        }
    }




    public boolean Validatepaname() {
        String username = paname.getText().toString().trim();

        if (username.isEmpty()) {
            paname.setError("Please enter your name");
            return false;
        } else {
            paname.setError(null);
            return true;
        }
    }

    private boolean isValidMobile(String phone) {


        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public boolean Validatemobileno() {
        String usermobile = mobile.getText().toString().trim();
        if (usermobile.isEmpty()) {
            mobile.setError("Please enter your mobileno");
            return false;
        } else if (usermobile.length() < 10) {

            mobile.setError("Please enter valid  mobileno");
            return false;

        } else {
            mobile.setError(null);
            return true;
        }
    }

    public boolean ValidateEmail() {

        String useremail = email.getText().toString().trim();
        if (useremail.isEmpty()) {
            email.setError("Please enter your email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()) {
            email.setError("Please enter valid  email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public boolean ValidatePerson() {
        String username = person.getText().toString().trim();

        if (username.isEmpty()) {
            person.setError("Please enter Person ");
            return false;
        } else {
            person.setError(null);
            return true;
        }
    }

    public boolean ValidateCity() {
        String username = city.getText().toString().trim();

        if (username.isEmpty()) {
            city.setError("Please enter  city");
            return false;
        } else {
            city.setError(null);
            return true;
        }
    }

    public boolean Validatewebsite() {
        String username = website.getText().toString().trim();

        if (username.isEmpty()) {
            website.setError("Please enter  website");
            return false;
        } else {
            website.setError(null);
            return true;
        }
    }

    public boolean ValidateEmpNo() {
        String username = EmplyeeNo.getText().toString().trim();

        if (username.isEmpty()) {
            EmplyeeNo.setError("Please enter Emp no");
            return false;
        } else {
            EmplyeeNo.setError(null);
            return true;
        }
    }

    public boolean ValidateRefBy() {
        String username = Referby.getText().toString().trim();

        if (username.isEmpty()) {
            Referby.setError("Please enter Refr Name");
            return false;
        } else {
            Referby.setError(null);
            return true;
        }
    }

    public boolean ValidateAdress1() {
        String username = adress1.getText().toString().trim();

        if (username.isEmpty()) {
            adress1.setError("Please enter  Address1");
            return false;
        } else {
            adress1.setError(null);
            return true;
        }
    }

    public boolean ValidateAdress2() {
        String username = adress2.getText().toString().trim();

        if (username.isEmpty()) {
            adress2.setError("Please enter  Adress2");
            return false;
        } else {
            adress2.setError(null);
            return true;
        }
    }

    public boolean ValidateAdress3() {
        String username = adress3.getText().toString().trim();

        if (username.isEmpty()) {
            adress3.setError("Please enter  Adress3");
            return false;
        } else {
            adress3.setError(null);
            return true;
        }
    }

    public boolean ValidateAdress4() {
        String username = adress4.getText().toString().trim();

        if (username.isEmpty()) {
            adress4.setError("Please enter Adress4");
            return false;
        } else {
            adress4.setError(null);
            return true;
        }
    }
}

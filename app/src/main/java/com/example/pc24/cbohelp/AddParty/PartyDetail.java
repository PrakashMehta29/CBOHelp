package com.example.pc24.cbohelp.AddParty;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.Toast;
import com.example.pc24.cbohelp.PartyView.PartyActivity;
import com.example.pc24.cbohelp.PartyView.mParty;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.Spinner_Dialog;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.services.CboServices;
import com.example.pc24.cbohelp.utils.DropDownModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.location.Location.convert;


public class PartyDetail extends AppCompatActivity {

    String[] STATUS = { "Done", "InProcess", "Not Intrested"};
    Button ComponeyType, User, User1;
    Spinner Status;
    public ProgressDialog progress1;
    private static final int MESSAGE_INTERNET = 1, FollowUpOrder_Commit = 2, ClientPopulate = 3;
    Shareclass shareclass;
    DBHelper dbHelper;
    String USERID = "", USERNAME = "";
    String USERID1 = "";
    String userName = "", userId = "";
    String userName1 = "", userId1 = "";
    String ComponeyName = "", ComponeyId = "";
    String StatusName = "", StatusId = "";
    private AlertDialog myalertDialog = null;
    ArrayList<mPartyField> mPartyFields = new ArrayList<mPartyField>();
    ArrayList<DropDownModel> componeydata = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> componeydataCopy = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> UserData = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> UserData1 = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> UserDataCopy = new ArrayList<DropDownModel>();


    //idpassword
/*1554hari
    1554
    */
    String Componeytype = "";
    mParty mParty = null;
    mPartyField mfield;
    String Paid="";

    public mParty getmParty() {
        return mParty;
    }

    public void setmParty(mParty mParty) {
        this.mParty = mParty;
    }


    EditText paname, mobile, email, person, city, website, EmplyeeNo, Referby, adress1, adress2, adress3, adress4;
    Button submit;
    private String Party_Id = "0";
    LinearLayout layoutComponey, layoutUser, layoutuser1;
    Menu menu;

    ImageView CompspinImg, UserSpingImg, User1SpinImg, statsuimg;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.party_detail);
        paname = (EditText) findViewById(R.id.party_name);
        mobile = (EditText) findViewById(R.id.party_mobile_no);
        email = (EditText) findViewById(R.id.party_email);
        person = (EditText) findViewById(R.id.party_person);
        city = (EditText) findViewById(R.id.party_city);
        website = (EditText) findViewById(R.id.party_website);
        EmplyeeNo = (EditText) findViewById(R.id.party_empolyye_no);
        Referby = (EditText) findViewById(R.id.party_Ref);
        Status = (Spinner) findViewById(R.id.status_type);
        statsuimg = (ImageView) findViewById(R.id.spinner_img_status);
        adress1 = (EditText) findViewById(R.id.party_Adrress1);
        adress2 = (EditText) findViewById(R.id.party_Adress_2);
        adress3 = (EditText) findViewById(R.id.party_Adress_3);
        adress4 = (EditText) findViewById(R.id.party_Adress_4);
        CompspinImg = (ImageView) findViewById(R.id.spinner_img_componey);
        UserSpingImg = (ImageView) findViewById(R.id.spinner_img_user);

        User1SpinImg = (ImageView) findViewById(R.id.spinner_img_user1);
        layoutComponey = (LinearLayout) findViewById(R.id.layout_componey_type);
        layoutUser = (LinearLayout) findViewById(R.id.layout_user);
        layoutuser1 = (LinearLayout) findViewById(R.id.layout_user1);
        submit = (Button) findViewById(R.id.show);
        ComponeyType = (Button) findViewById(R.id.componey_type);
        User = (Button) findViewById(R.id.party_user);
        User1 = (Button) findViewById(R.id.party_user1);
        progress1 = new ProgressDialog(this);
        shareclass = new Shareclass();
        dbHelper = new DBHelper(this);

        ArrayAdapter aa = new ArrayAdapter(PartyDetail.this, android.R.layout.simple_spinner_item, STATUS);
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
        ComponeyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Spinner_Dialog(PartyDetail.this, componeydataCopy, new Spinner_Dialog.OnItemClickListener() {

                    @Override
                    public void ItemSelected(DropDownModel item) {
                        ComponeyId = item.getId();
                        ComponeyName = item.getName();
                        ComponeyType.setText(ComponeyName);
                        ComponeyType.setPadding(1, 0, 5, 0);
                    }


                }).show();


            }
        });
        CompspinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Spinner_Dialog(PartyDetail.this, componeydataCopy, new Spinner_Dialog.OnItemClickListener() {

                    @Override
                    public void ItemSelected(DropDownModel item) {
                        ComponeyId = item.getId();
                        ComponeyName = item.getName();
                        ComponeyType.setText(ComponeyName);
                        ComponeyType.setPadding(1, 0, 5, 0);
                    }


                }).show();

            }
        });
        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Spinner_Dialog(PartyDetail.this, UserDataCopy, new Spinner_Dialog.OnItemClickListener() {

                    @Override
                    public void ItemSelected(DropDownModel item) {

                        userId = item.getId();
                        userName = item.getName();
                        User.setText(userName);
                        User.setPadding(1, 0, 5, 0);
                    }


                }).show();
            }
        });
        User1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Spinner_Dialog(PartyDetail.this, UserDataCopy, new Spinner_Dialog.OnItemClickListener() {

                    @Override
                    public void ItemSelected(DropDownModel item) {
                        userId1 = item.getId();
                        userName1 = item.getName();
                        User1.setText(userName1);
                        User1.setPadding(1, 0, 5, 0);
                    }


                }).show();
            }
        });
        UserSpingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Spinner_Dialog(PartyDetail.this, UserDataCopy, new Spinner_Dialog.OnItemClickListener() {

                    @Override
                    public void ItemSelected(DropDownModel item) {

                        userId = item.getId();
                        userName = item.getName();
                        User.setText(userName);
                        User.setPadding(1, 0, 5, 0);

                    }


                }).show();
            }
        });

        User1SpinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Spinner_Dialog(PartyDetail.this, UserDataCopy, new Spinner_Dialog.OnItemClickListener() {

                    @Override
                    public void ItemSelected(DropDownModel item) {
                        userId1 = item.getId();
                        userName1 = item.getName();
                        User1.setText(userName1);
                        User1.setPadding(1, 0, 5, 0);
                    }


                }).show();
            }
        });


        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Party");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Validatepaname() | !Validatemobileno() | !ValidateEmail() | !ValidatePerson() | !ValidateCity()  | !ValidateEmpNo() ) {

                    return;
                }

                Submitdata();


            }
        });


        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(this, "company_code", "demo"));

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);

        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();
        new CboServices(this, mHandler).customMethodForAllServices(request, "FollowupOrder_ddl", MESSAGE_INTERNET, tables);


    }

    public void Clientpoulate() {
        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            Paid=getIntent().getStringExtra("Paid");
            HashMap<String, String> request = new HashMap<>();
            request.put("sDbName", shareclass.getValue(this, "company_code", "demo"));
            request.put("sPaId", Paid);
            request.put("sPaName", "");
            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);

            progress1.setMessage("Please Wait..\n" +
                    " Fetching data");
            progress1.setCancelable(false);
            progress1.show();
            new CboServices(this, mHandler).customMethodForAllServices(request, "ClientPopulate", ClientPopulate, tables);


        }
    }

    private void Submitdata() {

        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(getApplicationContext(), "company_code", "demo"));
        request.put("iPA_ID", "0"+Paid);
        request.put("sPaName", paname.getText().toString());
        request.put("sMobile", mobile.getText().toString());
        request.put("sPerson", person.getText().toString());
        request.put("sCity", city.getText().toString());
        request.put("iUser_ID", userId);
        request.put("iAUser_ID", shareclass.getValue(getApplicationContext(), "PA_ID", "0"));
        request.put("sEmail", email.getText().toString());
        request.put("sCompany_Type", ComponeyType.getText().toString());
        request.put("sWebSite", website.getText().toString());
        request.put("iUser1_Id", userId1);
        request.put("iNoOfEmployee", EmplyeeNo.getText().toString());
        request.put("sRefBy", Referby.getText().toString());
        request.put("sPartyStatus",Status.getSelectedItem().toString());
        request.put("sAdd1", adress1.getText().toString());
        request.put("sAdd2", adress2.getText().toString());
        request.put("sAdd3", adress3.getText().toString());
        request.put("sAdd4", adress4.getText().toString());
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);


        progress1.setMessage("Please Wait..\n" +
                " Fetching data");
        progress1.setCancelable(false);
        progress1.show();
        new CboServices(getApplicationContext(), mHandler).customMethodForAllServices(request, "FollowUpOrder_Commit", FollowUpOrder_Commit, tables);
        Toast.makeText(PartyDetail.this, "Data Submitted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PartyDetail.this, PartyActivity.class);
//        intent.putExtra("iUser_ID", String.valueOf(User.getSelectedItemId()));
//        intent.putExtras(intent);
        startActivity(intent);
    }
/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1){
          mParty=new mParty(this);
          mParty.setId(data.getExtras().getString("Paid"));


          HashMap<String,String> request=new HashMap<>();
            request.put("sDbName", shareclass.getValue(this,"company_code","demo"));
            request.put("sPaId", mParty.getId());
            request.put("sPaName", "");
            ArrayList<Integer> tables=new ArrayList<>();
            tables.add(0);
            progress1.setMessage("Please Wait..\n" +
                    " Fetching data");
            progress1.setCancelable(false);
            progress1.show();
            new CboServices(this,mHandler).customMethodForAllServices(request,"ClientPopulate",ClientPopulate,tables);


        }
    }
*/


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


    public boolean Validatemobileno() {
        String usermobile = mobile.getText().toString().trim();

        if (usermobile.isEmpty()) {
            mobile.setError("Please enter your mobileno");
            return false;
        } else if (!Patterns.PHONE.matcher(usermobile).matches()) {
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
            email.setError("Please enter valid  email adresss");
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
            adress1.setError("Please enter  Adress1");
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

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser1(msg.getData());
                        Clientpoulate();

                    }
                    break;

                case FollowUpOrder_Commit:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        parser2(msg.getData());

                        //  parser1(msg.getData());

                    }
                    break;

                case ClientPopulate:

                    if ((null != msg.getData())) {
                        parser3(msg.getData());
                        SetSpinnerItem();
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

    public void parser1(Bundle result) {
        if (result != null) {
            //dbHelper.deleteMenu();
            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    componeydata.add(new DropDownModel(c.getString("PA_NAME"), "0"));
                }

                componeydataCopy.addAll(componeydata);

                if (componeydata.size() != 1) {


                    ComponeyId = componeydata.get(0).getId();
                    ComponeyName = componeydata.get(0).getName();
                    ComponeyType.setText(ComponeyName);
                    ComponeyType.setPadding(1, 0, 5, 0);


                }


                String table1 = result.getString("Tables1");
                JSONArray jsonArray2 = new JSONArray(table1);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);
                    UserData.add(new DropDownModel(c.getString("USER_NAME"), c.getString("ID")));
                }
                UserDataCopy.addAll(UserData);
                UserData1.addAll(UserData);
                if (UserData.size() != 1) {
                    userId = UserData.get(1).getId();
                    userName = UserData.get(1).getName();
                    User.setText(userName);
                    User.setPadding(1, 0, 5, 0);
                    User1.setText(userName);
                    userId1 = UserData.get(1).getId();
                    User1.setPadding(1, 0, 5, 0);

                 }


                progress1.dismiss();

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }
            progress1.dismiss();
        }

        progress1.dismiss();
    }

    private void parser2(Bundle result) {
        if (result != null) {


        }


    }

    private void parser3(Bundle result1) {
        {
            if ((result1 == null)) {
                progress1.dismiss();

            } else {


                try {


                    String table0 = result1.getString("Tables0");
                    JSONArray jsonArray1 = new JSONArray(table0);
                    mPartyFields.clear();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject c = jsonArray1.getJSONObject(i);
                        mPartyField mFields = new mPartyField();

                        String paid = c.getString("PA_ID");
                        mFields.setPAID(paid);
                        String name = c.getString("PA_NAME");
                        mFields.setPANAME(name);
                        String contact = c.getString("MOBILE");
                        mFields.setMOBILE(contact);
                        String companytype = c.getString("COMPANY_TYPE");
                        mFields.setCOMPANYTYPE(companytype);
                        USERID = c.getString("USER_ID");
                        mFields.setUSERID(USERID);
                        USERID1 = c.getString("USER1_ID");
                        mFields.setUSER1ID(USERID1);
                        String NoofEmp = c.getString("NO_OF_EMPLOYEE");
                        mFields.setNOOFEMPLOYEE(NoofEmp);
                        String websit = c.getString("WEB_SITE");
                        mFields.setWEBSITE(websit);
                        String Person = c.getString("PERSON");
                        mFields.setPERSON(Person);
                        String referby = c.getString("REF_BY");
                        mFields.setPERSON(referby);

                        String PStatus = c.getString("PARTY_STATUS");
                        mFields.setPARTYSTATUS(PStatus);
                        /*mFields.(convert(user1name));
                        String user = c.getString("USER_NAME");
                        mFields.setuSERNAME(convert(user));*/
                        String Email = c.getString("EMAIL");
                        mFields.setEMAIL(Email);
                        String City = c.getString("CITY");
                        mFields.setCITY(City);
                        String add1 = c.getString("ADD1");
                        mFields.setADD1(add1);
                        String add2 = c.getString("ADD2");
                        mFields.setADD2(add2);
                        String add3 = c.getString("ADD3");
                        mFields.setADD3(add3);
                        String add4 = c.getString("ADD4");
                        mFields.setADD4(add4);


                        mPartyFields.add(mFields);
                        paname.setText(name);
                        EmplyeeNo.setText(NoofEmp);
                        city.setText(City);
                        mobile.setText(contact);
                        ComponeyType.setText(companytype);
                        if(mFields.getPARTYSTATUS()!=""){
                           if(mFields.getPARTYSTATUS().equals("InProcess")){

                               Status.setSelection(1);
                           }
                             else if(mFields.getPARTYSTATUS().equals("Done")){

                                Status.setSelection(0);
                            }
                            else if (mFields.getPARTYSTATUS().equals("Not Intrested")){

                                Status.setSelection(2);
                            }
                           else{
                               Status.setSelection(1);
                           }

                          //  Status.setSelection(Integer.parseInt(mFields.getPARTYSTATUS()));
                        }

                        //User.setText(UserData.get(Integer.parseInt(userId)).getName());
                        // User1.setText(UserData.get(Integer.parseInt(user1Id)).getName());
                        email.setText(Email);
                        person.setText(Person);
                        Referby.setText(referby);
                        EmplyeeNo.setText(NoofEmp);
                        website.setText(websit);
                        adress1.setText(add1);
                        adress2.setText(add2);
                        adress3.setText(add3);
                        adress4.setText(add4);
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
        getMenuInflater().inflate(R.menu.party_add_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        finish();

        return super.onOptionsItemSelected(item);
    }

    public void SetSpinnerItem() {


        for (int i = 0; i < UserData.size(); i++) {

            //userId = String.valueOf(UserData.get(i).getId().equals(USERID));
            if (UserData.get(i).getId().equals(USERID)) {
                userId = UserData.get(i).getId();
                userName = UserData.get(i).getName();
                User.setText(userName);
                User.setPadding(1, 0, 5, 0);


            }
        }
            for (int j = 0; j < UserData1.size(); j++) {

                //userId = String.valueOf(UserData.get(i).getId().equals(USERID));
                if (UserData1.get(j).getId().equals(USERID1)) {
                  /*  userId = UserData.get(j).getId();
                    userName = UserData.get(j).getName();
                    User.setText(userName);
                    User.setPadding(1, 0, 5, 0);*/
                    userId1 = UserData1.get(j).getId();
                    userName1 = UserData1.get(j).getName();
                    User1.setText(userName1);
                    User1.setPadding(1, 0, 5, 0);

                    User1.setPadding(1, 0, 5, 0);

                }
            }
        }
    }
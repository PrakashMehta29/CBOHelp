package com.example.pc24.cbohelp.AddParty;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pc24.cbohelp.FollowUp.FollowupDialog;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.utils.DropDownModel;
import com.example.pc24.cbohelp.utils.MyAPIService;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VM_PartyzDetail
        extends ViewModel {


    public static final int FOLLOWUP_DIALOG = 0;
    ArrayList<mPartyField> mPartyFields = new ArrayList<>();
    ArrayList<DropDownModel> Componydata = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> Userlist = new ArrayList<DropDownModel>();
    mPartyField mPartyField = null;
    Context context;
    Shareclass shareclass = null;
    String Partyid;
    Isubmit isubmit = null;
    String Paid = "";
    Bundle bundle = null;
    String USERID = "";
    String USERID1 = "";
    Bundle Msg;
    String Existingid="";


    public String getPartyid( ) {
        return Partyid;
    }

    public void setPartyid(String partyid) {
        Partyid = partyid;
    }
    public VM_PartyzDetail() {

         shareclass = new Shareclass();



    }

    public void setListener(Context context, Isubmit isubmit) {
        this.context = context;
        this.isubmit = isubmit;

    }

    public mPartyField getmPartyField() {

        return mPartyField;
    }

    public void setmPartyField(mPartyField mPartyField) {
        this.mPartyField = mPartyField;
        }

    public void GetPartySubmit(Context context) {


        PartySubmit((Activity) context);


    }

    private void PartySubmit(final Activity context) {


        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));
        request.put("iPA_ID", "0" +Existingid);
        request.put("sPaName", getmPartyField().getPANAME());
        request.put("sMobile", getmPartyField().getMOBILE());
        request.put("sPerson", getmPartyField().getPERSON());
        request.put("sCity", getmPartyField().getCITY());
        request.put("iUser_ID", getmPartyField().getUSERID());
        request.put("iAUser_ID", shareclass.getValue(context, "PA_ID", "0"));
        request.put("sEmail", getmPartyField().getEMAIL());
        request.put("sCompany_Type", getmPartyField().getCOMPANYTYPE());
        request.put("sWebSite", getmPartyField().getWEBSITE());
        request.put("iUser1_Id", getmPartyField().getUSER1ID());
        request.put("iNoOfEmployee", getmPartyField().getNOOFEMPLOYEE());
        request.put("sRefBy", getmPartyField().getREFBY());
        request.put("sPartyStatus", getmPartyField().getPARTYSTATUS());
        request.put("sAdd1", getmPartyField().getADD1());
        request.put("sAdd2", getmPartyField().getADD2());
        request.put("sAdd3", getmPartyField().getADD3());
        request.put("sAdd4", getmPartyField().getADD4());
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);


        new MyAPIService(context)

                .execute(new ResponseBuilder("FollowUpOrder_Commit", request)
                        .setTables(tables)
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) {


                                if (getPartyid() == null) {
                                    bundle = new Bundle();
                                    bundle.putString("iId", "0");
                                    bundle.putInt("iSrno", 1);
                                    bundle.putString("iPaid", Paid);
                                    bundle.putString("header", getmPartyField().getPANAME());
                                    bundle.putString("sContactPerson", getmPartyField().getPERSON());
                                    bundle.putString("sContactNo", getmPartyField().getMOBILE());
                                    bundle.putString("iUserId", getmPartyField().getUSERID());

                                    new FollowupDialog(context, bundle, FOLLOWUP_DIALOG, false, new FollowupDialog.IFollowupDialog() {
                                        @Override
                                        public void onFollowSubmit() {

                                            Toast.makeText(context, "Data Submitted", Toast.LENGTH_SHORT).show();
                                            context.finish();

                                        }


                                    }).show();

                                } else {
                                    Toast.makeText(context, "Data  Submitted", Toast.LENGTH_SHORT).show();
                                    context.finish();

                                }
                            }


                            @Override
                            public void onResponse(Bundle response) {
                                parser2(response);

                            }


                        })
                );


    }

    public void GEtDDLResult(Context context) {


        DDLResult((Activity) context);

    }

    private void DDLResult(Activity context) {



        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);


        new MyAPIService(context).execute(new ResponseBuilder("FollowupOrder_ddl", request)
                .setMultiTable(true)
                .setTables(tables)
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) {
                        if (isubmit != null)
                            isubmit.onComponydatta(Componydata);
                        if (isubmit != null)
                            isubmit.UserData(Userlist);
                        Clientpoulate();

                    }

                    @Override
                    public void onResponse(Bundle bundle) {
                        parser1(bundle);
                    }
                }));

    }

    public void Clientpoulate() {

        if(getPartyid()!=null){
            Existingid=getPartyid().toString();
            HashMap<String, String> request = new HashMap<>();
            request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));
            request.put("sPaId",Existingid );
            request.put("sPaName", "");
            ArrayList<Integer> tables = new ArrayList<>();
            tables.add(0);


            new MyAPIService(context)
                    .execute(new ResponseBuilder("ClientPopulate", request)
                            .setTables(tables)

                            .setResponse(new CBOServices.APIResponse() {
                                @Override
                                public void onComplete(Bundle message) {

                                    if (isubmit != null)

                                        isubmit.onClientPopulate(mPartyFields, USERID, USERID1);
                                }

                                @Override
                                public void onResponse(Bundle response) {
                                    parser3(response);

                                }

                            })
                    );


        }
    }

    private void parser1(Bundle result) {

        try {
            String table0 = result.getString("Tables0");
            JSONArray jsonArray = new JSONArray(table0);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject c = jsonArray.getJSONObject(i);
                Componydata.add(new DropDownModel(c.getString("PA_NAME"), "0"));
            }

            String table1 = result.getString("Tables1");
            JSONArray jsonArray1 = new JSONArray(table1);
            mPartyFields.clear();
            for (int i = 0; i < jsonArray1.length(); i++) {

                JSONObject c = jsonArray1.getJSONObject(i);
                Userlist.add(new DropDownModel(c.getString("USER_NAME"), c.getString("ID")));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void parser2(Bundle response){
        if (response != null) {


            try {
                String Tables0 = response.getString("Tables0");
                JSONArray jsonArray = new JSONArray(Tables0);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    Paid = c.getString("PA_ID");

                }
                {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void parser3(Bundle result1) {
        {
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
                    mFields.setREFBY(referby);

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
                }


            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


}



package com.example.pc24.cbohelp.PartyView;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.pc24.cbohelp.Followingup.CustomDatePicker;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.services.CboServices_Old;
import com.example.pc24.cbohelp.utils.Custom_Variables_And_Method;
import com.example.pc24.cbohelp.utils.DropDownModel;
import com.example.pc24.cbohelp.utils.MyAPIService;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Vm_Party extends ViewModel{
    ArrayList<mParty>mParties=null;

    Shareclass shareclass;
    ArrayList<mParty> partydata = new ArrayList<mParty>();
    ArrayList<DropDownModel> UserData = new ArrayList<DropDownModel>();
    String Fadte="";
    String Todate="";
    String statusvalue ="";
    DropDownModel USER ,USER1;

    public DropDownModel getUSER() {
        return USER;
    }

    public void setUSER(DropDownModel user) {
        this.USER = user;
        if (iParty != null)
            iParty.onUserChanged(user);
    }

    public DropDownModel getUSER1() {
        return USER1;
    }

    public void setUSER1(DropDownModel user) {
        this.USER1 = user;
        if (iParty != null)
            iParty.onUser1Changed(user);
    }



    public String getViewBy() {
        return ViewBy;
    }

    public void setViewBy(String viewBy) {
        ViewBy = viewBy;
    }

    String ViewBy="";

    public String getFadte() {
        return Fadte;
    }

    public void setFadte(Date fadte) {
        Fadte = CustomDatePicker.formatDate(fadte, CustomDatePicker.CommitFormat);
        if (iParty != null)
            iParty.onFromDateChanged(fadte);
    }

    public String getTodate() {
        return Todate;
    }

    public void setTodate(Date todate) {
        Todate = CustomDatePicker.formatDate(todate, CustomDatePicker.CommitFormat);
        if (iParty != null)
            iParty.onToDateChanged(todate);
    }

    public String getStatusvalue() {
        return statusvalue;
    }

    public void setStatusvalue(String statusvalue) {
        this.statusvalue = statusvalue;
    }



    public  void GetUserList(Context context) {
        GetUserData((Activity)context);
    }

    private IParty iParty= null;
    public void setListener(IParty listener){
        iParty = listener;
    }

    private void GetUserData(Activity context) {
        shareclass =new Shareclass();

        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(1);


        new MyAPIService(context)
                .execute(new ResponseBuilder("FollowupOrder_ddl",request)
                        .setTables(tables)
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) {
                                if (iParty != null)
                                    iParty.onUserListChanged(UserData);
                            }

                            @Override
                            public void onResponse(Bundle response) {
                                parser1(response);

                            }


                        })
                );




    }

    public void parser1(Bundle result) {


        try {
            UserData.clear();

            String table1 = result.getString("Tables1");
            JSONArray jsonArray2 = new JSONArray(table1);
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject c = jsonArray2.getJSONObject(i);
                UserData.add(new DropDownModel(c.getString("USER_NAME"), c.getString("ID")));
            }
           } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public  void GetAllParty(Context context){
        GetData((Activity)context);
    }

  public void GetData(Activity context){

      shareclass =new Shareclass();
      HashMap<String, String> request = new HashMap<>();
      request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));
     request.put("iUserId",getUSER().getId());
     // request.put("iUserId","140");
      request.put("iStatus", ""+ getStatusvalue());
      request.put("sFDATE",getFadte() );
      request.put("sTDATE", getTodate());
      request.put("sDOC_TYPE",getViewBy());
      request.put("iUserId1" ,getUSER1().getId());
     // request.put("iUserId1" ,"140");


      new MyAPIService(context)
              .execute(new ResponseBuilder("ClientMainGrid",request)
                      .setResponse(new CBOServices.APIResponse() {
                          @Override
                          public void onComplete(Bundle message) {
                              if (iParty != null)
                                  iParty.onPartyListChanged(partydata);}

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
                partydata.clear();
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);


                   mParty rptModel= new mParty();

                     String Paid= c.getString("PA_ID");
                    rptModel.setId(Paid);
                    String name=c.getString("PA_NAME");
                    rptModel.setName(name);
                    String Mobile=c.getString("MOBILE");
                    rptModel.setMobile(Mobile);
                    String Person=c.getString("PERSON");
                    rptModel.setPerson(Person);
                    String Status=c.getString("STATUS");
                    rptModel.setStatus(Status);
                    String  LastRemark=c.getString("REMARK");
                    rptModel.setLastremark(LastRemark);
                    String  USER1=c.getString("USER1");
                    rptModel.setUser(USER1);
                    String  USER2=c.getString("USER2");
                    rptModel.setUser1(USER2);
                    partydata.add(rptModel);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}

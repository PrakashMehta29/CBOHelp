package com.example.pc24.cbohelp.PartyView;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;

import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.utils.Custom_Variables_And_Method;
import com.example.pc24.cbohelp.utils.MyAPIService;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Vm_Party extends ViewModel{
    ArrayList<mParty>mParties=null;
    Shareclass shareclass;
    OnResultlistener resultlistener;
    ArrayList<mParty> partydata = new ArrayList<mParty>();
    Custom_Variables_And_Method custom_variables_and_method;
    String Fadte="";
    String Todate="";
    String statusvalue ="";



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

    public void setFadte(String fadte) {
        Fadte = fadte;
    }

    public String getTodate() {
        return Todate;
    }

    public void setTodate(String todate) {
        Todate = todate;
    }

    public String getStatusvalue() {
        return statusvalue;
    }

    public void setStatusvalue(String statusvalue) {
        this.statusvalue = statusvalue;
    }

    public interface  OnResultlistener{
        void SucessResult(ArrayList<mParty>mParties);
        void ErrorResult(String Error, String Title);
    }

 public  void GetAllParty(Context context,OnResultlistener resultlistener){

         if(mParties!=null){
             resultlistener.SucessResult(mParties);
         }
         else {
             GetData((Activity)context,resultlistener);
         }


 }
  public void GetData(Activity context, OnResultlistener Listener){

      resultlistener=Listener;
      shareclass =new Shareclass();

      HashMap<String, String> request = new HashMap<>();
      request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));
      request.put("iUserId" ,shareclass.getValue(context,"PA_ID","0"));
      request.put("iStatus", ""+ getStatusvalue());
      request.put("sFDATE",getFadte() );
      request.put("sTDATE", getTodate());
      request.put("sDOC_TYPE",ViewBy);


      new MyAPIService(context)
              .execute(new ResponseBuilder("ClientMainGrid",request)
                      .setResponse(new CBOServices.APIResponse() {
                          @Override
                          public void onComplete(Bundle message) {
                              resultlistener.SucessResult(partydata);}

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
                    partydata.add(rptModel);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}

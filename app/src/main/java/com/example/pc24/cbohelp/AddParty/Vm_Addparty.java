package com.example.pc24.cbohelp.AddParty;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.pc24.cbohelp.Model.ComponeyType;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.services.CboServices_Old;
import com.example.pc24.cbohelp.utils.DropDownModel;
import com.example.pc24.cbohelp.utils.MyAPIService;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Vm_Addparty  extends ViewModel{
    ArrayList<mPartyField> mPartyFields = new ArrayList<mPartyField>();
    ArrayList<DropDownModel> componeydata = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> UserData = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel> UserData1 = new ArrayList<DropDownModel>();
    ArrayList<DropDownModel>dropDownModels=null;
    ArrayList<mPartyField>mPartyfield=null;
    OnResultListiner resultlistener;
    Context context;
    Shareclass  shareclass;



    public interface OnResultListiner{

        void OnSuccess(ArrayList<DropDownModel> componeydata,  ArrayList<DropDownModel> UserData, ArrayList<DropDownModel> UserData1);
        void OnError(String Message,String Error);
    }



public  void GetFollowupDDl(){

        if(dropDownModels!=null){
            resultlistener.OnSuccess(componeydata,UserData,UserData1);
        }

        else{

            GetData((Activity)context, componeydata,UserData,UserData1,resultlistener);
        }

    }

    private void GetData(Activity context, final ArrayList<DropDownModel> componeydata, ArrayList<DropDownModel> userData, ArrayList<DropDownModel> userData1, OnResultListiner Listener) {
        resultlistener=Listener;
        shareclass =new Shareclass();
        HashMap<String, String> request = new HashMap<>();
        request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);


        new MyAPIService(context)
                .execute(new ResponseBuilder("FollowupOrder_ddl",request)
                        .setTables(tables)
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) {
                           /*     componeydataCopy.addAll(componeydata);
                                UserData1.addAll(UserData);
                                ComponeyType.setText(ComponeyName);
                                UserData1.addAll(UserData);
                                User.setText(userName);
                                User.setPadding(1, 0, 5, 0);
                                User1.setText(userName);
                                User1.setPadding(1, 0, 5, 0);
                                Clientpoulate();*/

                                resultlistener.OnSuccess(componeydata,UserData,UserData1);
                            }

                            @Override
                            public void onResponse(Bundle response) {
                                parser1(response);

                            }


                        })
                );





    }

    public void parser1(Bundle result) {

        //dbHelper.deleteMenu();
        try {
            String table0 = result.getString("Tables0");
            JSONArray jsonArray1 = new JSONArray(table0);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject c = jsonArray1.getJSONObject(i);
                componeydata.add(new DropDownModel(c.getString("PA_NAME"), "0"));
            }

            // componeydataCopy.addAll(componeydata);

            if (componeydata.size() != 1) {


                ComponeyId = componeydata.get(0).getId();
                ComponeyName = componeydata.get(0).getName();



            }


            String table1 = result.getString("Tables1");
            JSONArray jsonArray2 = new JSONArray(table1);
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject c = jsonArray2.getJSONObject(i);
                UserData.add(new DropDownModel(c.getString("USER_NAME"), c.getString("ID")));
            }
            UserData.addAll(UserData1);
            // UserDataCopy.addAll(UserData);
            // UserData1.addAll(UserData);
            if (UserData.size() != 1) {
                userId = UserData.get(1).getId();
                userName = UserData.get(1).getName();

                userId1 = UserData.get(1).getId();


            }




        } catch (JSONException e) {
            Log.d("MYAPP", "objects are: " + e.toString());
            CboServices_Old.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
            e.printStackTrace();
        }

    }
}

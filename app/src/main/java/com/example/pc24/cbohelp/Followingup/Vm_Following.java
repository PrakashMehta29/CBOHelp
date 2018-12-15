package com.example.pc24.cbohelp.Followingup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;


import com.example.pc24.cbohelp.FollowUp.FollowupDialog;


import com.example.pc24.cbohelp.PartyView.mParty;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.utils.Custom_Variables_And_Method;
import com.example.pc24.cbohelp.utils.MyAPIService;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Vm_Following extends ViewModel {
    Context context;
    private ArrayList<mFollowupgrid> mFollowupgrids= null;



    private Custom_Variables_And_Method custom_variables_and_method;
    ArrayList<mFollowupgrid> followupdata = new ArrayList<>();
    OnResultlistner resultlistner=null;
    Shareclass shareclass;
    DBHelper dbHelper;
    ProgressDialog progress1=null;
    private  static final int FOLLOWUP_DIALOG=7;

    private  static final int FOLLOWUPGRID=1;

    com.example.pc24.cbohelp.PartyView.mParty mParty;
    IFollowingup iFollowingup=null;
    Integer NEXTFOLLOWUP=0;

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getViewby() {
        return "";//Viewby;
    }

    public void setViewby(String viewby) {
        Viewby = viewby;
    }

     String FromDate ="";
     String ToDate ="";
     String Viewby="";


    public mParty getParty() {
        return mParty;
    }

    public void setParty(mParty mParty) {
        this.mParty = mParty;
        if (iFollowingup != null){
            iFollowingup.updateparty(mParty);
        }

    }

    public void NextFollowupDialog(final Context context){
        Bundle bundle = new Bundle();
        bundle.putString("iId","0");
        bundle.putInt("iSrno", NEXTFOLLOWUP);
        bundle.putString("iPaid", getParty().getId());
        bundle.putString("header",getParty().getName());
        bundle.putString("sContactPerson",getParty().getPerson());
        bundle.putString("sContactNo",getParty().getMobile());
        bundle.putString("iUserId", shareclass.getValue(context,"PA_ID","0"));




        new FollowupDialog(context, bundle, FOLLOWUP_DIALOG,true,true, new FollowupDialog.IFollowupDialog() {
            @Override
            public void onFollowSubmit() {

                GETFOLLOWCALL(context,resultlistner);

            }


        }).show();



    }

    public Vm_Following() {

        super();

        custom_variables_and_method = Custom_Variables_And_Method.getInstance();
        dbHelper = new DBHelper(context);
        shareclass=new  Shareclass();

    }


    public  void  setListeners(Context context,IFollowingup iFollowingup){
        this.context=context;
        this .iFollowingup=iFollowingup;
    }
    public interface OnResultlistner{
        void Sucessresult(ArrayList<mFollowupgrid> mFollowupgrids);
        void ErrorResult(String Error, String Title);


    }
    public void GETFOLLOWCALL(Context context, OnResultlistner resultlistner){


        if(mFollowupgrids==null){
            getFollowdata( (Activity)context,resultlistner,iFollowingup);
        }else {
            resultlistner.Sucessresult(mFollowupgrids);
        }

    }
    public void getFollowdata(Activity context, OnResultlistner listner, IFollowingup ifollowingup)
    {




        resultlistner=listner;
        iFollowingup=ifollowingup;


        HashMap<String, String> request = new HashMap<>();

        request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));
        request.put("iPaId",getParty().getId());
        request.put("sFormType", "ORDER_STATUS_FOLLOWUP");
        request.put("sFDATE", getFromDate() );
        request.put("sTDATE", getToDate());
        request.put("sDOC_TYPE",getViewby());
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);



        new MyAPIService(context)
                .execute(new ResponseBuilder("FollowUpGrid",request)
                        .setTables(tables).setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) {
                                resultlistner.Sucessresult(followupdata);
                            }

                            @Override
                            public void onResponse(Bundle response) {
                                parser2(response);
                            }


                        })
                );




    }


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
                        mFollowupgrid mFollow = new mFollowupgrid();

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
                       // mFollow.setfOLLOWUPDATE(followdate);


                        mFollow.setfOLLOWUPDATE( followdate.substring(0,followdate.indexOf(" ")));

                        String user1name = c.getString("USER1_NAME");
                        mFollow.setuSER1NAME(convert(user1name));
                        String user = c.getString("USER_NAME");
                        mFollow.setuSERNAME(convert(user));
                        String nxtfollow = c.getString("NEXTFOLLOWUPDATE");
                        //mFollow.setnEXTFOLLOWUPDATE(nxtfollow);
                       mFollow.setnEXTFOLLOWUPDATE(nxtfollow.substring(0,nxtfollow.indexOf(" ")));


                        String contactPerson = c.getString("CONTACT_PERSON");
                        mFollow.setcONTACTPERSON(convert(contactPerson));
                        String freamrk = c.getString("FREMARK");mFollow.setfREMARK(freamrk);
                        followupdata.add(mFollow);




                    }


                    String Table1=result.getString("Tables1");
                    JSONArray jsonArray=new JSONArray(Table1);
                    for(int j=0;j< jsonArray.length();j++){
                        JSONObject b = jsonArray.getJSONObject(j);
                        NEXTFOLLOWUP = b.getInt("NEXTFOLLOWUP");
                    }
                    //resultlistner.Sucessresult(followupdata);
                   /* if (iFollowingup != null)
                        iFollowingup.updateparty(followupdata.size() > 0? followupdata.get(0) : new mFollowupgrid());*/
                    //progress1.dismiss();





                } catch (Exception e) {

                    //progress1.dismiss();
                    e.printStackTrace();
                }
            }
        }

    }


    static String convert(String str)
    {

        // Create a char array of given String
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }

}


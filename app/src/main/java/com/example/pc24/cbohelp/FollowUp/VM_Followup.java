package com.example.pc24.cbohelp.FollowUp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.pc24.cbohelp.PartyView.PartyActivity;
import com.example.pc24.cbohelp.PartyView.mParty;
import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.example.pc24.cbohelp.dbHelper.DBHelper;
import com.example.pc24.cbohelp.services.CboServices_Old;
import com.example.pc24.cbohelp.utils.Custom_Variables_And_Method;
import com.example.pc24.cbohelp.utils.SendMailTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VM_Followup extends ViewModel  {
  Context context;
  private  ArrayList<mFollowupgrid> mFollowupgrids= null;
  VM_Followup vm_followup;


  private  Custom_Variables_And_Method custom_variables_and_method;
    ArrayList<mFollowupgrid> followupdata = new ArrayList<>();
    OnResulyListner resulyListner=null;

    PartyActivity partyActivity=null;
    Shareclass shareclass;
    DBHelper dbHelper;
    ProgressDialog progress1=null;
    private  static final int FOLLOWUP_DIALOG=7;

    private  static final int FOLLOWUPGRID=1;

    mParty  mParty;
    IFollowup iFollowup=null;
    Integer NEXTFOLLOWUP=0;



    public mParty getParty() {
        return mParty;
    }

    public void setParty(mParty mParty) {
        this.mParty = mParty;
    }

    public void NextFollowupDialog(final Context context){

        Bundle bundle = new Bundle();


        bundle.putString("iId","0");
        bundle.putInt("iSrno", NEXTFOLLOWUP);
        bundle.putString("iPaId", getParty().getId());
        bundle.putString("header",getParty().getName());
        bundle.putString("sContactPerson",getParty().getPerson());
        bundle.putString("sContactNo",getParty().getMobile());
        bundle.putString("iUserId", shareclass.getValue(context,"PA_ID","0"));




        new FollowupDialog(context, bundle, FOLLOWUP_DIALOG,true,true, new FollowupDialog.IFollowupDialog() {
            @Override
            public void onFollowSubmit() {

                GETFOLLOWCALL(context,resulyListner);

            }
        }).show();



    }

    public VM_Followup() {
        super();
        custom_variables_and_method = Custom_Variables_And_Method.getInstance();
        dbHelper = new DBHelper(context);

    }


  public  void  setListener(IFollowup iFollowup){
        this .iFollowup=iFollowup;
  }
    public interface OnResulyListner{
        void Sucessresult(ArrayList<mFollowupgrid>mFollowupgrids);
        void ErrorResult(String Error,String Title);


    }

    public void GETFOLLOWCALL(Context context, OnResulyListner resulyListner){
        if(mFollowupgrids==null){
            getFollowdata( (Activity)context,resulyListner,iFollowup);
      }else {
          resulyListner.Sucessresult(mFollowupgrids);
      }

    }
    public void getFollowdata(Activity context, OnResulyListner listner,IFollowup ifollowup)
    {
        custom_variables_and_method = Custom_Variables_And_Method.getInstance();
        dbHelper= new DBHelper(context);
        shareclass=new  Shareclass();
        progress1=new ProgressDialog(context);

        resulyListner=listner;
        iFollowup=ifollowup;


        HashMap<String, String> request = new HashMap<>();

        request.put("sDbName", shareclass.getValue(context, "company_code", "demo"));
        request.put("iPaId", getParty().getId());
        request.put("sFormType", "ORDER_STATUS_FOLLOWUP");

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        tables.add(1);



       progress1.setMessage("Please Wait..\n" +" Fetching data");
       progress1.show();
        new CboServices_Old(context, mHandler).customMethodForAllServices(request, "FollowUpGrid", FOLLOWUPGRID, tables);



    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FOLLOWUPGRID:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        parser2(msg.getData());
                        //  parser1(msg.getData());

                    }
                    break;
                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                       // Toast.makeText(context, msg.getData().getString("Error"), Toast.LENGTH_SHORT).show();

                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

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

                        mFollow.setfOLLOWUPDATE( followdate.substring(0,followdate.indexOf(" ")));

                        String user1name = c.getString("USER1_NAME");
                        mFollow.setuSER1NAME(convert(user1name));
                        String user = c.getString("USER_NAME");
                        mFollow.setuSERNAME(convert(user));
                        String nxtfollow = c.getString("NEXTFOLLOWUPDATE");
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
                    resulyListner.Sucessresult(followupdata);
                    if (iFollowup != null)
                        iFollowup.updateparty(followupdata.size() > 0? followupdata.get(0) : new mFollowupgrid());
                    progress1.dismiss();





                } catch (Exception e) {
                    resulyListner.ErrorResult("Missing field error", e.toString());
                    Log.d("MYAPP", "objects are: " + e.toString());
                    CboServices_Old.getAlert(context,"Missing field error",e.toString());
                    List toEmailList = Arrays.asList("support@cboinfotech.com".split("\\s*,\\s*"));
                    new SendMailTask(context).execute("support@cboinfotech.com",
                            "Cbo12345",toEmailList , "Missing field error", e.toString());
                    progress1.dismiss();
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

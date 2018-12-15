package com.example.pc24.cbohelp.AddParty;

import android.os.Bundle;

import com.example.pc24.cbohelp.utils.DropDownModel;

import java.util.ArrayList;

public interface Isubmit {
    void onClientPopulate(ArrayList<mPartyField>  mPartyFields,String UserId,String UserId1);
    void  onComponydatta(ArrayList<DropDownModel> Componydata);
    void  UserData( ArrayList<DropDownModel> Userlist);




}

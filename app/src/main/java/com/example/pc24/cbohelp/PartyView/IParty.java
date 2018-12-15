package com.example.pc24.cbohelp.PartyView;

import com.example.pc24.cbohelp.utils.DropDownModel;

import java.util.ArrayList;
import java.util.Date;

interface IParty {
    void onUserChanged(DropDownModel user);
    void onUser1Changed(DropDownModel user);
    void onFromDateChanged(Date date);
    void onToDateChanged(Date date);
    void onUserListChanged(ArrayList<DropDownModel>  users);
    void onPartyListChanged(ArrayList<mParty>  parties);

}

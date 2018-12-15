package com.example.pc24.cbohelp.PartyView;

import android.content.Context;

public class mPartyContact {

    private  String number;
    private  String  Name;
    private  Context context;


    public mPartyContact(Context context,String number, String name) {
        this.number = number;
        Name = name;
        this.context=context;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

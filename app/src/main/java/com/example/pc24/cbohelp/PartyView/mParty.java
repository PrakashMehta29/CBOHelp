// Player.java
package com.example.pc24.cbohelp.PartyView;

import android.content.Context;

public class mParty {
  private  String id,Name,mobile,person,status,viewBy,FromDate,Todate;
Context context;

    public mParty(Context context) {
this.context=context;
    }
    public mParty(String Id,String name) {
        this.id=Id;
       this. Name = name;
    }


    public mParty(String Id, String name, String mobile, String person, String status) {
        Name = name;
        this.mobile = mobile;
        this.person = person;
        this.status = status;
        this.id=Id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}


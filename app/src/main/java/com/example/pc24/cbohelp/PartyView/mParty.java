// Player.java
package com.example.pc24.cbohelp.PartyView;

import android.content.Context;

public class mParty {
  private  String id;
    private String Name;
    private String mobile;
    private String person;
    private String status;
    private String User;
    private String User1;



    private String Lastremark;
Context context;

    public mParty() {

    }
    public mParty(String Id,String name) {
        this.id=Id;
       this. Name = name;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getUser1() {
        return User1;
    }

    public void setUser1(String user1) {
        User1 = user1;
    }

    public mParty(String Id, String name, String mobile, String person, String status) {
        Name = name;
        this.mobile = mobile;
        this.person = person;
        this.status = status;
        this.id=Id;

    }



    public mParty(String Id, String name, String mobile, String person, String status,String Lastremark,String User, String User1) {
        Name = name;
        this.mobile = mobile;
        this.person = person;
        this.status = status;
        this.id=Id;
        this.Lastremark=Lastremark;
        this.User= User;
        this.User1=User1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {

        return Name.trim();
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


    public String getLastremark() {
        return Lastremark;
    }

    public void setLastremark(String lastremark) {
        Lastremark = lastremark;
    }


}


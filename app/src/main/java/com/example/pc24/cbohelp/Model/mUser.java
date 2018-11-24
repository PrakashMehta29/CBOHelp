package com.example.pc24.cbohelp.Model;

public class mUser {

    private String USER_ID;
    private  String USer_NAME;

    public mUser( String USer_NAME ) {

        this.USer_NAME = USer_NAME;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSer_NAME() {
        return USer_NAME;
    }

    public void setUSer_NAME(String USer_NAME) {
        this.USer_NAME = USer_NAME;
    }
}

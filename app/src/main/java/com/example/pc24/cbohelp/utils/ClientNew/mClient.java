package com.example.pc24.cbohelp.utils.ClientNew;

public class mClient {


    public String name;
    public String Dept;

    public mClient(String name, String dept) {
        this.name = name;
        this.Dept = dept;
    }
    public mClient() {
    }


    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }

    public String getName() {

        return name;

    }

    public void setName(String name) {
        this.name = name;
    }
}

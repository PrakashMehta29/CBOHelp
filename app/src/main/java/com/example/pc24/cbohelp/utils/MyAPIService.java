package com.example.pc24.cbohelp.utils;

import android.content.Context;

import com.uenics.javed.CBOLibrary.CBOServices;

/**
 * Created by cboios on 24/11/18.
 */

public class MyAPIService extends CBOServices {


    private static String URL="http://www.cboservices.com/cbochat.asmx";

    public MyAPIService(Context context) {
        super(context, URL);
    }


}

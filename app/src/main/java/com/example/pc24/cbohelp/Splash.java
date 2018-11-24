package com.example.pc24.cbohelp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;


import com.example.pc24.cbohelp.appPreferences.Shareclass;

import java.io.File;

public class Splash extends AppCompatActivity {
    Shareclass shareclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        WebView gif= (WebView) findViewById(R.id.webview);
        gif.loadUrl("file:///android_asset/team.gif");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shareclass=new Shareclass();
                if (shareclass.getValue(Splash.this,"PA_ID","0").equals("0")){
                    startActivity(new Intent(Splash.this,Login.class));
                }else {
                    Intent intent = new Intent(Splash.this, Client_Complain_list.class);
                    intent.putExtra("pa_id", "");
                    intent.putExtra("name","ALL");
                    intent.putExtra("s1", "");
                    intent.putExtra("s2","");
                    intent.putExtra("who","party_list");
                    intent.putExtra("show_report","P");
                    intent.putExtra("DOC_NO","");
                    startActivity(intent);
                }

                finish();
            }
        }, 00);
    }
}

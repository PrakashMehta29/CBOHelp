package com.example.pc24.cbohelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pc24.cbohelp.PartyView.PartyActivity;
import com.example.pc24.cbohelp.appPreferences.Shareclass;

public class Dashboard extends AppCompatActivity {


      LinearLayout ChatLayout,FollowupLayout,payLayout,DevlopmentLayout;
      Button Logout,Exit;
      Shareclass shareclass;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashborad_menu);
        ChatLayout=(LinearLayout)findViewById(R.id.chatLayout);
        FollowupLayout=(LinearLayout)findViewById(R.id.FollowLayout);
        payLayout=(LinearLayout)findViewById(R.id.payLayout);
        DevlopmentLayout=(LinearLayout)findViewById(R.id.DevlopmentLayout);
        Logout=(Button)findViewById(R.id.logout) ;
        Exit=(Button)findViewById(R.id.back) ;

        shareclass=new Shareclass();

        if (getSupportActionBar() != null) {
          //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             getSupportActionBar().setTitle( "Welcome "+shareclass.getValue(this,"PA_NAME","0"));
        }


        ChatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*shareclass.save (Dashboard.this,"pa_id", "");
                shareclass.save (Dashboard.this,"name","ALL");
                shareclass.save (Dashboard.this,"s1", "");
                shareclass.save (Dashboard.this,"s2","");
                shareclass.save (Dashboard.this,"who","party_list");
                shareclass.save (Dashboard.this,"show_report","P");
                shareclass.save (Dashboard.this,"DOC_NO","");
                shareclass.save (Dashboard.this,"CLOSED_TAG","Y");
                */

                Intent intent=new Intent(Dashboard.this,Client_Complain_list.class);
                intent.putExtra("pa_id", "");
                intent.putExtra("name","ALL");
                intent.putExtra("s1", "");
                intent.putExtra("s2","");
                intent.putExtra("who","party_list");
                intent.putExtra("show_report","P");
                intent.putExtra("DOC_NO","");
                intent.putExtra ("CLOSED TAG","Y");


                startActivity(intent);
            }
        });


        FollowupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this,PartyActivity.class);
                intent.putExtra("headername", "Order Followup");
                startActivity(intent);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareclass.save(Dashboard.this,"PA_ID","0");
                startActivity(new Intent(Dashboard.this,Login.class));
                finish();
            }
        });
        DevlopmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Dashboard.this, "Under Devlopment", Toast.LENGTH_SHORT).show();
            }
        });
        payLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Dashboard.this, "Under Devlopment", Toast.LENGTH_SHORT).show();
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     finish();
    }

}


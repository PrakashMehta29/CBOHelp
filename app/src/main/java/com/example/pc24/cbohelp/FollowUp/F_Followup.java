package com.example.pc24.cbohelp.FollowUp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.pc24.cbohelp.R;


public class F_Followup extends Fragment {
    TextView Cperson,nextfollowupdate,followupdate,textView;
    CheckBox entrydate,followingdate;

    public F_Followup() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View retView = inflater.inflate(R.layout.fragment_f__followup, container, false);
         Cperson=(TextView)retView.findViewById(R.id.Conperson);
         followupdate=(TextView)retView.findViewById(R.id.followdate);
         nextfollowupdate=(TextView)retView.findViewById(R.id.nextfollowdate);
         entrydate=(CheckBox)retView.findViewById(R.id.entrydate);
         followingdate=(CheckBox)retView.findViewById(R.id.followingdate);

        return  retView;
    }



}

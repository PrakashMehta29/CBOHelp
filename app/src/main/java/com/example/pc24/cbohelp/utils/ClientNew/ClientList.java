package com.example.pc24.cbohelp.utils.ClientNew;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.pc24.cbohelp.R;

import java.util.ArrayList;
import java.util.List;

public class ClientList extends AppCompatActivity {

    private List<mClient> clientlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClientAdapter mAdapter;
    SwipeRevealLayout swipeRevealLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clienet_list);

        recyclerView = (RecyclerView) findViewById(R.id.rpt_clientlist);
        swipeRevealLayout=new SwipeRevealLayout(this);


        mAdapter = new ClientAdapter(clientlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();
    }

    private void prepareMovieData() {




            mClient mclient = new mClient("Call", "Message");
            clientlist.add(mclient);
        mclient = new mClient("Call", "Message");
        clientlist.add(mclient);
        mclient = new mClient("Call", "Message");
        clientlist.add(mclient);
        mclient = new mClient("Call", "Message");
        clientlist.add(mclient);
        mclient = new mClient("Call", "Message");
        clientlist.add(mclient);
        mclient = new mClient("Call", "Message");
        clientlist.add(mclient);
        mclient = new mClient("Call", "Message");
        clientlist.add(mclient);
            mAdapter.notifyDataSetChanged();




    }


}

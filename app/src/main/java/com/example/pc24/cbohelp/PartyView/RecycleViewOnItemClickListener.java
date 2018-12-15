package com.example.pc24.cbohelp.PartyView;

import android.view.View;

public interface RecycleViewOnItemClickListener {
    void onCallClick(View view,String number);
    void onClick(View view, int position, boolean isLongClick);
}

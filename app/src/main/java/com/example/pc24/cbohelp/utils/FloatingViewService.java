package com.example.pc24.cbohelp.utils;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc24.cbohelp.Client_Complain_list;
import com.example.pc24.cbohelp.R;
import com.example.pc24.cbohelp.Splash;

import java.util.Random;


public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    String company_name,remark,description,complain_type,DOC_NO,DONE_BY,TIME,PA_ID;
    TextView company,txt_remark,txt_description,txt_complain_type,txt_time,cir;
    Button openButton;
    String show_report="P";
    FloatingViewService context;

    public FloatingViewService() {
        super();
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flag,int startId) {
        super.onStartCommand(intent,flag, startId);
        if (intent.getStringExtra("title")!=null) {

            company_name = intent.getStringExtra("title");
            remark = intent.getStringExtra("msg");
            description = intent.getStringExtra("COMMENT");
            DOC_NO = intent.getStringExtra("DOC_NO");
            complain_type = intent.getStringExtra("COMPLANE_TYPE");
            DONE_BY = intent.getStringExtra("DONE_BY");
            TIME = intent.getStringExtra("TIME");
            PA_ID = intent.getStringExtra("PA_ID");

            if (intent.getStringExtra("WHO").equals("1")){
                openButton.setVisibility(View.GONE);
            }

            show_report = "P";
            if (intent.getStringExtra("status").toLowerCase().equals("complete")) {
                show_report = "C";
            }

            final Drawable drawable = cir.getBackground();
            Random rnd = new Random();
            final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
            drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

            if (show_report.equals("C")){
                color[0] = Color.argb(255,34,139,34);
                drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
            }else{
                color[0] = Color.argb(255,255,69,0);
                drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);
            }

            company.setText(company_name);
            txt_remark.setText(remark);

            if (description.equals("")) {
                txt_description.setVisibility(View.GONE);
            }
            txt_description.setText(DONE_BY + " :- " + description);
            txt_complain_type.setText(DOC_NO + " ( " + complain_type + " ) ");
            txt_time.setText(TIME);
            cir.setText(company_name.substring(0, 1).toUpperCase());
        }
        return START_STICKY;
    }


/*

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //company_name=intent.getStringExtra("Name");
    }
*/

    @Override
    public void onCreate() {
        super.onCreate();

        context=FloatingViewService.this;
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);

        /*//Set the close button
        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });*/

        //Set the close button
        Button closeButton = (Button) mFloatingView.findViewById(R.id.cancel);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });

        openButton = (Button) mFloatingView.findViewById(R.id.open);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FloatingViewService.this, Client_Complain_list.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("pa_id", "");
                intent.putExtra("name","ALL");
                intent.putExtra("s1", "");
                intent.putExtra("s2","");
                intent.putExtra("who","");
                intent.putExtra("show_report",show_report);
                intent.putExtra("DOC_NO",DOC_NO);
                context.startActivity(intent);
                //close the service and remove the from from the window
                stopSelf();
            }
        });

        company= (TextView) mFloatingView.findViewById(R.id.company_name);
        txt_remark= (TextView) mFloatingView.findViewById(R.id.description);
        txt_description= (TextView) mFloatingView.findViewById(R.id.mail_subto);
        txt_complain_type= (TextView) mFloatingView.findViewById(R.id.complain_type);
        txt_time= (TextView) mFloatingView.findViewById(R.id.mail_dateto);
        cir= (TextView) mFloatingView.findViewById(R.id.inbox_idto);
        //company.setText(company_name);



        /*//Set the close button
        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });


*/

        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }


    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }
}
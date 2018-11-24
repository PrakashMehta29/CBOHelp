package com.example.pc24.cbohelp.FollowUp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.pc24.cbohelp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FollowupoDialog extends Dialog {

  public Activity c;
  public Dialog d;
  public Button Submit, Cancel;
  Context context;
    Button nextfollowup_Date;
    ImageView spinner_img_nextfollowdate;

  public FollowupoDialog(Context a) {
    super(a);
    // TODO Auto-generated constructor stub

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.custom_dialog);
    //  setStyle(DialogFragment.STYLE_NO_FRAME, 0);

     Submit = (Button) findViewById(R.id.ok);
     Cancel = (Button) findViewById(R.id.cancel);
      spinner_img_nextfollowdate=(ImageView)findViewById(R.id.spinner_img_nextfollowdate);
      final Calendar myCalendar = Calendar.getInstance();

       nextfollowup_Date= (Button) findViewById(R.id.nextfollowdatebtn);
      SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yy" );
      nextfollowup_Date.setText( sdf.format( new Date() ));
     final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear,
                                int dayOfMonth) {
              // TODO Auto-generated method stub
              myCalendar.set(Calendar.YEAR, year);
              myCalendar.set(Calendar.MONTH, monthOfYear);
              myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
              updateLabel();
          }

         private void updateLabel() {

             String myFormat = "MM/dd/yy"; //In which you need put here
             SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

             nextfollowup_Date.setText(sdf.format(myCalendar.getTime()));
         }

     };
      nextfollowup_Date.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              new DatePickerDialog(getContext(), date, myCalendar
                      .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                      myCalendar.get(Calendar.DAY_OF_MONTH)).show();

          }
      });
      spinner_img_nextfollowdate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              new DatePickerDialog(getContext(), date, myCalendar
                      .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                      myCalendar.get(Calendar.DAY_OF_MONTH)).show();

          }
      });
      Submit.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
       dismiss();
       }
     });
     Cancel.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
          dismiss();
       }
     });

  }

}

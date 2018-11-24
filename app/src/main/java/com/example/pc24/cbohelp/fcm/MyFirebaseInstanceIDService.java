package com.example.pc24.cbohelp.fcm;

import android.util.Log;

import com.example.pc24.cbohelp.appPreferences.Shareclass;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by pc24 on 09/12/2016.
 */

//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private  static final int MESSAGE_INTERNET=1;
    private static final String TAG = "MyFirebaseIIDService";
    Shareclass shareclass;

    @Override
    public void onTokenRefresh() {
        shareclass=new Shareclass();

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
        shareclass.save(this,"FCM_key",token);


    }
}
package com.example.pc24.cbohelp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


public class DrRegistrationView extends AppCompatActivity {

    private WebView webView;
    String url;
    Intent intent;
    Context context;
    Bundle bundle;
    private ProgressDialog progressDialog;
    String menu_code="";
    private  static final int MESSAGE_INTERNET=1;
    int count=0;
    String previous_url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tp_pending_webview);
        context=this;
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bundle = getIntent().getExtras();
        if ((bundle != null) && ((bundle.getString("A_TP1")) != null)) {
            getSupportActionBar().setTitle(intent.getStringExtra("Title"));
            menu_code=bundle.getString("Menu_code");
            url = bundle.getString("A_TP1");
        }else {
            getSupportActionBar().setTitle(intent.getStringExtra("Title"));
            url = bundle.getString("addnew_chm_Url");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        webView = (WebView) findViewById(R.id.webview_tp);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new HelloWebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                //Calling an init method that tells the website, we're ready
                webView.loadUrl("javascript:m2Init()");

                if (url.contains("[BACK]") || previous_url.equals(url)){
                    webView.goBack();
                }else if (url.contains(".pdf")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    page11(webView);
                    previous_url=url;
                }

                progressDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                Toast.makeText(context, "error" + error,Toast.LENGTH_LONG).show();
            }
        });



       /* if (textView.getText().toString().equalsIgnoreCase("Salary Slip") && (url.toLowerCase().contains(".pdf"))){

            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);}
        else {*/
            if(!url.contains("http://") && !url.contains("emulated/0")){
                url="http://"+url;
            }else if(url.contains("emulated/0")){
                url="file:///"+url;
            }
            webView.loadUrl(url);
        //}


    }


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest  url) {
            view.loadUrl(url.toString());
            return true;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void page11(View view)
    {
        webView.loadUrl("javascript:m2LoadPage(1)");
    }


    @Override
    public void onBackPressed() {
            finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}



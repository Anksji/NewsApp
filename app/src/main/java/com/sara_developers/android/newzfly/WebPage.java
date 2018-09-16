package com.sara_developers.android.newzfly;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by ankit on 9/5/2017.
 */

public class WebPage extends AppCompatActivity {
    WebView web;
    static WebView mWebViw;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    private static int ItemPosition;
    private static int SubItemPosition;
    private static String Present_Link;
    private String NewsLinkPageURL;
    private static ProgressBar progressBar;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        else
            Toast.makeText(WebPage.this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webpage);

        web = (WebView) findViewById(R.id.webView);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);


        NewsLinkPageURL=getIntent().getStringExtra("news_url");

        //swipeLayout= (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        /*swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Insert your code here
                LoadInnerUrl(Present_Link);
                //LoadWebPage(ItemPosition,SubItemPosition); // refreshes the WebView
            }
        });*/

/*
        web.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d("WebView", "your current url when webpage loading.." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "your current url when webpage loading.. finish" + url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("when you click on any interlink on webview that time you got url :-" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });*/

        LoadInnerUrl(NewsLinkPageURL);
    }





    public boolean CheckConnection(){
        boolean isConnection;
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            isConnection=false;
        }else{
            isConnection=true;
        }
        return isConnection;
    }

    private void ShowDialog(){
        AlertDialog.Builder builderInner = new AlertDialog.Builder(WebPage.this);
        builderInner.setTitle("Please Open Internet Connection");

        builderInner.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderInner.show();
    }



    private void LoadInnerUrl(final String loadurl){
        /*web.setWebViewClient(new MyWebViewClient());
        */
        isPageCompletlyLoad=false;
        web.getSettings().setJavaScriptEnabled(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //web.loadDataWithBaseURL(loadurl,"text/html","utf-8","");

                //web.loadDataWithBaseURL(loadurl,"","text/html","utf-8","");
                web.loadUrl(loadurl);
                web.setWebChromeClient(new WebChromeClient() {
                    // For 3.0+ Devices (Start)
                    // onActivityResult attached before constructor

                    public void onProgressChanged(WebView view, int progress) {
                        progressBar.setProgress(progress);
                        if (progress == 100) {
                            progressBar.setVisibility(View.GONE);

                        }else{
                            progressBar.setVisibility(View.VISIBLE);

                        }

                    }

                    protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                        mUploadMessage = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("image/*");
                        startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
                    }


                    // For Lollipop 5.0+ Devices
                    public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                        if (uploadMessage != null) {
                            uploadMessage.onReceiveValue(null);
                            uploadMessage = null;
                        }

                        uploadMessage = filePathCallback;

                        Intent intent = fileChooserParams.createIntent();
                        try {
                            startActivityForResult(intent, REQUEST_SELECT_FILE);
                        } catch (ActivityNotFoundException e) {
                            uploadMessage = null;
                            Toast.makeText(WebPage.this, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        return true;
                    }

                    //For Android 4.1 only
                    protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                        mUploadMessage = uploadMsg;
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
                    }

                    protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                        mUploadMessage = uploadMsg;
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("image/*");
                        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                    }
                });
            }
        });

    }


    private final String googleDocs = "https://docs.google.com/viewer?url=";
    public static boolean isPageCompletlyLoad=false;



    @Override
    public void onBackPressed() {

        if (web.canGoBack()) {
            web.goBack();

        } else {
            super.onBackPressed();

        }
    }

}

package com.example.ab.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by ab on 2/2/15.
 */
public class WebviewActivity extends ActionBarActivity {

    private WebView webView;
    //private ImageView backButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

//        backButton = (ImageView) findViewById(R.id.webviewUpIcon);
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            // Called when user clicks the Show Map button
//            public void onClick(View v) {
//                finish();
//            }
//        });


        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        // To open links clicked by the user, simply provide a WebViewClient
        // for your WebView, using setWebViewClient()
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                //Toast.makeText(getApplicationContext(), "page load finished", Toast.LENGTH_SHORT).show();
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }

        });
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.isFocused() && webView.canGoBack()) {
            webView.goBack();
        }
        else {
            super.onBackPressed();
            finish();
        }
    }
}

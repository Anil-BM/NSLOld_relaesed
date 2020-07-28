package com.nsl.beejtantra.product_catalogue;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.nsl.beejtantra.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Product_Catlogue extends AppCompatActivity {


    ProgressBar progressBar;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__catlogue);

        progressDialog = new ProgressDialog(Product_Catlogue.this);

        progressDialog.setMessage("Loading....");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        progressDialog.setCancelable(false);

        progressDialog.setCanceledOnTouchOutside(false);


        progressBar = (ProgressBar) findViewById(R.id.progress_bar_ptc);
        WebView webView = new WebView(Product_Catlogue.this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
        webView.setWebViewClient(new Callback());
        String pdfURL = "http://nslbeejtantra.com/uploads/catalouge/2017%20GAPL%20VEG%20Catalogue.pdf";
        webView.loadUrl(
                "http://docs.google.com/gview?embedded=true&url=" + pdfURL);
        setContentView(webView);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {

            progressDialog.show();

            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {


            progressDialog.dismiss();

        }
    }

}

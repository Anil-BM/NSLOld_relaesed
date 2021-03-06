package com.nsl.beejtantra.products;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nsl.beejtantra.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class Activity_Webview1 extends AppCompatActivity  {
    private WebView webView;
    private ProgressBar progress;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__social_networking);
       /* Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.back);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });*/
        webView = (WebView) findViewById(R.id.webView);
        /*webView.setWebViewClient(new MyWebViewClient());

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);
        // get intent data
        Intent i = getIntent();

        // Selected image id
        String url = getIntent().getStringExtra("URL");
        String title = getIntent().getStringExtra("TITLE");
        toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Fonts/SEGOEWP.TTF");
        toolbarTitle.setTypeface(myTypeface);
        toolbarTitle.setText(""+title);*/

        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);

/*
        String htmlFilename = "NCS589Zordar.html";
        AssetManager mgr = getBaseContext().getAssets();
        try {
            InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
            String htmlContentInStringFormat = StreamToString(in);
            in.close();
            webView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);

        } catch (IOException e) {
            e.printStackTrace();
        }*/

         String url1 = "http://107.180.69.138/gokaldas_exports/";
        if (validateUrl(url1)) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadUrl(url1);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }
            });



new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        final String js = "javascript:" +
                "document.getElementById('user_email').value = '" + "admin@gokuldas.com" + "';"  +
                "document.getElementById('user_password').value = '" + "admin@gokuldas.com" + "';"  +
                //"document.getElementsByClassName('btn btn-success btn-submit').submit()";
               "document.getElementById('signin').submit()";

        if (Build.VERSION.SDK_INT >= 19) {
            webView.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    String result = s;

                }
            });
        } else {
            webView.loadUrl(js);
        }

    }
},1000*10);

        }
    }

    public static String StreamToString(InputStream in) throws IOException {
        if(in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private boolean validateUrl(String url) {
        return true;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("onPageFinished",url);
            progress.setVisibility(View.GONE);
            Activity_Webview1.this.progress.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("onPageFinished","onPageStarted "+url);
            progress.setVisibility(View.VISIBLE);
            Activity_Webview1.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }



    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }
}





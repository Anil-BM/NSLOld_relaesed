package com.nsl.beejtantra.products;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class Activity_Webview extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progress;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__social_networking);
       // Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);

       // mToolbar.setNavigationIcon(R.drawable.back);

      /*  mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

        /* String url1 = "file:///android_asset/Dashboard.html";
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
        }*/

        String htmlS="<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n" +
                "    <title>NSL</title>\n" +
                "    <link href=\"file:///android_asset/css/base.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "    <link href=\"file:///android_asset/css/bootstrap.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "    <link href=\"file:///android_asset/css/bootstrap-select.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "    <link href=\"file:///android_asset/css/font-awesome.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i\" rel=\"stylesheet\"> \n" +
                "  </head>\n" +
                "  <body>\n" +
                "<div class=\"main_wrapper\">\n" +
                "    <!-- Content Wrapper. Contains page content -->\n" +
                "  <div class=\"container\">\n" +
                "    <!-- Main content -->\n" +
                "    <section class=\"content\">\n" +
                "    <div class=\"col-sm-12\">\n" +
                "    <div class=\"row catelogo_header\">\n" +
                "    <div class=\"col-sm-12  text-center\">\n" +
                "    <h2 class=\"title_cat\">Mallika - <span>NCS 207 BG II</span></h2>\n" +
                "\t<a href=\"#\" onclick=\"window.history.go(-1)\"><i class=\"fa fa-angle-left\"></i></a>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "    <!--<div class=\"col-sm-4  text-center\">\n" +
                "    <img src=\"images/mallikansc207bg11.png\" class=\"img-responsive\" alt=\"mallikansc207bg11\">\n" +
                "    </div>\n" +
                "\t-->\n" +
                "        <div class=\"col-sm-8\">\n" +
                "\t <div class=\"table-responsive\">\n" +
                "    <table class=\"table table-bordered\">\n" +
                "    <thead>\n" +
                "      <tr>\n" +
                "        <th  colspan=\"2\">Morphological Characters</th>\n" +
                "      </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td>Hybrid Character:</td>\n" +
                "        <td>Mallika BG II(NCS-207 BG II)</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>Plant Height:</td>\n" +
                "        <td>Very tall</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>Reaction to Major Pests:</td>\n" +
                "        <td>Resistant to American\n" +
                "Bollworm,<br/> Pink Bollworm,<br/>\n" +
                "Spotted Bollworm and\n" +
                "Spodoptera.<br/> Tolerant to\n" +
                "sucking pests,<br/> Jassids and\n" +
                "Whitefly</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>Boll Shape & Size:</td>\n" +
                "        <td>Ovate and very large</td>\n" +
                "      </tr>  \n" +
                "       <tr>\n" +
                "        <td>Quality of Produce: </td>\n" +
                "        <td>Ginning%: 35 - 36<br/>\n" +
                "Staple length: > 32.5 mm<br/>\n" +
                "Strength: 21 - 24 g/tex<br/>\n" +
                "Micronaire: 3.0 - 3.9</td>\n" +
                "      </tr>\n" +
                "       <tr>\n" +
                "        <td  colspan=\"2\">\n" +
                "        <h2>Special Features/USPs:</h2>\n" +
                "        <ul>\n" +
                "        <li>Best Hybrid suitable for various soil types</li>\n" +
                "        <li>Big bolls and higher boll weight</li>\n" +
                "        <li>Sucking pests tolerant</li>\n" +
                "        <li>Easy picking and Good rejuvenation ability.</li>\n" +
                "        </ul>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table></div></div>\n" +
                "    </section>\n" +
                "  </div>\n" +
                "  <!-- End- Content Wrapper. Contains page content -->\n" +
                "</div>\n" +
                "  </body>\n" +
                "  <script src=\"file:///android_asset/js/jquery.js\" type=\"text/javascript\"></script>\n" +
                "  <script src=\"file:///android_asset/js/bootstrap.js\" type=\"text/javascript\"></script>\n" +
                "  <script src=\"file:///android_asset/js/bootstrap-select.js\" type=\"text/javascript\"></script>\n" +
                "</html>";

        webView.loadDataWithBaseURL("file:///android_asset/", htmlS, "text/html", "utf-8", null);
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
            Activity_Webview.this.progress.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("onPageFinished","onPageStarted "+url);
            progress.setVisibility(View.VISIBLE);
            Activity_Webview.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }



    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }
}





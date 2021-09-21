package chat.app.web.sozialnmedien;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import chat.app.web.sozialnmedien.R;
import chat.app.web.sozialnmedien.WebAppInterface;

public class MainActivity extends Activity {
    
    private WebView wv;
    protected static Context context;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.main);
        // requesting permissions
        if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == -1) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1000);
        }
        // initialise Webview
        this.wv = (WebView) findViewById(R.id.wv);
        this.wv.getSettings().setJavaScriptEnabled(true);
        this.wv.addJavascriptInterface(new WebAppInterface(this), "Android");
        this.wv.getSettings().setSupportZoom(true);
        this.wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        this.wv.setScrollbarFadingEnabled(true);
        this.wv.getSettings().setLoadWithOverviewMode(true);
        this.wv.getSettings().setUseWideViewPort(false);
        this.wv.getSettings().setAllowContentAccess(true);
        this.wv.getSettings().setAllowFileAccess(true);
        this.wv.getSettings().setDatabaseEnabled(true);
        this.wv.getSettings().setDomStorageEnabled(true);
        
        // adding the WebChromeClient
        this.wv.setWebChromeClient(new WebChromeClient());
        
        // custom Webview client
        this.wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().getHost() != null
                 && !request.getUrl().getHost().contains("sozialnmedien.web.app")
                 && !request.getUrl().getHost().contains("sozialnmedien.firebaseapp.com")
                 && !request.getUrl().getHost().contains("localhost")) {
                    // the link is not for a page on my site, so launch another Activity that handles URLs
                    Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    MainActivity.this.startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        
        // load webpage
        this.wv.loadUrl("http://localhost:5000");
    }
    
    // on back button pressed
    public void onBackPressed() {
        if (this.wv.canGoBack()) {
            this.wv.goBack();
        } else {
            finish();
        }
    }
}

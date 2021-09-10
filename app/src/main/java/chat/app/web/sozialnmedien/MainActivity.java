package chat.app.web.sozialnmedien;

import android.app.Activity;
import android.os.Bundle;
import android.os.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;

import chat.app.web.sozialnmedien.R;
import chat.app.web.sozialnmedien.WebAppInterface;

public class MainActivity extends Activity {
    
    private WebView wv;
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == -1) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1000);
        }
        // initialise Webview
        this.wv = (WebView) findViewById(R.id.wv);
        this.wv.getSettings().setJavaScriptEnabled(true);
        this.wv.addJavascriptInterface(new WebAppInterface(this), "Android");
        this.wv.getSettings().setSupportZoom(true);
        this.wv.setScrollbarFadingEnabled(true);
        this.wv.getSettings().setLoadWithOverviewMode(true);
        this.wv.getSettings().setUseWideViewPort(false);
        this.wv.getSettings().setAllowContentAccess(true);
        this.wv.getSettings().setAllowFileAccess(true);
        this.wv.getSettings().setDatabaseEnabled(true);
        this.wv.getSettings().setDomStorageEnabled(true);
        
        // custom Webview client
        this.wv.setWebViewClient(new MyWebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if ("sozialnmedien.web.app".equalsIgnoreCase(request.getUrl().getHost()) ||
                    "sozialnmedien.firebaseapp.com".equalsIgnoreCase(request.getUrl().getHost())) {
                    // This is my website, so do not override; let my WebView load the page
                    return false;
                }
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                MainActivity.this.startActivity(intent);
                return true;
            }
        });
        
        // load webpage
        this.wv.loadUrl("https://sozialnmedien.web.app/chat");
    }
    
    // on back button pressed
    public void onBackPressed() {
        if (this.wv.canGoBack()) {
            this.wv.goBack();
        } else {
            finish();
        }
    }
    
    /* Access modifiers changed, original: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }
}

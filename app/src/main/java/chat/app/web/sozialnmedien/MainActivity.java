package chat.app.web.sozialnmedien;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

// other classes
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
        this.wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (!request.getUrl().getHost().contains("sozialnmedien.web.app") &&
                    !request.getUrl().getHost().contains("sozialnmedien.firebaseapp.com")) {
                    // the link is not for a page on my site, so launch another Activity that handles URLs
                    Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    MainActivity.this.startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        
        // load webpage
        this.wv.loadUrl("https://sozialnmedien.web.app/chat");

        this.wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimeType);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("File download started...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                // ignored since API 29
                request.allowScanningByMediaScanner();
                // Notify client once download is completed!
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                // To notify the client that the file is being downloaded
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });
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

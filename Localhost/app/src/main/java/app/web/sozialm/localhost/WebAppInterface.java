package app.web.sozialm.localhost;

import android.text.ClipboardManager;
import android.content.Context;
import android.app.DownloadManager;
import android.os.Environment;
import java.io.IOException;
import android.webkit.JavascriptInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.Scanner;
import android.widget.Toast;
import android.net.Uri;
import java.net.URL;

public class WebAppInterface {
    
    Context context;
    
    public WebAppInterface(Context c) {
        context = c;
    }
    
    @JavascriptInterface
    public boolean isSozialMWebapp() {
        return true;
    }
    
    @JavascriptInterface
    public String updateAvailable() {
        int verCode = 0;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            verCode = pInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "failed";
        }
        int currentVerCode = 0;
        try {
            URL url = new URL("https://sozialm.web.app/records/currentapkversion");
            Scanner sc = new Scanner(url.openStream());
            currentVerCode = sc.nextInt();
            sc.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return "failed";
        }
        return currentVerCode > verCode ? "true" : "false";
    }
    
    @JavascriptInterface
    public void download(String url, String filename) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url)); 
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                       DownloadManager.Request.NETWORK_MOBILE);
        // set title and description
        request.setTitle(filename);
        request.setDescription("Source: " + url);
        // ignored by API 29, deprecated before 29
        request.allowScanningByMediaScanner();
        // on download complete notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // set the local destination for download file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
    }
    
    @JavascriptInterface
    public void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
    }
    
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }
}

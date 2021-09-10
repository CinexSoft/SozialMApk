package chat.app.web.sozialnmedien;

import android.content.Context;
import android.text.ClipboardManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;
    
    public WebAppInterface(Context c) {
        mContext = c;
    }
    
    @JavascriptInterface
    public boolean isSozialnMedienWebapp() {
        return true;
    }
    
    @JavascriptInterface
    public void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
    }
    
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}

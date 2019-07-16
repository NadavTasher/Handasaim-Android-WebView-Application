package nadav.tasher.handasaim;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.*;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup WebView
        WebView webView = new WebView(this);
        // Setup cookies
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        // Setup client
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (!request.getUrl().toString().startsWith(getString(R.string.app_url))) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(request.getUrl());
                    startActivity(intent);
                } else {
                    view.loadUrl(request.getUrl().toString());
                }
                return true;
            }
        });
        // Setup Javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // Setup app cache
        webView.getSettings().setAppCacheEnabled(true);
        // Load url
        webView.loadUrl(getString(R.string.app_url));
        // Set content view
        setContentView(webView);
        // Cookie flush timer - essential
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                CookieManager.getInstance().flush();
            }
        }, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Flush cookies one last time
        CookieManager.getInstance().flush();
    }
}

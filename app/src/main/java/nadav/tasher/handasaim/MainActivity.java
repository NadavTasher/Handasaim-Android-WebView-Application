package nadav.tasher.handasaim;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
        // Setup Javascript theme interface
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void colors(final String colorTop, final String colorBottom) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Set recents color
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                setTaskDescription(new ActivityManager.TaskDescription(getString(R.string.app_name), R.drawable.ic_launcher_foreground, Color.parseColor(colorTop)));
                            } else {
                                setTaskDescription(new ActivityManager.TaskDescription(getString(R.string.app_name), BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground), Color.parseColor(colorTop)));
                            }
                            // Set statusbar color
                            getWindow().setStatusBarColor(Color.parseColor(colorTop));
                            // Set navigationbar color
                            getWindow().setNavigationBarColor(Color.parseColor(colorBottom));
                        } catch (Exception ignored) {
                        }
                    }
                });
            }
        }, "android");
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

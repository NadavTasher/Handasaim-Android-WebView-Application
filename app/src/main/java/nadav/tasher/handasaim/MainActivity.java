package nadav.tasher.handasaim;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(getApplicationContext());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(request.getUrl());
                getApplicationContext().startActivity(intent);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://handasaim.app");
        setContentView(webView);
    }
}

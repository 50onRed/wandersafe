package com.fifty.wdrsfe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fifty.wdrsfe.R;
import com.fifty.wdrsfe.service.WandersafeService;

public class Main extends Activity {

    private WebView mWebView;

    private final static String MAP_URL = "http://www.wandersafe.com";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make sure the service is started
        startService(new Intent(this, WandersafeService.class));

        mWebView = new WebView(this);
        mWebView.loadUrl(MAP_URL);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        this.setContentView(mWebView);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

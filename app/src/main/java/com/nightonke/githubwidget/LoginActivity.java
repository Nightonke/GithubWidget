package com.nightonke.githubwidget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends AppCompatActivity {

    private WebView webView;
    private boolean oldUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.login_to_github);

        webView = findView(R.id.webview);
        webView.loadUrl(Util.getString(R.string.login_url));
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString) {
                String str = CookieManager.getInstance().getCookie(paramAnonymousString);
                if ((str != null) && (str.split(";")[0].equals("logged_in=yes"))) {
                    LoginActivity.this.finish();
                }
            }
        });

        oldUserLogin = Util.getLoggedIn();
    }

    private <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {

        if (oldUserLogin != Util.getLoggedIn()) {
            Util.showToast(R.string.refreshing);
            Intent intent = new Intent();
            intent.setAction(Actions.CLICK_AVATAR);
            sendBroadcast(intent);
        }

        super.finish();
    }
}

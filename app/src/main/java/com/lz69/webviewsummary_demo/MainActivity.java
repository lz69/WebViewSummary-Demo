package com.lz69.webviewsummary_demo;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bnUseJs;
    private LinearLayout llWebview;
    private WebView webView;

    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private void assignViews() {
        bnUseJs = (Button) findViewById(R.id.bn_use_js);
        llWebview = (LinearLayout) findViewById(R.id.ll_webview);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        setListeners();
        addWebView();
        setWebViewSettings();
        setWebViewUrl();
        setWebViewClient();
        setWebChromeClient();
        addJavascriptInterface();
        setWebContentsDebuggingEnabled();
        setCache();
    }

    private void setCache() {
        //设置缓存模式
//        if(Utils.checkNetworkAvailable(this)) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        } else {
//            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        }
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
        //设置数据库缓存路径
        webView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能，默认为关闭
        webView.getSettings().setAppCacheEnabled(true);
        //设置大小
        webView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024);
    }

    private void setWebContentsDebuggingEnabled() {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void jsUseAndroid(String tip) {
            Toast.makeText(MainActivity.this, "js调用android代码:" + tip, Toast.LENGTH_LONG).show();
        }
    }

    private void addJavascriptInterface() {
        webView.addJavascriptInterface(new JavascriptInterface(), "webfunction");
    }

    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void setWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    private void setWebViewUrl() {
        webView.loadUrl("http://www.baidu.com");
    }

    private void setWebViewSettings() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void addWebView() {
        webView = new WebView(getApplicationContext());
        llWebview.addView(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void setListeners() {
        bnUseJs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_use_js:
                webView.loadUrl("javascript:usedByAndroid('I am from Android!')");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        llWebview.removeAllViews();
        webView.destroy();
        webView = null;
        webView.loadUrl(null);
        llWebview = null;
        //清除缓存
//        webView.clearCache(true);
        System.exit(0);
        super.onDestroy();
    }
}

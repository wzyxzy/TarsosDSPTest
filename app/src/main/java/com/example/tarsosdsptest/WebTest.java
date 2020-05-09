package com.example.tarsosdsptest;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebTest extends AppCompatActivity {

    private WebView webview;
    private WebSettings mWebSettings;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_test);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("JavascriptInterface")
    private void initView() {
        webview = (WebView) findViewById(R.id.webview);
        mWebSettings = webview.getSettings();

        mWebSettings.setJavaScriptEnabled(true);
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名

        webview.addJavascriptInterface(this, "android");//AndroidtoJS类对象映射到js的test对象


        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowFileAccessFromFileURLs(true);
        mWebSettings.setAllowUniversalAccessFromFileURLs(true);
        // 是否允许缩放
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                String url = "";
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    url = request.getUrl().toString();
//                } else {
//                    url = request.toString();
//                }
////                if (url.startsWith("file://") || url.contains("yinji"))
//                Logger.d(url);
//                    view.loadUrl(url);
////                else
////                    ActivityUtils.showToast(WorkWebActivity.this, "非音基应用选项，不可操作！");
                return false;
            }
        });
        webview.loadUrl("https://0110.be/phd/presentation/spectrogram/live.html");

    }



}

package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fleamarket.R;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;

import java.net.URISyntaxException;
import java.util.List;

public class AnalyzeWebActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private WebView webView;
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
            try {
                //处理intent协议
                if (newurl.startsWith("intent://")) {
                    Intent intent;
                    try {
                        intent = Intent.parseUri(newurl, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                            intent.setSelector(null);
                        }
                        List<ResolveInfo> resolves = context.getPackageManager().queryIntentActivities(intent, 0);
                        if (resolves.size() > 0) {
                            startActivityIfNeeded(intent, -1);
                        }
                        Log.e("12","12");
                        return true;
                    } catch (URISyntaxException e) {
                        Log.e("12","121232");
                        e.printStackTrace();
                    }
                }
                // 处理自定义scheme协议
                if (!newurl.startsWith("http")) {
                    Log.e("12122", "" + newurl);
                    try {
                        // 以下固定写法
                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(newurl));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        ToastEx.warning(AnalyzeWebActivity.this, "打开的第三方").show();
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        e.printStackTrace();
                        ToastEx.warning(AnalyzeWebActivity.this, "您所打开的第三方App未安装!").show();
                    }
                    return true;
                }
            } catch (Exception e) {
                Log.e("1212","12122");
                e.printStackTrace();
            }

            return super.shouldOverrideUrlLoading(view, newurl);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.d(TAG, "======onReceivedError: ");
            super.onReceivedError(view, request, error);
            if (view.getUrl().equals("https://www.google.com/")) {
                Toast.makeText(AnalyzeWebActivity.this, "国内无法访问国外的网站", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {// 可能涉及重定向导致再次调用此方法
            Log.d(TAG, "========onPageStarted: " + url);
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);//！！====少了这一句加载国外网站需要等待很久，加上立马显示不能访问google
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "========onPageFinished: ");
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    };

    private ProgressBar progressBar;


    @Override
    protected int initLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.web_back);
        webView = findViewById(R.id.web_webview);
        progressBar = findViewById(R.id.web_progressbar);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);//主要是这句
        webSettings.setJavaScriptEnabled(true);//启用js
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.supportMultipleWindows();
        webSettings.setAllowContentAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);

        webView.setWebChromeClient(new WebChromeClient());//这行最好不要丢掉
        //该方法解决的问题是打开浏览器不调用系统浏览器，直接用webview打开

        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url);
    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.web_back:
                finish();
                break;
        }

    }
}

package com.njupt.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.njupt.R;

public class GuideWeb extends Activity {

    private WebView webView;

    private ImageButton left;
    private ImageButton right;
    private ImageView close;

    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

        setContentView(R.layout.activity_guide_web);

        findView();

        webViewInit();

        ViewOnClick();
    }

    private void findView() {
        webView = (WebView) findViewById(R.id.web_webview_main);
        left = (ImageButton) findViewById(R.id.web_button_left);
        right = (ImageButton) findViewById(R.id.web_button_right);
        close = (ImageView) findViewById(R.id.web_imageButton_close);
        titleText = (TextView) findViewById(R.id.web_textView_title);
    }

    private void ViewOnClick() {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goForward();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @SuppressLint("SetJavaScriptEnabled")
    private void webViewInit() {
        String urlString = getIntent().getStringExtra("URL");
//WebView加载web资源
        webView.loadUrl(urlString);
        //设置WebView属性，能够执行java_script脚本
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    titleText.setText(title);
                }
            }
        });
    }
}

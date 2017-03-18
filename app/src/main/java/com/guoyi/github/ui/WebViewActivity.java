package com.guoyi.github.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.bean.GithubResponse;
import com.guoyi.github.utils.FavoReposHelper;
import com.guoyi.github.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WebViewActivity extends BaseAcitvity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.action_favos)
    ImageView actionFavos;
    @InjectView(R.id.title)
    TextView title;


    private GithubResponse githubDATA;

    public static void startCurActivity(Context context, GithubResponse data) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("GithubDATA", data);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        githubDATA = (GithubResponse) getIntent().getSerializableExtra("GithubDATA");
        if (githubDATA != null) {
            title.setText(githubDATA.name);
            setWebSettingStr();
        } else {
            finish();
        }

        if (FavoReposHelper.getInstance().contains(githubDATA)) {
            actionFavos.setImageResource(R.mipmap.ic_star_checked);
        } else {
            actionFavos.setImageResource(R.mipmap.ic_star_unchecked);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @OnClick(R.id.action_favos)
    public void Favort(View v) {
        if (FavoReposHelper.getInstance().contains(githubDATA)) {
            FavoReposHelper.getInstance().removeFavo(githubDATA);
            githubDATA.sendRxBus(Constants.FAVORT_DELETE);
            actionFavos.setImageResource(R.mipmap.ic_star_unchecked);
        } else {
            FavoReposHelper.getInstance().addFavo(githubDATA);
            githubDATA.sendRxBus(Constants.FAVORT_ADD);
            actionFavos.setImageResource(R.mipmap.ic_star_checked);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_openweb:
                Uri uri = Uri.parse(githubDATA.url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, this.getPackageName());
                startActivity(intent);
                return true;
            case R.id.action_copy:
                StringUtils.copyText(githubDATA.url, this);
                Toast.makeText(this, R.string.done_copy, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web, menu);
        return true;
    }

    @SuppressLint("AddJavascriptInterface")
    public void setWebSettingStr() {

        WebSettings settings = webview.getSettings();
        //setPluginsEnabled(true);  //支持插件
        //webview webSettings.setBuiltInZoomControls(true); //设置支持缩放
        settings.setAllowFileAccess(true);//设置可以访问文件

        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.setSupportZoom(true); //支持缩放

        settings.setJavaScriptEnabled(true);  //支持js
        settings.setJavaScriptCanOpenWindowsAutomatically(true); ////支持通过JS打开新窗口

        //设置进度条
        webview.setWebChromeClient(new MyWebChromeClient());

        //webView 点击连接如何不让跳转到系统的 浏览器
        webview.setWebViewClient(new MyWebViewClient());
        webview.loadUrl(githubDATA.url);
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
            finish();
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }
    }

}

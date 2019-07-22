package com.example.niubilityapp;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.just.agentweb.AgentWeb;
import com.kongzue.baseframework.base.BaseActivity;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.SwipeBack;
import com.kongzue.baseframework.util.activityParam.JumpParameter;

import java.util.HashMap;
import java.util.Map;

@Layout(R.layout.activity_web)
@SwipeBack(true)
@DarkStatusBarTheme(true)
public class WebActivity extends BaseActivity {

    private AgentWeb mAgentWeb;
    public static final String KEY_URL = "URL";

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
    }

    @Override
    public void initViews() {
        ViewGroup webContent = findViewById(R.id.web_content);
        String url = getIntent().getStringExtra(KEY_URL);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(webContent,
                        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
//                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(client)
                .createAgentWeb()
                .ready()
                .go(url);

        mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true);
        //支持缩放
        mAgentWeb.getAgentWebSettings().getWebSettings().setSupportZoom(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setBuiltInZoomControls(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setDisplayZoomControls(false);
    }

    /**
     * 完善去广告的功能
     */
    static {
        loadedUrls = new HashMap<>();
    }

    static Map<String, Boolean> loadedUrls;
    WebViewClient client = new WebViewClient() {

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Log.i("@@@@", "url : "+url);
            //做广告拦截，ADFIlterTool 为广告拦截工具类
            if (!AdBlocker.hasAd(getBaseContext(),url)){
                return super.shouldInterceptRequest(view, url);
            }else {
                Log.i("@@@@", "拦截");
                return new WebResourceResponse(null,null,null);
            }
        }
    };

    @Override
    public void initDatas(JumpParameter paramer) {

    }

    @Override
    public void setEvents() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAgentWeb != null) {
            mAgentWeb.destroy();
            mAgentWeb.getWebLifeCycle().onDestroy();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}

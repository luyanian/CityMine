package cn.soft_x.citymine.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maverick.utils.Cfg;
import com.maverick.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.citymine.R;
import cn.soft_x.citymine.utils.Constant;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.web)
    WebView webView;
    //    private ProgressDialog dialog;
    private String webUrl = "http://www.baidu.com";
    /**
     * 搜索内容
     */
    private String searchInfo = "";
    /**
     * 搜索类型
     */
    private String searchType = "";
    /**
     * 附近拆解企业id
     */
    private String nearCompanyId = "";
    /**
     * 货车id
     */
    private String truckId = "";
    /**
     * 新闻id
     */
    private String newsId = "";
    /**
     * 关联id
     */
    private String glId = "";

    /**
     * 订单类型
     */
    private String ddlx = "";
    /**
     * 混合id
     */
    private String hhid = "";
    /**
     * 采购id
     */
    private String cgId = "";
    /**
     * 客户要加的底部导航的标记
     */
    private String mIndex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        initIntent();
        initWebView();
        showProgressDialog("正在加载...");
    }


    @Override
    protected void initBaseTitle() {
        getRlTitleRoot().setVisibility(View.GONE);
    }

    private void initWebView() {
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsInterFace(), "jsinterface");
        //启动缓存
        webView.getSettings().setAppCacheEnabled(true);
        //设置缓存模式
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //貌似不可信
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissProgressDialog();
            }


        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(webUrl);
    }

    private void initIntent() {
        Intent intent = getIntent();
        webUrl = intent.getStringExtra(Constant.WEB_URL);
        nearCompanyId = intent.getStringExtra(Constant.WEB_NEAR_ID);
        truckId = intent.getStringExtra(Constant.WEB_TRUCK_ID);
        searchInfo = intent.getStringExtra(Constant.WEB_SEARCH);
        searchType = intent.getStringExtra(Constant.WEB_SEARCH_TYPE);
        newsId = intent.getStringExtra(Constant.WEB_NEWS_ID);
        glId = intent.getStringExtra(Constant.WEB_GLID);
        ddlx = intent.getStringExtra(Constant.WEB_DDLX);
        cgId = intent.getStringExtra(Constant.WEB_CGID);
        hhid = intent.getStringExtra(Constant.WEB_HUNHEID);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != webView) {
            ViewParent viewParent = webView.getParent();
            if (viewParent instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) viewParent;
                parent.removeView(webView);
                webView.destroy();
            }
        }
        webUrl = "";
        nearCompanyId = "";
        truckId = "";
        searchInfo = "";
        searchType = "";
        newsId = "";
        glId = "";
        ddlx = "";
        hhid = "";
        cgId = "";
        super.onDestroy();
    }

    private void callPhone(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel://" + phone));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.REQUEST_PERMISSION_CALL_PHONE_CODE) {
            if (grantResults.length >= 1) {
                int cameraResult = grantResults[0];//相机权限
                boolean cameraGranted = cameraResult == PackageManager.PERMISSION_GRANTED;//拍照权限
                if (cameraGranted) {
                    //具有电话权限
                    callPhone(phone);
                } else {
                    //不具有相关权限，给予用户提醒，比如Toast或者对话框，让用户去系统设置-应用管理里把相关权限开启
                    ToastUtil.showToast(this, "电话权限未设置，可能出现使用异常！请去“设置-应用管理”中设置照相权限");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String phone = "";


    public class JsInterFace {
        /**
         * 关闭界面
         */
        @JavascriptInterface
        public void doFinish() {
            WebViewActivity.this.finish();
        }

        /**
         * 得到userId
         *
         * @return
         */
        @JavascriptInterface
        public String getUserID() {
            if (!TextUtils.isEmpty(Constant.USER_ID)) {
                return Constant.USER_ID;
            } else {
                if (Cfg.loadStr(WebViewActivity.this, Constant.SH_USER_ID).equals("defaults")
                        || TextUtils.isEmpty(Cfg.loadStr(WebViewActivity.this, Constant.SH_USER_ID))) {
                    return "我没有USER_ID,别找我要啦！";
                } else {
                    return Cfg.loadStr(WebViewActivity.this, Constant.SH_USER_ID);
                }
            }
        }

        /**
         * 打电话
         *
         * @param phone
         */
        @JavascriptInterface
        public void goCallPhone(String phone) {
            WebViewActivity.this.phone = phone;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WebViewActivity.this, new String[]{Manifest.permission.CALL_PHONE}, Constant.REQUEST_PERMISSION_CALL_PHONE_CODE);
                } else {
                    callPhone(phone);
                }
            } else {
                callPhone(phone);
            }

        }

        @JavascriptInterface
        public String getSearchInfo() {
            return searchInfo;
        }

        @JavascriptInterface
        public String getNearCompanyId() {
            if (TextUtils.isEmpty(nearCompanyId)) {
                return "没有东西";
            } else {
                return nearCompanyId;
            }
        }

        @JavascriptInterface
        public String getTruckId() {
            if (TextUtils.isEmpty(truckId)) {
                return "没有东西";
            } else {
                return truckId;
            }
        }

        @JavascriptInterface
        public String getSearchType() {
            return searchType;
        }

        @JavascriptInterface
        public String getNewsId() {
            if (TextUtils.isEmpty(newsId)) {
                return "没有东西";
            } else {
                return newsId;
            }
        }

        /**
         * 设置
         */
        public static final String SETTING_AC = "1";
        /**
         * 附近的拆解企业
         */
        public static final String NEAR_COMPANY_AC = "2";
        /**
         * 邀请函 区别于供货商
         */
        public static final String INVITATION_AC = "3";

        /**
         * 跳转的activity
         *
         * @param type
         */
        @JavascriptInterface
        public void goActivity(String type) {
            Intent intent = null;
            switch (type) {
                case SETTING_AC:
                    intent = new Intent(WebViewActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case NEAR_COMPANY_AC:
                    intent = new Intent(WebViewActivity.this, NearCompanyActivity.class);
                    startActivity(intent);
                    break;
                case INVITATION_AC:
                    intent = new Intent(WebViewActivity.this, InvitationActivity.class);
                    startActivity(intent);
                    break;
            }
        }

        @JavascriptInterface
        public void showToastMsg(String toast) {
            ToastUtil.showToast(WebViewActivity.this, toast);
        }

        @JavascriptInterface
        public String getUserType() {
            return "dhs";
        }


        @JavascriptInterface
        public String getGLID() {
            if (TextUtils.isEmpty(glId)) {
                return "没有东西";
            } else {
                return glId;
            }
        }

        @JavascriptInterface
        public String getDDLX() {
            if (TextUtils.isEmpty(ddlx)) {
                return "没有东西";
            } else return ddlx;
        }

        @JavascriptInterface
        public String getHHID() {
            if (TextUtils.isEmpty(hhid)) {
                return "没有东西";
            } else return hhid;
        }

        /**
         * 得到采购id
         *
         * @return
         */
        @JavascriptInterface
        public String getCGID() {
            if (TextUtils.isEmpty(cgId)) {
                return "没有东西";
            } else {
                return cgId;
            }
        }

        @JavascriptInterface
        public String showBackButton(){
            return "show";
        }

    }


}

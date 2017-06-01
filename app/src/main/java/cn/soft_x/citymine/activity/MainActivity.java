package cn.soft_x.citymine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.citymine.R;
import cn.soft_x.citymine.application.CityMineApplication;
import cn.soft_x.citymine.fragment.HomeFragment;
import cn.soft_x.citymine.fragment.MessageFragment;
import cn.soft_x.citymine.fragment.WebFragment1;
import cn.soft_x.citymine.fragment.WebFragment2;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.activity_main_bottom_bar)
    public BottomNavigationBar mainBottomBar;

    private MessageFragment mMsgFragment;
    private HomeFragment mHomeFragment;
    private WebFragment1 mWebFragment1;//订单
    private WebFragment2 mWebFragment2;//采购

    public int fragmentIndex = 0;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            CityMineApplication.getInstance().exitApp();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //        mFragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottomBar();
        if (null == mHomeFragment) {
            mHomeFragment = new HomeFragment();
        }
        if (!mHomeFragment.isAdded())
            mFragmentManager.beginTransaction().add(R.id.fragment_container, mHomeFragment).commit();

    }

    Bundle bundle = null;

    private void initIntent() {
        Intent intent = getIntent();
        int index = -1;
        index = intent.getIntExtra("index", 0);
        boolean isFromReceiver = intent.getBooleanExtra("boolean", false);
        if (isFromReceiver)
            fragmentIndex = index;
        bundle = intent.getBundleExtra("bundle");
        Logger.i("index->%d", index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initIntent();
        mainBottomBar.selectTab(fragmentIndex);
        onTabSelected(fragmentIndex);
//        if (fragmentIndex==3){
//            getRlTitleRoot().setVisibility(View.VISIBLE);
//            setTitleText("消息");
//            Logger.i("onResume fragmentIndex->%d  VISIBLE ", fragmentIndex);
//        }else {
//            getRlTitleRoot().setVisibility(View.GONE);
//            Logger.i("onResume fragmentIndex->%d  GONE ", fragmentIndex);
//        }
//        Logger.i("onResume fragmentIndex->%d", fragmentIndex);

    }

    private void initBottomBar() {
        mainBottomBar.setActiveColor(R.color.color_app_theme)
                .setInActiveColor(R.color.color_home_bottom)
                .setBarBackgroundColor(R.color.color_app_white);
        mainBottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        mainBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mainBottomBar.addItem(new BottomNavigationItem(R.drawable.bottom_home, "首页"))
                //                .addItem(new BottomNavigationItem(R.drawable.bottom_huoche, "货车"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_dingdan, "订单"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_caigou, "采购"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_msg, "消息"))
                .initialise();
        mainBottomBar.setTabSelectedListener(this);
    }

    @Override
    protected void initBaseTitle() {
        getRlTitleRoot().setVisibility(View.GONE);
    }


    @Override
    public void onTabSelected(int position) {
        fragmentIndex = position;
        Logger.i(fragmentIndex + "<-");
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (null == mHomeFragment) {
                    mHomeFragment = new HomeFragment();
                    if (!mHomeFragment.isAdded())
                        transaction.add(R.id.fragment_container, mHomeFragment);
                } else
                    transaction.show(mHomeFragment);
//                getRlTitleRoot().setVisibility(View.GONE);
                break;
            case 1:
                mWebFragment1 = new WebFragment1();
                if (!mWebFragment1.isAdded()) {
                    transaction.add(R.id.fragment_container, mWebFragment1);
                }
                transaction.show(mWebFragment1);
//                getRlTitleRoot().setVisibility(View.GONE);
                //                Intent intent1 = new Intent(this, WebViewActivity.class);
                //                intent1.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/alldingdan.html");
                //                startActivity(intent1);
                //                fragmentIndex = 0;
                break;
            case 2:
                mWebFragment2 = new WebFragment2();
                if (!mWebFragment2.isAdded()) {
                    transaction.add(R.id.fragment_container, mWebFragment2);
                }
                transaction.show(mWebFragment2);
//                getRlTitleRoot().setVisibility(View.GONE);
                //                Intent intent2 = new Intent(this, WebViewActivity.class);
                //                intent2.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou.html");
                //                startActivity(intent2);
                //                fragmentIndex = 0;
                break;
            case 3:
                if (null == mMsgFragment) {
                    mMsgFragment = new MessageFragment();
                    if (!mMsgFragment.isAdded())
                        transaction.add(R.id.fragment_container, mMsgFragment);
                    if (null != bundle) {
                        mMsgFragment.setArguments(bundle);
                    }
                } else
                    transaction.show(mMsgFragment);
//                getRlTitleRoot().setVisibility(View.VISIBLE);
                break;
            //            case 4:
            //                if (null == mMsgFragment) {
            //                    mMsgFragment = new MessageFragment();
            //                    if (!mMsgFragment.isAdded())
            //                        transaction.add(R.id.fragment_container, mMsgFragment);
            //                } else
            //                    transaction.show(mMsgFragment);
            //                if (null != bundle) {
            //                    mMsgFragment.setArguments(bundle);
            //                }
            //                getRlTitleRoot().setVisibility(View.VISIBLE);
            //                break;
            default:
                return;

        }
        transaction.commitAllowingStateLoss();
        Logger.i("onTabSelected fragmentIndex->%d", fragmentIndex);
    }

    @Override
    public void onTabUnselected(int position) {

    }


    @Override
    public void onTabReselected(int position) {

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != mHomeFragment) {
            transaction.hide(mHomeFragment);
        }
        if (null != mMsgFragment) {
            transaction.hide(mMsgFragment);
        }
        if (null != mWebFragment1) {
            transaction.hide(mWebFragment1);
        }
        if (null != mWebFragment2) {
            transaction.hide(mWebFragment2);
        }

    }
}

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
import android.view.View;
import android.widget.TextView;

import com.maverick.utils.Cfg;
import com.maverick.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.citymine.R;
import cn.soft_x.citymine.http.HttpUrl;
import cn.soft_x.citymine.utils.Constant;

public class WoActivity extends BaseActivity {

    @BindView(R.id.wo_name)
    TextView woName;
    @BindView(R.id.wo_quanbudingdan)
    TextView woQuanbudingdan;
    @BindView(R.id.wo_weiwancheng)
    TextView woWeiwancheng;
    @BindView(R.id.wo_yiwancheng)
    TextView woYiwancheng;
    @BindView(R.id.wo_tousu)
    TextView woTousu;
    @BindView(R.id.wo_near)
    TextView woNear;
    @BindView(R.id.wo_guaqi)
    TextView woGuaqi;
    @BindView(R.id.wo_jushou)
    TextView woJushou;
    @BindView(R.id.wo_fabu)
    TextView woFabu;
    @BindView(R.id.wo_yaoqinghan)
    TextView woYaoqinghan;
    @BindView(R.id.wo_lianxi)
    TextView woLianxi;
    @BindView(R.id.wo_huoche)
    TextView woHuoche;
    @BindView(R.id.wo_zhuce)
    TextView woZhuce;
    @BindView(R.id.wo_shezhi)
    TextView woShezhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wo);
        ButterKnife.bind(this);
        woName.setText(Cfg.loadStr(this,Constant.SH_USER_NC));
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("我");
        setTitleLeftVisibility(true);
    }

    @OnClick({R.id.title_bar_left, R.id.wo_quanbudingdan, R.id.wo_weiwancheng, R.id.wo_yiwancheng, R.id.wo_tousu, R.id.wo_near, R.id.wo_guaqi, R.id.wo_jushou, R.id.wo_fabu, R.id.wo_yaoqinghan, R.id.wo_lianxi, R.id.wo_huoche, R.id.wo_zhuce, R.id.wo_shezhi})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.wo_quanbudingdan:
                // 全部订单
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST+"/s/page/alldingdan.html");
                intent.putExtra(Constant.WEB_DDLX,"");
                startActivity(intent);
                break;
            case R.id.wo_weiwancheng:
                // 未完成订单
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST+"/s/page/dingdanlist.html");
                intent.putExtra(Constant.WEB_DDLX,"2");
                startActivity(intent);
                break;
            case R.id.wo_yiwancheng:
                //已完成订单
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST+"/s/page/dingdanlist.html");
                intent.putExtra(Constant.WEB_DDLX,"5");
                startActivity(intent);
                break;
            case R.id.wo_tousu:
                // 投诉
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST+"/s/page/tousu.html");
                startActivity(intent);
                break;
            case R.id.wo_near:
                // 附近的拆解企业
                intent = new Intent(this,NearCompanyActivity.class);
                startActivity(intent);
                break;
            case R.id.wo_guaqi:
                // 挂起
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST+"/s/page/dingdanlist.html");
                intent.putExtra(Constant.WEB_DDLX,"3");
                startActivity(intent);
                break;
            case R.id.wo_jushou:
                // 拒收
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST+"/s/page/dingdanlist.html");
                intent.putExtra(Constant.WEB_DDLX,"1");
                startActivity(intent);
                break;
            case R.id.wo_fabu:
                // 发布
                ToastUtil.showToast(this,"没有权限操作！");
                break;
            case R.id.wo_yaoqinghan:
                // 邀请函
                intent = new Intent(this,InvitationActivity.class);
                startActivity(intent);
                break;
            case R.id.wo_lianxi:
                // 联系客服
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(WoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, Constant.REQUEST_PERMISSION_CALL_PHONE_CODE);
                    } else {
                        callPhone("4000660567");
                    }
                } else {
                    callPhone("4000660567");
                }
                break;
            case R.id.wo_huoche:
                // 货车
                ToastUtil.showToast(this,"没有权限操作！");
                break;
            case R.id.wo_zhuce:
                // 注册
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST+"/s/page/zhucexinxi-dhs.html");
                intent.putExtra(Constant.WEB_DDLX,"2");
                startActivity(intent);
                break;
            case R.id.wo_shezhi:
                // 设置
                intent = new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
        }
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
                    callPhone("4000660567");
                } else {
                    //不具有相关权限，给予用户提醒，比如Toast或者对话框，让用户去系统设置-应用管理里把相关权限开启
                    ToastUtil.showToast(this, "电话权限未设置，可能出现使用异常！请去“设置-应用管理”中设置照相权限");
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}

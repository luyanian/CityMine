package cn.soft_x.citymine.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.maverick.utils.Bimp;
import com.maverick.utils.EditTextUtils;
import com.maverick.utils.FormatUtils;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.citymine.R;
import cn.soft_x.citymine.http.HttpUrl;
import cn.soft_x.citymine.http.MyXUtilsCallBack;
import cn.soft_x.citymine.utils.Constant;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 调货商注册
 * 注册
 */
public class RegisterActivity extends BaseActivity implements OnGetGeoCoderResultListener, AdapterView.OnItemSelectedListener {


    @BindView(R.id.register_ed_name)
    EditText registerEdName;
    @BindView(R.id.register_ed_phone)
    EditText registerEdPhone;
    @BindView(R.id.register_ed_pwd1)
    EditText registerEdPwd1;
    @BindView(R.id.register_ed_pwd2)
    EditText registerEdPwd2;
    @BindView(R.id.register_ed_address)
    EditText registerEdAddress;
    @BindView(R.id.register_ed_emergency)
    EditText registerEdEmergency;
    @BindView(R.id.register_ed_id_card)
    EditText registerEdIdCard;
    @BindView(R.id.ac_auth_left_iv)
    ImageView acAuthLeftIv;
    @BindView(R.id.ac_auth_right_iv)
    ImageView acAuthRightIv;
    @BindView(R.id.register_ed_ver)
    EditText registerEdVer;
    @BindView(R.id.register_btn_get_ver)
    Button registerBtnGetVer;
    @BindView(R.id.register_do_register)
    Button registerDoRegister;
    @BindView(R.id.register_checkbox)
    CheckBox registerCheckbox;
    @BindView(R.id.register_tv_read)
    TextView registerTvRead;
    @BindView(R.id.register_tv_agreement)
    TextView registerTvAgreement;
    @BindView(R.id.register_sp)
    Spinner registerSp;
    @BindView(R.id.register_ed_city)
    EditText registerEdCity;
    // 调货商不需要勾选货物
    private boolean isLeft = true;
    private static File LEFT_IMG = null, RIGHT_IMG = null;
    private ArrayList<String> imgPath = new ArrayList<>();

    private final static int REQUEST_IMAGE_LEFT = 0x001;
    private final static int REQUEST_IMAGE_RIGHT = 0x002;

    private static final String TAG = "RegisterActivity";

    private GeoCoder mSearch = null;

    private String[] citys;

    private CountDownTimer countDownTimer = new CountDownTimer(120000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (registerBtnGetVer != null) {
                registerBtnGetVer.setClickable(false);//防止重复点击
                registerBtnGetVer.setText(millisUntilFinished / 1000 + "s重发");
                registerBtnGetVer.setBackgroundResource(R.drawable.bg_yellow_btn_enable);
            }
        }

        @Override
        public void onFinish() {
            if (registerBtnGetVer != null) {
                registerBtnGetVer.setClickable(true);//防止重复点击
                registerBtnGetVer.setText(R.string.get_ver);
                registerBtnGetVer.setBackgroundResource(R.drawable.bg_yellow_btn);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText(R.string.register_detail);
        setTitleLeftVisibility(true);
    }

    private void initView() {
        citys = getResources().getStringArray(R.array.city);
        registerEdIdCard.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        registerSp.setOnItemSelectedListener(this);
    }


    @OnClick({R.id.title_bar_left, R.id.ac_auth_left_iv, R.id.ac_auth_right_iv,
            R.id.register_btn_get_ver, R.id.register_do_register, R.id.register_tv_read, R.id.register_tv_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.ac_auth_left_iv:
                isLeft = true;
                checkTakePhotoPermission(REQUEST_IMAGE_LEFT);
                break;
            case R.id.ac_auth_right_iv:
                isLeft = false;
                checkTakePhotoPermission(REQUEST_IMAGE_RIGHT);
                break;
            case R.id.register_btn_get_ver:
                getVerCode();
                break;
            case R.id.register_do_register:
                if (isReadyToRegister()) {
                    showProgressDialog("正在上传信息，请稍候...");
                    getLatLng();
                }
                break;
            case R.id.register_tv_read:
                registerCheckbox.setChecked(!registerCheckbox.isChecked());
                break;
            case R.id.register_tv_agreement:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/zhucetk.html");
                startActivity(intent);
                break;

        }
    }

    /**
     * 得到经纬度
     */
    private void getLatLng() {
        mSearch.geocode(new GeoCodeOption().city(EditTextUtils.getEdText(registerEdCity))
                .address(EditTextUtils.getEdText(registerEdAddress)));
    }

    private void checkTakePhotoPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.REQUEST_PERMISSION_CAMERA_CODE);
            } else {
                takePhoto(requestCode);
            }
        } else {
            takePhoto(requestCode);
        }

    }

    private void takePhoto(int requestCode) {
        imgPath.clear();
        MultiImageSelector.create(this).showCamera(true) // 是否显示相机. 默认为显示
                .single() // 单选模式
                .origin(imgPath) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(this, requestCode);
    }

    private void getVerCode() {
        if (EditTextUtils.isEmpty(registerEdPhone)) {
            ToastUtil.showToast(this, "手机号不能为空！");
            return;
        }else if (!FormatUtils.checkPhone(EditTextUtils.getEdText(registerEdPhone))){
            ToastUtil.showToast(this, "手机号格式不对！");
            return;
        }
        countDownTimer.start();
        showProgressDialog("正在获取验证码...");
        RequestParams params = new RequestParams(HttpUrl.GET_YZM);
        params.addBodyParameter("dhhm", EditTextUtils.getEdText(registerEdPhone));
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {

            }

            @Override
            public void finished() {
                dismissProgressDialog();
            }
        });
    }

    private void doRegister(double latitude, double longitude) {
        //        {"yhtype":"1","shebei":"0","brxm":"韩雪","dhhm":"18522815693","password":"111111",
        // "zz":"北京市雪花区","jjlxfs":"13898986969","sfzh":"8526963695685265","ggpz":"空调|冰箱","yzm":5492}
        RequestParams params = new RequestParams(HttpUrl.REGISTER);
        params.addBodyParameter("file1", LEFT_IMG);
        params.addBodyParameter("file2", RIGHT_IMG);
        params.addBodyParameter("yhtype", 0 + "");
        params.addBodyParameter("shebei", "0");
        params.addBodyParameter("brxm", EditTextUtils.getEdText(registerEdName));
        params.addBodyParameter("dhhm", EditTextUtils.getEdText(registerEdPhone));
        params.addBodyParameter("password", EditTextUtils.getEdText(registerEdPwd1));
        params.addBodyParameter("zz", citys[spinnerIndex] + EditTextUtils.getEdText(registerEdCity) + EditTextUtils.getEdText(registerEdAddress));
        params.addBodyParameter("jjlxfs", EditTextUtils.getEdText(registerEdEmergency));
        params.addBodyParameter("sfzh", EditTextUtils.getEdText(registerEdIdCard));
        params.addBodyParameter("yzm", EditTextUtils.getEdText(registerEdVer));
        params.addBodyParameter("latitude", String.valueOf(latitude));
        params.addBodyParameter("longitude", String.valueOf(longitude));
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                if (resCode.equals("0")) {
                    ToastUtil.showToast(RegisterActivity.this, "信息已经提交，请等待审核通过！");
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                }
            }
        });
    }

    private boolean isReadyToRegister() {
        if (EditTextUtils.isEmpty(registerEdName)) {
            ToastUtil.showToast(this, "姓名不能为空！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdPhone)) {
            ToastUtil.showToast(this, "手机号不能为空！");
            return false;
        } else if (!FormatUtils.checkPhone(EditTextUtils.getEdText(registerEdPhone))) {
            ToastUtil.showToast(this, "手机号格式不对！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdPwd1)) {
            ToastUtil.showToast(this, "密码不能为空！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdPwd2)) {
            ToastUtil.showToast(this, "重复密码不能为空！");
            return false;
        } else if (!EditTextUtils.getEdText(registerEdPwd2).equals(EditTextUtils.getEdText(registerEdPwd1))) {
            ToastUtil.showToast(this, "两次密码输入不一致！");
            return false;
        } else if (spinnerIndex == 0) {
            ToastUtil.showToast(this, "请选择所在地的省(直辖市)！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdCity)) {
            ToastUtil.showToast(this, "请输入所在城市！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdAddress)) {
            ToastUtil.showToast(this, "住址不能为空！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdIdCard)) {
            ToastUtil.showToast(this, "身份证号不能为空！");
            return false;
        } else if (!FormatUtils.checkIDCard(EditTextUtils.getEdText(registerEdIdCard))) {
            ToastUtil.showToast(this, "身份证号码格式不对！");
            return false;
        }else if (null == LEFT_IMG || null == RIGHT_IMG) {
            ToastUtil.showToast(this, "请上传身份证正面和反面的照片！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdVer)) {
            ToastUtil.showToast(this, "请输入验证码！");
            return false;
        } else if (!registerCheckbox.isChecked()) {
            ToastUtil.showToast(this, "请同意用户协议！");
            return false;
        } else if (!FormatUtils.checkPhone(EditTextUtils.getEdText(registerEdEmergency))) {
            ToastUtil.showToast(this, "紧急联系方式手机号码格式不对！");
            return false;
        } else if (EditTextUtils.getEdText(registerEdPhone).equals(EditTextUtils.getEdText(registerEdEmergency))) {
            ToastUtil.showToast(this, "紧急联系人与注册号码不能一致！");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_LEFT) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                LEFT_IMG = new File(path.get(0));
                Bitmap bitmap = Bimp.getimage(path.get(0));
                acAuthLeftIv.setImageBitmap(bitmap);
            }
        } else if (requestCode == REQUEST_IMAGE_RIGHT) {
            if (resultCode == RESULT_OK) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                RIGHT_IMG = new File(path.get(0));
                Bitmap bitmap = Bimp.getimage(path.get(0));
                acAuthRightIv.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.REQUEST_PERMISSION_CAMERA_CODE) {
            if (grantResults.length >= 2) {
                int cameraResult = grantResults[0];//相机权限
                int sdcardResult = grantResults[1];

                boolean cameraGranted = cameraResult == PackageManager.PERMISSION_GRANTED;//拍照权限
                boolean sdcardGranted = sdcardResult == PackageManager.PERMISSION_GRANTED;
                if (cameraGranted && sdcardGranted) {
                    //具有拍照权限，调用相机
                    if (isLeft) takePhoto(REQUEST_IMAGE_LEFT);
                    else takePhoto(REQUEST_IMAGE_RIGHT);
                } else {
                    //不具有相关权限，给予用户提醒，比如Toast或者对话框，让用户去系统设置-应用管理里把相关权限开启
                    ToastUtil.showToast(this, "相关权限未设置，可能出现使用异常！请去“设置-应用管理”中设置相关权限");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RegisterActivity.this, "抱歉，无法获取正确地址！请修改后重试！", Toast.LENGTH_LONG)
                    .show();
            dismissProgressDialog();
            return;
        }
        Logger.i("纬度：%f 经度：%f", geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude);
        doRegister(geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        super.onDestroy();
    }

    private int spinnerIndex = 0;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerIndex = position;
        switch (position) {
            case 1:
                registerEdCity.setText(citys[1]);
                registerEdCity.setEnabled(false);
                break;
            case 2:
                registerEdCity.setText(citys[2]);
                registerEdCity.setEnabled(false);
                break;
            case 3:
                registerEdCity.setText(citys[3]);
                registerEdCity.setEnabled(false);
                break;
            case 4:
                registerEdCity.setText(citys[4]);
                registerEdCity.setEnabled(false);
                break;
            default:
                registerEdCity.setEnabled(true);
                registerEdCity.setText("");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

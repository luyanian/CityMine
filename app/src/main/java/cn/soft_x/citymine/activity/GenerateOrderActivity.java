package cn.soft_x.citymine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.citymine.R;
import cn.soft_x.citymine.http.HttpUrl;
import cn.soft_x.citymine.http.MyXUtilsCallBack;
import cn.soft_x.citymine.model.TruckItemModel;
import cn.soft_x.citymine.model.XiaoshourenyuanModel;
import cn.soft_x.citymine.utils.Constant;
@Deprecated
public class GenerateOrderActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.order_spinner)
    Spinner orderSpinner;
    @BindView(R.id.order_sure)
    Button orderSure;
    @BindView(R.id.order_dp)
    DatePicker orderDp;

    private ArrayAdapter mAdapter;

    private List<String> strings = new ArrayList<>();

    private ArrayList<TruckItemModel> data = new ArrayList<>();
    private String id = "";
    private XiaoshourenyuanModel model;
    private int spinnerIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_order);
        ButterKnife.bind(this);
        initIntent();
        initData();
    }

    private void initIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        data = (ArrayList<TruckItemModel>) bundle.getSerializable("model");
        id = bundle.getString("id");
        Logger.i("id->%s data->%s", id, data.toString());

    }


    private void initData() {
        showProgressDialog("正在加载...");
        RequestParams params = new RequestParams(HttpUrl.XSRY);
        x.http().post(params, new MyXUtilsCallBack() {

            @Override
            public void success(String result) {
                model = JSON.parseObject(result, XiaoshourenyuanModel.class);
                for (XiaoshourenyuanModel.ListBean bean :
                        model.getList()) {
                    strings.add(bean.getName());
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()) {
                    mAdapter = new ArrayAdapter(GenerateOrderActivity.this, android.R.layout.simple_list_item_1, strings);
                    orderSpinner.setAdapter(mAdapter);
                    orderSpinner.setOnItemSelectedListener(GenerateOrderActivity.this);
                }

            }
        });
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("生成订单");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Logger.i(strings.get(position));
        spinnerIndex = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick({R.id.title_bar_left, R.id.order_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.order_sure:
                readyForUpload();
                break;
        }
    }

    private void readyForUpload() {
        //        {"appyhid":"用户ID",
        // "gwcids":"购物车组(多个购物车id用|分割)","id":"锁单ID",
        // "yjdcsj":"预计到厂时间","sxrymc":"销售人员","sxryid":"销售人员Id"}
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String currentTime = formatter.format(curDate);
        String choseTime = orderDp.getYear() + "-" + (orderDp.getMonth() + 1) + "-" + orderDp.getDayOfMonth();
        Date choseDate;
        Logger.i("currentTime->%s,choseTime->%s", currentTime, choseTime);

        try {
            choseDate = formatter.parse(choseTime);
            Logger.i("curDate->" + curDate.getTime() + ",choseTime->" + choseDate.getTime());
            if (currentTime.equals(choseTime)) {
                doUpload(choseTime);
                return;
            } else if (curDate.getTime() > choseDate.getTime()) {
                ToastUtil.showToast(this, "送达时间选择异常！");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        doUpload(choseTime);
    }

    private void doUpload(String choseTime) {
        showProgressDialog("正在生成订单...");
        RequestParams params = new RequestParams(HttpUrl.SCDD);
        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("id", id);
        params.addBodyParameter("gwcids", getGWCids());
        params.addBodyParameter("yjdcsj", choseTime);
        params.addBodyParameter("sxrymc", strings.get(spinnerIndex));
        params.addBodyParameter("sxryid", model.getList().get(spinnerIndex).getId());
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {

            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()) {
                    ToastUtil.showToast(GenerateOrderActivity.this, "订单生成成功！");
                }
            }
        });
    }

    private String getGWCids() {
        String s = "";
        Logger.i("data size -> %d", data.size());
        for (int i = 0; i < data.size(); i++) {
            if (i != data.size() - 1) {
                s += data.get(i).getGwcid() + "|";
            } else
                s += data.get(i).getGwcid();
        }
        Logger.i(s);
        return s;
    }
}

package cn.soft_x.citymine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.soft_x.citymine.R;
import cn.soft_x.citymine.adapter.TruckAdapter;
import cn.soft_x.citymine.http.HttpUrl;
import cn.soft_x.citymine.http.MyXUtilsCallBack;
import cn.soft_x.citymine.model.TruckItemModel;
import cn.soft_x.citymine.model.TruckModel;
import cn.soft_x.citymine.utils.Constant;

@Deprecated
public class TruckActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    /**
     * 调货商没有货车！
     */
    @BindView(R.id.truck_listview)
    ListView truckListview;
    @BindView(R.id.truck_refresh)
    BGARefreshLayout truckRefresh;
    @BindView(R.id.truck_order)
    Button truckOrder;

    private List<TruckModel.DataBean> mData = new ArrayList<>();
    private TruckAdapter mAdapter;

    private BGARefreshViewHolder mRefreshHolder;

    private int mPage = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck);
        ButterKnife.bind(this);
        initView();
        initData(1);
    }

    private void initData(final int page) {
        //        {"appyhid":"12345678","currentPage":"1","numPerPage":"2"}
        showProgressDialog("正在加载...");
        RequestParams params = new RequestParams(HttpUrl.HC);
        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("currentPage", page + "");
        params.addBodyParameter("numPerPage", 5 + "");
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                TruckModel bean = JSON.parseObject(result, TruckModel.class);
                if (page == 1) {
                    if (mData.size() > 0)
                        mData.clear();
                    mData.addAll(0, bean.getData());
                } else {
                    mData.addAll(bean.getData());
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initView() {
        truckRefresh.setIsShowLoadingMoreView(true);
        mRefreshHolder = new BGANormalRefreshViewHolder(this, true);
        truckRefresh.setRefreshViewHolder(mRefreshHolder);
        truckRefresh.setDelegate(this);
        mAdapter = new TruckAdapter(mData);
        truckListview.setAdapter(mAdapter);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("货车");
    }


    @OnClick({R.id.truck_order, R.id.title_bar_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.truck_order:
                if (isOK()) {
                    Intent intent = new Intent(this, GenerateOrderActivity.class);
                    ArrayList<TruckItemModel> lists = new ArrayList<>();
                    for (TruckModel.DataBean dataBean : mData) {
                        for (TruckItemModel model :
                                dataBean.getList()) {
                            if (model.isChecked())
                                lists.add(model);
                        }
                    }
                    Bundle b = new Bundle();
                    b.putSerializable("model", lists);
                    b.putString("id", id);
                    intent.putExtra("bundle", b);
                    startActivity(intent);
                }
                break;
        }
    }

    private String id = "";

    private boolean isOK() {
        int res1 = 0;
        int res2 = 0;
        for (TruckModel.DataBean dataBean : mData) {
            if (dataBean.isChecked()) {
                res1++;
            }
            for (TruckItemModel model :
                    dataBean.getList()) {
                if (model.isChecked()) {
                    id = dataBean.getId();
                    res2++;
                }
            }
        }
        Logger.i("res1->%d,res2->%d", res1, res2);
        if (res1 == 0) {
            if (res2 != 0) {
                return true;
            }
            ToastUtil.showToast(this, "请选择一条订单！");
            return false;
        } else if (res1 > 1) {
            ToastUtil.showToast(this, "只能选择同一类订单！");
            return false;
        }
        return true;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mPage = 2;
        initData(1);
        refreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        initData(mPage);
        mPage++;
        refreshLayout.endLoadingMore();
        return false;
    }
}

package cn.soft_x.citymine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

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
import cn.soft_x.citymine.adapter.InvitationAdapter;
import cn.soft_x.citymine.http.HttpUrl;
import cn.soft_x.citymine.http.MyXUtilsCallBack;
import cn.soft_x.citymine.model.InvitationModel;
import cn.soft_x.citymine.utils.Constant;

public class InvitationActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.invitation_listView)
    ListView invitationListView;
    @BindView(R.id.invitation_refresh)
    BGARefreshLayout invitationRefresh;

    private BGARefreshViewHolder mRefreshHolder;

    private List<InvitationModel.FblistBean> mData = new ArrayList<>();

    private InvitationAdapter mAdapter;

    private int mPage = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        ButterKnife.bind(this);
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(1);
    }

    private void initData(final int page) {
        //        {"currentPage":"1","appyhid":"","appyhlx":""}
        showProgressDialog("正在加载...");
        RequestParams params = new RequestParams(HttpUrl.YQH);
        params.addBodyParameter("currentPage", page + "");
        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("appyhlx", "0");
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                InvitationModel model = JSON.parseObject(result, InvitationModel.class);
                if (page == 1) {
                    if (mData.size() > 0) {
                        mData.clear();
                    }
                    mData.addAll(0, model.getFblist());
                } else {
                    mData.addAll(model.getFblist());
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                dismissProgressDialog();
                dismissProgressDialog();
                dismissProgressDialog();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initListView() {
        invitationRefresh.setIsShowLoadingMoreView(true);
        mRefreshHolder = new BGANormalRefreshViewHolder(this, true);
        invitationRefresh.setRefreshViewHolder(mRefreshHolder);
        invitationRefresh.setDelegate(this);
        mAdapter = new InvitationAdapter(mData, this);
        invitationListView.setAdapter(mAdapter);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("供货信息列表");
    }

    @OnClick({R.id.title_bar_left})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
        }
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
        dismissProgressDialog();
        refreshLayout.endLoadingMore();
        return false;
    }

}

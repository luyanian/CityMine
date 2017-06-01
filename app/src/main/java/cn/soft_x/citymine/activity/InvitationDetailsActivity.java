package cn.soft_x.citymine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.maverick.utils.ToastUtil;

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
import cn.soft_x.citymine.adapter.InvitationDetailsAdapter;
import cn.soft_x.citymine.http.HttpUrl;
import cn.soft_x.citymine.http.MyXUtilsCallBack;
import cn.soft_x.citymine.model.InvitationDetailsModel;
import cn.soft_x.citymine.utils.Constant;

public class InvitationDetailsActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener {

    @BindView(R.id.invitation_details_listView)
    ListView invitationDetailsListView;
    @BindView(R.id.invitation_details_refresh)
    BGARefreshLayout invitationDetailsRefresh;
    @BindView(R.id.invitation_details_btn)
    Button invitationDetailsBtn;

    private BGARefreshViewHolder viewHolder;
    private List<InvitationDetailsModel.ListBean> mData = new ArrayList<>();
    private InvitationDetailsAdapter mAdapter;

    private int mPage = 2;
    private String fbzId = "";
    private String ghId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_details);
        ButterKnife.bind(this);
        initIntent();
        initListView();
        initData(1);
    }

    private void initIntent() {
        ghId = getIntent().getStringExtra("gh");
        fbzId = getIntent().getStringExtra("fbz");
    }

    private void initData(final int page) {
        showProgressDialog("正在加载...");
        RequestParams params = new RequestParams(HttpUrl.YQH_2);
        //        {"appyhid":"","currentPage":"1","numPerPage":"10"}
        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("currentPage", page + "");
        params.addBodyParameter("numPerPage", "10");
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                InvitationDetailsModel model = JSON.parseObject(result, InvitationDetailsModel.class);
                if (page == 1) {
                    if (mData.size() > 0) {
                        mData.clear();
                    }
                    mData.addAll(0, model.getList());
                } else {
                    mData.addAll(model.getList());
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initListView() {
        invitationDetailsRefresh.setIsShowLoadingMoreView(true);
        viewHolder = new BGANormalRefreshViewHolder(this, true);
        invitationDetailsRefresh.setRefreshViewHolder(viewHolder);
        invitationDetailsRefresh.setDelegate(this);
        mAdapter = new InvitationDetailsAdapter(mData);
        invitationDetailsListView.setAdapter(mAdapter);
        invitationDetailsListView.setOnItemClickListener(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("采购信息");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (InvitationDetailsModel.ListBean b :
                mData) {
            b.setChecked(false);
        }
        mData.get(position).setChecked(true);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.title_bar_left, R.id.invitation_details_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.invitation_details_btn:
                if (!isOk()) {
                    ToastUtil.showToast(this, "请选择其中一项！");
                    return;
                }
                upLoad();
                break;
        }
    }

    private boolean isOk() {
        for (InvitationDetailsModel.ListBean b : mData) {
            if (b.isChecked())
                return true;
        }
        return false;
    }


    private void upLoad() {
        showProgressDialog("正在上传...");
        RequestParams params = new RequestParams(HttpUrl.SEND_YQH);
        //        {"ghid":"value","fbzid":"value","cgid":"value","appyhid":"value"}

        //        {"ghid":"供货主键","fbzid":"发布者主键","cgid":"采购主键","appyhid":"App用户主键"}
        params.addBodyParameter("ghid", ghId);
        params.addBodyParameter("fbzid", fbzId);
        for (InvitationDetailsModel.ListBean b :
                mData) {
            if (b.isChecked()) {
                params.addBodyParameter("cgid", b.getId());
            }
        }
        params.addBodyParameter("appyhid", Constant.USER_ID);
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {

            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()) {
                    ToastUtil.showToast(InvitationDetailsActivity.this, "邀请函发送成功！");
                    finish();
                } else {
                    ToastUtil.showToast(InvitationDetailsActivity.this, "邀请函发送失败！");
                }
            }
        });
    }
}

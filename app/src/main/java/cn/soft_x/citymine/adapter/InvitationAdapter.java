package cn.soft_x.citymine.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.citymine.R;
import cn.soft_x.citymine.activity.InvitationDetailsActivity;
import cn.soft_x.citymine.application.CityMineApplication;
import cn.soft_x.citymine.http.HttpUrl;
import cn.soft_x.citymine.model.InvitationModel;

/**
 * Created by Administrator on 2016-12-09.
 */
public class InvitationAdapter extends BaseAdapter {

    private List<InvitationModel.FblistBean> mData;
    private Context c;

    public InvitationAdapter(List<InvitationModel.FblistBean> mData, Context c) {
        this.mData = mData;
        this.c = c;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public InvitationModel.FblistBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(CityMineApplication.getAppContext()).inflate(R.layout.item_invitation, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        InvitationModel.FblistBean item = getItem(position);
        viewHolder.itemInvitationTitle.setText(item.getGHBT());
        viewHolder.itemInvitationGgTv.setText(item.getGG());
        viewHolder.itemInvitationNumTv.setText(item.getSL());
        viewHolder.itemInvitationPingzhongTv.setText(item.getPZ());
        viewHolder.itemInvitationBtn.setOnClickListener(new OnBtnClickListener(position));
        PointF focusPoint = new PointF(0f, 0f);
        viewHolder.itemInvitationIv.getHierarchy().setActualImageFocusPoint(focusPoint);
        viewHolder.itemInvitationIv.setImageURI(Uri.parse(HttpUrl.API_HOST+item.getFBZTX()));

        return convertView;
    }

    class OnBtnClickListener implements View.OnClickListener{

        private int pos;

        public OnBtnClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(c, InvitationDetailsActivity.class);
            intent.putExtra("fbz",mData.get(pos).getFBZID());
            intent.putExtra("gh",mData.get(pos).getGHID());
            c.startActivity(intent);
        }
    }

    static class ViewHolder {
        @BindView(R.id.item_invitation_title)
        TextView itemInvitationTitle;
        @BindView(R.id.item_invitation_iv)
        SimpleDraweeView itemInvitationIv;
        @BindView(R.id.item_invitation_pingzhong)
        TextView itemInvitationPingzhong;
        @BindView(R.id.item_invitation_pingzhong_tv)
        TextView itemInvitationPingzhongTv;
        @BindView(R.id.item_invitation_num)
        TextView itemInvitationNum;
        @BindView(R.id.item_invitation_num_tv)
        TextView itemInvitationNumTv;
        @BindView(R.id.item_invitation_gg)
        TextView itemInvitationGg;
        @BindView(R.id.item_invitation_gg_tv)
        TextView itemInvitationGgTv;
        @BindView(R.id.item_invitation_btn)
        Button itemInvitationBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package cn.soft_x.citymine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.citymine.R;
import cn.soft_x.citymine.application.CityMineApplication;
import cn.soft_x.citymine.model.InvitationDetailsModel;

/**
 * Created by Administrator on 2016-12-09.
 */
public class InvitationDetailsAdapter extends BaseAdapter {

    private List<InvitationDetailsModel.ListBean> mData;

    public InvitationDetailsAdapter(List<InvitationDetailsModel.ListBean> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public InvitationDetailsModel.ListBean getItem(int position) {
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
            convertView = LayoutInflater.from(CityMineApplication.getAppContext()).inflate(R.layout.item_invitation_details, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        InvitationDetailsModel.ListBean model = getItem(position);
        viewHolder.itemIDCheck.setOnClickListener(new OnCheckedClickListener(position));
        if (model.isChecked()) {
            viewHolder.itemIDCheck.setBackgroundResource(R.drawable.bg_truck_2);
        } else {
            viewHolder.itemIDCheck.setBackgroundResource(R.drawable.bg_truck_1);
        }
        viewHolder.itemIDGgTv.setText(model.getGgname());
        viewHolder.itemIDNumTv.setText(model.getDhsDanjia()+"元");
        viewHolder.itemIDFukuanTv.setText(model.getJsms()+"");
        viewHolder.itemIDPingzhongTv.setText(model.getPzname());
        viewHolder.itemIDCode.setText(model.getCgbm());

        return convertView;
    }

    class OnCheckedClickListener implements View.OnClickListener {
        private int pos;

        public OnCheckedClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            for (InvitationDetailsModel.ListBean m :
                    mData) {
                m.setChecked(false);
            }
            mData.get(pos).setChecked(true);
            notifyDataSetChanged();
        }
    }


    static class ViewHolder {
        @BindView(R.id.item_i_d_check)
        TextView itemIDCheck;
        @BindView(R.id.item_i_d_code)
        TextView itemIDCode;
        @BindView(R.id.item_i_d_iv)
        SimpleDraweeView itemIDIv;
        @BindView(R.id.item_i_d_pingzhong)
        TextView itemIDPingzhong;
        @BindView(R.id.item_i_d_pingzhong_tv)
        TextView itemIDPingzhongTv;
        @BindView(R.id.item_i_d_num)
        TextView itemIDNum;
        @BindView(R.id.item_i_d_num_tv)
        TextView itemIDNumTv;
        @BindView(R.id.item_i_d_gg)
        TextView itemIDGg;
        @BindView(R.id.item_i_d_gg_tv)
        TextView itemIDGgTv;
        @BindView(R.id.item_i_d_fukuan)
        TextView itemIDFukuan;
        @BindView(R.id.item_i_d_fukuan_tv)
        TextView itemIDFukuanTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

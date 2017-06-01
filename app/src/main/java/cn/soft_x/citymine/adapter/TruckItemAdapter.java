package cn.soft_x.citymine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.citymine.R;
import cn.soft_x.citymine.application.CityMineApplication;
import cn.soft_x.citymine.model.TruckItemModel;

/**
 * Created by Administrator on 2016-12-05.
 */
public class TruckItemAdapter extends BaseAdapter {

    private List<TruckItemModel> data;
    private int itemIndex;
    private CheckedForAllListener listener;

    public TruckItemAdapter(List<TruckItemModel> data, int itemIndex, CheckedForAllListener listener) {
        this.data = data;
        this.itemIndex = itemIndex;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public TruckItemModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (null == convertView) {
            convertView = LayoutInflater.from(CityMineApplication.getAppContext()).inflate(R.layout.item_item_truck, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TruckItemModel itemModel = data.get(position);
        holder.itemItemBianhaoTv.setText(itemModel.getCgcode());
        holder.itemItemPingzhongTv.setText(itemModel.getPzmc());
        holder.itemItemCheck.setOnClickListener(new OnTruckItemCheckedListener(position));
        Logger.e("getView itemItemCheck is click->" + data.get(position).isChecked());
        if (itemModel.isChecked()) {
            holder.itemItemCheck.setBackgroundResource(R.drawable.bg_truck_2);
        } else {
            holder.itemItemCheck.setBackgroundResource(R.drawable.bg_truck_1);
        }
        return convertView;
    }

    class OnTruckItemCheckedListener implements View.OnClickListener {
        int position;

        public OnTruckItemCheckedListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            data.get(position).setChecked(!data.get(position).isChecked());
            Logger.e("itemItemCheck is click->" + data.get(position).isChecked());
            /**
             * 1.都选中
             * 2.都未选中
             * 3.都选中的情况下取消某项
             * 4.都未选中的情况下选择某项
             * 分析：
             * 1:都选中则上层选中
             * 2:都选中取消某项则上层取消，且不改变其他
             * 3:都未选中选择某项上层不变，直至1情况
             * 4:
             */
            if (listener != null) {
                listener.OnCheckedForALLListener(itemIndex);
            }
            TruckItemAdapter.this.notifyDataSetChanged();
        }
    }

    interface CheckedForAllListener {
        void OnCheckedForALLListener(int position);
    }

    static class ViewHolder {
        @BindView(R.id.item_item_check)
        TextView itemItemCheck;
        @BindView(R.id.item_item_iv)
        SimpleDraweeView itemItemIv;
        @BindView(R.id.item_item_bianhao)
        TextView itemItemBianhao;
        @BindView(R.id.item_item_bianhao_tv)
        TextView itemItemBianhaoTv;
        @BindView(R.id.item_item_pingzhong)
        TextView itemItemPingzhong;
        @BindView(R.id.item_item_pingzhong_tv)
        TextView itemItemPingzhongTv;
        @BindView(R.id.item_item_caigoufang)
        TextView itemItemCaigoufang;
        @BindView(R.id.item_item_caigoufang_tv)
        TextView itemItemCaigoufangTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

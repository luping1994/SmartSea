package com.suntrans.smartsea.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.smartsea.R;
import com.suntrans.smartsea.widget.Switch;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Looney on 2016/11/10.
 */

public class SettingAdapter extends RecyclerView.Adapter {

    private  ArrayList<Map<String, String>> datas;
    private  Context context;

    public SettingAdapter(ArrayList<Map<String, String>> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            RecyclerView.ViewHolder holder= new viewHolder2(LayoutInflater.from(
                    context).inflate(R.layout.item2, parent,false));
            return holder;

        }else {
            RecyclerView.ViewHolder holder= new viewHolder1(LayoutInflater.from(
                    context).inflate(R.layout.item1, parent,false));
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //判断holder是哪个类，从而确定是哪种布局
        /////布局1
        if(holder instanceof viewHolder1) {
            ((viewHolder1)holder).setData(position);
        }else {
            ((viewHolder2)holder).setData(position);

        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0||position==8){
            return 1;
        }
        return 0;
    }

    class viewHolder1 extends RecyclerView.ViewHolder {
        TextView list_header;   //小标题
        TextView name;    //名称
        TextView value;    //参数值

        public viewHolder1(View view) {
            super(view);
            list_header = (TextView) view.findViewById(R.id.list_header);
            name = (TextView) view.findViewById(R.id.name);
            value = (TextView) view.findViewById(R.id.value);
        }

        public void setData(int position) {
            if (position == 0) {
                list_header.setVisibility(View.VISIBLE);
                name.setText("数据自动上传周期:");
            } else {
                list_header.setVisibility(View.GONE);
                name.setText("摄像头档次开启时长:");
            }
            value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    class viewHolder2 extends RecyclerView.ViewHolder
    {
        TextView list_header;   //小标题
        TextView name;    //名称
        Switch aSwitch;
        public viewHolder2(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            aSwitch = (Switch) view.findViewById(R.id.switch_);
        }

        public void setData(final int position) {
            switch (position){
                case 1:
                    name.setText("3G路由器电源");
                    break;
                case 2:
                    name.setText("摄像头电源");
                    break;
                case 3:
                    name.setText("负载1:0");
                    break;
                case 4:
                    name.setText("负载2:0");
                    break;
                case 5:
                    name.setText("负载3:0");

                    break;
                case 6:
                    name.setText("负载4:0");
                    break;
                case 7:
                    name.setText("负载5:0");
                    break;
            }
            aSwitch.setOnChangeListener(new Switch.OnSwitchChangedListener() {
                @Override
                public void onSwitchChange(Switch switchView, boolean isChecked) {
                    onSwitchClickListener.onSwitchClick(position,isChecked);
                }
            });
        }
    }

    public void setOnSwitchClickListener(OnSwitchClickListener onSwitchClickListener) {
        this.onSwitchClickListener = onSwitchClickListener;
    }

    private OnSwitchClickListener onSwitchClickListener;

    public interface OnSwitchClickListener{
        void onSwitchClick(int position,boolean isChecked);
    }
}

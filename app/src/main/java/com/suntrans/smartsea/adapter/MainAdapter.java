package com.suntrans.smartsea.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Map;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.smartsea.R;

/**
 * Created by Looney on 2016/11/10.
 */

public class MainAdapter extends RecyclerView.Adapter {

    private  ArrayList<Map<String, String>> datas;
    private  Context context;

    public MainAdapter(ArrayList<Map<String, String>> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder= new viewHolder1(LayoutInflater.from(
                context).inflate(R.layout.item_, parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //判断holder是哪个类，从而确定是哪种布局
        /////布局1
        if(holder instanceof viewHolder1) {
            viewHolder1 viewholder = (viewHolder1) holder;
            viewholder.setData(position);

        }
    }

    @Override
    public int getItemCount() {
        return datas.size()-1;
    }

    class viewHolder1 extends RecyclerView.ViewHolder
    {
        LinearLayout layout;   //整体布局
        TextView list_header;   //小标题
        ImageView image;    //图标
        ImageView arrow;    //图标
        TextView name;    //名称
        TextView value;    //参数值
        public viewHolder1(View view)
        {
            super(view);
            layout=(LinearLayout)view.findViewById(R.id.layout);
            list_header = (TextView) view.findViewById(R.id.list_header);
            image=(ImageView)view.findViewById(R.id.image);
            arrow=(ImageView)view.findViewById(R.id.arrow);
            name = (TextView) view.findViewById(R.id.name);
            value = (TextView) view.findViewById(R.id.value);
        }

        public void setData(final int position) {
            Map<String, String> map = datas.get(position);
            String Name = map.get("Name");
            String Value = map.get("Value");
            String Evaluate = map.get("Evaluate");
            String Title = map.get("Title");
            if(Evaluate!=null)
                Value = Value + "(" + Evaluate + ")";
            if(Title.equals("-1"))
                list_header.setVisibility(View.GONE);
            else{
                list_header.setVisibility(View.VISIBLE);
                list_header.setText(Title);
            }
            if (position==0||position==1){
                arrow.setVisibility(View.INVISIBLE);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else {
                arrow.setVisibility(View.VISIBLE);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClickListener(position);
                    }
                });
            }
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), Integer.valueOf(map.get("Image")));
            image.setImageBitmap(bitmap);
            name.setText(Name);
            value.setText(Value);
        }
    }


    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}

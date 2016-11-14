package com.suntrans.smartsea.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.suntrans.smartsea.Utils.ParseSoapObj;
import com.suntrans.smartsea.Utils.RecyclerViewDivider;
import com.suntrans.smartsea.adapter.MainAdapter;
import com.suntrans.smartsea.R;
import com.suntrans.smartsea.webServices.soap;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Looney on 2016/11/10.
 */

public class Main_activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @InjectView(R.id.layout_setting)
    LinearLayout layoutSetting;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;

    private ArrayList<Map<String,String>> datas = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private MainAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.inject(this);
        initDatas();
        initView();
    }

    private void initDatas() {
        datas.clear();
        Map<String, String> map0 = new HashMap<String, String>();
        map0.put("Name", "功率");
        map0.put("Value", "null");  //当前时间
        map0.put("Title","");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map0.put("Image", String.valueOf(R.mipmap.ic_input));   //时间图标
        map0.put("ziduanma", "");   //时间图标
        datas.add(map0);

        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("Name", "电度数");
        map1.put("Value", "null");  //当前时间
        map1.put("Title","-1");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map1.put("Image", String.valueOf(R.mipmap.ic_input));   //时间图标
        map1.put("ziduanma", "");
        datas.add(map1);

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("Name", "强电电压");
        map2.put("Value", "null");  //当前时间
        map2.put("Title","");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map2.put("Image", String.valueOf(R.mipmap.ic_volgate));   //时间图标
        map2.put("ziduanma", "");
        datas.add(map2);

        Map<String, String> map3 = new HashMap<String, String>();
        Map<String, String> map4 = new HashMap<String, String>();
        Map<String, String> map5 = new HashMap<String, String>();
        Map<String, String> map6 = new HashMap<String, String>();

        map3.put("Name", "强电电流");
        map3.put("Value", "null");  //当前时间
        map3.put("Title","-1");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map3.put("Image", String.valueOf(R.mipmap.ic_current));   //时间图标
        map3.put("ziduanma", "");
        datas.add(map3);

        map4.put("Name", "充电电压");
        map4.put("Value", "null");  //当前时间
        map4.put("Title","-1");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map4.put("Image", String.valueOf(R.mipmap.ic_chrage));   //时间图标
        map4.put("ziduanma", "");
        datas.add(map4);

        map5.put("Name", "充电电流");
        map5.put("Value", "null");  //当前时间
        map5.put("Title","-1");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map5.put("Image", String.valueOf(R.mipmap.ic_chrage));   //时间图标
        map5.put("ziduanma", "");
        datas.add(map5);

        map6.put("Name", "发动机转速");
        map6.put("Value", "null");  //当前时间
        map6.put("Title","-1");  //此栏是否需要显示标题,=-1表示不需要，否则，就填写标题的名称
        map6.put("Image", String.valueOf(R.mipmap.ic_input));   //图标
        map6.put("ziduanma", "");
        datas.add(map6);
        new GetDataTask().execute();
    }

    private void initView() {
        adapter = new MainAdapter(datas,this);
        refreshlayout .setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));   //添加分割线
        layoutSetting.setOnClickListener(this);
        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Intent intent = new Intent();
                intent.putExtra("ziduanma",datas.get(position).get("ziduanma"));
                intent.putExtra("name",datas.get(position).get("name"));
                intent.setClass(Main_activity.this,Query_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onRefresh() {
        new GetDataTask().execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_setting:
                startActivity(new Intent(Main_activity.this,Setting_activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    class GetDataTask extends AsyncTask<Void,Void,SoapObject>{

        @Override
        protected SoapObject doInBackground(Void... params) {
            SoapObject object = soap.Inquiry_RealData("1");
//            System.out.println(object.toString());
            return object;
        }

        @Override
        protected void onPostExecute(SoapObject s) {
            if (s==null){
                refreshlayout.setRefreshing(false);
                return;
            }
            ArrayList<Map<String,String>> data = ParseSoapObj.inquiry_userinfo(s);
               datas.get(0).put("Value",data.get(10).get("value")+"W");
               datas.get(0).put("ziduanma",data.get(10).get("ziduanma"));
               datas.get(0).put("name",data.get(10).get("name"));

               datas.get(1).put("Value",data.get(9).get("value")+"KWH");
               datas.get(1).put("ziduanma",data.get(9).get("ziduanma"));
               datas.get(1).put("name",data.get(9).get("name"));

               datas.get(2).put("Value",data.get(0).get("value")+"V");
               datas.get(2).put("ziduanma",data.get(0).get("ziduanma"));
               datas.get(2).put("name",data.get(0).get("name"));

               datas.get(3).put("Value",data.get(1).get("value")+"A");
               datas.get(3).put("ziduanma",data.get(1).get("ziduanma"));
               datas.get(3).put("name",data.get(1).get("name"));

               datas.get(4).put("Value",data.get(2).get("value")+"V");
               datas.get(4).put("ziduanma",data.get(2).get("ziduanma"));
               datas.get(4).put("name",data.get(2).get("name"));

               datas.get(5).put("Value",data.get(3).get("value")+"A");
               datas.get(5).put("ziduanma",data.get(3).get("ziduanma"));
               datas.get(5).put("name",data.get(3).get("name"));

               double aDouble = Double.valueOf(data.get(5).get("value"));
               DecimalFormat df = new DecimalFormat("######0.00");
               String value = df.format(aDouble);
               datas.get(6).put("Value",value+"转/分");
               datas.get(6).put("ziduanma",data.get(5).get("ziduanma"));
               datas.get(6).put("name",data.get(5).get("name"));

            refreshlayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

                logoutApp();
                return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    private  long exitTime = 0;

    private void logoutApp()
    {

        if (System.currentTimeMillis() - exitTime > 2000)
        {
            Toast.makeText(Main_activity.this,"再按一次退出",Toast.LENGTH_SHORT).show();

            exitTime = System.currentTimeMillis();
        } else
        {
//            finish();
//            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}

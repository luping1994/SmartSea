package com.suntrans.smartsea.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suntrans.smartsea.R;
import com.suntrans.smartsea.Utils.Converts;
import com.suntrans.smartsea.Utils.LogUtil;
import com.suntrans.smartsea.Utils.ParseSoapObj;
import com.suntrans.smartsea.Utils.Utils;
import com.suntrans.smartsea.service.MainService;
import com.suntrans.smartsea.webServices.soap;
import com.suntrans.smartsea.widget.Switch;
import com.suntrans.smartsea.widget.WaitDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Looney on 2016/11/10.
 */

public class Setting_activity extends AppCompatActivity implements View.OnClickListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    private RecyclerView recyclerview;
    private ArrayList<Map<String, String>> datas;
    private RecyclerView.LayoutManager layoutManager;
    private mAdapter adapter;
    private LinearLayout ll_refresh;
    private WaitDialog mWaitDialog = null;
    public MainService.ibinder binder;  //用于Activity与Service通信
    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder=(MainService.ibinder)service;   //activity与service通讯的类，调用对象中的方法可以实现通讯
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getApplication(), "网络错误！", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        Intent intent = new Intent(getApplicationContext(), MainService.class);
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        IntentFilter filter_dynamic = new IntentFilter();
        filter_dynamic.addAction("com.suntrans.beijing.RECEIVE");
        registerReceiver(receiver, filter_dynamic);
        mWaitDialog = new WaitDialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        mWaitDialog.setCancelable(false);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        ll_refresh = (LinearLayout) findViewById(R.id.layout_refresh);
        layoutManager = new LinearLayoutManager(this);
        adapter = new mAdapter();
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);

        layoutBack.setOnClickListener(this);
        ll_refresh.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetDataTask().execute();
    }

    @Override
    protected void onDestroy() {
        try {
            unbindService(con);   //解除Service的绑定
            unregisterReceiver(receiver);  //注销广播接收者
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_refresh:
                mWaitDialog.setWaitText("正在刷新...");
                mWaitDialog.show();
//                timer.schedule(new TimeTask1("获取数据失败"),3000);
                new GetDataTask().execute();
                break;
        }
    }

    /**
     * recycleview适配器
     */
    class mAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                RecyclerView.ViewHolder holder = new viewHolder2(LayoutInflater.from(
                        Setting_activity.this).inflate(R.layout.item2, parent, false));
                return holder;

            } else {
                RecyclerView.ViewHolder holder = new viewHolder1(LayoutInflater.from(
                        Setting_activity.this).inflate(R.layout.item1, parent, false));
                return holder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //判断holder是哪个类，从而确定是哪种布局
            /////布局1
            if (holder instanceof viewHolder1) {
                ((viewHolder1) holder).setData(position);
            } else {
                ((viewHolder2) holder).setData(position);

            }
        }


        @Override
        public int getItemCount() {
            return 9;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == 8) {
                return 1;
            }
            return 0;
        }

        //viewholder1 设置数据自动上传周期和摄像头档次开启时长的
        class viewHolder1 extends RecyclerView.ViewHolder {
            TextView list_header;   //小标题
            TextView name;    //名称
            TextView value;    //参数值
            Button button;
            public viewHolder1(View view) {
                super(view);
                list_header = (TextView) view.findViewById(R.id.list_header);
                name = (TextView) view.findViewById(R.id.name);
                value = (TextView) view.findViewById(R.id.value);
                button = (Button) view.findViewById(R.id.button);
            }

            public void setData(final int position) {
                if (position == 0) {
                    list_header.setVisibility(View.VISIBLE);
                    name.setText("数据自动上传周期:");

                } else {
                    list_header.setVisibility(View.GONE);
                    name.setText("摄像头单次开启时长:");
                }
                try {
                    if (datas != null)
                        value.setText(datas.get(position).get("value"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                value.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = View.inflate(Setting_activity.this,R.layout.dialog_view1,null);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Setting_activity.this);
                        final AlertDialog dialog = builder.setView(view).setTitle("请输入数据").create();
                        final EditText editText = (EditText)view.findViewById(R.id.edit);
                        final Button qvxiao = (Button)view.findViewById(R.id.qvxiao);
                        final Button queding = (Button)view.findViewById(R.id.queding);
                        editText.setText(value.getText().toString());
                        editText.setSelection(value.getText().toString().length());
                        qvxiao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        queding.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String value1 =editText.getText().toString();
                                float a = Float.valueOf(value1);
                                int b = (int)a;
                                value1= String.valueOf(b);
                                String ziduanma1 = "";
                                String order = Utils.DecConvert2Hex(value1);
                                if (position==0){
                                    ziduanma1="dataup";
                                    order = "aa aa 06 0200"+order;
                                }else {
                                    ziduanma1="sheopentime";
                                    order = "aa aa 06 0210"+order;
                                }
                                settingDialog();
                                new SetParameter(order,ziduanma1,value1).execute();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
            }
        }

        //viewholder2设置其余参数值为0和1的参数
        class viewHolder2 extends RecyclerView.ViewHolder {
            TextView list_header;   //小标题
            TextView name;    //名称
            Switch aSwitch;

            public viewHolder2(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                aSwitch = (Switch) view.findViewById(R.id.switch_);
            }

            public void setData(final int position) {
                switch (position) {
                    case 1:
                        name.setText("3G路由器电源");
                        break;
                    case 2:
                        name.setText("摄像头电源");
                        break;
                    case 3:
                        name.setText("负载1");
                        break;
                    case 4:
                        name.setText("负载2");
                        break;
                    case 5:
                        name.setText("负载3");

                        break;
                    case 6:
                        name.setText("负载4");
                        break;
                    case 7:
                        name.setText("负载5");
                        break;
                }
                try {
                    if (datas != null)
                        aSwitch.setState(datas.get(position).get("value").equals("0.00") ? false : true);
                    else {
                        aSwitch.setState(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                aSwitch.setOnChangeListener(new Switch.OnSwitchChangedListener() {
                    @Override
                    public void onSwitchChange(Switch switchView, boolean isChecked) {
                            sendOrder(position,switchView,isChecked);
                    }
                });
            }
            //发送命令
            private void sendOrder(int position, Switch switchView, boolean isChecked) {
                String order = "";
                String ziduanma1 = "";
                String value="";
                switch (position){
                    case 1:
                        ziduanma1="thrroupower";
                        if (!isChecked){
                            value="0";
                            order = "aa aa 06 0201 0000";
                        }else {
                            value="1";
                            order = "aa aa 06 0201 0001";
                        }
                        settingDialog();
                        new SetParameter(order,ziduanma1,value).execute();
                        break;
                    case 2:
                        ziduanma1="shextpower";
                        if (!isChecked){
                            value="0";
                            order = "aa aa 06 0202 0000";
                        }else {
                            value="1";
                            order = "aa aa 06 0202 0001";
                        }
                        settingDialog();
                        new SetParameter(order,ziduanma1,value).execute();
                        break;
                    case 3:
                        ziduanma1="ok1";
                        if (!isChecked){
                            value="0";
                            order = "aa aa 06 0205 0000";
                        }else {
                            value="1";
                            order = "aa aa 06 0205 0001";
                        }
                        settingDialog();
                        new SetParameter(order,ziduanma1,value).execute();
                        break;
                    case 4:
                        ziduanma1="ok2";
                        if (!isChecked){
                            value="0";
                            order = "aa aa 06 0206 0000";
                        }else {
                            value = "1";
                            order = "aa aa 06 0206 0001";
                        }
                        settingDialog();
                        new SetParameter(order,ziduanma1,value).execute();
                        break;
                    case 5:
                        ziduanma1="ok3";
                        if (!isChecked){
                            value="0";
                            order = "aa aa 06 0207 0000";
                        }else {
                            value="1";
                            order = "aa aa 06 0207 0001";
                        }
                        settingDialog();
                        new SetParameter(order,ziduanma1,value).execute();
                        break;
                    case 6:
                        ziduanma1="ok4";
                        if (!isChecked){
                            value="0";
                            order = "aa aa 06 0208 0000";
                        }else {
                            value="1";
                            order = "aa aa 06 0208 0001";
                        }
                        settingDialog();
                        new SetParameter(order,ziduanma1,value).execute();
                        break;
                    case 7:
                        ziduanma1="ok5  ";
                        if (!isChecked){
                            value="0";
                            order = "aa aa 06 0209 0000";
                        }else {
                            value="1";
                            order = "aa aa 06 0209 0001";
                        }
                        settingDialog();
                        new SetParameter(order,ziduanma1,value).execute();
                        break;
                }

            }
        }
    }

    public void  settingDialog(){
        mWaitDialog.setWaitText("设置中,请稍后");
        mWaitDialog.show();
    }


    /**
     * 获取数据任务
     */
    class GetDataTask extends AsyncTask<Void, Void, SoapObject> {
        @Override
        protected SoapObject doInBackground(Void... params) {
            SoapObject object = soap.Inquiry_AllParam("1");
            return object;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(SoapObject s) {
            if (datas != null) {
                datas.clear();
            }
            if (s!=null){
                datas = ParseSoapObj.parseAllra(s);
                adapter.notifyDataSetChanged();
                if (mWaitDialog.isShowing())
                    mWaitDialog.setWaitText("获取数据成功!");
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimeTask2(),800);
            }else {
                if (mWaitDialog.isShowing())
                    mWaitDialog.setWaitText("获取数据失败！");
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimeTask2(),800);
            }

        }
    }

    /**
     *设置参数任务,未知原因设置参数需要通过tcp连接发送命令直接设置,而不是通过webServices
     */
    class SetParameter extends AsyncTask<Void, Void, SoapObject>{
        private String ziduanma;
        private String order;
        private String value;
        public SetParameter(String order,String ziduanma,String value){
            this.order = order;
            this.ziduanma = ziduanma;
            this.value = value;
        }
        @Override
        protected SoapObject doInBackground(Void... params) {
            binder.sendOrder(order,0);
//            SoapObject result = soap.Set_Param("1",ziduanma,value);
            SoapObject result = null;
            return result;
        }

        @Override
        protected void onPostExecute(SoapObject str) {
            binder.sendOrder("aa aa 03 02 00 00 0b",0);//读取所有参数
//            if (str==null){
//                if (mWaitDialog.isShowing())
//                    mWaitDialog.setWaitText("设置失败！");
//                adapter.notifyDataSetChanged();
//                timer.cancel();
//                timer = new Timer();
//                timer.schedule(new TimeTask2(),800);
//            }else if(str.toString().equals("Set_ParamResponse{Set_ParamResult=1; }")){
//                if (mWaitDialog.isShowing())
//                    mWaitDialog.setWaitText("设置成功!");
//                timer.cancel();
//                timer = new Timer();
//                timer.schedule(new TimeTask2(),800);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new GetDataTask().execute();
                    }
                },1000);
//            }

        }
    }
    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    //延时任务
    private Timer timer = new Timer();
    class TimeTask2 extends TimerTask{
        @Override
        public void run() {
            if (mWaitDialog.isShowing())
            mWaitDialog.dismiss();
        }
    }




    //广播接收者
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int count = intent.getIntExtra("ContentNum", 0);   //byte数组的长度
            byte[] data = intent.getByteArrayExtra("Content");  //内容数组
            if(count>13)   //通过handler将数据传过去
            {
//                Message msg=new Message();
//                msg.obj=data;
//                msg.what=data.length;
//                handler.sendMessage(msg);
                String str = Converts.Bytes2HexString(data);
                LogUtil.i(str);
            }
        }
    };

}

package com.suntrans.smartsea.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.suntrans.smartsea.R;
import com.suntrans.smartsea.Utils.LogUtil;
import com.suntrans.smartsea.Utils.ParseSoapObj;
import com.suntrans.smartsea.Utils.Utils;
import com.suntrans.smartsea.webServices.soap;
import com.suntrans.smartsea.widget.WaitDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.suntrans.smartsea.R.id.chart;
import static com.suntrans.smartsea.R.id.layout_query;


/**
 * Created by Looney on 2016/11/10.
 */

public class Query_activity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(layout_query)
    LinearLayout layoutQuery;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.begin_time_ymd)
    TextView beginTimeYmd;
    @InjectView(R.id.begin_time_hh)
    TextView beginTimeHh;
    @InjectView(R.id.begin_time_mm)
    TextView beginTimeMm;
    @InjectView(R.id.end_time_ymd)
    TextView endTimeYmd;
    @InjectView(R.id.end_time_hh)
    TextView endTimeHh;
    @InjectView(R.id.end_time_mm)
    TextView endTimeMm;

    private LineChart mChart;
    private ArrayList<Map<String, String>> datas;
    private RecyclerView.LayoutManager layoutManager;
    private mAdapter  adapter;
    private String ziduanma = "";
    private String beginTime = "";
    private String endTime = "";
    private WaitDialog mWaitDialog = null;
    private LinearLayout ll_back;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.inject(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private boolean initDatas() {
        beginTime = beginTimeYmd.getText().toString()+" "+beginTimeHh.getText().toString()+":"+beginTimeMm.getText().toString();
        endTime = endTimeYmd.getText().toString()+" "+endTimeHh.getText().toString()+":"+endTimeMm.getText().toString();

        if(Utils.parseDate(beginTime) - Utils.parseDate(endTime)>0){
            Toast.makeText(this,"起始时间必须小于结束时间!",Toast.LENGTH_SHORT).show();
            return false;
        }
        new GetDataTask().execute();
        return true;
    }

    private void initView() {
        ziduanma = getIntent().getStringExtra("ziduanma");
        name = getIntent().getStringExtra("name");
        mChart = (LineChart) findViewById(chart);
        adapter = new mAdapter();
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
        ll_back = (LinearLayout) findViewById(R.id.layout_back);

        mWaitDialog = new WaitDialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        mWaitDialog.setCancelable(false);
        String today =Utils.getToday();
        String twohourago =Utils.get2backhour();
        beginTimeYmd.setText(twohourago.substring(0,10));
        beginTimeHh.setText(twohourago.substring(11,13));
        beginTimeMm.setText(twohourago.substring(14,16));

        endTimeYmd.setText(today.substring(0,10));
        endTimeHh.setText(today.substring(11,13));
        endTimeMm.setText(today.substring(14,16));

        beginTimeYmd.setOnClickListener(this);
        beginTimeMm.setOnClickListener(this);
        beginTimeHh.setOnClickListener(this);
        endTimeYmd.setOnClickListener(this);
        endTimeHh.setOnClickListener(this);
        endTimeMm.setOnClickListener(this);
        layoutQuery.setOnClickListener(this);
        ll_back.setOnClickListener(this);

        initChart();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case layout_query:
                if (!initDatas()){
                    break;
                }
                timer = new Timer();
                mWaitDialog.setWaitText("查询中...");
                mWaitDialog.show();
                break;
            case R.id.begin_time_ymd:
                showEditTimeDialog("请输入开始日期");
                break;
            case R.id.end_time_ymd:
                showEditTimeDialog("请输入结束日期");
                break;
            case R.id.end_time_hh:
                shoEditHHDialog("end",true);
                break;
            case R.id.end_time_mm:
                shoEditHHDialog("end",false);

                break;
            case R.id.begin_time_hh:
                shoEditHHDialog("beg",true);

                break;
            case R.id.begin_time_mm:
                shoEditHHDialog("beg",false);
                break;
            case R.id.layout_back:
                finish();
                break;
        }
    }

    /**
     * 选择小时和分钟数的对话框
     * @param beg 起始 beg = beg else beg = end
     * @param ishh 点击的为小时对话框为true否则为false
     */
    private void shoEditHHDialog(final String beg,final boolean ishh) {
        final AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("请选择时间");
        final String[] hh= {"00","01","02","03","04","05","06","07",
                            "08","09","10","11","12","13","14","15",
                            "16","17","18","19","20","21","22","23",
                            };
        final String[] mm= {"00","01","02","03","04","05","06","07",
                "08","09","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32",
                "33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49",
                "50","51","52","53","54","55","56","57","58","59"
        };
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        if (ishh){
            builder.setItems(hh, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (beg.equals("beg"))
                        beginTimeHh.setText(hh[which]);
                    else
                        endTimeHh.setText(hh[which]);
                }
            });
        }else {
            builder.setItems(mm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (beg.equals("beg"))
                        beginTimeMm.setText(mm[which]);
                    else
                        endTimeMm.setText(mm[which]);
                }
            });
        }
        builder.create().show();
    }

    //设置日期对话框,title用于判断起始时间还是终止时间
    private void showEditTimeDialog(final String title) {
        View v = View.inflate(this,R.layout.dialog_view,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setView(v).setTitle(title).create();
        final EditText editText = (EditText)v.findViewById(R.id.edit);
        final Button qvxiao = (Button)v.findViewById(R.id.qvxiao);
        final Button queding = (Button)v.findViewById(R.id.queding);
        if (title.equals("请输入开始日期"))
            editText.setText(beginTimeYmd.getText().toString().replace("-",""));
        else
            editText.setText(endTimeYmd.getText().toString().replace("-",""));
        editText.setSelection(8);
        qvxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                s = checkData(s);
                if (s!=null){
                    dialog.dismiss();
                    if (title.equals("请输入开始日期")){
                        beginTimeYmd.setText(s);
                    }else {
                        endTimeYmd.setText(s);
                    }
                }else {
                    Toast.makeText(Query_activity.this,"日期格式不对",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

    }


    //判断日期格式是否正确
    private String checkData(String s) {
        if (s.length()!=8)
            return null;
        s=s.substring(0,4)+"-"+s.substring(4,6)+"-"+s.substring(6,8);
        String expression2 ="((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(10|12|0?[13578])([-\\/\\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(11|0?[469])([-\\/\\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(0?2)([-\\/\\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([3579][26]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^( [1] [89][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^( [1] [89][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^( [1] [89][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$))";
        if (s.matches(expression2)){
            return s;
        }else {
            return null;
        }
    }


    //初始化图表参数
    private void initChart() {
        mChart.setDrawGridBackground(false);
        // no description text
        mChart.getDescription().setEnabled(true);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setNoDataText("暂无数据...");
        mChart.setNoDataTextColor(Color.WHITE);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);


        mChart.setPinchZoom(true);

        //x轴设置相关参数
        XAxis xAxis = mChart.getXAxis();
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisMinimum(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false); //是否显示X坐标轴上的刻度，默认是tru

        //Y轴设置相关参数
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0.0f);
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);


        mChart.getAxisRight().setEnabled(false);

        // add data
        setData();

        mChart.animateX(2500);
        Legend l = mChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
    }

    //设置图表数据
    private void setData() {
        if (datas==null){
            return;
        }
        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < datas.size(); i++) {
            float val = Float.valueOf(datas.get(i).get("value"));
            values.add(new Entry(i+1, val));
        }
//        System.err.println("ssssssssssssssssssssssss"+values.size());
        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, name);
            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.WHITE);
            set1.setLineWidth(1f);
            set1.setDrawCircles(false);
            set1.setDrawCircleHole(false);
            set1.setDrawFilled(true);
            set1.setFillColor(Color.WHITE);
            set1.setDrawValues(false);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

//            if (Utils.getSDKInt() >= 18) {
//                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
//                set1.setFillDrawable(drawable);
//            }
//            else {
//                set1.setFillColor(Color.WHITE);
//            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }

    /**
     * recycleview适配器
     */
    class mAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder= new viewHolder1(LayoutInflater.from(
                    Query_activity.this).inflate(R.layout.item3, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((viewHolder1)holder).setData(position);
        }

        @Override
        public int getItemCount() {
            return datas==null?0:datas.size();
        }

        class viewHolder1 extends RecyclerView.ViewHolder{
            TextView name;
            TextView value;
            public viewHolder1(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                value = (TextView) itemView.findViewById(R.id.value);
            }
            public void setData(int position){
                name.setText(datas.get(position).get("gettime").replace("+08:00","").replace("T","  "));
//                if (ziduanma.equals("ik2")){
//                    value.setText(datas.get(position).get("value")+"转/分");
//                }else {
                    value.setText(datas.get(position).get("value")+datas.get(position).get("danwei"));
//                }
            }
        }
    }


    /**
     * 获取数据异步任务
     */
    class GetDataTask extends AsyncTask<Void, Void, SoapObject> {
        @Override
        protected SoapObject doInBackground(Void... params) {
            String frq ="0.1";
            SoapObject object = soap.Inquiry_HisData("1", ziduanma, beginTime, endTime, frq);
            return object;
        }

        @Override
        protected void onPostExecute(SoapObject s) {
            if (s!=null){
                if (datas!=null){
                    datas.clear();
                }
                datas = ParseSoapObj.parseHisData(s);
                mChart.clear();
                setData();
                adapter.notifyDataSetChanged();
                if (mWaitDialog.isShowing())
                    mWaitDialog.setWaitText("获取数据成功!");
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimeTask2(),800);
            }else {
                if (mWaitDialog.isShowing())
                    mWaitDialog.setWaitText("获取数据失败!");
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimeTask2(),800);
            }

        }
    }

    //延时任务
    private Timer timer = new Timer();
    class TimeTask2 extends TimerTask {
        @Override
        public void run() {
            if (mWaitDialog.isShowing())
                mWaitDialog.dismiss();
        }
    }
}

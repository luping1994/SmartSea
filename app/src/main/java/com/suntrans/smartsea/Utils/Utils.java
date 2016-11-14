package com.suntrans.smartsea.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

	public static Toast mToast;

	public static void showToast(Context mContext, String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}
	
	/**
	 * dip 转换成 px
	 * @param dip
	 * @param context
	 * @return
	 */
	public static float dip2Dimension(float dip, Context context) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
	}
	/**
	 * @param dip
	 * @param context
	 * @param complexUnit {@link TypedValue#COMPLEX_UNIT_DIP} {@link TypedValue#COMPLEX_UNIT_SP}}
	 * @return
	 */
	public static float toDimension(float dip, Context context, int complexUnit) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(complexUnit, dip, displayMetrics);
	}

	/** 获取状态栏高度
	 * @param v
	 * @return
	 */
	public static int getStatusBarHeight(View v) {
		if (v == null) {
			return 0;
		}
		Rect frame = new Rect();
		v.getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	public static String getActionName(MotionEvent event) {
		String action = "unknow";
		switch (MotionEventCompat.getActionMasked(event)) {
		case MotionEvent.ACTION_DOWN:
			action = "ACTION_DOWN";
			break;
		case MotionEvent.ACTION_MOVE:
			action = "ACTION_MOVE";
			break;
		case MotionEvent.ACTION_UP:
			action = "ACTION_UP";
			break;
		case MotionEvent.ACTION_CANCEL:
			action = "ACTION_CANCEL";
			break;
		case MotionEvent.ACTION_SCROLL:
			action = "ACTION_SCROLL";
			break;
		case MotionEvent.ACTION_OUTSIDE:
			action = "ACTION_SCROLL";
			break;
		default:
			break;
		}
		return action;
	}

	/**
	 * 获取当前时间的前一天时间
	 *
	 * @return
	 */
	public static String getYesterday(){
		String yesterday="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.DATE,-1);
		Date lastDay = ca.getTime();
		yesterday=sdf.format(lastDay);

		return yesterday;
	}

	/**
	 * 获得当前时间两小时之前的时间(*￣▽￣*)ブ
	 * @return
     */
	public static String get2backhour(){
		String yesterday="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR_OF_DAY,-2);
		Date lastDay = ca.getTime();
		yesterday=sdf.format(lastDay);

		return yesterday;
	}

	public static String getToday(){
		String today;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		today=sdf.format(new java.util.Date());
		return today;
	}

	public static long parseDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			long millionSeconds = sdf.parse(date).getTime();//毫秒
			return millionSeconds;
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static String DecConvert2Hex(String a) {
		int b = Integer.valueOf(a);
		String c = Integer.toHexString(b);
		System.err.println("a=" + a + ";b=" + b + ";c=" + c);
		StringBuffer sb;
		sb = new StringBuffer();
		if (c.length()>4){
			return  null;
		}
		if (c.length() != 4) {
			for (int i = 0; i < 4 - c.length(); i++) {
				sb.append(0);
			}
		}
		sb.append(c);

		return sb.toString();
	}
}

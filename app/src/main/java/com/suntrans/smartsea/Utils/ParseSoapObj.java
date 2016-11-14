package com.suntrans.smartsea.Utils;

import com.suntrans.smartsea.webServices.soap;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;

public class ParseSoapObj {

	//1.用于解析Inqurey_UserInfo请求数据时返回数据的静态方法    
	public static ArrayList<Map<String, String>> inquiry_userinfo(SoapObject result) {
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();   //要返回的数据集合
		// TODO Auto-generated constructor stub
		try {
			result = (SoapObject) result.getProperty(1);
			if (result.getPropertyCount() >= 1)
				result = (SoapObject) result.getProperty(0);  //有数据  则继续
			else
				return data;     //无数据直接返回空
			//result = (SoapObject)result.getProperty(1);*/
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soap = (SoapObject) result.getProperty(i);
				Map<String, String> map = new HashMap<String, String>();
				try {
					DecimalFormat df = new DecimalFormat("######0.00");
					double value = Double.valueOf(soap.getProperty("value").toString());
					String s = df.format(value);
					map.put("name", soap.getProperty("name").toString());
					map.put("value", s);
					map.put("ziduanma", soap.getProperty("ziduanma").toString());
					data.add(map);
				} catch (Exception e) {

				}
			}

		} catch (Exception e) {

		}

			return data;
	}

	//2.用于解析Inqurey_HisData请求数据时返回数据的静态方法
	public static ArrayList<Map<String, String>> parseHisData(SoapObject result) {
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();   //要返回的数据集合
		// TODO Auto-generated constructor stub
		try {
			result = (SoapObject) result.getProperty(1);
			if (result.getPropertyCount() >= 1)
				result = (SoapObject) result.getProperty(0);  //有数据  则继续
			else
				return data;     //无数据直接返回空
			//result = (SoapObject)result.getProperty(1);*/
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soap = (SoapObject) result.getProperty(i);
				Map<String, String> map = new HashMap<String, String>();
				try {
					map.put("name", soap.getProperty("name").toString());
					map.put("value", soap.getProperty("value").toString());
					map.put("gettime", soap.getProperty("gettime").toString());
					map.put("danwei", soap.getProperty("danwei").toString());
					data.add(map);
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}

		} catch (Exception e) {

		}
		return data;
	}

	//3.用于解析Inqurey_HisData请求数据时返回数据的静态方法
	public static ArrayList<Map<String, String>> parseAllra(SoapObject result) {
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();   //要返回的数据集合
		// TODO Auto-generated constructor stub
		try {
			result = (SoapObject) result.getProperty(1);
			if (result.getPropertyCount() >= 1)
				result = (SoapObject) result.getProperty(0);  //有数据  则继续
			else
				return data;     //无数据直接返回空
			//result = (SoapObject)result.getProperty(1);*/
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soap = (SoapObject) result.getProperty(i);
				Map<String, String> map = new HashMap<String, String>();
				try {
					map.put("name", soap.getProperty("name").toString());
					map.put("value", soap.getProperty("value").toString());
					data.add(map);
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}

		} catch (Exception e) {

		}
		return data;
	}
}

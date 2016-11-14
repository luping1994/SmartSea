package com.suntrans.smartsea.webServices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class soap {
	private static String NAMESPACE="http://www.suntrans.net/";   //命名空间
	private static String URL = "http://www.suntrans.net:8999";    //WebServices地址

	//1.调用Inquiry_UserInfo方法（查询学生用户信息）的soap通讯静态方法   返回一个SoapObject对象
	public static SoapObject Inquiry_RealData(String sid)
	{
		//方法名
		final String METHOD_NAME ="Inquiry_RealData";
		final String SOAP_ACTION ="http://www.suntrans.net/Inquiry_RealData";
		//SoapObject detail;
		SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("sid", sid);    //添加参数：学号
		HttpTransportSE ht =new HttpTransportSE(URL,10000);    //设置访问地址，第二个参数是设置超时的毫秒数
		ht.debug =true;
		SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet =true;
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//  Object result = envelope.bodyIn;
		SoapObject result = null;

		try {
			result = (SoapObject)envelope.getResponse();    //获取结果
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}     //result是服务器返回的数据  形式为SoapObject对象
		return result;
	}


	//2.Inquiry_HisData（查询历史数据）的soap通讯静态方法   返回一个SoapObject对象
	public static SoapObject Inquiry_HisData(String sid,String ziduanma,String startTime,String endTime,String freq)
	{
		//方法名
		final String METHOD_NAME ="Inquiry_HisData";
		final String SOAP_ACTION ="http://www.suntrans.net/Inquiry_HisData";
		//SoapObject detail;
		SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("sid", sid);    //添加参数：设备号
		rpc.addProperty("ziduanma", ziduanma);    //添加参数：字段码
		rpc.addProperty("StartTime", startTime);    //添加参数：开始时间
		rpc.addProperty("EndTime", endTime);    //添加参数：结束时间
		rpc.addProperty("Freq", freq);    //添加参数：频率
		HttpTransportSE ht =new HttpTransportSE(URL,10000);    //设置访问地址，第二个参数是设置超时的毫秒数
		ht.debug =true;
		SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet =true;
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//  Object result = envelope.bodyIn;
		SoapObject result = null;

		try {
			result = (SoapObject)envelope.getResponse();    //获取结果
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}     //result是服务器返回的数据  形式为SoapObject对象
		return result;
	}

//	Inquiry_AllParam

	//3.Inquiry_AllParam（查询所有参数数据）的soap通讯静态方法   返回一个SoapObject对象
	public static SoapObject Inquiry_AllParam(String sid)
	{
		//方法名
		final String METHOD_NAME ="Inquiry_AllParam";
		final String SOAP_ACTION ="http://www.suntrans.net/Inquiry_AllParam";
		//SoapObject detail;
		SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("sid", sid);    //添加参数：设备号
		HttpTransportSE ht =new HttpTransportSE(URL,10000);    //设置访问地址，第二个参数是设置超时的毫秒数
		ht.debug =true;
		SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet =true;
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//  Object result = envelope.bodyIn;
		SoapObject result = null;

		try {
			result = (SoapObject)envelope.getResponse();    //获取结果
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}     //result是服务器返回的数据  形式为SoapObject对象
		return result;
	}

	//4.Inquiry_AllParam（查询所有参数数据）的soap通讯静态方法   返回一个SoapObject对象
	public static SoapObject Set_Param(String sid,String ziduanma,String newValue)
	{
		//方法名
		final String METHOD_NAME ="Set_Param";
		final String SOAP_ACTION ="http://www.suntrans.net/Set_Param";
		//SoapObject detail;
		SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("sid", sid);    //添加参数：设备号
		rpc.addProperty("ziduanma", ziduanma);    //添加参数：设备号
		rpc.addProperty("newValue", newValue);    //添加参数：设备号
		HttpTransportSE ht =new HttpTransportSE(URL,10000);    //设置访问地址，第二个参数是设置超时的毫秒数
		ht.debug =true;
		SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet =true;
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//  Object result = envelope.bodyIn;
		SoapObject result = null;

		try {
			result = (SoapObject)envelope.bodyIn;    //获取结果
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     //result是服务器返回的数据  形式为SoapObject对象
		return result;
	}

}
	 

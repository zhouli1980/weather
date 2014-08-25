package org.zhouli.utils;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.widget.Toast;

public class HttpUtils {
	
	private Context context;
	
//	public HttpUtils(Context context){
//		this.context = context;
//	}
	
	
	public String getReluts(String cityNum){
		
		
		
		HttpGet httpget = new HttpGet("http://m.weather.com.cn/atad/"+ cityNum+".html");
//		HttpGet httpget = new HttpGet("http://www.baidu.com");
//		HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params, 2000);
//		HttpConnectionParams.setSoTimeout(params, 2000);
		HttpClient httpclient = new DefaultHttpClient();
		try{
			HttpResponse response = httpclient.execute(httpget);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String content = EntityUtils.toString(response.getEntity());
				return cityWeatherToUTF8(content);
			}else{
//				Toast.makeText(context,"�������ʧ�ܣ���",Toast.LENGTH_LONG).show();
				System.out.println("�������ʧ�ܣ�");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			httpclient.getConnectionManager();
		}
		return null;
	}
	
	
	public static String cityWeatherToUTF8(String str) throws UnsupportedEncodingException{
		return new String(str.getBytes("UTF-8"),"UTF-8");
	}

}

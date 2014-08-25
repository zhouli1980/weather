package org.zhouli.service;

import java.util.Date;
import java.util.List;

import org.zhouli.activity.WeathersWidget;
import org.zhouli.adapter.db.WeatherDBAdapter;
import org.zhouli.model.CityName;
import org.zhouli.weather.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class UpdateWidgetService extends Service{
	

	
	//记录定时管理器
	AlarmManager alarmmanager;
	PendingIntent pendingintent;
	//定位
	public LocationClient mLocationClient ;
	public MyLocationListener myLocationListener;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("UpdateWidgetService", "starts");
		mLocationClient = new LocationClient(this.getApplicationContext());
		myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.e("Widget_Serivce", "*****************update widget*****************");
		//得到widget布局对象
		RemoteViews views = WeathersWidget.getWeahterMain(this);
		//得到AppWidgetManager widget管理器
		AppWidgetManager appwidgetmanager = AppWidgetManager.getInstance(this);
		
		int[] appids = appwidgetmanager.getAppWidgetIds(new ComponentName(this,WeathersWidget.class));
		
		//定位并获取城市代码======================
		initLocation();//定位初始化
		if(isConnNet()){
			mLocationClient.start();
		}else{
			Toast.makeText(this, R.string.err_get_weather_info, Toast.LENGTH_LONG).show();
			return;
		}
		

		//======================================
		startService(intent);
	}
	
	public class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(final BDLocation location) {
			// TODO Auto-generated method stub
			if(location == null){
				return;
			}else{
				mLocationClient.stop();
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					WeatherDBAdapter weatherdbadapter = new WeatherDBAdapter(UpdateWidgetService.this);
					weatherdbadapter.open();
					
//					CityNameDBAdapter citynamedbadapter = new CityNameDBAdapter(LocationApplication.this);
//					citynamedbadapter.open();
//					List<CityName> citycode = citynamedbadapter.getCityAll();
					System.out.println(location.getCity());
					List<CityName> citycode = weatherdbadapter.getCityCode(location.getCity());
					if(citycode.size() <=0 || citycode == null){
						mLocationClient.stop();
						return;
					}
					
					WeathersWidget.getWeatherJson(weatherdbadapter, citycode.get(0).getCode(),true);
					
				}
//准备给选择地区看天气用
			}).start();

		}
	}

	//定位初始化
	private void initLocation(){
		//初始化
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//��λ����ģʽ
		//百度设置的定位标准
		option.setCoorType("gcj02");
		//定位间隔时间
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		//百度设置的应用程序名称
		option.setProdName("WeatherForecast");
		mLocationClient.setLocOption(option);
	}
	
	public boolean isConnNet(){
		boolean flag = false;
		ConnectivityManager connectivityManger = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectivityManger.getActiveNetworkInfo();
		if(networkinfo != null && networkinfo.isAvailable()){
			flag = true;//已联网

		}else{
//			ProgressActivity.instance.finish();
			Toast.makeText(this, R.string.net_no_works, Toast.LENGTH_LONG).show();
			flag = false;//未联网
		}
		return flag;
	}

	public ComponentName startService(Intent intent){
		Date date = new Date();
		long nowTime = date.getTime();
		long unit = 60000;//间隔一分钟
		int ss = date.getSeconds();//获取秒数
		unit = 60000 - ss * 1000;//将时间精确到秒;
		pendingintent = PendingIntent.getService(this, 0, intent, 0);
		//计时器
		alarmmanager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		//第二个参数是下次启动Service时间
		alarmmanager.set(AlarmManager.RTC_WAKEUP, nowTime + unit, pendingintent);
		return null;
	}
	
}

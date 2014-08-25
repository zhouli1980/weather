package org.zhouli.Location;

import java.util.List;

import org.zhouli.adapter.db.CityNameDBAdapter;
import org.zhouli.adapter.db.WeatherDBAdapter;
import org.zhouli.json.weather.WeatherTransJson;
import org.zhouli.model.CityName;
import org.zhouli.model.CityWeather;
import org.zhouli.utils.HttpUtils;
import org.zhouli.weather.R;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;


public class LocationApplication extends Application {
	
	//百度定位
	public LocationClient mLocationClient ;
	public MyLocationListener myLocationListener;
	public TextView cityTv;
	public String relutss;
	public Button buts;
	public List<CityWeather> list;
	public Handler msgHandler;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);
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
					WeatherDBAdapter weatherdbadapter = new WeatherDBAdapter(LocationApplication.this);
					weatherdbadapter.open();
					
//					CityNameDBAdapter citynamedbadapter = new CityNameDBAdapter(LocationApplication.this);
//					citynamedbadapter.open();
//					List<CityName> citycode = citynamedbadapter.getCityAll();
//					System.out.println(location.getCity());
					List<CityName> citycode = weatherdbadapter.getCityCode(location.getCity());
					if(citycode.size() <=0 || citycode == null){
						mLocationClient.stop();
						return;
					}
					
					getWeatherJson(weatherdbadapter, citycode.get(0).getCode(),true);
					
				}
//准备给选择地区看天气用
			}).start();

		}
	}
	
	public boolean getWeatherJson(WeatherDBAdapter weatherdbadapter,
			int citycode,boolean flag) {
		boolean flags = false;
		WeatherTransJson wtj = new WeatherTransJson();
		HttpUtils hu = new HttpUtils();
		relutss = hu.getReluts(String.valueOf(citycode));
		if("".equals(relutss)){
			Toast.makeText(this, R.string.err_get_weather_info, Toast.LENGTH_LONG).show();
			return flags;
		}
		System.out.println("今日天气-->>" + relutss);
		try {
			CityWeather cityweather = wtj.WeatherTransJson(relutss);
			if(cityweather != null){

				Long reluts = weatherdbadapter.addWeather(cityweather);
				if(reluts == 1 &&  flag == true){
					Message msg = msgHandler.obtainMessage();
					msg.arg1 = R.string.msg_ui_update;
					msgHandler.sendMessage(msg);
				}else if(reluts == 1 &&  flag == false){
					flags = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flags;
	}
	
}






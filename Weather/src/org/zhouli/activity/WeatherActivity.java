package org.zhouli.activity;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONException;
import org.zhouli.Location.LocationApplication;
import org.zhouli.adapter.db.WeatherDBAdapter;
import org.zhouli.json.weather.WeatherTransJson;
import org.zhouli.model.CityWeather;
import org.zhouli.utils.DateUtils;
import org.zhouli.utils.HttpUtils;
import org.zhouli.weather.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;








//import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

//********************************************************************************************


public class WeatherActivity extends Activity {
	
	private static final String TAG= "MainActivity";
	public Toast toast;
	public LocationClient mLocationClicent;
	public TextView tvCity ;
	public TextView tvWeek,tvWind,tvWinds,tvTemperature1,tvTemperature2,tvWeather;
	public TextView tvGw,tvDw,tvGw2,tvDw2,tvToDate,tvToDate2,tvWeeks,tvWeeks2,tvGengxin;
	public TextView tvSheshi1,tvBuolang,tvSheShi2,bshi1,bs,bshi2,bshi12,bs2,bshi22;
	public Button buts;
	public Button bntCityList;
	public List<CityWeather> list;
	public ImageView images,imgaeTomorrow,imgaeTomorrow2;
	public LinearLayout rl;
	final WeatherDBAdapter weatherdbadapter = new WeatherDBAdapter(WeatherActivity.this);
	
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mains_weather);
//		Intent intent = new Intent(WeatherActivity.this, ProgressActivity.class);
//		startActivity(intent);
		//更多按钮(显示城市列表)
		bntCityList = (Button)findViewById(R.id.buts);
		bntCityList.setOnClickListener(new myOnClickListener());
		tvCity = (TextView)findViewById(R.id.tvCity);
		//显示周和日期
		tvWeek = (TextView)findViewById(R.id.tvWeek);
		//风向
		tvWind = (TextView)findViewById(R.id.tvWind);
		//风力
		tvWinds = (TextView)findViewById(R.id.tvWinds);
		//最高温
		tvTemperature1 = (TextView)findViewById(R.id.tvTemperature1);
		tvTemperature2 = (TextView)findViewById(R.id.tvTemperature2);
		//天气
		tvWeather = (TextView)findViewById(R.id.tvWeather);
		//当前图片
		images = (ImageView)findViewById(R.id.images);
		imgaeTomorrow = (ImageView)findViewById(R.id.imgaeTomorrow);
		imgaeTomorrow2 = (ImageView)findViewById(R.id.imgaeTomorrow2);
		//设置高温与低温
		tvGw = (TextView)findViewById(R.id.tvGw);
		tvDw = (TextView)findViewById(R.id.tvDw);
		tvGw2 = (TextView)findViewById(R.id.tvGw2);
		tvDw2 = (TextView)findViewById(R.id.tvDw2);
		tvToDate = (TextView)findViewById(R.id.tvToDate);
		tvToDate2 = (TextView)findViewById(R.id.tvToDate2);
		tvWeeks = (TextView)findViewById(R.id.tvWeeks);
		tvWeeks2 = (TextView)findViewById(R.id.tvWeeks2);
		//更新日期
		tvGengxin = (TextView)findViewById(R.id.tvGengxin);
		tvSheshi1 = (TextView)findViewById(R.id.tvSheshi1);
		tvBuolang = (TextView)findViewById(R.id.tvBuolang);
		tvSheShi2 = (TextView)findViewById(R.id.tvSheShi2);
		bshi1 = (TextView)findViewById(R.id.bshi1);
		bs = (TextView)findViewById(R.id.bs);
		bshi2 = (TextView)findViewById(R.id.bshi2);
		bshi12 = (TextView)findViewById(R.id.bshi12);
		bs2 = (TextView)findViewById(R.id.bs2);
		bshi22 = (TextView)findViewById(R.id.bshi22);
		buts = (Button)findViewById(R.id.buts);
		buts.setVisibility(8);
		rl = (LinearLayout)findViewById(R.id.weatherActivity);
		weatherdbadapter.open();
			//初始界面背景
			rl.setBackgroundResource(R.drawable.sunny_day);
			//启动一个handler进行定位及天气查询
			mLocationClicent = ((LocationApplication)getApplication()).mLocationClient;
			((LocationApplication)getApplication()).cityTv = tvCity;
			((LocationApplication)getApplication()).msgHandler  = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					switch (msg.arg1) {
					case R.string.msg_ui_update:
						//读数据
						list = weatherdbadapter.getWeatherByCityName("1");
						setBackgroundResource(rl);
						setConShow(list);
						break;

					default:
						break;
					}
				}

			};
			initLocation();//初始化定位信息
			//查询是否联网
			if(!isConnNet()){
				return;
			}else{
				Intent intent = new Intent(WeatherActivity.this, ProgressActivity.class);
				startActivity(intent);
				mLocationClicent.start();//如果已联网启动handler
			}

	}

	//设置Activity中各控件显示
	public  void setConShow(List<CityWeather> list){
		if(list.size() <=0 || list== null){
			return;
		}
		int fchh = Integer.valueOf(list.get(0).getFchh());
		//获取月日，星期
		tvWeek.setText(list.get(0).getDate_y().substring(5)+list.get(0).getWeek());	
		tvWind.setText("风向:" + list.get(0).getFx1());
		tvWinds.setText("风力:" + list.get(0).getFl1());
		tvCity.setText(list.get(0).getCity());
		tvTemperature1.setText(list.get(0).getTemp1().substring(0, 2));
		tvTemperature2.setText(list.get(0).getTemp1().substring(4, 6));
		tvWeather.setText(list.get(0).getWeather1());
		//白天与夜晚转换
		if(fchh<18){
			tvGw.setText(list.get(0).getTemp2().substring(0, 2));
			tvDw.setText(list.get(0).getTemp2().substring(4, 6));
			tvGw2.setText(list.get(0).getTemp3().substring(0, 2));
			tvDw2.setText(list.get(0).getTemp3().substring(4, 6));
		}
		else{
			tvGw.setText(list.get(0).getTemp2().substring(4, 6));
			tvDw.setText(list.get(0).getTemp2().substring(0, 2));
			tvGw2.setText(list.get(0).getTemp3().substring(4, 6));
			tvDw2.setText(list.get(0).getTemp3().substring(0, 2));
		}

		tvSheshi1.setText("℃");
		tvBuolang.setText("~");
		tvSheShi2.setText("℃");
		bshi1.setText("℃");
		bs.setText("~");
		bshi2.setText("℃");
		bshi12.setText("℃");
		bs2.setText("~");
		bshi22.setText("℃");
		setTomorrow(list,1);
		setTomorrow(list,2);
		//设置天气图标
		setPic(list);
		buts.setVisibility(0);
		tvGengxin.setText("更新日期:"+getNowTimes());
		ProgressActivity.instance.finish();//关闭进度Activity
	}
	
	//显示未找到信息
	public void showToast(){
		Toast.makeText(WeatherActivity.this, R.string.err_get_weather_info, Toast.LENGTH_LONG).show();
	}
	
	//获取当前日期
	public String getNowTimes(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	//设置明天，后天日期，星期
	public void setTomorrow(List<CityWeather> list,int dayCount){
		if(list.size() <=0 || list == null){
			return;
		}
		
		String[] weeks = new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		
		Date dates = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			dates = formatter.parse(list.get(0).getDate_y());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Date dates = new Date(string)
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dates);
		calendar.add(calendar.DATE, dayCount);//把日期往后增加一天.整数往后推,负数往前移动
		
		dates = calendar.getTime();//这个时间就是日期往后推一天的结果
		SimpleDateFormat formatters = new SimpleDateFormat("MM月dd日");
		String datestr = formatters.format(dates);
		calendar.setTime(dates);
		int days = calendar.get(calendar.DAY_OF_WEEK) - 1 ;
		if(dayCount == 1){
			tvToDate.setText(datestr);
			tvWeeks.setText(weeks[days].toString());
		}else if(dayCount ==2){
			tvToDate2.setText(datestr);
			tvWeeks2.setText(weeks[days].toString());
		}
//		System.out.println(String.valueOf(calendar.get(calendar.DAY_OF_WEEK) - 1));
//		System.out.println(datestr);
		
	}
	
	
	//设置图片
	public void setPic(List<CityWeather> list){
		if(list.size() <=0 || list== null){
			return;
		}
		
		Integer times = DateUtils.getNowTimes();
		String filePath = "day/";
		if(times >= 18 || times<=6 ){
			//晚间图标文件夹
			filePath = "night/";
		}
		loadImages(filePath,times);
	}
	
	//加载各图片
	private void loadImages(String day,int times){
		List<String> listImage = new ArrayList<String>();
		listImage.add(String.valueOf(list.get(0).getImg1()));
		listImage.add(String.valueOf(list.get(0).getImg2()));
		listImage.add(String.valueOf(list.get(0).getImg3()));
		listImage.add(String.valueOf(list.get(0).getImg4()));
		listImage.add(String.valueOf(list.get(0).getImg5()));
		listImage.add(String.valueOf(list.get(0).getImg6()));
		
		if(times <= 18 && times >=6){
			for(int i=0 ;i<listImage.size();i+=2){
				StringBuilder stringbuiler = new StringBuilder();
				stringbuiler.append(day);
				stringbuiler.append(String.format("%02d", Integer.valueOf(listImage.get(i))));//两位数，补0
				stringbuiler.append(".png");
				if(i==0){
					//当前天气
					images.setImageBitmap(getWeatherPic(stringbuiler.toString()));
				}else if(i == 2){
					//第二天天气
					imgaeTomorrow.setImageBitmap(getWeatherPic(stringbuiler.toString()));
				}else if(i == 4){
					//第三天天气
					imgaeTomorrow2.setImageBitmap(getWeatherPic(stringbuiler.toString()));
				}
			}
		}else{
			for(int i=1 ;i<listImage.size();i+=2){
				StringBuilder stringbuiler = new StringBuilder();
				stringbuiler.append(day);
				if(listImage.get(i).equals("99")){
					stringbuiler.append(String.format("%02d", Integer.valueOf(listImage.get(i-1))));
				}else{
					stringbuiler.append(String.format("%02d", Integer.valueOf(listImage.get(i))));
				}
				stringbuiler.append(".png");
				if(i==1){
					images.setImageBitmap(getWeatherPic(stringbuiler.toString()));
				}else if(i == 3){
					imgaeTomorrow.setImageBitmap(getWeatherPic(stringbuiler.toString()));
				}else if(i == 5){
					imgaeTomorrow2.setImageBitmap(getWeatherPic(stringbuiler.toString()));
				}
			}			
		}
	}
	
	//从assets中获取图片
	private Bitmap getWeatherPic(String picName){
		if("".equals(picName)){
			picName = "day/undefined.png";
		}
		Bitmap bitmap = null;
		AssetManager assets = getResources().getAssets();
		try {
			InputStream inputstream = assets.open(picName);
			bitmap = BitmapFactory.decodeStream(inputstream);
			inputstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bitmap;
	}
	
	//设置Activity天气背景
	public void setBackgroundResource(LinearLayout rl){
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		Date dates = new Date(System.currentTimeMillis());
		Integer times = Integer.valueOf(sdf.format(dates));
		String str = list.get(0).getWeather1();
		if(times >= 18 || times<=6){
			//设置晚上背景
			if(str.equals("晴")){
				rl.setBackgroundResource(R.drawable.night);
			}else if(str.equals("多云") || str.equals("晴转多云") || str.equals("多云转晴") || str.equals("阵雨转多云") || str.equals("晴转阴")){
				rl.setBackgroundResource(R.drawable.cloudys);
			}
		}else{
			//设置白天背景
			if(str.equals("晴")){
				rl.setBackgroundResource(R.drawable.sunny_day);
			}else if(str.equals("多云") || str.equals("晴转多云") || str.equals("多云转晴") || str.equals("阵雨转多云") || str.equals("晴转阴")){
				rl.setBackgroundResource(R.drawable.cloudy);
			}else if(str.equals("雾") || str.equals("雾霾")){
				rl.setBackgroundResource(R.drawable.haze);
			}
		}

		if(str.equals("雷阵雨") || str.equals("阴转雷阵雨") || str.equals("中雨转雷阵雨") || str.equals("多云转雷阵雨")){
			rl.setBackgroundResource(R.drawable.thunderstorm);
		}else if(str.equals("雷阵雨转小雨") || str.equals("小雨")){
			rl.setBackgroundResource(R.drawable.rain);
		}else if(str.equals("阴")){
			rl.setBackgroundResource(R.drawable.haze);
		}else if(str.equals("沙尘暴")){
			rl.setBackgroundResource(R.drawable.dust);
		}else if(str.equals("雪")){
			rl.setBackgroundResource(R.drawable.snow);
		}else if(str.equals("多云转阴")){
			rl.setBackgroundResource(R.drawable.cloudys);
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
		mLocationClicent.setLocOption(option);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	//Activity退出停止更新
	protected void onStop() {
		mLocationClicent.stop();//停止定位
		super.onStop();
	}

	//更多按钮单击监听（启动城市列表）
	public class myOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(WeatherActivity.this, CityListActivity.class);
			startActivityForResult(intent, 1);
		}
		
	}
	
	//查询是否已联网信息
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(weatherdbadapter != null){
			weatherdbadapter.close();
		}
	}

	//接收从城市列表中选择并反回的城市名称
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1:
			if(resultCode == Activity.RESULT_OK){
				Bundle bundle = data.getExtras();
				int codes = bundle.getInt("citycode");
				getWeatherJson(weatherdbadapter, Integer.valueOf(codes));
			}
			break;

		default:
			break;
		}
	}

	// 启动AsyncTask进行查询
	private void getWeatherJson(WeatherDBAdapter weatherdbadapter,
			int citycode) {
			toast = Toast.makeText(WeatherActivity.this, "null", Toast.LENGTH_LONG);
			UpdateUI updateui = new UpdateUI(citycode);
			updateui.execute(citycode);
	}
	
	private class UpdateUI extends AsyncTask<Integer, Integer, Integer>{
		
		private int citycode;
		
		
		public UpdateUI(int city){
			this.citycode = city;
		}

		//在AsyncTaskK中先执行此方法
		@SuppressLint("NewApi") @Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			int flags = 0;
			WeatherTransJson wtj = new WeatherTransJson();
			HttpUtils hu = new HttpUtils();
			String reluts = hu.getReluts(String.valueOf(citycode));	
			if(reluts != null && !reluts.isEmpty()){
				System.out.println(reluts);
				try {
					CityWeather cityweather = wtj.WeatherTransJson(reluts);
					if(cityweather != null){

						Long addReluts = weatherdbadapter.addWeather(cityweather);
						if(addReluts == 1){
							flags = Integer.valueOf(addReluts.toString());
						}
					}				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return flags;
		}

		//执行完doInBackground后执行此方法,并接收反回值
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result == 1){
				list = weatherdbadapter.getWeatherByCityName("1");
				setBackgroundResource(rl);
				setConShow(list);
			}else{
				toast.setText(R.string.err_get_weather_info);
				toast.show();
			}
		}

	}
	
	
	
//	@Override
	//菜单
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.weather, menu);
//		return true;
//	}

}

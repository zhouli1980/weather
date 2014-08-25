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

import org.zhouli.adapter.db.WeatherDBAdapter;
import org.zhouli.json.weather.WeatherTransJson;
import org.zhouli.model.CityWeather;
import org.zhouli.service.UpdateTimeService;
import org.zhouli.service.UpdateWidgetService;
import org.zhouli.utils.DateUtils;
import org.zhouli.utils.HttpUtils;
import org.zhouli.weather.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;


public class WeathersWidget extends AppWidgetProvider{
	
	//这是AppWidget
	
	public static Context contexts;
	public static AppWidgetManager appwidgetmanagers;
	public static int[] appids ;
	
	
	
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		//初次添加到桌面后先执行此方法,而且此方法只执行一次
		//这个Service,控制其电子表显示
		context.startService(new Intent(context,UpdateTimeService.class));
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Log.e("WeathersWidget", "onDisabled");
		//将全部AppWidget删除后停止所有的Service
		context.stopService(new Intent(context,UpdateTimeService.class));
		context.stopService(new Intent(context,UpdateWidgetService.class));
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		//此方法是更新UI操作
		contexts = context;
		appwidgetmanagers = appWidgetManager;
		appids = appWidgetIds;
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		appWidgetManager.updateAppWidget(appWidgetIds, getWeahterMain(context));
		context.startService(new Intent(context,UpdateWidgetService.class));
	}
	
	//点击widget的主体来启动activity
	public static RemoteViews getWeahterMain(Context context){
		RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
		//点击widget的主体来启动activity
		Intent intent = new Intent(context, WeatherActivity.class);
		PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.llWeathers, pendingintent);
		return views;
	}
	
	public static boolean getWeatherJson(WeatherDBAdapter weatherdbadapter,
			int citycode,boolean flag) {
		boolean flags = false;
		WeatherTransJson wtj = new WeatherTransJson();
		HttpUtils hu = new HttpUtils();
		String relutss = hu.getReluts(String.valueOf(citycode));
		if("".equals(relutss)){
			return flags;
		}
		System.out.println("我是Widget获取的---->"+relutss);//打印获取的天气信息
		try {
			CityWeather cityweather = wtj.WeatherTransJson(relutss);
			if(cityweather != null){

				Long reluts = weatherdbadapter.addWeather(cityweather);
				weatherdbadapter.open();
				List<CityWeather> list = weatherdbadapter.getWeatherByCityName("1");
				updateUi(list);
				Log.e("数据------>", list.get(0).getDate_y());
			}else{
				return flags;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flags;
	}
	
	//更新UI
	public static void updateUi(List<CityWeather> list){
		RemoteViews views = new RemoteViews(contexts.getPackageName(), R.layout.widget_layout);
		getWeahterPic(list,views);
		views.setTextViewText(R.id.Widget_tvCity, list.get(0).getCity() + " "+ getNowTimes());
//		views.setTextViewText(R.id.tvWendu, list.get(0).getTemp1().subSequence(0, 3));
		views.setTextViewText(R.id.widget_tvWeather, list.get(0).getWeather1());
		views.setTextViewText(R.id.widget_tvWeeks, getNowWeeks(list));
		views.setViewVisibility(R.id.widget_tvAmPm, 8);
		appwidgetmanagers.updateAppWidget(appids, views);
	}
	
	//获取当前日期
	public static String getNowTimes(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
		return sdf.format(new Date());
	}
	
	//获取当前周
	public static String getNowWeeks(List<CityWeather> list){
		if(list.size()<=0 || list == null){
			return "错误";
		}
		String[] weeks = new String[]{"周日","周一","周二","周三","周四","周五","周六"};
		
		Date dates = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			dates = formatter.parse(list.get(0).getDate_y());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dates);
		dates = calendar.getTime();//这个时间就是日期往后推一天的结果
		SimpleDateFormat formatters = new SimpleDateFormat("MM月dd日");
		String datestr = formatters.format(dates);
		calendar.setTime(dates);
		int days = calendar.get(calendar.DAY_OF_WEEK) - 1 ;
		return weeks[days].toString();
	}

	//更新图片
	public static void  getWeahterPic(List<CityWeather> list,RemoteViews views){
		if(list.size()<=0 || list ==null){
			return ;
		}
		//设置去那个文件夹找
		String filePath = "day/";
		views.setTextViewText(R.id.tvWendu, list.get(0).getTemp1().subSequence(0, 3));//取白天的温度
		Integer times = DateUtils.getNowTimes();
		if(times >= 18 || times<=6 ){
			filePath = "night/";
//			views.setTextViewText(R.id.tvWendu, list.get(0).getTemp1().subSequence(4,7));//取晚上的温度
		}
		loadImages(filePath,times,list,views);
	}
	
	//加载各图片
	private static void loadImages(String day,int times,List<CityWeather> list,RemoteViews views){
//		times = 19;
		List<String> listImage = new ArrayList<String>();
		listImage.add(String.valueOf(list.get(0).getImg1()));
		listImage.add(String.valueOf(list.get(0).getImg2()));
		StringBuilder stringbuiler = new StringBuilder();
		stringbuiler.append(day);
		if(listImage.get(1).equals("99")){
			stringbuiler.append(String.format("%02d", Integer.valueOf(listImage.get(0))));
		}else{
			stringbuiler.append(String.format("%02d", Integer.valueOf(listImage.get(1))));
		}
		stringbuiler.append(".png");	

		System.out.println(stringbuiler.toString());
		views.setImageViewBitmap(R.id.widget_imageWeather, getWeatherPic(stringbuiler.toString()));
	}
	
	//从assets中获取图片
	private static  Bitmap getWeatherPic(String picName){
		if("".equals(picName)){
			picName = "day/undefined.png";
		}
		Bitmap bitmap = null;
		AssetManager assets = contexts.getResources().getAssets();
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
	
	//启动电子钟
	public static void startClock(RemoteViews views){

	}
}

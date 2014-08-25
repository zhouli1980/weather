package org.zhouli.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.zhouli.activity.WeathersWidget;
import org.zhouli.weather.R;

import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateTimeService extends Service {
	private Timer timer;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		timer = new Timer();
		timer.schedule(new MyTimer(), 0, 1000);
	}
	
	

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		timer.cancel();
		timer = null;
		super.onDestroy();
	}
	
	private final class MyTimer extends TimerTask{

		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String time = sdf.format(new Date());
			//获取Widget管理器
			AppWidgetManager appManager = AppWidgetManager.getInstance(getApplicationContext());
			//appManager所操作的Widget对应的远程视图当前widget_layout文件
			RemoteViews views = new RemoteViews(getPackageName(),R.layout.widget_layout);
			views.setTextViewText(R.id.widget_tvTimes, time);
			ComponentName comName = new ComponentName(getApplicationContext(),WeathersWidget.class);
			appManager.updateAppWidget(comName, views);
		}
	}
}

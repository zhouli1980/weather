package org.zhouli.adapter.db;

import java.util.List;

import org.zhouli.dao.WeatherDAO;
import org.zhouli.model.CityName;
import org.zhouli.model.CityWeather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDBAdapter {
	
	
	private SQLiteDatabase sqlitedatabase;
	
	private Context context;
	
	private WeatherDAO weatherDao;
	
	private DatabaseDBAdapter databaseDBAdapter;
	
	public WeatherDBAdapter(Context context){
		this.context = context;
		weatherDao = new WeatherDAO();
	}
	
	public void open(){
		databaseDBAdapter = new DatabaseDBAdapter(context);
		databaseDBAdapter.open();
		sqlitedatabase = databaseDBAdapter.getsQLiteDatabase();
	}
	
	public void close(){
		databaseDBAdapter.close();
	}
	
	public List<CityWeather> getCityWeather(){
		return weatherDao.getWeather(sqlitedatabase);
	}
	
	public Long addWeather(CityWeather cityweather){
		return weatherDao.addWeather(sqlitedatabase, cityweather);
	}
	
	public Integer delWeather(){
		return weatherDao.delWeather(sqlitedatabase);
	}
	
	public List<CityWeather> getWeatherByCityName(String cityName){
		return weatherDao.getWeatherByCityName(sqlitedatabase, cityName);
	}
	
	public List<CityName> getCityCode(String cityName){
		return weatherDao.getCityCode(sqlitedatabase, cityName);
	}
	
	
	
}

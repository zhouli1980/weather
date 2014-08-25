package org.zhouli.dao;

import java.util.ArrayList;
import java.util.List;

import org.zhouli.model.CityName;
import org.zhouli.model.CityWeather;
import org.zhouli.utils.DateUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class WeatherDAO {
	
	//��ȡ���ݿ���ȫ������
	public List<CityWeather> getWeather(SQLiteDatabase sqlitedatabase){
		try{
			
			List<CityWeather> listWeather = new ArrayList<CityWeather>();
			Cursor cursor = sqlitedatabase.query(CityWeather.TABLE_NAME, 
					null, null, null, null, null, null);
			while(cursor.moveToNext()){
				listWeather.add(getCityWeatherByCursor(cursor));
			}
			return listWeather;
 		}catch(Exception err){
			return null;
		}
	}
	
	//获取第一条天气 
	public List<CityWeather> getWeatherByCityName(SQLiteDatabase sqlitedatabase,String cityName){
		try{
			List<CityWeather> listWeather = new ArrayList<CityWeather>();
			String andText = cityName;
			String nowDate = DateUtils.getNowDate();
			Cursor cursor = sqlitedatabase.query(CityWeather.TABLE_NAME, null, "id = ?",
					new String[]{cityName},null,null,null);
			
			while(cursor.moveToNext()){
				listWeather.add(getCityWeatherByCursor(cursor));
			}
			return listWeather;
		}catch(Exception err){
			Log.e("getWeatherByCityName", err.getMessage());
		}
		return null;
	}
	
	//添加
	public Long addWeather(SQLiteDatabase sqlitedatabase,CityWeather cityWeather){
		try{
			if(cityWeather == null){
				return (long) -1;
			}
			if(delWeather(sqlitedatabase)<0){
				return (long)-2;
			}

			ContentValues conValues = new ContentValues();
			conValues.put("city", cityWeather.getCity());
			conValues.put("city_en", cityWeather.getCity_en());
			conValues.put("date_y", cityWeather.getDate_y());
			conValues.put("week", cityWeather.getWeek());
			conValues.put("fchh", cityWeather.getFchh());
			conValues.put("cityid", cityWeather.getCityid());
			conValues.put("temp1", cityWeather.getTemp1());
			conValues.put("temp2", cityWeather.getTemp2());
			conValues.put("temp3", cityWeather.getTemp3());
			conValues.put("tempf1", cityWeather.getTempf1());
			conValues.put("tempf2", cityWeather.getTempf2());
			conValues.put("tempf3", cityWeather.getTempf3());
			conValues.put("weather1",cityWeather.getWeather1());
			conValues.put("weather2", cityWeather.getWeather2());
			conValues.put("weather3", cityWeather.getWeather3());
			conValues.put("img1", cityWeather.getImg1());
			conValues.put("img2", cityWeather.getImg2());
			conValues.put("img3", cityWeather.getImg3());
			conValues.put("img4", cityWeather.getImg4());
			conValues.put("img5", cityWeather.getImg5());
			conValues.put("img6", cityWeather.getImg6());
			conValues.put("img_single", cityWeather.getImg_single());
			conValues.put("img_title1", cityWeather.getImg_title1());
			conValues.put("img_title2", cityWeather.getImg_title2());
			conValues.put("img_title3", cityWeather.getImg_title3());
			conValues.put("img_title4", cityWeather.getImg_title4());
			conValues.put("img_title5", cityWeather.getImg_title5());
			conValues.put("img_title6", cityWeather.getImg_title6());
			conValues.put("img_title_single", cityWeather.getImg_title_single());
			conValues.put("wind1", cityWeather.getWind1());
			conValues.put("wind2", cityWeather.getWind2());
			conValues.put("wind3", cityWeather.getWind3());
			conValues.put("wind4", cityWeather.getWind4());
			conValues.put("wind5", cityWeather.getWind5());
			conValues.put("wind6", cityWeather.getWind6());
			conValues.put("fx1", cityWeather.getFx1());
			conValues.put("fx2", cityWeather.getFx2());
			conValues.put("fl1", cityWeather.getFl1());
			conValues.put("fl2", cityWeather.getFl2());
			conValues.put("fl3", cityWeather.getFl3());
			conValues.put("indexs", cityWeather.getIndexs());
			conValues.put("index_d", cityWeather.getIndex_d());
			conValues.put("index48", cityWeather.getIndex48());
			conValues.put("index48_d", cityWeather.getIndex48_d());
			conValues.put("index_uv", cityWeather.getIndex_uv());
			conValues.put("index48_uv", cityWeather.getIndex48_uv());
			conValues.put("index_xc", cityWeather.getIndex_xc());
			conValues.put("index_tr", cityWeather.getIndex_tr());
			conValues.put("index_co", cityWeather.getIndex_co());
			conValues.put("index_cl", cityWeather.getIndex_cl());
			conValues.put("index_ls", cityWeather.getIndex_ls());
			conValues.put("index_ag", cityWeather.getIndex_ag());
			
			Long reluts = sqlitedatabase.insert(CityWeather.TABLE_NAME, null, conValues);
			return reluts;
		}catch(Exception err){
			err.printStackTrace();
			return (long) -1;
		}
	}
	
	public Integer delWeather(SQLiteDatabase sqlitedatabase){
		try{
//			Integer reluts = sqlitedatabase.delete(CityWeather.TABLE_NAME, null, new String[]{});
			String sqlStr = "DELETE FROM "+ CityWeather.TABLE_NAME +";";
			String sqlStr2 = "DELETE FROM sqlite_sequence";
			sqlitedatabase.execSQL(sqlStr);
			sqlitedatabase.execSQL(sqlStr2);
			return 1;
		}catch(Exception err){
			err.printStackTrace();
			return -2;
		}
	}

	//��ֵ��model
	private CityWeather getCityWeatherByCursor(Cursor cursor){
		CityWeather weather = new CityWeather();
		weather.setId(cursor.getInt(cursor.getColumnIndex("id")));
		weather.setCity(cursor.getString(cursor.getColumnIndex("city")));
		weather.setCity_en(cursor.getString(cursor.getColumnIndex("city_en")));
		weather.setDate_y(cursor.getString(cursor.getColumnIndex("date_y")));
		weather.setWeek(cursor.getString(cursor.getColumnIndex("week")));
		weather.setFchh(cursor.getInt(cursor.getColumnIndex("fchh")));
		weather.setCityid(cursor.getInt(cursor.getColumnIndex("cityid")));
		weather.setTemp1(cursor.getString(cursor.getColumnIndex("temp1")));
		weather.setTemp2(cursor.getString(cursor.getColumnIndex("temp2")));
		weather.setTemp3(cursor.getString(cursor.getColumnIndex("temp3")));
		weather.setTempf1(cursor.getString(cursor.getColumnIndex("tempF1")));
		weather.setTempf2(cursor.getString(cursor.getColumnIndex("tempF2")));
		weather.setTempf3(cursor.getString(cursor.getColumnIndex("tempF3")));
		weather.setWeather1(cursor.getString(cursor.getColumnIndex("weather1")));
		weather.setWeather2(cursor.getString(cursor.getColumnIndex("weather2")));
		weather.setWeather3(cursor.getString(cursor.getColumnIndex("weather3")));
		weather.setImg1(cursor.getInt(cursor.getColumnIndex("img1")));
		weather.setImg2(cursor.getInt(cursor.getColumnIndex("img2")));
		weather.setImg3(cursor.getInt(cursor.getColumnIndex("img3")));
		weather.setImg4(cursor.getInt(cursor.getColumnIndex("img4")));
		weather.setImg5(cursor.getInt(cursor.getColumnIndex("img5")));
		weather.setImg6(cursor.getInt(cursor.getColumnIndex("img6")));
		weather.setImg_single(cursor.getString(cursor.getColumnIndex("img_single")));
		weather.setImg_title1(cursor.getString(cursor.getColumnIndex("img_title1")));
		weather.setImg_title2(cursor.getString(cursor.getColumnIndex("img_title2")));
		weather.setImg_title3(cursor.getString(cursor.getColumnIndex("img_title3")));
		weather.setImg_title4(cursor.getString(cursor.getColumnIndex("img_title4")));
		weather.setImg_title5(cursor.getString(cursor.getColumnIndex("img_title5")));
		weather.setImg_title6(cursor.getString(cursor.getColumnIndex("img_title6")));
		weather.setImg_title_single(cursor.getString(cursor.getColumnIndex("img_title_single")));
		weather.setWind1(cursor.getString(cursor.getColumnIndex("wind1")));
		weather.setWind2(cursor.getString(cursor.getColumnIndex("wind2")));
		weather.setWind3(cursor.getString(cursor.getColumnIndex("wind3")));
		weather.setWind4(cursor.getString(cursor.getColumnIndex("wind4")));
		weather.setWind5(cursor.getString(cursor.getColumnIndex("wind5")));
		weather.setWind6(cursor.getString(cursor.getColumnIndex("wind6")));
		weather.setFx1(cursor.getString(cursor.getColumnIndex("fx1")));
		weather.setFx2(cursor.getString(cursor.getColumnIndex("fx2")));
		weather.setFl1(cursor.getString(cursor.getColumnIndex("fl1")));
		weather.setFl2(cursor.getString(cursor.getColumnIndex("fl2")));
		weather.setFl3(cursor.getString(cursor.getColumnIndex("fl3")));
		weather.setIndexs(cursor.getString(cursor.getColumnIndex("indexs")));
		weather.setIndex_d(cursor.getString(cursor.getColumnIndex("index_d")));
		weather.setIndex48(cursor.getString(cursor.getColumnIndex("index48")));
		weather.setIndex48_d(cursor.getString(cursor.getColumnIndex("index48_d")));
		weather.setIndex_uv(cursor.getString(cursor.getColumnIndex("index_uv")));
		weather.setIndex48_uv(cursor.getString(cursor.getColumnIndex("index48_uv")));
		weather.setIndex_xc(cursor.getString(cursor.getColumnIndex("index_xc")));
		weather.setIndex_tr(cursor.getString(cursor.getColumnIndex("index_tr")));
		weather.setIndex_co(cursor.getString(cursor.getColumnIndex("index_co")));
		weather.setIndex_cl(cursor.getString(cursor.getColumnIndex("index_cl")));
		weather.setIndex_ls(cursor.getString(cursor.getColumnIndex("index_ls")));
		weather.setIndex_ag(cursor.getString(cursor.getColumnIndex("index_ag")));
		return weather;
	}

	
	//查询城市代码
	public List<CityName> getCityCode(SQLiteDatabase sqlitedatabase,String cityName){
		
		List<CityName> listCityName = new ArrayList<CityName>();
		String cityCode = "0";
//		cityName = "呼和浩特";
		if(cityName.equals("")){
			return null;
		}
		try{
			String citys = "";
			if(cityName.length() <= 3){
				citys = "%"+cityName.substring(0,2)+"%";
			}else{
				citys = "%"+ cityName.substring(0,3) +"%";
			}
			Cursor cursor = sqlitedatabase.query(CityName.TABLE_NAME, null, "county like ?",
					new String[]{citys},null,null,null);
			
			while(cursor.moveToNext()){
				listCityName.add(getCityName(cursor));
			}
			
		}catch(Exception err){
			Log.e("getWeatherByCityName", err.getMessage());
		}
		return listCityName;
	}
	
	public CityName getCityName(Cursor cursor){
		CityName cityname = new CityName();
		cityname.setId(cursor.getInt(Integer.valueOf(cursor.getColumnIndex("id"))));
		cityname.setCity(cursor.getString(cursor.getColumnIndex("city")));
		cityname.setCode(cursor.getInt(Integer.valueOf(cursor.getColumnIndex("code"))));
		cityname.setProvince(cursor.getString(cursor.getColumnIndex("province")));
		cityname.setCounty(cursor.getString(cursor.getColumnIndex("county")));
		return cityname;
	}
}

package org.zhouli.dao;

import java.util.ArrayList;
import java.util.List;

import org.zhouli.model.CityName;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityDAO {
	
	//获取全部城市信息
	public List<CityName> getCityAll(SQLiteDatabase sqlitedatabase){
		try{
			List<CityName> listCity = new ArrayList<CityName>();
			Cursor cursor = sqlitedatabase.query(CityName.TABLE_NAME,null,null,null,null,null,null);
			while(cursor.moveToNext()){
				listCity.add(getCityName(cursor));
			}
			return listCity;
		}catch(Exception err){
			Log.e("getCityAll()", err.getMessage());
		}
		return null;
	}
	
	//获取指定城市信息
	public List<CityName> getCityByName(SQLiteDatabase sqlitedatabase,String cityname){
		try{
			String citys = "%" + cityname + "%";
			List<CityName> list = new ArrayList<CityName>();
			Cursor cursor = sqlitedatabase.query(CityName.TABLE_NAME, null, "county like ?", new String[]{citys}, 
					null, null, null);
			while(cursor.moveToNext()){
				list.add(getCityName(cursor));
			}
			return list;
		}catch(Exception err){
			Log.e("getCityByName()", err.getMessage());
		}
		return null;
	}
	
	
	
	//赋值model
	public CityName getCityName(Cursor cursor){
		CityName city = new CityName();
		city.setId(cursor.getInt(cursor.getColumnIndex("id")));
		city.setCity(cursor.getString(cursor.getColumnIndex("city")));
		city.setCode(cursor.getInt(cursor.getColumnIndex("code")));
		city.setCounty(cursor.getString(cursor.getColumnIndex("county")));
		city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
		return city;
	}

}

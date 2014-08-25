package org.zhouli.adapter.db;

import java.util.List;

import org.zhouli.dao.CityDAO;
import org.zhouli.model.CityName;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CityNameDBAdapter {
	
	
	private SQLiteDatabase sqlitedatabase;
	
	private Context context;
	
	private CityDAO citynamedao;
	
	private DatabaseDBAdapter databasedbadapter;
	
	public CityNameDBAdapter(Context context){
		this.context = context;
		citynamedao = new CityDAO();
	}
	
	public void open(){
		databasedbadapter = new DatabaseDBAdapter(context);
		databasedbadapter.open();
		sqlitedatabase = databasedbadapter.getsQLiteDatabase();
	}
	
	public void close(){
		databasedbadapter.close();
	}
	
	public List<CityName> getCityAll(){
		return citynamedao.getCityAll(sqlitedatabase);
	}
	
}

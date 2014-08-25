package org.zhouli.adapter.db;

import java.util.List;

import org.zhouli.dao.CityDAO;
import org.zhouli.dbhelper.SqliteHelper;
import org.zhouli.model.CityName;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseDBAdapter {
	
	public static final String TAG="DatabaseDBAdapter";
	
	public static final String DATABASE_NAME="weathers.db";
	
	private SQLiteDatabase sqlitedatabase;
	
	private Context context;
	
	private SqliteHelper dbHelper;
	
	
	public SQLiteDatabase getsQLiteDatabase(){
		return sqlitedatabase;
	}
	
	public void setsQLiteDatabase(SQLiteDatabase sQLiteDatabase){
		this.sqlitedatabase = sQLiteDatabase;
	}
	
	public DatabaseDBAdapter(Context context){
		this.context=context;
	}
	
	public void open(){
		dbHelper = new SqliteHelper(context,DATABASE_NAME,null,1);
		sqlitedatabase = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}

}

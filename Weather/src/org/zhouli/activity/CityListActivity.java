package org.zhouli.activity;

import java.util.ArrayList;
import java.util.List;

import org.zhouli.adapter.db.CityNameDBAdapter;
import org.zhouli.model.CityName;
import org.zhouli.weather.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CityListActivity extends Activity {
	
	ListView listview;
	
	public List<CityName> cityList = new  ArrayList<CityName>();
	
	public LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_name);
		
		listview = (ListView)findViewById(R.id.listCity);
		//鑾峰彇鍏ㄩ儴鍩庡競鍚嶇О
		CityNameDBAdapter citynamedbadapter = new CityNameDBAdapter(CityListActivity.this);
		citynamedbadapter.open();
		
		cityList = citynamedbadapter.getCityAll();
		//濉厖list
		listview.setAdapter(new ListAdapter(cityList));
		listview.setOnItemClickListener(new onClickListLisenter());
		
		
	}
	
	public class onClickListLisenter implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long id) {
			// TODO Auto-generated method stub
			//鐐归�夋煇鍩庡競鍚庤繑鍥濿eathersActivity
			CityName cityname = cityList.get(pos);
			Intent intent = new Intent(CityListActivity.this, WeatherActivity.class);
			intent.putExtra("citycode", cityname.getCode());
			setResult(RESULT_OK, intent);
			finish();
//			Toast.makeText(CityListActivity.this, cityname.getCounty() + cityname.getCode(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	//listview 鍏呭～鏁版嵁
	public class ListAdapter extends BaseAdapter{

		View[] itemViews;
		
		public ListAdapter(List<CityName> list){
			itemViews = new View[list.size()];
			for(int i=0;i<list.size();i++){
				CityName getName = (CityName)list.get(i);//鑾峰彇绗琁涓璞�
				itemViews[i] = makeItemView(getName.getCounty());
				
			}
		}
		
		public View makeItemView(String strTitle){
			inflater = (LayoutInflater)CityListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(R.layout.city_items, null);
			TextView tvName = (TextView)itemView.findViewById(R.id.tvItemCity);
			tvName.setText(strTitle);
			return itemView;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemViews.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemViews[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
                           
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//convertView涓簄ull鐨勬椂鍊欏垵濮嬪寲convertView銆�  
			ViewHolder viewholder = null;
 			if(convertView == null){
 				viewholder = new ViewHolder();
 				//鍔犺浇ITEMS
 				convertView = inflater.inflate(R.layout.city_items, null);
 				//鍒濆鍖栨湭鏄剧ず鐨則extview
 				viewholder.tvName = (TextView)convertView.findViewById(R.id.tvItemCity);
 				convertView.setTag(viewholder);
			}else{
				viewholder = (ViewHolder)convertView.getTag();
			}
 			//璁剧疆鏈樉绀虹殑鍚嶇О
 			viewholder.tvName.setText(cityList.get(position).getCounty());
 			return convertView;
		}
		
		public final class ViewHolder{
			public TextView tvName;
		}
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.city_list, menu);
//		return true;
//	}

}

package org.zhouli.json.weather;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.zhouli.model.CityWeather;

import android.util.Log;

public class WeatherTransJson {
	//获取JSON,与解析JSON
	public CityWeather WeatherTransJson (String reluts) throws JSONException{
		if(reluts.equals("")){
			return null;
		}
		CityWeather cityweather = new CityWeather();
		
		try{
			JSONObject json = new JSONObject(reluts);
			JSONObject jsons = json.getJSONObject("weatherinfo");
			cityweather = getWeathers(jsons);//��ȡjson����

//			return cityweather;	
		}catch(Exception err){
			Log.e("jsonTrans", err.getMessage());
			System.out.println(err.getMessage());
			return null;
		}
		return cityweather;
	}

	public CityWeather getWeathers(JSONObject json){
		if(json == null){
			return null;
		}
		CityWeather cityweather = new CityWeather();
		try{

			cityweather.setCity(json.getString("city"));
			cityweather.setCity_en(json.getString("city_en"));
			cityweather.setCityid(json.getInt("cityid"));
			cityweather.setDate_y(json.getString("date_y"));
			cityweather.setWeek(json.getString("week"));
			cityweather.setFchh(json.getInt("fchh"));
			cityweather.setTemp1(json.getString("temp1"));
			cityweather.setTemp2(json.getString("temp2"));
			cityweather.setTemp3(json.getString("temp3"));
 			cityweather.setTempf1(json.getString("tempF1"));
			cityweather.setTempf2(json.getString("tempF2"));
			cityweather.setTempf3(json.getString("tempF3"));
			cityweather.setWeather1(json.getString("weather1"));
			cityweather.setWeather2(json.getString("weather2"));
			cityweather.setWeather3(json.getString("weather3"));
			cityweather.setImg1(json.getInt("img1"));
			cityweather.setImg2(json.getInt("img2"));
			cityweather.setImg3(json.getInt("img3"));
			cityweather.setImg4(json.getInt("img4"));
			cityweather.setImg5(json.getInt("img5"));
			cityweather.setImg6(json.getInt("img6"));
			cityweather.setImg_single(json.getString("img_single"));
			cityweather.setImg_title1(json.getString("img_title1"));
			cityweather.setImg_title2(json.getString("img_title2"));
			cityweather.setImg_title3(json.getString("img_title3"));
			cityweather.setImg_title4(json.getString("img_title4"));
			cityweather.setImg_title5(json.getString("img_title5"));
			cityweather.setImg_title6(json.getString("img_title6"));
			cityweather.setImg_title_single(json.getString("img_title_single"));
			cityweather.setWind1(json.getString("wind1"));
			cityweather.setWind2(json.getString("wind2"));
			cityweather.setWind3(json.getString("wind3"));
			cityweather.setWind4(json.getString("wind4"));
			cityweather.setWind5(json.getString("wind5"));
			cityweather.setWind6(json.getString("wind6"));
			cityweather.setFx1(json.getString("fx1"));
			cityweather.setFx2(json.getString("fx2"));
			cityweather.setFl1(json.getString("fl1"));
			cityweather.setFl2(json.getString("fl2"));
			cityweather.setFl3(json.getString("fl3"));
			cityweather.setIndexs(json.getString("index"));
			cityweather.setIndex_d(json.getString("index_d"));
			cityweather.setIndex48(json.getString("index48"));
			cityweather.setIndex48_d(json.getString("index48_d"));
			cityweather.setIndex48_uv(json.getString("index48_uv"));
			cityweather.setIndex_xc(json.getString("index_xc"));
			cityweather.setIndex_tr(json.getString("index_tr"));
			cityweather.setIndex_co(json.getString("index_co"));
			cityweather.setIndex_cl(json.getString("index_cl"));
			cityweather.setIndex_ls(json.getString("index_ls"));
			cityweather.setIndex_ag(json.getString("index_ag"));
//			return cityweather;
		}catch(Exception err){
			Log.e("jsonTrans", err.getMessage());
			System.out.println(err.getMessage());
			return null;
		}
		return cityweather;

		
	}

	

}

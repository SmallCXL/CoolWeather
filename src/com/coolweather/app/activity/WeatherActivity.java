package com.coolweather.app.activity;

import com.coolweather.app.utils.HttpCallbackListener;
import com.coolweather.app.utils.HttpUtil;
import com.coolweather.app.utils.Utility;
import com.example.coolweather.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity {
	
	private LinearLayout weatherInfoLayout;
	private TextView cityName;
	private TextView publishText;
	private TextView weatherDesp;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDate;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_info_layout);
		//各种控件初始化
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		cityName = (TextView)findViewById(R.id.city_name);
		publishText = (TextView)findViewById(R.id.publish_text);
		weatherDesp = (TextView)findViewById(R.id.weather_desp);
		temp1Text = (TextView)findViewById(R.id.temp1);
		temp2Text = (TextView)findViewById(R.id.temp2);
		currentDate = (TextView)findViewById(R.id.current_date);		
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//有县级代号就去查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityName.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
			//Toast.makeText(WeatherActivity.this, "同步中", Toast.LENGTH_SHORT).show();
		}
		else{
			//没有县级代号，查询本地的天气
			//Toast.makeText(WeatherActivity.this, "同步 失败 读取本地", Toast.LENGTH_SHORT).show();

			showWeather();
		}
	}

/*
 * 组装查询天气代号所需的网址
 */
	private void queryWeatherCode(String countyCode) {
		// TODO Auto-generated method stub
		String address = new StringBuilder().append("http://www.weather.com.cn/data/list3/city")
				.append(countyCode).append(".xml").toString();
		
		queryFromServer(address,"countyCode");
	}
/*
 * 根据传入的地址和类型向服务器查询天气代号或者天气信息
 */
	private void queryFromServer(final String address, final String code) {
		// TODO Auto-generated method stub
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub
				if("countyCode".equals(code)){
					//此时返回的response数据格式应该是   190404|101190404 将该结果分割并取出天气代码
					if(!TextUtils.isEmpty(response)){
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							queryWeatherInfo(array[1]);							
						}
					}
				}//end if("conutyCode".equals(code))
				else if("weatherCode".equals(code)){
					//此时返回的response数据应该是JSON的格式天气数据，将该结果解析并保存
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}//end run					
					});//end Thread
				}//end else if("weatherCode".equals(code))
			}//end onFinish

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("同步失败");
					}//end run					
				});//end Thread				
			}//end onError	
		});//end HttpUtil.sendHttpRequest
	}
/*
 * 组装查询天气信息所需的网址
 */
	private void queryWeatherInfo(String weatherCode) {
		// TODO Auto-generated method stub
		String address = new StringBuilder().append("http://www.weather.com.cn/data/cityinfo/")
				.append(weatherCode).append(".html").toString();
		
		queryFromServer(address,"weatherCode");
	}	
/*
 * 从SharedPreference中读取存储的天气信息并显示
 */
	private void showWeather() {
		// TODO Auto-generated method stub
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		cityName.setText(pref.getString("city_name", ""));
		temp1Text.setText(pref.getString("temp1", ""));
		temp2Text.setText(pref.getString("temp2", ""));
		weatherDesp.setText(pref.getString("weather_desp", ""));		
		publishText.setText(pref.getString("publish_time", "")+"发布");
		currentDate.setText(pref.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityName.setVisibility(View.VISIBLE);
	}	
	
}

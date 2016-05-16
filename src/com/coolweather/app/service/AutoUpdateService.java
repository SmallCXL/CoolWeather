package com.coolweather.app.service;

import com.coolweather.app.receiver.AutoUpdateReceiver;
import com.coolweather.app.utils.HttpCallbackListener;
import com.coolweather.app.utils.HttpUtil;
import com.coolweather.app.utils.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override 
	public int onStartCommand(Intent intent, int flags, int startId){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateWeather();
			}
		}).start();
		
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		int anHour = 60 * 60 * 1000;
		long triggerAtTime = 4 * anHour + SystemClock.elapsedRealtime();
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(i, flags, startId);
	}
	protected void updateWeather() {
		// TODO Auto-generated method stub
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = pref.getString("weather_code", "");
		String address = new StringBuilder().append("http://www.weather.com.cn/data/cityinfo/")
				.append(weatherCode).append(".html").toString();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
			
		});
	}
}

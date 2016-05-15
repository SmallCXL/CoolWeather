package com.coolweather.app.utils;

import android.text.TextUtils;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
/*
 * 用来解析和处理服务器返回的省级数据，并保存至数据库中
 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0){
				for(String p : allProvinces){
					Province province = new Province();
					String[] array = p.split("\\|");
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}//end for
				return true;
			}//end if(allProvince != null && allProvince.length>0)
		}//end if(!TextUtils.isEmpty(response))
				return false;	
	}
	/*
	 * 用来解析和处理服务器返回的城市数据，并保存至数据库中
	 */	
	public synchronized static boolean handleCitiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length > 0){
				for(String c : allCities){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProviceId(provinceId);
					coolWeatherDB.saveCity(city);
				}//end for
				return true;
			}//end if(allCities != null && allCities.length>0)
		}//end if(!TextUtils.isEmpty(response))
				return false;
	}
	/*
	 * 用来解析和处理服务器返回的县级数据，并保存至数据库中
	 */	
	public synchronized static boolean handleCountiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0){
				for(String c : allCounties){
					County county = new County();
					String[] array = c.split("\\|");
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}//end for
				return true;
			}
		}//end if(!TextUtils.isEmpty(response))
				return false;
	}
	
}

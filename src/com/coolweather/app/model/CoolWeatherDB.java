package com.coolweather.app.model;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {

	/*
	 * ���ݿ��� 
	 */
	public static final String DB_NAME = "cool_weather";
	/*
	 * ���ݿ�汾�� 
	 */
	public static final int VERSION = 1;
	/*
	 * ˽�б���
	 */
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	/*
	 * ���췽��˽�л���ȷ��ֻ��һ��CoolWeatherDB��ʵ��
	 */
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/*
	 * ��ȡCoolWeatherDB��ʵ��
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);			
		}
		return coolWeatherDB;
	}
	/*
	 * �����ݿ��ж�ȡȫ�����е�ʡ����Ϣ
	 */
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));//ȡ����Ϊ"id"��һ�е�����
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();			
		}
		return list;	
	}
	/*
	 * ��Provinceʵ���洢�����ݿ�
	 */
	public void saveProvince(Province province){
		if (province!=null){
			ContentValues value = new ContentValues();
			value.put("province_name", province.getProvinceName());
			value.put("province_code", province.getProvinceCode());
			db.insert("Province", null, value);
		}
	}
	/*
	 * �����ݿ��ж�ȡ��Ӧʡ�ݵĳ�����Ϣ
	 */
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = 
				db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setProviceId(provinceId);
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	/*
	 * ��Cityʵ���洢�����ݿ���
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues value = new ContentValues();
			value.put("city_name", city.getCityName());
			value.put("city_code", city.getCityCode());
			value.put("province_id", city.getProvinceId());
			db.insert("City", null, value);
		}
	}
	/*
	 * �����ݿ��ж�ȡ��Ӧ���е�����Ϣ
	 */
	public List<County> loadCounties(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor =
			db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.setCityId(cityId);
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				list.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	/*
	 * ��Countyʵ���洢�����ݿ���
	 */
	public void saveCounty(County county){
		if(county != null){
			ContentValues value = new ContentValues();
			value.put("city_id", county.getCityId());
			value.put("county_name",county.getCountyName());
			value.put("county_code", county.getCountyCode());
			db.insert("County", null, value);
		}
	}
	
	
	
	
	
	
	
	
}

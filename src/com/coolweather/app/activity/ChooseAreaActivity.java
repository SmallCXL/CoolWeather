package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.utils.HttpCallbackListener;
import com.coolweather.app.utils.HttpUtil;
import com.coolweather.app.utils.Utility;
import com.example.coolweather.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	
	public static final int PROVINCE = 0;
	public static final int CITY = 1;
	public static final int COUNTY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	/*
	 * �����б�
	 */
	private List<String> dataList = new ArrayList<String>();
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	/*
	 * ѡ�е�����
	 */
	private Province seletedProvince;
	private City seletedCity;
	private County seletedCounty;
	
	private int currentLevel = PROVINCE;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		listView = (ListView)findViewById(R.id.list_view);
		titleText = (TextView)findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		
		queryProvinces();
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if(currentLevel == PROVINCE){
					seletedProvince = provinceList.get(index);
					queryCities();
				}
				else if(currentLevel == CITY){
					seletedCity = cityList.get(index);
					queryCounties();					
				}
			}
		});
		
		
	}
/*
 * �������������������ݿ��м����������������
 */
	private void queryProvinces() {
		// TODO Auto-generated method stub
		provinceList = coolWeatherDB.loadProvinces();
		if(provinceList.size() > 0){
			dataList.clear();
			for(Province p : provinceList){
				dataList.add(p.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = PROVINCE;
		}//end if
		else{
			queryFromServer(null, "province");
		}
	}
	
	private void queryCities() {
		// TODO Auto-generated method stub		
		cityList = coolWeatherDB.loadCities(seletedProvince.getId());
		if(cityList.size() > 0){
			dataList.clear();
			for(City c : cityList){
				dataList.add(c.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);			
			titleText.setText(seletedProvince.getProvinceName());
			currentLevel = CITY;			
		}
		else{
			queryFromServer(seletedProvince.getProvinceCode(), "city");
		}
	}
	
	private void queryCounties() {
		// TODO Auto-generated method stub
		countyList = coolWeatherDB.loadCounties(seletedCity.getId());
		if(countyList.size() > 0){
			dataList.clear();
			for(County c : countyList){
				dataList.add(c.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);			
			titleText.setText(seletedCity.getCityName());
			currentLevel = COUNTY;			
		}
		else{
			queryFromServer(seletedCity.getCityCode(), "county");
		}		
	}
/*
 *  �����ݿ����Ҳ���������ݻ��ߵ�һ�μ��أ������queryFromServer������������������������
 */
	private void queryFromServer(final String code, final String type) {
		// TODO Auto-generated method stub
		String address;
		if (!TextUtils.isEmpty(code)){
			address = new StringBuilder().append("http://www.weather.com.cn/data/list3/city").
					append(code).append(".xml").toString();
		}
		else{
			address = new StringBuilder().append("http://www.weather.com.cn/data/list3/city.xml").toString();
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				if(type.equals("province")){
					result = Utility.handleProvincesResponse(coolWeatherDB, response);
				}
				else if(type.equals("city")){
					result = Utility.handleCitiesResponse(coolWeatherDB, response, seletedProvince.getId());				
				}
				else if(type.equals("county")){
					result = Utility.handleCountiesResponse(coolWeatherDB, response, seletedCity.getId());				
				}
				//�����Ѿ�������ϣ��������µ��ò�ѯ���ݿ�ķ���
				if(result){
					//queryProvinceһ��ķ������漰��UI��������onFinish�����������߳���ִ�еġ�
					//��ˣ���Ҫ�ڵ���queryProvince֮ǰ�����������UI�߳���ִ��
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(type.equals("province")){
								queryProvinces();
							}
							else if(type.equals("city")){
								queryCities();
							}
							else if(type.equals("county")){
								queryCounties();
							}
							closeProgressDialog();
						}//end run
					});//end runOnUiThread
				}//end if(result)
				//closeProgressDialog();
			}//end onFinish

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				//�������߳��н���UI����
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this,
								"������������ʧ��!", Toast.LENGTH_SHORT).show();
					}//end run
				});//end runOnUiThread
			}//end onError
			
		});//end HttpUtil.sendHttpRequest
		
	}
/*
 * ��ʾ���ؽ��ȶԻ���
 */
	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("����������...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
/*
 * �رս��ȶԻ���	
 */
	private void closeProgressDialog() {
		// TODO Auto-generated method stub
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}

/*
 * ���ط����¼���Ӧ���жϴ�ʱ���صĽ���	
 */
	@Override
	public void onBackPressed(){
		if(currentLevel == COUNTY){
			queryCities();
		}
		else if(currentLevel == CITY){
			queryProvinces();
		}
		else if(currentLevel == PROVINCE){
			finish();
		}
	}
	
}//end ChooesAreaActivity

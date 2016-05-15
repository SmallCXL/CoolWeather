package com.coolweather.app.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/*
 * Http«Î«Û∞Ô÷˙¿‡
 */
public class HttpUtil {
	public static void sendHttpRequest(final String address, 
			final HttpCallbackListener listener){
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null){
						response.append(line);
					}
					listener.onFinish(response.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					listener.onError(e);
				}finally{
					if(connection != null){
						connection.disconnect();
					}//end if
				}//end finally				
			}//end run
		 }//end runnable
		).start();//end Thread
	}//end sendHttpRequest
}

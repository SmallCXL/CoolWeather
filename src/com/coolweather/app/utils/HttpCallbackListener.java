package com.coolweather.app.utils;
/*
 * Http请求监听器 
 * onFinish() 方法用于将子线程中获取的数据返回
 * onError()  方法用于处理Http请求出错的处理
 * 注意：这两个方法的实现仍然在HttpUtil类开启的子线程中，因此这两个方法中都不允许进行系统的UI操作
 * 若需要根据获取的数据更新系统UI，则需要开启一个异步任务
 */
public interface HttpCallbackListener {
	public void onFinish(String response);
	public void onError(Exception e);
}

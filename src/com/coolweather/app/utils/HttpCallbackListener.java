package com.coolweather.app.utils;
/*
 * Http��������� 
 * onFinish() �������ڽ����߳��л�ȡ�����ݷ���
 * onError()  �������ڴ���Http�������Ĵ���
 * ע�⣺������������ʵ����Ȼ��HttpUtil�࿪�������߳��У���������������ж����������ϵͳ��UI����
 * ����Ҫ���ݻ�ȡ�����ݸ���ϵͳUI������Ҫ����һ���첽����
 */
public interface HttpCallbackListener {
	public void onFinish(String response);
	public void onError(Exception e);
}

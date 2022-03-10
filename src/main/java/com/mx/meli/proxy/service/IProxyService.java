package com.mx.meli.proxy.service;

import com.mx.meli.proxy.bo.ProxyRequestBO;

public interface IProxyService {

	
	public void insertRequest(ProxyRequestBO req);
	
	public boolean isRequestAllowed(ProxyRequestBO req);
	
}

package com.mx.meli.proxy.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.mx.meli.proxy.bo.ProxyRequestBO;
import com.mx.meli.proxy.service.IProxyService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationsZuulFilter extends ZuulFilter {


	  @Autowired
	  private IProxyService iProxyService;
	  
	  @Override
	  public String filterType() {
	    return "pre";
	  }

	  @Override
	  public int filterOrder() {
	    return 1;
	  }

	  @Override
	  public boolean shouldFilter() {
	    return true;
	  }

	  @Override
	  public Object run() {
	    RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletRequest request = ctx.getRequest();

	    String ipAddress = request.getRemoteAddr();
	    String ipHeader = request.getHeader("IP_HEADER");
       
        String url =  request.getRequestURL().toString();
        String[] parts = url.split("/v1/meli/");
        String domain = parts[0];
        String path = "/" + parts[1].split("/")[0];
        
        log.info("ipAddress: {} ipHeaderp: {} url: {} domain: {} path: {}",ipAddress,ipHeader,url,domain,path);
        
        ProxyRequestBO req = new ProxyRequestBO();
        req.setIp(ipHeader);
        req.setTarjetPath( path );
        
        if (iProxyService.isRequestAllowed(req)){

            ctx.setSendZuulResponse(true);
            iProxyService.insertRequest(req);
           
        }else {
        	   // response to client
            ctx.setResponseBody("API key not authorized");
            ctx.getResponse().setHeader("Content-Type", "text/plain;charset=UTF-8");
            ctx.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            ctx.setSendZuulResponse(false);
        }


	    return null;
	  }

	}
package com.mx.meli.proxy.service.impl;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.mx.meli.proxy.Entity.ProxyRequestEntity;
import com.mx.meli.proxy.bo.CountBO;
import com.mx.meli.proxy.bo.ProxyRequestBO;
import com.mx.meli.proxy.mapper.CountMapper;
import com.mx.meli.proxy.repository.ProxyRequestRepository;
import com.mx.meli.proxy.service.IProxyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProxyService implements IProxyService{

	@Value("${proxy.conf.limit.req-by.ip}")
	private Long limitReqsByIp;
	
	@Value("${proxy.conf.limit.req-by.tarjet-srv}")
	private Long limitReqsByTarjet;
	
	@Value("${proxy.conf.limit.req-by.ip-tarjet-srv}")
	private Long limitReqsByIpAndTarjet;
	
	@Autowired
    @Qualifier("endpointsCat")
    private Map<String, Integer> endPointsCat;
    
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	@Autowired
	private ProxyRequestRepository proxyRequestRepository;
	
	@Override
	public void insertRequest(ProxyRequestBO req) {
		ProxyRequestEntity entity = new ProxyRequestEntity();
		
		entity.setIp			(req.getIp());
		entity.setDate			(new Date() );
		entity.setTarjetPathId	(endPointsCat.get(req.getTarjetPath()) );
		
		proxyRequestRepository.save(entity);
	
	}

	@Override
	public boolean isRequestAllowed(ProxyRequestBO req) {
		Boolean response = false;
		req.setTarjetPathId(endPointsCat.get(req.getTarjetPath()));
		
		if( validateIpAndTarjet(req.getIp(), req.getTarjetPathId()) && validateIp(req.getIp()) && validateTarjet(req.getTarjetPathId()) ) {
			response = true;
		}
		log.info("response isRequestAllowed [{}]", response);
		return response;
	}

	public boolean validateIp( final String ip) {
		Boolean response = true;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer();

		query.append( "SELECT COUNT(*) as count FROM fidena.requests WHERE ip = :ip ");
		parameters.addValue("ip", ip, Types.VARCHAR);
		
		
		
		log.debug("QUERY [{}]", query);
		
		List<CountBO> count = namedJdbcTemplate.query(query.toString(), parameters, new CountMapper());
		
		if( count != null && !count.isEmpty() && count.get(0).getCount() >= limitReqsByIp  ) {
			response = false;
		}
		
		log.debug("response [{}]", response);
		
		return response;
	}
	
	public boolean validateTarjet( final Integer tarjet) {
		Boolean response = true;
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer();

		query.append( "SELECT COUNT(*) as count FROM fidena.requests WHERE tarjet_path_id = :tarjet ");
		parameters.addValue("tarjet", tarjet, Types.SMALLINT);
		
		
		log.debug("QUERY [{}]", query);
		
		List<CountBO> count = namedJdbcTemplate.query(query.toString(), parameters, new CountMapper());
		
		if( count != null && !count.isEmpty() && count.get(0).getCount() > limitReqsByIp  ) {
			response = false;
		}
		log.debug("response [{}]", response);
		return response;
	}
	
	
	public boolean validateIpAndTarjet( final String ip, final Integer tarjet) {
		Boolean response = true;
	
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer();
		
		query.append( "SELECT COUNT(*) as count FROM fidena.requests WHERE tarjet_path_id = :tarjet and ip = :ip");
		parameters.addValue("tarjet", tarjet, Types.SMALLINT);
		parameters.addValue("ip", ip, Types.VARCHAR);
		
		log.debug("QUERY [{}]", query);
		
		List<CountBO> count = namedJdbcTemplate.query(query.toString(), parameters, new CountMapper());
		
		if( count != null && !count.isEmpty() && count.get(0).getCount() > limitReqsByIpAndTarjet  ) {
			response = false;
		}
		log.debug("response [{}]", response);
		return response;
	}
	
	
	
}

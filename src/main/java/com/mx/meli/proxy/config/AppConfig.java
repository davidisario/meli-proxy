package com.mx.meli.proxy.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mx.meli.proxy.Entity.EndPointEntity;
import com.mx.meli.proxy.repository.EndPointsRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Configuration
public class AppConfig {

	@Autowired
	private EndPointsRepository endPointsRepository;
	
	@Bean("endpointsCat")
    public Map<String, Integer> getEndPointsCatalog() throws IOException {
		
		Map<String, Integer> res = new HashMap<>();
        
		Iterable<EndPointEntity> iretator = endPointsRepository.findAll();
		
		iretator.forEach( endPoint -> {res.put(endPoint.getTarjetPath(), endPoint.getId().intValue() ); });
		
		log.info("Cat endpoints: {}" , res);
        return res;
    }
}

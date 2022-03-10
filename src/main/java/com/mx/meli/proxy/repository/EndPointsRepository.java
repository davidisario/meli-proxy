package com.mx.meli.proxy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mx.meli.proxy.Entity.EndPointEntity;

@Repository
public interface EndPointsRepository extends CrudRepository<EndPointEntity, Long>{

	
}

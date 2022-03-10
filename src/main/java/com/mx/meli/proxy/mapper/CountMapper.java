package com.mx.meli.proxy.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mx.meli.proxy.bo.CountBO;

public class CountMapper implements RowMapper<CountBO>{

	@Override
	public CountBO mapRow(ResultSet rs, int rowNum) throws SQLException {
		CountBO count = new CountBO();
		count.setCount ( rs.getLong("count")	);
		
		return count; 
	}

	
}
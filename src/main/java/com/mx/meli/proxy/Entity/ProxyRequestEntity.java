package com.mx.meli.proxy.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="requests")
public class ProxyRequestEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "ip")
	private String ip;
	
	@Column(name = "tarjet_path_id")	
	private Integer tarjetPathId;
	

	@Column(name = "date")
	private Date date;
	
}

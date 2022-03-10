package com.mx.meli.proxy.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyRequestBO {

	
	private String ip;

	private String tarjetPath;
	
	private Integer tarjetPathId;

}

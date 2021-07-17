package com.webservice.common.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebServiceConfig {

	private String KEYSTOREPATH = null;
	private String TRUSTSTOREPATH = null;
	private String KEYSTOREPW = null;
	private String TRUSTSTOREPW = null;
	private String KEYPASS = null;
	private String HTTPS_SERV_URL = null;
	private String trustAllCertificate = "false";	// DEFAULT VALUE
	private String keystoreType = "JKS";			// DEFAULT VALUE
	private String regex = null;
	private String keymanageralgorithm = null;
	private int mqreadinterval = 1;
	private int httpsfialureinterval = 5;
	private int prodissueinterval = 1;
	
	private static WebServiceConfig configInstance = null;
	
	public static WebServiceConfig getInstance() {
		System.out.println("in WebServiceConfig getInstance");
		if (configInstance == null) {
			configInstance = new WebServiceConfig();
		}
		
		return configInstance;
	}
		
}
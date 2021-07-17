package com.webservice.ssl.client;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.log4j.Logger;

import com.webservice.common.config.WebServiceConfig;
import com.webservice.ssl.config.SSLContextConfig;

public class SSLClient {
	
	static SSLClient sslClient = null;
	int responseCode = -1;
	static private WebServiceConfig config = WebServiceConfig.getInstance();
	private static final Logger LOGGER = Logger.getLogger(SSLClient.class);
	URL url = null;
	HttpsURLConnection connection = null;
	static SSLContext sslContext = null;
	
	
	private SSLClient() {
		SSLContextConfig sslConfig = new SSLContextConfig();
		sslContext = sslConfig.setupSslContext();
	}
	
	public static SSLClient getSSLClient() {
		if (sslClient == null)
			sslClient = new SSLClient();
		
		return sslClient;
	}
	
	private boolean setSSLConnection(URL url, String method, String msgtype) {
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		
		try {
			connection = (HttpsURLConnection) url.openConnection();
			connection.setSSLSocketFactory(sslContext.getSocketFactory());
			
			if (method == "POST")
				connection.setRequestMethod(method);
			
			connection.setDoOutput(true );
			connection.setRequestProperty("Content-Type", msgtype /*"text/xml" */ );
			connection.setRequestProperty("Authorization"
					, "Bearer "+"410ace14930aa0ab2c4caf4d27b4d1a8c1aae601c02d0c1cb4111910b659b187");
			connection.connect();
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception occurred while establishing connection to "
					+ "SSL server. Error :"
					+ e.getMessage());
			connection.disconnect();
			connection = null;
			return false;
		}
	}
	
	public void releaseConnection() {
		connection.disconnect();
		connection = null;
	}
	
	/**
	 * 
	 * @param url
	 * @param method
	 * @param message
	 * @param msgtype json or xml
	 * @return
	 */
	public String sendRequest(URL url, String method, String message, String msgtype) {
		String response = null;
		
		if (setSSLConnection(url,method,msgtype)) {
			
			try {
				//Sending the request to Remote server
				if (message!=null && !message.trim().isEmpty()) {
					OutputStreamWriter writer
							= new OutputStreamWriter(connection.getOutputStream());
					
					writer.write(message);
					writer.flush();
					writer.close();
				}
				
				responseCode = connection.getResponseCode();
				LOGGER.info("Response Code :" + responseCode);
				// reading the response
				InputStreamReader reader
						= new InputStreamReader(connection.getInputStream());
				
				StringBuilder buf = new StringBuilder();
				char[] cbuf = new char[ 2048 ];
				int num;
				
				while ( -1 != (num = reader.read( cbuf ))) {
					buf.append(cbuf, 0, num );
				}
				
				response = buf.toString();
				
			} catch(Exception e) {
				response = "Exception occurred while sending message";
				e.printStackTrace();
			}
			
		}
		
		releaseConnection();
		return response;
	}
	
}
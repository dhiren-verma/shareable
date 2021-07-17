package com.webservice.ssl.client.app;

import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.webservice.common.config.WebServiceConfig;
import com.webservice.ssl.client.SSLClient;

public class ClientApp {

	public static void main(String[] args) {
		Configuration config = null;
		
		try {
			config = new PropertiesConfiguration("app.properties");
		} catch(Exception e) {
			System.out.println("Exception in reading properties file : app.properties");
			e.printStackTrace();
			System.exit(-1);
		}
		
		WebServiceConfig ac = WebServiceConfig.getInstance();
		
		ac.setKEYSTOREPATH((String)config.getProperty("KEYSTOREPATH"));
		ac.setTRUSTSTOREPATH((String)config.getProperty("TRUSTSTOREPATH"));
		ac.setKEYSTOREPW((String)config.getProperty("KEYSTOREPW"));
		ac.setTRUSTSTOREPW((String)config.getProperty("TRUSTSTOREPW"));
		ac.setKEYPASS((String)config.getProperty("KEYPASS"));
		ac.setKeystoreType((String)config.getProperty("keystoreType"));
		ac.setTrustAllCertificate((String)config.getProperty("trustAllCertificate"));
		ac.setKeymanageralgorithm((String)config.getProperty("keymanageralgorithm"));
		
		String strurl;
		URL url;
		String method;
		String message;
		String msgtype;
		String response;
		
		try {
			//we can add all the urls in config file & load from there:
			SSLClient sslClient = SSLClient.getSSLClient();
			
			int selectedWebService = Integer.parseInt(JOptionPane.showInputDialog("Select which Webservice you want to call:\n"
					+ "1. SOAP Web Service\n"
					+ "2. REST Web Service(GET-Method)\n"
					+ "3. REST Web Service(POST-Method)\n"
					+ "Select your Pick..."));
			
			switch (selectedWebService) {
				case 1: //SOAP web service call:
					strurl = "https://www.crcind.com/csp/samples/%25SOAP.WebServiceInvoke.cls?CLS=SOAP.Demo&OP=AddInteger";
					url = new URL(strurl);
					method = "GET";
					message = "";
					msgtype = "text/xml";
					response = sslClient.sendRequest(url, method, message, msgtype);
					
					System.out.println("SOAP Call Response is: \n"+response);
					break;
					
				case 2: //RESTFul GET web service call:
					strurl = "https://gorest.co.in/public/v1/posts";
					url = new URL(strurl);
					method = "GET";
					message = "";
					msgtype = "application/json";
					response = sslClient.sendRequest(url, method, message, msgtype);
					
					System.out.println("REST Call Response is: \n"+response);
					break;
					
				case 3: //RESTFul POST web service call
					strurl = "https://gorest.co.in/public/v1/users";
					url = new URL(strurl);
					method = "POST";
					message = "{\"name\":\"Mr. Y\", \"gender\":\"male\", \"email\":\"mr.Y@xyz.com\", \"status\":\"active\"}";
					msgtype = "application/json";
					response = sslClient.sendRequest(url, method, message, msgtype);
					
					System.out.println("REST Call Response is: \n"+response);
					break;
					
				default:
					System.out.println("Incorrect Option picked!");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
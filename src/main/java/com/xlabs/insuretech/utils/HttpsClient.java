package com.xlabs.insuretech.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class HttpsClient {
    
    final static Logger logger = Logger.getLogger(HttpsClient.class);
    
    private static CloseableHttpClient mutualAuthHttpClient;


//    private static CloseableHttpClient XPayHttpClient;

    
    private CloseableHttpClient fetchMutualAuthHttpClient() throws KeyManagementException, UnrecoverableKeyException,
    NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        if (mutualAuthHttpClient == null) {
            mutualAuthHttpClient = HttpClients.custom().setSSLSocketFactory(getSSLSocketFactory()).build();


        }
        return mutualAuthHttpClient;
    }






    private SSLContext loadClientCertificate() throws KeyManagementException, UnrecoverableKeyException,
    NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {


/*

        File jks= new File(HttpsClient.class.getClassLoader().getResource("keys/api.vp-fintech.com.jks").getFile());
        System.out.println("PATH: "+jks.getAbsolutePath());
    	SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(jks, "1qaz2wsx@".toCharArray(),"1qaz2wsx@".toCharArray())
                .build();

*/

        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());




        return sc;
    }
    
    private SSLConnectionSocketFactory getSSLSocketFactory() throws KeyManagementException, UnrecoverableKeyException,
    NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {

        HostnameVerifier allHostsValid = (hostname, session) -> true;

//        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(loadClientCertificate(), new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(loadClientCertificate(), new String[] { "TLSv1" }, null, allHostsValid);





        return sslSocketFactory;
    }
    
    private HttpRequest createHttpRequest(MethodTypes methodType, String url) throws Exception {
    	HttpRequest request = null;
    	switch (methodType) {
    	case GET:
    		request = new HttpGet(url);
    	    break;
    	case POST:
    		request = new HttpPost(url);
    	    break;
    	case PUT:
    		request = new HttpPut(url);
    	    break;
    	case DELETE:
    		request = new HttpDelete(url);
    	    break;
    	default:
    		logger.error("Incompatible HTTP request method " + methodType);
    	}
    	return request;
   }
    
    public CloseableHttpResponse doMutualAuthRequest(String url, String testInfo,  String body, MethodTypes methodType, Map<String, String> headers)
                    throws Exception {


        HttpRequest request = createHttpRequest(methodType, url);

        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setHeader(HttpHeaders.ACCEPT, "application/json");

//        System.out.println(BasicAuthHeaderGenerator.getBasicAuthHeader());
        if (headers != null && headers.isEmpty() == false) {
            for (Entry<String, String> header : headers.entrySet()) {
            	request.setHeader(header.getKey(), header.getValue());
            }
        }
        
        if (request instanceof HttpPost) {

		    ((HttpPost) request).setEntity(new StringEntity(body, "UTF-8"));
		} else if (request instanceof HttpPut) {
		    ((HttpPut) request).setEntity(new StringEntity(body, "UTF-8"));
		}


        CloseableHttpResponse response =null;
        try {
            response = fetchMutualAuthHttpClient().execute((HttpUriRequest) request);

        }
        catch (JsonMappingException e){
            e.printStackTrace();
        }

        return response;
       
    }
    




    private static void logResponse(CloseableHttpResponse response) throws IOException {
        Header[] h = response.getAllHeaders();
        
        // Get the response json object
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        
        // Print the response details
        HttpEntity entity = response.getEntity();
        logger.info("Response status : " + response.getStatusLine() + "\n");
        
        logger.info("Response Headers: \n");
        
        for (int i = 0; i < h.length; i++)
            logger.info(h[i].getName() + ":" + h[i].getValue());
        logger.info("\n Response Body:");
        
        if(!StringUtils.isEmpty(result.toString())) {
            ObjectMapper mapper = getObjectMapperInstance();
            Object tree;
            try {
                tree = mapper.readValue(result.toString(), Object.class);
                logger.info("ResponseBody: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        
        EntityUtils.consume(entity);
    }
    
    private static void logRequestBody(String URI, String testInfo, String payload) {
        ObjectMapper mapper = getObjectMapperInstance();
        Object tree;
        logger.info("URI: " + URI);
        logger.info(testInfo);
        if(!StringUtils.isEmpty(payload)) {
            try {
                tree = mapper.readValue(payload,Object.class);
                logger.info("RequestBody: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
    
    /**
     * Get Correlation Id for the API Call.
     * @return correlation Id  
     */
    private String getCorrelationId() {
        //Passing correlation Id Header is optional while making an API call.
        return RandomStringUtils.random(10, true, true) + "_SC";
    }
    /**
     * Get New Instance of ObjectMapper
     * @return
     */
    protected static ObjectMapper getObjectMapperInstance() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true); // format json
        return mapper;
    }
    
}

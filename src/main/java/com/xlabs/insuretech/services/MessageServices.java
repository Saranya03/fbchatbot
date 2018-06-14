package com.xlabs.insuretech.services;

import com.xlabs.insuretech.models._Message;
import com.xlabs.insuretech.utils.HttpsClient;
import com.xlabs.insuretech.utils.MethodTypes;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by svarathalingam on 6/13/2018.
 */

@Service
public class MessageServices {

    @Autowired
    private HttpsClient httpsClient;

    public String sendMessage(_Message message){

        String url = "https://api.twilio.com/2010-04-01/Accounts/AC2b209faeb459290df1fc85df0aa0b926/Messages.json";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Accept","application/json");
        headers.put("Authorization", "Basic QUMyYjIwOWZhZWI0NTkyOTBkZjFmYzg1ZGYwYWEwYjkyNjozNWQzMDBmOTFjM2RmMWFjYjc5MjZmMGJmYjI3MjRiZQ==");

        String requestBody = "To=" + URLEncoder.encode(message.getToNumber()) + "&From=%2B19897860135&Body=" + message.getBody();

        try {
            CloseableHttpResponse response = httpsClient.doMutualAuthRequest(url, "", requestBody, MethodTypes.POST, headers);

            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);

            System.out.println(responseString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

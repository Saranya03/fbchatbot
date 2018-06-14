package com.xlabs.insuretech.controllers;

import com.xlabs.insuretech.models._Message;
import com.xlabs.insuretech.services.MessageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by svarathalingam on 6/13/2018.
 */

@RestController
public class MessageController {

    @Autowired
    private MessageServices messageServices;

    @RequestMapping(value="/sendMessage", method= RequestMethod.POST)
    public ResponseEntity<String> sendMessage(@RequestBody _Message message){
        try {

            String status  = messageServices.sendMessage(message);
            if(status ==null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(status);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}

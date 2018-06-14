package com.xlabs.insuretech.utils;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by svarathalingam on 6/13/2018.
 */

@Service
public class DomainUtils {

    private static final int MAX_VERIFICATION_CODE = 100000;
    private static final int MIN_VERIFICATION_CODE = 999999;

    public String generateVerificationCode(){
        Random rand = new Random();
        Integer code = rand.nextInt(MIN_VERIFICATION_CODE
                - MAX_VERIFICATION_CODE + 1) + MAX_VERIFICATION_CODE;
        return code.toString();
    }

}

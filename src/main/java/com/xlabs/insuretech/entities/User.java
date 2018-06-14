package com.xlabs.insuretech.entities;

/**
 * Created by svarathalingam on 6/13/2018.
 */

//@Entity
public class User {

    //@Id
    private String id;

    //@Column(name = "FIRST_NAME")
    private String firstName;

    //@Column(name = "LAST_NAME")
    private String lastName;

    //@Column(name = "EMAIL", unique = true)
    private String email;

    //@Column(name = "PASSWORD")
    //private String password;

    //@Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    //@Column(name = "VERIFICATION_CODE")
    private String verificationCode;

    //@Column(name = "CONFIRMED")
    private boolean confirmed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}

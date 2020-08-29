package com.mycompany.myordertest.rest;

import com.mycompany.myordertest.constants.RAppConsts;
import com.mycompany.myordertest.utils.AppUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by Geanni on 6/26/2019.
 */



public class GeneralParams  extends RRequestBaseClass {
    @MyAnnotation("myorder_call_param_value")
    @MyAnnotationSequence(0)
    private String udid = "";

    @MyAnnotation("myorder_call_param_value")
    @MyAnnotationSequence(1)
    private String name;

    @MyAnnotation("myorder_call_param_value")
    @MyAnnotationSequence(2)
    private String phone;

    @MyAnnotation("myorder_call_param_value")
    @MyAnnotationSequence(3)
    private String language;

    @MyAnnotation("myorder_call_param_value")
    @MyAnnotationSequence(4)
    private String osVersion;

    @MyAnnotation("myorder_call_param_value")
    @MyAnnotationSequence(5)
    private String deviceType;

    @MyAnnotation("myorder_call_param_value")
    @MyAnnotationSequence(6)
    private String carrier;

    public GeneralParams(){}

    public GeneralParams (String udid, String phone, String language, String deviceType, String carrier, String osVersion, String name){

        this.udid = udid;
        this.phone = phone;
        this.language = language;
        this.deviceType = deviceType;
        this.carrier = carrier;
        this.osVersion = osVersion;
        this.name = name;
    }

    public String makeReqString(String prefix, String currentString){

        List<Field> fields = getFields(this);


        String shString = udid + name + phone + language + osVersion + deviceType + carrier;
        for (Field field: fields ) {

            MyAnnotation annotation = field.getAnnotation(MyAnnotation.class);
            if (annotation != null && annotation.value().compareToIgnoreCase("myorder_call_param_value") == 0) {

                String fieldValue = "";
                try {
                    Object value = field.get(this);
                    if (value != null) {
                        fieldValue = value.toString();
                        //shString += fieldValue;
                    }
                } catch (IllegalAccessException iae){
                    System.out.println(iae.getMessage());
                }
                String token = prefix + "[" + field.getName() + "]=" + fieldValue;

                if (currentString.length() > 0 ) {
                    currentString = currentString + "&" + token;
                } else {
                    currentString = token;
                }
            }
        }

        return currentString;
    }


    public String getHash(){

        List<Field> allFields = getFields(this);
        Field[] orderedFields = new Field[allFields.size()];
        String hashString = "";

        for (Field field: allFields){
            MyAnnotationSequence annotation = field.getAnnotation(MyAnnotationSequence.class);
            if (annotation != null ){

                int idx = annotation.value();
                if (orderedFields.length > idx) {
                    orderedFields[idx] = field;
                }
            }
        }

        for (Field prop: orderedFields){
            try {
                hashString += prop.get(this);
            }catch (IllegalAccessException iae){
                iae.printStackTrace();
            }
        }
        try {
            hashString = AppUtils.SHA1(hashString + RAppConsts.APIKEY);
        }catch (NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        } catch (UnsupportedEncodingException uee){
            uee.printStackTrace();
        }

        return  hashString;
    }
    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}

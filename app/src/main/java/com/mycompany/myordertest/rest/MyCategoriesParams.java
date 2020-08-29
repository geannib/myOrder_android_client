package com.mycompany.myordertest.rest;

import com.mycompany.myordertest.constants.RAppConsts;
import com.mycompany.myordertest.utils.AppUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.List;



public class MyCategoriesParams extends  GeneralParams {

    @MyAnnotation("riorder_call_param_value")
    @MyAnnotationSequence(7)
    private String id;

    public MyCategoriesParams(){

    }


    public MyCategoriesParams(String udid, String phone, String language, String deviceType, String carrier, String osVersion, String name, String id){

        this.setUdid(udid);
        this.setPhone(phone);
        this.setLanguage(language);
        this.setDeviceType(deviceType);
        this.setCarrier(carrier);
        this.setOsVersion(osVersion);
        this.setName(name);
        this.setId(id);
    }
    @Override
    public String makeReqString(String prefix, String currentString){

        List<Field> fields = getFields(this);


        for (Field field: fields ) {

            MyAnnotation annotation = field.getAnnotation(MyAnnotation.class);
            if (annotation != null && annotation.value().compareToIgnoreCase("riorder_call_param_value") == 0) {

                String fieldValue = "";
                try {
                    field.setAccessible(true);
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
    @Override
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
                prop.setAccessible(true);
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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

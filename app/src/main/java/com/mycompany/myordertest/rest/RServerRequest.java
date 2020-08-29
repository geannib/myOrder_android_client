package com.mycompany.myordertest.rest;


import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;

public class RServerRequest extends RRequestBaseClass {

    private static final String TAG = RServerRequest.class.getName();

    @MyAnnotation("myorder_call_param_class")
    private MyRequestParamsFactory request;

    private String queryString = "";


    public  RServerRequest(){

        request = new MyRequestParamsFactory();
    }

    public  RServerRequest(String requestType){

        request = new MyRequestParamsFactory();
        request.setReqType(requestType);
    }

    public String getRequestString(){

        return makeReqString("request");
    }

    private String makeReqString(String request) {

        List<Field> fields = getFields(this);
        String retString = "";

        if (fields.size() != 1){

            Log.d(TAG, "Class should have only one exposed parameter called request");
            return retString;

        }

        Field reqField = fields.get(0);


        try {
            MyRequestParamsFactory rp = (MyRequestParamsFactory) reqField.get(this);

            String qString = rp.makeReqString(reqField.getName());
            this.queryString = this.queryString + qString;
        } catch (IllegalAccessException iae){
            iae.printStackTrace();
        }

        return queryString;
    }


}

package com.mycompany.myordertest.rest;
import android.util.Pair;

import com.mycompany.myordertest.constants.RAppConsts;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MyRequestParamsFactory extends RRequestBaseClass {


    private String request;
    @MyAnnotation("myorder_call_param_class")
    private GeneralParams data;

    @MyAnnotation("myorder_call_param_value")
    private  String reqType= RAppConsts.kGetStores;

    @MyAnnotation("myorder_call_param_value")
    private  String hash="";

    private String currentString = "";
    private String prefix = "request";


    public String makeReqString(String prefix){

        String validPhone = "+400726745444"; //SplashActivity.sharedPref.getString(RAppConsts.kPreferrencesPhone, "NA");
        String selectedLng = "EN-us" ;//SplashActivity.sharedPref.getString(RAppConsts.kSelectedLanguage, "en");
        // 0730946269
        switch (reqType){
            case RAppConsts.kGetStores:
                data = new GeneralParams( "2E76E122-DFAD-44B8-BA4B-39D53C351FF4", validPhone, selectedLng, "iPhone", "vodafone", "iOS", "marcel");
                break;
            case RAppConsts.kGetProducts:
                data = new MyCategoriesParams("2E76E122-DFAD-44B8-BA4B-39D53C351FF4", validPhone, selectedLng, "iPhone", "vodafone", "iOS", "marcel", "1");
                break;
            case RAppConsts.kGetCategories:
                data = new MyCategoriesParams("2E76E122-DFAD-44B8-BA4B-39D53C351FF4", validPhone, selectedLng, "iPhone", "vodafone", "iOS", "marcel", "1");
                break;

            default:
                data = new GeneralParams( "2E76E122-DFAD-44B8-BA4B-39D53C351FF4", validPhone, selectedLng, "iPhone", "vodafone", "iOS", "marcel");
                break;
        }
//
//
//        hash = data.getHash();
        String ret = getFields2(this);

        return currentString;
    }

    public String getFields2(Object o) {

        List<Field> ret = new ArrayList<>();
        Class<?> clazz = o.getClass();

        Pair<String, String> retPair = new Pair<>("", "");
        List<Field> fields = new ArrayList<>();
        for (Field fld: clazz.getDeclaredFields()){
            fields.add(fld);
        }

        Predicate<Field> fieldPredicate = p-> p.getName().compareToIgnoreCase("hash") == 0;
        //fields.removeIf(fieldPredicate);

        for(Field field : fields) {
            //you can also use .toGenericString() instead of .getName(). This will
            //give you the type information as well.

            System.out.println(field.getName());
            MyAnnotation annotation = field.getAnnotation(MyAnnotation.class);
            if (annotation != null && annotation.value().compareToIgnoreCase("myorder_call_param_value") == 0) {
                ret.add(field);
                String fieldValue = "";
                try {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value != null)
                        fieldValue = value.toString();
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


            // TODO: make this complex thing much easy
            if (annotation != null && annotation.value().compareToIgnoreCase("myorder_call_param_class") == 0) {
                if (field.getName().compareToIgnoreCase("data") == 0) {
                    if (this.data.getClass().getName().contains("RestaurantParams")) {

                        try {
                            MyStoresParams rbs = (MyStoresParams) field.get(this);

                            String retString = rbs.makeReqString("request" + "[" + field.getName() + "]", currentString);
                            this.currentString = this.currentString + retString;
                        } catch (IllegalAccessException iae){
                            iae.printStackTrace();
                        }

                    } else
                    if (this.data.getClass().getName().contains("GeneralParams")) {

                        try {
                            GeneralParams rbs = (GeneralParams) field.get(this);

                            String retString = rbs.makeReqString("request" + "[" + field.getName() + "]", currentString);
                            this.currentString = this.currentString + retString;
                        } catch (IllegalAccessException iae){
                            iae.printStackTrace();
                        }

                    }
                }
            }


        }

        return currentString;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }
}


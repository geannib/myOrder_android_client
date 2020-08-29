package com.mycompany.myordertest.rest;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RRequestBaseClass {

    public List<Field> getFields(Object o) {


        List<Field> ret = new ArrayList<>();
        Class<?> clazz = o.getClass();

        Field[] f1 = clazz.getDeclaredFields();
        Field[] f2 = clazz.getSuperclass().getDeclaredFields();

        List<Field> allFields = new ArrayList<Field>();
        Collections.addAll(allFields, f1);
        Collections.addAll(allFields, f2);

        for(Field field : allFields) {

            System.out.println(field.getName());
            MyAnnotation annotation = field.getAnnotation(MyAnnotation.class);
            if (annotation != null && (annotation.value().compareToIgnoreCase("myorder_call_param_class") == 0 ||
                    annotation.value().compareToIgnoreCase("myorder_call_param_class") == 0)) {

                ret.add(field);
            }

        }


        return ret;
    }
}

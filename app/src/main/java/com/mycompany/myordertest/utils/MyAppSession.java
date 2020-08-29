package com.mycompany.myordertest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mycompany.myordertest.rest.DTO.MyProductPOJO;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MyAppSession {
    private static MyAppSession mInstance= null;

    private List<MyProductPOJO> favouriteList = new ArrayList<>();
    private MyLocalCart localCart = null;
    private static Context context;
    private FirebaseUser currentUser;

    protected MyAppSession(){}

    public void init(Context context) {
        // this.context = context; // (1)
        this.context = context.getApplicationContext(); // (2)
    }
    public static synchronized MyAppSession getInstance() {
        if(null == mInstance){
            mInstance = new MyAppSession();

        }
        return mInstance;
    }
    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }


    public List<MyProductPOJO> getFavList(){
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String favStr = preferences.getString("myFavList", "");
        Gson gson = new Gson();

        if(favStr != null && favStr.length() > 1){
            try{
                Type founderListType = new TypeToken<ArrayList<MyProductPOJO>>(){}.getType();
                this.favouriteList = gson.fromJson(favStr, founderListType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            this.favouriteList = new ArrayList<>();
            this.saveLocalCart();
        }

        return this.favouriteList;
    }
    public MyLocalCart getLocalCart(){

        // Get local cart from shared preferrences if it is
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String cartStr = preferences.getString("myLocalCart", "");
        Gson gson = new Gson();

        if(cartStr != null && cartStr.length() > 1){
            try{
                this.localCart = gson.fromJson(cartStr, MyLocalCart.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            this.localCart = new MyLocalCart();
            this.saveLocalCart();
        }

        return this.localCart;
    }
    public void saveLocalCart(){
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String myCartStr = gson.toJson(this.localCart);
        String strFav = gson.toJson(this.favouriteList);
        editor.putString("myLocalCart", myCartStr);
        editor.putString("myFavList", strFav);
        editor.commit();
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }
}
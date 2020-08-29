package com.mycompany.myordertest.rest;

import com.mycompany.myordertest.rest.response.MyAddressesAPIResponse;
import com.mycompany.myordertest.rest.response.MyCategoriesAPIResponse;
import com.mycompany.myordertest.rest.response.MyProductsAPIResponse;
import com.mycompany.myordertest.rest.response.MyPushTokenAPIResponse;
import com.mycompany.myordertest.rest.response.MySaveCartAPIResponse;
import com.mycompany.myordertest.rest.response.MyStoreAPIResponse;
import com.mycompany.myordertest.rest.response.MySubcategoriesAPIResponse;
import com.mycompany.myordertest.utils.MyBuyProduct;
import com.mycompany.myordertest.utils.MyLocalCart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyAPI {

    @Headers({"Connection: keep-alive",
            "Content-Type: application/x-www-form-urlencoded",
            "Accept: text/html",
    })//    @FormUrlEncoded


    @FormUrlEncoded
    @POST("clientapi/getStores")
    Call<MyStoreAPIResponse> getStores(
            @Field("md") String id,
            @Field("firebase_uid") String firebase_uid
    );

    @FormUrlEncoded
    @POST("clientapi/getproducts")
    Call<MyProductsAPIResponse> getProducts(
            @Field("category_id") String id,
            @Field("firebase_uid") String firebase_uid
    );

    @FormUrlEncoded
    @POST("clientapi/getstorecategories")
    Call<MyCategoriesAPIResponse> getstorecategories(
            @Field("store_id") String storeId,
            @Field("firebase_uid") String firebase_uid
    );

    @FormUrlEncoded
    @POST("clientapi/getstorecategories")
    Call<MyCategoriesAPIResponse> getstorecategories2(
            @Field("store_id") String storeId,
            @Field("parent_id") String parentId,
            @Field("firebase_uid") String firebase_uid
    );

    @FormUrlEncoded
    @POST("clientapi/getstoresubcategories")
    Call<MySubcategoriesAPIResponse> getstoresubcategories(
            @Field("id") String id,
            @Field("firebase_uid") String firebase_uid
    );

    @FormUrlEncoded
    @POST("clientapi/getuseraddresses")
    Call<MyAddressesAPIResponse> getAddresses(
            @Field("firebase_uid") String firebase_uid
    );

    @FormUrlEncoded
    @POST("clientapi/savecart")
    Call<MySaveCartAPIResponse> savecart(
            @Field("firebase_uid") String firebase_uid,
            @Field("store_id") String store_id,
            @Field("notes") String notes,
            @Field("user_uuids[]") List<MyBuyProduct> products
    );


    @FormUrlEncoded
    @POST("clientapi/savecart")
    public Call<MySaveCartAPIResponse> saveCart2(
            @Field("store_id") String store_id,
            @Field("notes") String notes,
            @Field("firebase_uid") String firebase_id,
            @Field ("products") String products
    );

    @FormUrlEncoded
    @POST("clientapi/updateuserfirebasetoken")
    public Call<MyPushTokenAPIResponse> updateToken(
            @Field("firebase_uid") String firebase_id,
            @Field ("firebase_token") String pushToken
    );


}

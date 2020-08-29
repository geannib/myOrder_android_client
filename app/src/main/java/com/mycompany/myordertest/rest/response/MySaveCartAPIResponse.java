package com.mycompany.myordertest.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mycompany.myordertest.rest.DTO.MyStoresPOJO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySaveCartAPIResponse {

    public String resType;
    public String resMessage;
    @SerializedName("data")
    @Expose
    public int data = 0;



}
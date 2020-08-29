package com.mycompany.myordertest.rest.response;

import com.mycompany.myordertest.rest.DTO.MyStoresPOJO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyStoreAPIResponse {

    public String resType;
    public String resMessage;
    @SerializedName("data")
    @Expose
    public List<MyStoresPOJO> data = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
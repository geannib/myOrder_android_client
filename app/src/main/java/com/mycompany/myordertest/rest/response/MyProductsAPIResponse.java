package com.mycompany.myordertest.rest.response;
import java.io.Serializable;
import java.util.List;

import com.mycompany.myordertest.rest.DTO.MyProductPOJO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyProductsAPIResponse implements Serializable
{

    @SerializedName("resType")
    @Expose
    public String resType;
    @SerializedName("resMessage")
    @Expose
    public String resMessage;
    @SerializedName("data")
    @Expose
    public List<MyProductPOJO> data = null;
    private final static long serialVersionUID = -921440967258590446L;

}

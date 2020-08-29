package com.mycompany.myordertest.rest.DTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyCategoryPOJO implements Serializable
{

    @SerializedName("store_category.Id")
    @Expose
    public String id;
    @SerializedName("store_category.Label")
    @Expose
    public String label;
    @SerializedName("subcategories")
    @Expose
    public String subcategories;

    private final static long serialVersionUID = 5724435978867306545L;

}
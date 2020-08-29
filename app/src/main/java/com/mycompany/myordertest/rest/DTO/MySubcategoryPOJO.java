package com.mycompany.myordertest.rest.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MySubcategoryPOJO
{

    @SerializedName("store_product_subcategory.Id")
    @Expose
    public long id;
    @SerializedName("store_product_subcategory.Label")
    @Expose
    public String label;

    private final static long serialVersionUID = 5724435978867306545L;

}
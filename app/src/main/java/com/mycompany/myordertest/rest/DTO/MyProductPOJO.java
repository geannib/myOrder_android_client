package com.mycompany.myordertest.rest.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyProductPOJO implements Serializable
{

    @SerializedName("store_product.Id")
    @Expose
    public long id;
    @SerializedName("store_product.CodeEAN")
    @Expose
    public long CodeEAN;
    @SerializedName("store_product.TVA")
    @Expose
    public long TVA;
    @SerializedName("store_product.PackageUnits")
    @Expose
    public long PackageUnits;
    @SerializedName("store_product.MaxSellingQuantity")
    @Expose
    public long MaxSellingQuantity;
    @SerializedName("store_product.Name")
    @Expose
    public String name;
    @SerializedName("store_product.Description")
    @Expose
    public Object description;
    @SerializedName("store_product.Price")
    @Expose
    public double price;
    @SerializedName("store_product.Image")
    @Expose
    public Object image;
    @SerializedName("store_product.Maxsellingquantity")
    @Expose
    public long maxsellingquantity;
    @SerializedName("store_product.Idstoreproductsubcategory")
    @Expose
    public long idstoreproductsubcategory;
    private final static long serialVersionUID = 1158893504164233019L;

}

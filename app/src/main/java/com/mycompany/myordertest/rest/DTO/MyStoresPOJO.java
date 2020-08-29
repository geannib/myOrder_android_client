package com.mycompany.myordertest.rest.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyStoresPOJO implements Serializable
{

    @SerializedName("store.Id")
    @Expose
    public String id;
    @SerializedName("store.Name")
    @Expose
    public String name;
    @SerializedName("store.Description")
    @Expose
    public String description;
    @SerializedName("store.Image")
    @Expose
    public String image;
    @SerializedName("store.OrderFee")
    @Expose
    public String orderfee;
    @SerializedName("store.DeliveryTime")
    @Expose
    public String deliverytime;
    @SerializedName("store.MinimumOrder")
    @Expose
    public String minimumorder;
    @SerializedName("store_type.Label")
    @Expose
    public String typeLabel;
    private final static long serialVersionUID = -3465969452602425274L;
}

package com.mycompany.myordertest.rest.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyAddressPOJO implements Serializable
{

    @SerializedName("address.Id")
    @Expose
    public String id;

    @SerializedName("address.Country")
    @Expose
    public String country;

    @SerializedName("address.County")
    @Expose
    public String county;

    @SerializedName("address.Town")
    @Expose
    public String town;

    @SerializedName("address.Street")
    @Expose
    public String street;

    @SerializedName(" address.StreetNumber")
    @Expose
    public String streetNumber;

    @SerializedName("address.Building")
    @Expose
    public String building;

    @SerializedName("LocationPoint")
    @Expose
    public String locationPoint;

    private final static long serialVersionUID = 5724435978867306545L;

}
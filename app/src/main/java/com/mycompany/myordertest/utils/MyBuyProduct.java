package com.mycompany.myordertest.utils;

import java.io.Serializable;

public class MyBuyProduct implements Serializable {
    public String id;
    public int quantity;
    public transient String name;
    public transient double price;
}

package com.mycompany.myordertest.utils;

import com.mycompany.myordertest.rest.DTO.MyProductPOJO;

import java.util.ArrayList;
import java.util.List;

public class MyLocalCart {

    private List<MyBuyProduct> products = new ArrayList<>();
    private String notes = "bla bla bla";
    private String firebase_uid = MyAppSession.getInstance().getCurrentUser().getUid();
    private String store_id = "3";

    public int getCartSize() {

        int psize = 0;
        for (MyBuyProduct p: products){
            psize += p.quantity;
        }
        return psize;
    }

    public void addProduct(MyProductPOJO newProd, int quantity){

        if (products == null){
            products = new ArrayList<>();
        }

        boolean prodFound = false;
        for (MyBuyProduct p: products){
            if(p.id.compareTo("" + newProd.id) == 0){
                p.quantity +=  1;
                prodFound = true;
                break;
            }
        }

        if (prodFound == false) {
            MyBuyProduct mp = new MyBuyProduct();
            mp.id = "" + newProd.id;
            mp.quantity = quantity;
            mp.name = newProd.name;
            mp.price = newProd.price;
            products.add(mp);
        }
    }

    public void deleProduct(MyBuyProduct prod){

        if (products == null){
            products = new ArrayList<>();
        }
        int idx = 0;
        for (MyBuyProduct p: products){
            if(p.id.compareTo("" + prod.id) == 0){
               products.remove(idx);
                break;
            }
            idx++;
        }
    }

    public void modifyQuantityProduct(String id, int quantity){

        for (MyBuyProduct p: products){
            if(p.id.compareTo(id) == 0){
                p.quantity +=  quantity;
                break;
            }
        }
    }
    public List<MyBuyProduct> getAllProducts(){

        return this.products;
    }
}

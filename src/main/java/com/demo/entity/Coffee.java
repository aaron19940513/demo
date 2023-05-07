package com.demo.entity;

import lombok.Data;

@Data
public class Coffee {

    private int coffeeId;
    private String coffeeName;
    private String coffeeKinds;
    private String coffeeDiscount;
    private int newPrice;
    private int oldPrice;
    private String coffeeImage;

    public int getCoffeeId() {
        return coffeeId;
    }

    public void setCoffeeId(int coffeeId) {
        this.coffeeId = coffeeId;
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }

    public String getCoffeeKinds() {
        return coffeeKinds;
    }

    public void setCoffeeKinds(String coffeeKinds) {
        this.coffeeKinds = coffeeKinds;
    }

    public String getCoffeeDiscount() {
        return coffeeDiscount;
    }

    public void setCoffeeDiscount(String coffeeDiscount) {
        this.coffeeDiscount = coffeeDiscount;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getCoffeeImage() {
        return coffeeImage;
    }

    public void setCoffeeImage(String coffeeImage) {
        this.coffeeImage = coffeeImage;
    }

}

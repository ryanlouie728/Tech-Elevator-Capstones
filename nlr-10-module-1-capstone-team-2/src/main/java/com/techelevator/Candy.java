package com.techelevator;

import java.math.BigDecimal;

public class Candy extends VendingMachineItems{
    public Candy(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSound() {
        return "Munch Munch, Yum!";
    }
}

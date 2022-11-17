package com.techelevator;

import java.math.BigDecimal;

public class Drinks extends VendingMachineItems{
    public Drinks(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSound() {
        return "Glug glug, Yum!";
    }
}

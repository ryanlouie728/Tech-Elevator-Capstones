package com.techelevator;

import java.math.BigDecimal;

public class Chips extends VendingMachineItems {

    public Chips(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSound() {
        return "Crunch Crunch, Yum!";
    }
}

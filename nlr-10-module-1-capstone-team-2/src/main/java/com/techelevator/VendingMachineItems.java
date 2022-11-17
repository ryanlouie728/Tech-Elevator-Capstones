package com.techelevator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendingMachineItems {

    public String name;
    public BigDecimal price;
    public int quantity;

    public VendingMachineItems(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.quantity = 5;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSound() {
        return "";
    }

    @Override
    public String toString() {
        return name + " | $" + price + "\n";
    }
}



package com.zahjava.ecommercebackend.enums;

public enum OrderType {
    PURCHASE_TYPE(1),
    SALE_TYPE(2);

    private final int value;

    OrderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

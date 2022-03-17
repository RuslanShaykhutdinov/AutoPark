package com.autoPark.AutoPark.category;

public enum Category {
    MOTORCYCLE,
    CAR,
    TRUCK,
    BUS,
    TRAILER;

    public Category toCategory(String str) {
        if (str.equals("MOTORCYCLE")) return MOTORCYCLE;
        if (str.equals("CAR")) return CAR;
        if (str.equals("TRUCK")) return TRUCK;
        if (str.equals("BUS")) return BUS;
        if (str.equals("TRAILER")) return TRAILER;
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

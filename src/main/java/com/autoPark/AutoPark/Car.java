package com.autoPark.AutoPark;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String carId;
    private Category category;
    private Long driverId = null;

    public Car() {
    }

    public Car(String carId, Object category) {
        carId = carId;
        category = category;
    }

    public Car(String carId, Object category, long driverId) {
        carId = carId;
        category = category;
        driverId = driverId;
    }
    public Boolean isOwned(){
        if(driverId == 0)
            return false;
        else{
            return true;
        }
    }
}

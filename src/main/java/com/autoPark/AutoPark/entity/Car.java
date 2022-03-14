package com.autoPark.AutoPark.entity;

import javax.persistence.*;

@Entity
@Table(
        name="Car",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"carId"})
)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String carId;

    private Category category;

    @ManyToOne
    private Driver driver;

    public Car() {
    }

    public Car(String carId, Category category) {
        this.carId = carId;
        this.category = category;
    }

    public Car(String carId, Category category, Driver driver) {
        this.carId = carId;
        this.category = category;
        this.driver = driver;
    }

    public long getId() {
        return Id;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}

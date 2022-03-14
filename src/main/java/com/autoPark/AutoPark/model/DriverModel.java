package com.autoPark.AutoPark.model;

import com.autoPark.AutoPark.entity.Car;
import com.autoPark.AutoPark.entity.Category;
import com.autoPark.AutoPark.entity.Driver;

import java.util.ArrayList;

public class DriverModel {
    private long Id;

    private String passportId;
    private String name;
    private Category category;
    private ArrayList<Car> cars;


    public static DriverModel toModel(Driver driver) {
        DriverModel model = new DriverModel();
        model.setId(driver.getId());
        model.setPassportId(driver.getPassportId());
        model.setCategory(driver.getCategory());
        model.setCars(driver.getCars());
        return model;
    }

    public static Iterable<DriverModel> toModels(Iterable<Driver> drivers) {
        ArrayList<DriverModel> models = new ArrayList();
        drivers.forEach(driver -> {
            models.add(DriverModel.toModel(driver));
        });
        return models;
    }

    public DriverModel() {
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }
}

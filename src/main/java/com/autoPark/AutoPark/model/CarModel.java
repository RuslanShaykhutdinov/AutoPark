package com.autoPark.AutoPark.model;


import com.autoPark.AutoPark.entity.Car;
import com.autoPark.AutoPark.entity.Category;
import com.autoPark.AutoPark.entity.Driver;

import java.util.ArrayList;


public class CarModel {
    private long Id;
    private String carId;
    private Category category;
    private Driver driver;

    public static CarModel toModel(Car car) {
        CarModel model = new CarModel();
        model.setId(car.getId());
        model.setCarId(car.getCarId());
        model.setCategory(car.getCategory());
        model.setDriver(car.getDriver());

        return model;
    }

    public static Iterable<CarModel> toModels(Iterable<Car> cars) {
        ArrayList<CarModel> models = new ArrayList();
        cars.forEach(car -> {
            models.add(CarModel.toModel(car));
        });
        return models;
    }


    public CarModel() {
    }

    public void setId(long id) {
        Id = id;
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

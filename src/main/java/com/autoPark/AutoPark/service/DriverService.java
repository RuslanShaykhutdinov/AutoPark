package com.autoPark.AutoPark.service;


import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;

import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.repos.DriverRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DriverService {
    private final DriverRepo driverRepo;
    private final CarRepo carRepo;

    public DriverService(DriverRepo driverRepo, CarRepo carRepo) {
        this.driverRepo = driverRepo;
        this.carRepo = carRepo;
    }

    public void pinDriverToCar(Car car, Driver driver) {
        car.setDriverId(driver.getId());
        carRepo.save(car);
    }

    public void unpinDriverFromCar(Car car) {
        car.setDriverId(null);
        carRepo.save(car);
    }

    public Driver registration(String passportId, String name, Category category) {
        Driver driver = new Driver(passportId, name, category);
        if (driverRepo.findByPassportId(passportId) != null) {
            return null;
        }
        driverRepo.save(driver);
        return driver;
    }

    public List<Driver> withoutCars() {
        Set<Long> driversWithCars = new HashSet<>();
        carRepo.carsWithDriver().forEach(car -> {
            driversWithCars.add(car.getDriverId());
        });
        List<Driver> drivers = new ArrayList<>();
        driverRepo.findAll().forEach(driver -> {
            if (!(driversWithCars.contains(driver.getId())))
                drivers.add(driver);
        });
        return drivers;
    }

    public Driver editDriver (Long id, String passportId, String name){
        Driver oldDriver = driverRepo.findById(id).orElse(null);
        if (oldDriver != null) {
            oldDriver.setName(name);
            oldDriver.setPassportId(passportId);
            driverRepo.save(oldDriver);
            return oldDriver;
        }
        return null;
    }
}

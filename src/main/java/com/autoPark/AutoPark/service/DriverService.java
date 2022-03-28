package com.autoPark.AutoPark.service;


import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.controller.DriverController;
import com.autoPark.AutoPark.dto.Car;

import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.repos.DriverRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.PatternSyntaxException;

@Service
public class DriverService {
    private static final Logger log = LoggerFactory.getLogger(DriverService.class);
    private final DriverRepo driverRepo;
    private final CarRepo carRepo;

    public DriverService(DriverRepo driverRepo, CarRepo carRepo) {
        this.driverRepo = driverRepo;
        this.carRepo = carRepo;
    }

    public void pinDriverToCar(Car car, Driver driver) {
        log.info("> pinDriverToCar");
        car.setDriverId(driver.getId());
        carRepo.save(car);
        log.info("< pinDriverToCar");
    }

    public void unpinDriverFromCar(Car car) {
        log.info("> unpinDriverToCar");
        car.setDriverId(null);
        carRepo.save(car);
        log.info("< unpinDriverToCar");
    }

    public Driver registration(String passportId, String name, Category category) {
        log.info("> registration");
        Driver driver = null;
        String errMess = "";
        try {
            if (driverRepo.findByPassportId(passportId) != null) {
                errMess = "Driver is already exist";
                log.info("< registration with error={}", errMess);
            }else {
                driver = new Driver(passportId, name, category);
                driverRepo.save(driver);
                log.info("< registration");
            }
        }catch (PatternSyntaxException e){
            errMess = "RegEx not suitable";
        }
        return driver;
    }

    public List<Driver> withoutCars() {
        log.info("> withoutCars");
        Set<Long> driversWithCars = new HashSet<>();
        carRepo.carsWithDriver().forEach(car -> {
            driversWithCars.add(car.getDriverId());
        });
        List<Driver> drivers = new ArrayList<>();
        driverRepo.findAll().forEach(driver -> {
            if (!(driversWithCars.contains(driver.getId())))
                drivers.add(driver);
        });
        log.info("< withoutCars");
        return drivers;
    }

    public Driver editDriver(Long id, String passportId, String name){
        log.info("> editDriver");
        Driver oldDriver = driverRepo.findById(id).orElse(null);
        if (oldDriver != null) {
            oldDriver.setName(name);
            oldDriver.setPassportId(passportId);
            driverRepo.save(oldDriver);
        }
        log.info("< editDriver");
        return oldDriver;
    }
}

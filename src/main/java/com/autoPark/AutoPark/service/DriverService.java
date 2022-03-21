package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.repos.DriverRepo;
import org.springframework.stereotype.Service;

@Service
public class DriverService {
    private final DriverRepo driverRepo;
    private final CarRepo carRepo;
    public DriverService(DriverRepo driverRepo,CarRepo carRepo) {
        this.driverRepo = driverRepo;
        this.carRepo = carRepo;
    }

    public void pinDriverToCar(Car car,Driver driver){
        car.setDriverId(driver.getId());
        carRepo.save(car);
    }
    public void unpinDriverFromCar(Car car){
        car.setDriverId(null);
        carRepo.save(car);
    }
}

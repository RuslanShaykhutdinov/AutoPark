package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.repos.CarRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.PatternSyntaxException;


@Service
public class CarService {
    private final CarRepo carRepo;
    private static final Logger log = LoggerFactory.getLogger(CarService.class);
    String errMess = "Unknown error";

    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }


    public Car registration(String carId, Category category, Long driverId) {
        log.info("> registration");
        try {
            if (carRepo.findByCarId(carId) == null) {
                Car car = new Car(carId, category, driverId);
                carRepo.save(car);
                log.info("< registration");
                return car;
            }
            errMess = "The car is already exist";
        } catch (PatternSyntaxException e){
            errMess = "RegEx not suitable";
        }
        log.error("< registration with error = {}", errMess);
        return null;
    }

    public Car editCarById(Car car, String newCarId) {
        log.info("> editCarById");
        try {
            car.setCarId(newCarId);
            carRepo.save(car);
            log.info("< editCarById");
            return car;
        } catch (PatternSyntaxException e){
            errMess = "RegEx not suitable";
        }
        log.error("< RegEx not suitable with error = {}", errMess);
        return null;
    }
}

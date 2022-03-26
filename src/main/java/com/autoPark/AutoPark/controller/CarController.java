package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.repos.DriverRepo;
import com.autoPark.AutoPark.service.CarService;
import com.autoPark.AutoPark.utils.Parser;
import com.autoPark.AutoPark.utils.RestError;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/Car")
public class CarController {
    private static final Logger log = LoggerFactory.getLogger(CarController.class.getName());
    private final CarService carService;
    private final CarRepo carRepo;
    private final DriverRepo driverRepo;

    public CarController(CarService carService, CarRepo carRepo, DriverRepo driverRepo) {
        this.carService = carService;
        this.carRepo = carRepo;
        this.driverRepo = driverRepo;
    }

    //TODO добавить логи ко всем методам и сделать у каждой ошибки свой номер!

    @PostMapping(value = "/registration")
    public ResponseEntity<RestError> registration(@RequestBody String json) {
        log.info("start registration");
        try {
            JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
            String carId = jo.get("carId").getAsString();
            Category category = Category.valueOf(jo.get("category").getAsString().toUpperCase(Locale.ROOT));
            if (carId != null && category != null) {
                log.info("Data accepted");
                try {
                    Long driverId = jo.get("driverId").getAsLong();
                    Driver driver = driverRepo.findById(driverId).orElse(null);
                    if (driver != null) {
                        if (driver.getCategory().equals(category)) {
                            Car car = carService.registration(carId, category, driverId);
                            if (car != null) {
                                log.info("registration end");
                                return ResponseEntity.ok(new RestError(car));
                            }
                            log.error("The car is already");
                            return ResponseEntity.badRequest().body(new RestError(3, "Машина уже имеется"));
                        }
                        log.error("not suitable categories");
                        return ResponseEntity.badRequest().body(new RestError(5, "не подходящие категории"));
                    }
                    log.error("Driver not found");
                    return ResponseEntity.badRequest().body(new RestError(4, "водителя не существует"));
                } catch (NullPointerException e) {
                    Car car = carService.registration(carId, category, null);
                    if (car != null) {
                        log.info("registration end");
                        return ResponseEntity.ok(new RestError(car));
                    }
                    log.error("The car is already");
                    return ResponseEntity.badRequest().body(new RestError(3, "Машина уже имеется"));
                }
            }
            log.error("Empty data");
            return ResponseEntity.badRequest().body(new RestError(2, "Пустые данные"));
        } catch (Exception e) {
            log.error("Unknown error");
            return ResponseEntity.badRequest().body(new RestError(1, "ошибка "));
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<RestError> deleteCar(@RequestParam Long id) {
        Car car = carRepo.findById(id).orElse(null);
        if (car == null) {
            return ResponseEntity.badRequest().body(new RestError(1, "Нету такого автомобиля"));
        }
        try {
            carRepo.delete(car);
            return ResponseEntity.ok(new RestError("OK"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(3, "Ошибка"));
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<RestError> editCarById(@RequestBody String json) {
        JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
        Long id = jo.get("id").getAsLong();
        String newCarId = jo.get("newCarId").getAsString();
        Car oldCar = carRepo.findById(id).orElse(null);
        if (oldCar == null) {
            return ResponseEntity.badRequest().body(new RestError(1, "Машины существует не существует"));
        }
        try {
            Car car = carService.editCarById(
                    oldCar,
                    newCarId
            );
            return ResponseEntity.ok(new RestError(car));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(2, "Ошибка"));
        }
    }

    @GetMapping("/get-car-by-id")
    public ResponseEntity<RestError> getCarById(@RequestParam Long id) {
        Car car = carRepo.findById(id).orElse(null);
        if (car == null) {
            return ResponseEntity.badRequest().body(new RestError(1, "Нету такого автомобиля"));
        }
        try {
            return ResponseEntity.ok(new RestError(car));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(2, "Ошибка"));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<RestError> getAll() {
        try {
            return ResponseEntity.ok(new RestError(carRepo.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Ошибка"));
        }
    }

    @GetMapping("/without-driver")
    public ResponseEntity<RestError> getWithoutDriver() {
        return ResponseEntity.ok(new RestError(carRepo.findCarsWithoutDriver()));
    }


    @GetMapping("/get-drivers-by-category-car")
    public ResponseEntity<RestError> getDriversByCategoryCar(@RequestParam Long id) {
        Car car = carRepo.findById(id).orElse(null);
        if (car != null) {
            try {
                List<Driver> drivers = new ArrayList<Driver>();
                driverRepo.findAll().forEach(driver -> {
                    if (car.getCategory().equals(driver.getCategory())) drivers.add(driver);
                });
                return ResponseEntity.ok(new RestError(drivers));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(new RestError(2, "Ошибка поиска по категориям"));
            }
        } else {
            return ResponseEntity.badRequest().body(new RestError(1, "Нету такого автомобиля"));
        }
    }
}

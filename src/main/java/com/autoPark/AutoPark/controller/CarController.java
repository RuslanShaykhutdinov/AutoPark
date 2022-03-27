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
    private static final Logger log = LoggerFactory.getLogger(CarController.class);
    private final CarService carService;
    private final CarRepo carRepo;
    private final DriverRepo driverRepo;
    private String errMess = "Unknown error";

    public CarController(CarService carService, CarRepo carRepo, DriverRepo driverRepo) {
        this.carService = carService;
        this.carRepo = carRepo;
        this.driverRepo = driverRepo;
    }


    @PostMapping(value = "/registration")
    public ResponseEntity<RestError> registration(@RequestBody String json) {
        log.info("> registration with json={}", json);
        try {
            JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
            String carId = jo.get("carId").getAsString();
            Category category = null;
            try {
                category = Category.valueOf(jo.get("category").getAsString().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                log.warn("wrong category");
            }
            if (carId != null && category != null) {
                try {
                    Long driverId = jo.get("driverId").getAsLong();
                    Driver driver = driverRepo.findById(driverId).orElse(null);
                    if (driver != null) {
                        if (driver.getCategory().equals(category)) {
                            Car car = carService.registration(carId, category, driverId);
                            if (car != null) {
                                log.info("< registration");
                                return ResponseEntity.ok(new RestError(car));
                            }
                            errMess = "The car is already exist";
                            log.error("< registration with error = {}", errMess);
                            return ResponseEntity.badRequest().body(new RestError(3, errMess));
                        }
                        errMess = "not suitable categories";
                        log.error("< registration with error = {}", errMess);
                        return ResponseEntity.badRequest().body(new RestError(5, errMess));
                    }
                    errMess = "Driver not found";
                    log.error("< registration with error = {}", errMess);
                    return ResponseEntity.badRequest().body(new RestError(4, errMess));
                } catch (NullPointerException e) {
                    log.warn("driverId is null");
                    Car car = carService.registration(carId, category, null);
                    if (car != null) {
                        log.info("< registration");
                        return ResponseEntity.ok(new RestError(car));
                    }
                    errMess = "The car is already exist";
                    log.error("< registration with error = {}", errMess);
                    return ResponseEntity.badRequest().body(new RestError(3, errMess));
                }
            }
            errMess = "empty data";
            log.error("< registration with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(2, errMess));
        } catch (Exception e) {
            log.error("< registration with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<RestError> deleteCar(@RequestParam Long id) {
        log.info("> deleteCar with id)={}", id);
        try {
            Car car = carRepo.findById(id).orElse(null);
            if (car != null) {
                carRepo.delete(car);
                log.info("< deleteCar");
                return ResponseEntity.ok(new RestError("OK"));
            }
            errMess = "Car not found";
            log.error("< deleteCar with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(6, errMess));
        } catch (Exception e) {
            log.error("< deleteCar with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<RestError> editCarById(@RequestBody String json) {
        log.info("> editCarById with json)={}", json);
        try {
            JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
            Long id = jo.get("id").getAsLong();
            String newCarId = jo.get("newCarId").getAsString();
            Car oldCar = carRepo.findById(id).orElse(null);
            if (oldCar != null) {
                Car car = carService.editCarById(oldCar, newCarId);
                log.info("< editCarById");
                return ResponseEntity.ok(new RestError(car));
            }
            errMess = "Car not found";
            log.error("< editCarById with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(6, errMess));
        } catch (Exception e) {
            log.error("< editCarById with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }

    @GetMapping("/get-car-by-id")
    public ResponseEntity<RestError> getCarById(@RequestParam Long id) {
        log.info("> getCarById with id)={}", id);
        try {
            Car car = carRepo.findById(id).orElse(null);
            if (car != null) {
                log.info("< getCarById");
                return ResponseEntity.ok(new RestError(car));
            }
            errMess = "Car not found";
            log.error("< getCarById with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(6, errMess));
        } catch (Exception e) {
            log.error("< getCarById with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<RestError> getAllCar() {
        log.info("> getAllCar");
        try {
            log.info("< getAllCar");
            return ResponseEntity.ok(new RestError(carRepo.findAll()));
        } catch (Exception e) {
            log.error("< getAll with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }

    @GetMapping("/without-driver")
    public ResponseEntity<RestError> getWithoutDriver() {
        log.info("> getWithoutDriver");
        try {
            log.info("< getWithoutDriver");
            return ResponseEntity.ok(new RestError(carRepo.findCarsWithoutDriver()));
        } catch (Exception e) {
            log.error("< getWithoutDriver with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }


    @GetMapping("/get-drivers-by-category-car")
    public ResponseEntity<RestError> getDriversByCategoryCar(@RequestParam Long id) {
        log.info("> getDriversByCategoryCar with id)={}", id);
        try {
            Car car = carRepo.findById(id).orElse(null);
            if (car != null) {
                try {
                    List<Driver> drivers = new ArrayList<Driver>();
                    driverRepo.findAll().forEach(driver -> {
                        if (car.getCategory().equals(driver.getCategory())) drivers.add(driver);
                    });
                    log.info("<  getDriversByCategoryCar");
                    return ResponseEntity.ok(new RestError(drivers));
                } catch (Exception e) {
                    errMess = "not suitable categories";
                    log.error("< getDriversByCategoryCar with error = {}", errMess);
                    return ResponseEntity.badRequest().body(new RestError(5, errMess));
                }
            } else {
                errMess = "Car not found";
                log.error("< getDriversByCategoryCar with error = {}", errMess);
                return ResponseEntity.badRequest().body(new RestError(6, errMess));
            }
        } catch (Exception e) {
            log.error("< getDriversByCategoryCar with error = {}", errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }
}

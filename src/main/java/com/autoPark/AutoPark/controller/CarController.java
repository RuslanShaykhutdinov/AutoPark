package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.repos.DriverRepo;
import com.autoPark.AutoPark.service.CarService;
import com.autoPark.AutoPark.service.DriverService;
import com.autoPark.AutoPark.utils.RestError;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/Car")
public class CarController {
    JsonParser parser = new JsonParser();
    private final CarService carService;
    private final CarRepo carRepo;
    private final DriverRepo driverRepo;
    private final DriverService driverService;

    public CarController(CarService carService, CarRepo carRepo, DriverRepo driverRepo, DriverService driverService) {
        this.carService = carService;
        this.carRepo = carRepo;
        this.driverRepo = driverRepo;
        this.driverService = driverService;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<RestError> registration(@RequestBody String json) {
        try {
            JsonObject jo = parser.parse(json).getAsJsonObject();
            String carId = jo.get("carId").getAsString();
            Category category = Category.valueOf(jo.get("category").getAsString().toUpperCase(Locale.ROOT));
            try {
                String passportDriver = jo.get("passportDriver").getAsString();
                Driver driver = driverRepo.findByPassportId(passportDriver);
                if (driver != null) {
                    if (driver.getCategory().equals(category)) {
                        Car car = new Car(carId, category, driver.getId());
                        if (carService.registration(car))
                            return ResponseEntity.ok(new RestError(car));
                        return ResponseEntity.badRequest().body(new RestError(4, "Машина уже имеется"));
                    }
                    return ResponseEntity.badRequest().body(new RestError(3, "не подходящие категории"));
                }
            } catch (NullPointerException e) {
                Car car = new Car(carId, category, null);
                if (carService.registration(car))
                    return ResponseEntity.ok(new RestError(car));
                return ResponseEntity.badRequest().body(new RestError(4, "Машина уже имеется"));
            }
            return ResponseEntity.badRequest().body(new RestError(2, "водителя не существует"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Возможно ошибка в парсинге"));
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
        JsonObject jo = parser.parse(json).getAsJsonObject();
        Long id = jo.get("Id").getAsLong();
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

    @GetMapping("/getCarById")
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
            return ResponseEntity.ok(new RestError(carService.getAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Ошибка"));
        }
    }

    @GetMapping("/withoutDriver")
    public ResponseEntity<RestError> getWithoutDriver() {
        return ResponseEntity.ok(new RestError(carRepo.findCarsWithoutDriver()));
    }


    @GetMapping("/getDriversByCategoryCar")
    public ResponseEntity<RestError> getDriversByCategoryCar(@RequestParam Long id) {
        Car car = carRepo.findById(id).orElse(null);
        if (car!=null) {
            try {
                return ResponseEntity.ok(new RestError(driverRepo.findByCategory(car.getCategory().toString())));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(new RestError(2, "Ошибка поиска по категориям"));
            }
        } else {
            return ResponseEntity.badRequest().body(new RestError(1, "Нету такого автомобиля"));
        }
    }
}

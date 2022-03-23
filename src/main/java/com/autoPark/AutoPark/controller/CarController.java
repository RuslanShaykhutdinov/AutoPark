package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.repos.DriverRepo;
import com.autoPark.AutoPark.service.CarService;
import com.autoPark.AutoPark.utils.RestError;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/Car")
public class CarController {
    JsonParser parser = new JsonParser();
    private final CarService carService;
    private final CarRepo carRepo;
    private final DriverRepo driverRepo;
    public CarController(CarService carService, CarRepo carRepo, DriverRepo driverRepo) {
        this.carService = carService;
        this.carRepo = carRepo;
        this.driverRepo = driverRepo;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<RestError> registration(@RequestBody String json) {
        try {
            JsonObject jo = parser.parse(json).getAsJsonObject();
            String carId = jo.get("carId").getAsString();
            String passportDriver = jo.get("passportDriver").getAsString();
            Category category = Category.valueOf(jo.get("category").getAsString().toUpperCase(Locale.ROOT));
            if (Pattern.matches("((01)|(10)|(20)|(30)|(40)|(60)|(80)|(90))(([A-Z]+[0-9]{3})+([A-Z]{2})|([0-9]{3}[A-Z]{3}))", carId)) {
                if (passportDriver.equals("null")) {
                    Car car = new Car(carId, category, null);
                    if (carService.registration(car))
                        return ResponseEntity.ok(new RestError(car));
                    return ResponseEntity.badRequest().body(new RestError(6, "Машина уже имеется"));
                } else if (Pattern.matches("[A-Z]{2}[0-9]{7}", passportDriver)) {
                    Driver driver = driverRepo.findByPassportId(passportDriver);
                    if (driver != null) {
                        if (driver.getCategory().equals(category)) {
                            Car car = new Car(carId, category, driver.getId());
                            if (carService.registration(car))
                                return ResponseEntity.ok(new RestError(car));
                            return ResponseEntity.badRequest().body(new RestError(6, "Машина уже имеется"));
                        }
                        return ResponseEntity.badRequest().body(new RestError(5, "не подходящие категории"));
                    }
                    return ResponseEntity.badRequest().body(new RestError(4, "водителя не существует"));
                } else
                    return ResponseEntity.badRequest().body(new RestError(3, "Невозможный номер пасспорта водителя"));
            }
            return ResponseEntity.badRequest().body(new RestError(2, "не правильные данные"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Возможно ошибка в парсинге"));
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<RestError> delete(@RequestParam Long id) {
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

/*  TODO
 @GetMapping("/getDriversByCategoryCar")
    public ResponseEntity<RestError> getDriversByCategoryCar(@RequestParam Long id) {
        Car car = carRepo.findById(id).orElse(null);
        if (car != null) {
            return ResponseEntity.ok(new RestError(driverRepo.findByCategory(car.getCategory())));
        }
        return ResponseEntity.badRequest().body(new RestError(1, "Нету такого автомобиля"));
    }*/
}

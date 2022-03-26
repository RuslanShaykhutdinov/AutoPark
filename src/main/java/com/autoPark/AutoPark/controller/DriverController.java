package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.repos.DriverRepo;
import com.autoPark.AutoPark.service.DriverService;
import com.autoPark.AutoPark.utils.Parser;
import com.autoPark.AutoPark.utils.RestError;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/Driver")
public class DriverController {
    private final DriverRepo driverRepo;
    private final DriverService driverService;
    private final CarRepo carRepo;

    public DriverController(DriverRepo driverRepo, DriverService driverService, CarRepo carRepo) {
        this.driverRepo = driverRepo;
        this.driverService = driverService;
        this.carRepo = carRepo;
    }
    //TODO добавить логи ко всем методам и сделать у каждой ошибки свой номер!

    @PostMapping(value = "/registration")
    public ResponseEntity<RestError> registration(@RequestBody String json) {
        try {
            JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
            String passportId = jo.get("passportId").getAsString();
            String name = jo.get("name").getAsString();
            Category category = Category.valueOf(jo.get("category").getAsString().toUpperCase(Locale.ROOT));
            if (passportId != null && name != null && category != null) {
                Driver driver = driverService.registration(passportId, name, category);
                if (driver != null) return ResponseEntity.ok(new RestError(driver));
                return ResponseEntity.badRequest().body(new RestError(3, "Уже имеется"));
            }
            return ResponseEntity.badRequest().body(new RestError(2, "Пустые данные"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Возможно ошибка в парсинге"));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<RestError> getAll() {
        try {
            return ResponseEntity.ok(new RestError(driverRepo.findAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Ошибка"));
        }
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<RestError> getById(@RequestParam Long id) {
        try {
            Driver driver = driverRepo.findById(id).orElse(null);
            if (driver != null)
                return ResponseEntity.ok(new RestError(driver));
            return ResponseEntity.badRequest().body(new RestError(2, "Не найден"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Не известная ошибка"));
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<RestError> editDriver(@RequestParam Long id, @RequestBody String json) {
        try {
            JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
            String passportId = jo.get("passportId").getAsString();
            String name = jo.get("name").getAsString();
            if (passportId != null && name != null) {
                Driver newDriver = driverService.editDriver(id, passportId, name);
                if (newDriver != null)
                    return ResponseEntity.ok(new RestError(newDriver));
                return ResponseEntity.badRequest().body(new RestError(3, "Водителя не существует"));
            }
            return ResponseEntity.badRequest().body(new RestError(2, "Не верные данные"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Возможно ошибка в парсинге"));
        }
    }

    @GetMapping(value = "/get-by-passport")
    public ResponseEntity<RestError> getByPassport(@RequestParam String passportId) {
        try {
            Driver driver = driverRepo.findByPassportId(passportId);
            if (driver != null)
                return ResponseEntity.ok(new RestError(driver));
            return ResponseEntity.badRequest().body(new RestError(2, "Не найден"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Не известная ошибка"));
        }
    }

    @GetMapping(value = "/without-cars")
    public ResponseEntity<RestError> withoutCars() {
        return ResponseEntity.ok(new RestError(driverService.withoutCars()));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<RestError> deleteDriver(@RequestParam Long id) {
        Driver driver = driverRepo.findById(id).orElse(null);
        if (driver != null) {
            List<Car> cars = carRepo.findCarsByDriverId(driver.getId());
            cars.forEach(car -> {
                car.setDriverId(null);
                carRepo.save(car);
            });
            driverRepo.delete(driver);
            return ResponseEntity.ok(new RestError("OK"));
        }
        return ResponseEntity.badRequest().body(new RestError(1, "Несуществующий водитель"));
    }

    @GetMapping(value = "/driver-cars")
    public ResponseEntity<RestError> driverCars(@RequestParam Long id) {
        List<Car> carsList = carRepo.findCarsByDriverId(id);
        if (carsList.isEmpty()) {
            return ResponseEntity.badRequest().body(new RestError(1, "У данного водителя нету автомобилей"));
        } else {
            return ResponseEntity.ok(new RestError(carsList));
        }
    }

    @PutMapping(value = "/pin-driver")
    public ResponseEntity<RestError> pinDriver(@RequestBody String json) {
        JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
        String passportId = jo.get("passportId").getAsString();
        String carId = jo.get("carId").getAsString();
        Car car = carRepo.findByCarId(carId);
        Driver driver = driverRepo.findByPassportId(passportId);
        if (driver == null) {
            return ResponseEntity.badRequest().body(new RestError(2, "Такого водителя не существует"));
        }
        if (car == null) {
            return ResponseEntity.badRequest().body(new RestError(4, "Данного автомобиля не существует"));
        }
        if (!car.getCategory().equals(driver.getCategory())) {
            return ResponseEntity.badRequest().body(new RestError(5, "Данный водитель не может водить данный автомобиль"));
        }
        try {
            driverService.pinDriverToCar(car, driver);
            return ResponseEntity.ok(new RestError("OK"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(6, "Ошибка"));
        }
    }

    @PutMapping(value = "/unpin-driver")
    public ResponseEntity<RestError> unpinDriver(@RequestBody String json) {
        JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
        String passportId = jo.get("passportId").getAsString();
        String carId = jo.get("carId").getAsString();
        Car car = carRepo.findByCarId(carId);
        Driver driver = driverRepo.findByPassportId(passportId);
        if (car.getDriverId().equals(driver.getId())) {
            driverService.unpinDriverFromCar(car);
            return ResponseEntity.ok(new RestError("OK"));
        } else {
            return ResponseEntity.badRequest().body(new RestError(1, "Ошибка"));
        }
    }
}

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/Driver")
public class DriverController {
    JsonParser parser = new JsonParser();
    private final DriverRepo driverRepo;
    private final DriverService driverService;
    private final CarRepo carRepo;
    private final CarService carService;

    public DriverController(DriverRepo driverRepo, DriverService driverService, CarRepo carRepo, CarService carService) {
        this.driverRepo = driverRepo;
        this.driverService = driverService;
        this.carRepo = carRepo;
        this.carService = carService;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<RestError> registration(@RequestBody String json) {
        try {
            JsonObject jo = parser.parse(json).getAsJsonObject();
            String passportId = jo.get("passportId").getAsString();
            String name = jo.get("name").getAsString();
            Category category = Category.valueOf(jo.get("category").getAsString().toUpperCase(Locale.ROOT));
                Driver driver = new Driver(passportId, name, category);
                if (driverService.registration(driver)) return ResponseEntity.ok(new RestError(driver));
                return ResponseEntity.badRequest().body(new RestError(3, "Уже имеется"));
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

    @GetMapping("/getById")
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
            JsonObject jo = parser.parse(json).getAsJsonObject();
            String passportId = jo.get("passportId").getAsString();
            String name = jo.get("name").getAsString();
            if (passportId!=null && name!=null) {
                if (driverService.editDriver(id, passportId , name)) return ResponseEntity.ok(new RestError("Ok"));
                return ResponseEntity.badRequest().body(new RestError(3, "Водителя не существует"));
            }
            return ResponseEntity.badRequest().body(new RestError(2, "Не верные данные"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "Возможно ошибка в парсинге"));
        }
    }

    @GetMapping(value = "/getByPassport")
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

    @GetMapping(value = "/withoutCars")
    public ResponseEntity<RestError> withoutCars() {
        HashSet<Long> driversWithCars= new HashSet<>();
        carRepo.carsWithDriver().forEach(car -> {
            driversWithCars.add(car.getDriverId());
        });
        ArrayList<Driver> drivers=new ArrayList<>();
        driverRepo.findAll().forEach(driver -> {
            if (!(driversWithCars.contains(driver.getId())))
                drivers.add(driver);
        });
        return ResponseEntity.ok(new RestError(drivers));
    }


    @DeleteMapping(value = "/delete")
    public ResponseEntity<RestError> deleteDriver(@RequestParam Long id) {
        Driver driver = driverRepo.findById(id).orElse(null);
        if (driver != null) {
            List<Car> cars=carRepo.findCarsByDriverId(driver.getId());
            cars.forEach( car -> {
                car.setDriverId(null);
                carRepo.save(car);
            });
            driverRepo.delete(driver);
            return ResponseEntity.ok(new RestError("OK"));
        }
        return ResponseEntity.badRequest().body(new RestError(1, "Несуществующий водитель"));
    }
}

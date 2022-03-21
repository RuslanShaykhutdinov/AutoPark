package com.autoPark.AutoPark.controller;

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

import java.util.List;

@RestController
@RequestMapping(value = "/Driver")
public class DriverController {
    JsonParser parser = new JsonParser();
    private final DriverRepo driverRepo;
    private final DriverService driverService;
    private final CarService carService;
    private final CarRepo carRepo;

    public DriverController(DriverRepo driverRepo, DriverService driverService, CarService carService, CarRepo carRepo) {
        this.driverRepo = driverRepo;
        this.driverService = driverService;
        this.carService = carService;
        this.carRepo = carRepo;
    }

    @GetMapping(value = "/drivercars")
    public ResponseEntity<RestError> drivercars(@RequestParam Long Id){
        List<Car> carsList = carRepo.findCarsByDriverId(Id);
        if(carsList.isEmpty()){
            return ResponseEntity.badRequest().body(new RestError(1,"У данного водителя нету автомобилей"));
        }else{
            return ResponseEntity.ok(new RestError(carsList));
        }
    }
    @PutMapping(value = "/pindriver")
    public ResponseEntity<RestError> pindriver(@RequestBody String json){
        JsonObject jo = parser.parse(json).getAsJsonObject();
        String passportId = jo.get("PassportId").getAsString();
        String carId = jo.get("CarId").getAsString();
        Car car = carRepo.findByCarId(carId);
        Driver driver = driverRepo.findByPassportId(passportId);
        if (passportId == null){
            return ResponseEntity.badRequest().body(new RestError(1,"Введите номер пасспорта"));
        }else if(driver == null){
            return ResponseEntity.badRequest().body(new RestError(2,"Такого водителя не существует"));
        }
        if(carId == null){
            return ResponseEntity.badRequest().body(new RestError(3,"Введите номер автомобиля"));
        }else if(car == null){
            return ResponseEntity.badRequest().body(new RestError(4,"Данного автомобиля не существует"));
        }
        try {
             driverService.pinDriverToCar(car,driver);
             return ResponseEntity.ok(new RestError("OK"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new RestError(5,"Ошибка"));
        }
    }

    @PutMapping(value = "/unpindriver")
    public ResponseEntity<RestError> unpindriver(@RequestBody String json){
        JsonObject jo = parser.parse(json).getAsJsonObject();
        String passportId = jo.get("PassportId").getAsString();
        String carId = jo.get("CarId").getAsString();
        Car car = carRepo.findByCarId(carId);
        Driver driver = driverRepo.findByPassportId(passportId);
        if (passportId == null){
            return ResponseEntity.badRequest().body(new RestError(1,"Введите номер пасспорта"));
        }else if(driver == null){
            return ResponseEntity.badRequest().body(new RestError(2,"Такого водителя не существует"));
        }
        if(carId == null){
            return ResponseEntity.badRequest().body(new RestError(3,"Введите номер автомобиля"));
        }else if(car == null){
            return ResponseEntity.badRequest().body(new RestError(4,"Данного автомобиля не существует"));
        }
        if(car.getDriverId() == driver.getId()){
            driverService.unpinDriverFromCar(car);
            return ResponseEntity.ok(new RestError("OK"));
        }else{
            return ResponseEntity.badRequest().body(new RestError(5,"Ошибка"));
        }
    }
}

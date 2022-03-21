package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.service.CarService;
import com.autoPark.AutoPark.utils.RestError;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/Car")
public class CarController {
    JsonParser parser = new JsonParser();
    private final CarService carService;
    private final CarRepo carRepo;
    public CarController(CarService carService, CarRepo carRepo) {
        this.carService = carService;
        this.carRepo = carRepo;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<RestError> registration(@RequestBody String json)  {
        try {
            JsonObject jo = parser.parse(json).getAsJsonObject();
            Car car = new Car(
                    jo.get("carId").getAsString(),
                    Category.valueOf(jo.get("category").getAsString().toUpperCase(Locale.ROOT))
            );
            /*try {
                Long driverId=jo.get("driverId").getAsLong();
                car.setDriverId(driverId);
            } catch (NullPointerException e){
                e.getMessage();
            }*/

            if (!carService.registration(car))
                return ResponseEntity.badRequest().body(new RestError(1, "Машина уже имеется"));
            return ResponseEntity.ok(new RestError(car));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "не правильные данные"));
        }
    }
    @DeleteMapping(value = "/delete")
    public ResponseEntity<RestError> delete(@RequestBody String json){
        JsonObject jo = parser.parse(json).getAsJsonObject();
        Long id = jo.get("Id").getAsLong();
        Car car = carRepo.findById(id).orElse(null);
        if(car == null){
            return ResponseEntity.badRequest().body(new RestError(1,"Нету такого автомобиля"));
        }
        try {
            carRepo.delete(car);
            return ResponseEntity.ok(new RestError("OK"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new RestError(3,"Ошибка"));
        }
    }
    @PutMapping(value = "/edit")
    public ResponseEntity<RestError> editCarById(@RequestBody String json){
        JsonObject jo = parser.parse(json).getAsJsonObject();
        Long id = jo.get("Id").getAsLong();
        String newCarId = jo.get("newCarId").getAsString();
        Car oldCar = carRepo.findById(id).orElse(null);
        if(oldCar == null){
            return ResponseEntity.badRequest().body(new RestError(1,"Машины существует не существует"));
        }
        try{
            Car car = carService.editCarById(
                        oldCar,
                        newCarId
            );
            return ResponseEntity.ok(new RestError(car));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new RestError(2,"Ошибка"));
        }
    }

    @GetMapping("/getCarById")
    public ResponseEntity<RestError> getCarById(@RequestParam Long Id){
        Car car = carRepo.findById(Id).orElse(null);
        if(car == null){
            return ResponseEntity.badRequest().body(new RestError(1,"Нету такого автомобиля"));
        }
        try{
            return ResponseEntity.ok(new RestError(car));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new RestError(2,"Ошибка"));
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

}

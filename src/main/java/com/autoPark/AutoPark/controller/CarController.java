package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.service.CarService;
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
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<RestError> registration(@RequestBody String json)  {
        try {
            JsonObject jo = parser.parse(json).getAsJsonObject();
            Car car = new Car(
                    jo.get("carId").getAsString(),
                    carService.toCategory(jo.get("category").getAsString().toUpperCase(Locale.ROOT))
            );
            /*try {
                Long driverId=jo.get("driverId").getAsLong();
                car.setDriverId(driverId);
            } catch (NullPointerException e){
                e.getMessage();
            }*/

            if (carService.registration(car) == false)
                return ResponseEntity.badRequest().body(new RestError(1, "Машина уже имеется"));
            return ResponseEntity.ok(new RestError(car));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1, "не правильные данные"));
        }
    }
    @PutMapping(value = "/manage")
    public ResponseEntity<RestError> delete(@RequestBody String json){
        try {
            JsonObject jo = parser.parse(json).getAsJsonObject();
            if (!carService.delete(jo.get("Id").getAsLong())){
                return ResponseEntity.badRequest().body(new RestError(1,"Ошибка"));
            }else {
                return ResponseEntity.ok(new RestError(carService.delete(jo.get("Id").getAsLong())));
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new RestError(1,"Ошибка"));
        }
    }
    @PutMapping(value = "/manage")
    public ResponseEntity<RestError> editCarById(@RequestBody String json){
        try{
            JsonObject jo = parser.parse(json).getAsJsonObject();
            Car car = carService.editCarById(
                        carService.getCarById(jo.get("Id").getAsLong()),
                        jo.get("newCarId").getAsString(),
                        carService.toCategory(jo.get("category").getAsString().toUpperCase(Locale.ROOT))
            );
            return ResponseEntity.ok(new RestError(car));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new RestError(1,"Ошибка"));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<RestError> getCarById(@RequestBody String json){
        try{
            JsonObject jo = parser.parse(json).getAsJsonObject();
            return ResponseEntity.ok(new RestError(carService.getCarById(jo.get("Id").getAsLong())));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new RestError(1,"Ошибка"));
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

package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.entity.Car;
import com.autoPark.AutoPark.exception.MessageException;
import com.autoPark.AutoPark.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Car")
public class CarController {
    @Autowired
    private CarService carService;
    @PostMapping
    public ResponseEntity registration(@RequestBody Car car){
        try {
            carService.registration(car);
            return ResponseEntity.ok("Машина зарегестрирована");
        }catch (MessageException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }

    @GetMapping()
    public ResponseEntity getOneCars(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(carService.getOne(id));
        } catch (MessageException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }

    @GetMapping("carId")
    public ResponseEntity getOneByCarId(@RequestParam String id) {
        try {
            return ResponseEntity.ok(carService.getOneByCarId(id));
        } catch (MessageException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }

    @GetMapping("/many")
    public ResponseEntity getManyCar(@RequestParam boolean driver) {
        try {
            return ResponseEntity.ok(carService.getMany(driver));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }

    //TODO  change


    @DeleteMapping("/{id}")
    public ResponseEntity deleteCar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(carService.delete(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }

}
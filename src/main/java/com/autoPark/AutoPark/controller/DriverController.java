package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.entity.Driver;
import com.autoPark.AutoPark.exception.MessageException;
import com.autoPark.AutoPark.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Driver")
public class DriverController {
    @Autowired
    private DriverService driverService;
    @PostMapping
    public ResponseEntity registration(@RequestBody Driver driver){
        try {
            driverService.registration(driver);
            return ResponseEntity.ok("Prinyali klienta");
        }catch (MessageException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Oshibka klienta");
        }
    }

    @GetMapping("/many")
    public ResponseEntity getManyDrivers(@RequestParam boolean car) {
        try {
            return ResponseEntity.ok(driverService.getMany(car));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }

    @GetMapping
    public ResponseEntity getOneDriverById(@RequestParam Long id ) {
        try {
            return ResponseEntity.ok(driverService.getOne(id));
        } catch (MessageException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }

    @GetMapping("/passport")
    public ResponseEntity getOneDriverByPassport(@RequestParam String id ) {
        try {
            return ResponseEntity.ok(driverService.getOneByPassport(id));
        } catch (MessageException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteDriver(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(driverService.delete(id));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }
}
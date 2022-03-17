package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.service.DriverService;
import com.autoPark.AutoPark.utils.RestError;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
/**
 *
 * @author HououinKyoma2000
 */

@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping(value = "registration")
    public ResponseEntity registration(@RequestBody String json) throws ParseException {
        try {
        JSONParser parser=new JSONParser(json);
        LinkedHashMap<String,Object> hashMap=parser.parseObject();
        Driver driver=new Driver(
                (String) hashMap.get("passportId"),
                (String) hashMap.get("name"),
                Category.BUS.toCategory((String) hashMap.get("category"))
        );

            if (driverService.registration(driver).equals(null))
                return ResponseEntity.badRequest().body(new RestError(1,"Клиент уже имеется"));
            return ResponseEntity.ok(new RestError("клиент принят "));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1,"не правильные данные"));
        }
    }

    @GetMapping("byId")
    public ResponseEntity getOneDriverById(@RequestParam Long id ) {
        try {
            return ResponseEntity.ok(new RestError(driverService.getOne(id)));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1,"Ошибка"));
        }
    }

    @GetMapping("/passport")
    public ResponseEntity getOneDriverByPassport(@RequestParam String id ) {
        try {
            return ResponseEntity.ok(new RestError(driverService.getOneByPassport(id)));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1,"Ошибка"));
        }
    }

    @GetMapping("/many")
    public ResponseEntity getManyDrivers(@RequestParam @Value(value = "true") boolean car) {
        try {
            return ResponseEntity.ok(new RestError(driverService.getMany(car)));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1,"Ошибка"));
        }
    }

    // TODO
    /*@PutMapping
    public ResponseEntity changeDriver(@RequestBody String json) throws ParseException {
        try {
            JSONParser parser=new JSONParser(json);
            LinkedHashMap<String,Object> hashMap=parser.parseObject();
            Driver driver=new Driver(
                    (String) hashMap.get("passportId"),
                    (String) hashMap.get("name"),
                    Category.BUS.toCategory((String) hashMap.get("category"))
            );

            TODO
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1,"не правильные данные"));
        }
    }*/

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteDriver(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new RestError(driverService.delete(id)));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestError(1,"Ошибка"));
        }
    }
}

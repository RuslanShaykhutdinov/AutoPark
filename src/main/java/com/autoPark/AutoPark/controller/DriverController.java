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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/Driver")
public class DriverController {
    private static final Logger log = LoggerFactory.getLogger(DriverController.class);
    private final DriverRepo driverRepo;
    private final DriverService driverService;
    private final CarRepo carRepo;
    private String errMess = "Unknown error";
    public DriverController(DriverRepo driverRepo, DriverService driverService, CarRepo carRepo) {
        this.driverRepo = driverRepo;
        this.driverService = driverService;
        this.carRepo = carRepo;
    }
    //TODO добавить логи ко всем методам и сделать у каждой ошибки свой номер!

    @PostMapping(value = "/registration")
    public ResponseEntity<RestError> registration(@RequestBody String json) {
        log.info("> registration with json = {}",json);
        try {
            JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
            String passportId = jo.get("passportId").getAsString();
            String name = jo.get("name").getAsString();
            Category category = Category.valueOf(jo.get("category").getAsString().toUpperCase(Locale.ROOT));
            if (passportId != null && name != null && category != null) {
                Driver driver = driverService.registration(passportId, name, category);
                if (driver != null){
                    log.info("< registration");
                    return ResponseEntity.ok(new RestError(driver));
                }else{
                    errMess = "Driver already exist";
                    log.info("< registration with error = {}",errMess);
                    return ResponseEntity.badRequest().body(new RestError(3, errMess));
                }
            }else{
                errMess = "Empty fields";
                log.info("< registration with error = {}",errMess);
                return ResponseEntity.badRequest().body(new RestError(2, errMess));
            }
        } catch (Exception e) {
            log.info("< registration with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<RestError> getAll() {
        log.info("> getAll(drivers)");
        try {
            log.info("< getAll(drivers)");
            return ResponseEntity.ok(new RestError(driverRepo.findAll()));
        } catch (Exception e) {
            log.info("< getAll(drivers) with unknown error");
            return ResponseEntity.badRequest().body(new RestError(1, "Ошибка"));
        }
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<RestError> getById(@RequestParam Long id) {
        log.info("> getById with id = {}",id);
        try {
            Driver driver = driverRepo.findById(id).orElse(null);
            if (driver != null){
                log.info("< getById");
                return ResponseEntity.ok(new RestError(driver));
            }else{
                errMess = "not found";
                log.info("< getById(drivers) with error ={}",errMess);
                return ResponseEntity.badRequest().body(new RestError(2, errMess));
            }
        } catch (Exception e) {
            log.info("< getById(drivers) with error ={}",errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<RestError> editDriver(@RequestParam Long id, @RequestBody String json) {
        log.info("> editDriver with id = {},json = {}",id,json);
        try {
            JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
            String passportId = jo.get("passportId").getAsString();
            String name = jo.get("name").getAsString();
            if (passportId != null && name != null) {
                Driver newDriver = driverService.editDriver(id, passportId, name);
                if (newDriver != null){
                    log.info("< editDriver");
                    return ResponseEntity.ok(new RestError(newDriver));
                }else{
                    errMess = "Driver doesn't exist";
                    log.info("< editDriver with error = {}",errMess);
                    return ResponseEntity.badRequest().body(new RestError(3, errMess));
                }
            }else {
                errMess = "Wrong input";
                log.info("< editDriver with error = {}",errMess);
                return ResponseEntity.badRequest().body(new RestError(2, "Не верные данные"));
            }
        } catch (Exception e) {
            log.info("< editDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }

    @GetMapping(value = "/get-by-passport")
    public ResponseEntity<RestError> getByPassport(@RequestParam String passportId) {
        log.info("> getByPassport with passportId = {}",passportId);
        try {
            Driver driver = driverRepo.findByPassportId(passportId);
            if (driver != null){
                log.info("< getByPassport");
                return ResponseEntity.ok(new RestError(driver));
            }else{
                errMess = "Driver not found";
                log.info("< getByPassport with error = {}",errMess);
                return ResponseEntity.badRequest().body(new RestError(2, errMess));
            }
        } catch (Exception e) {
            log.info("< getByPassport with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
    }

    @GetMapping(value = "/without-cars")
    public ResponseEntity<RestError> withoutCars() {
        log.info("> withoutCars");
        return ResponseEntity.ok(new RestError(driverService.withoutCars()));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<RestError> deleteDriver(@RequestParam Long id) {
        log.info("> deleteDriver with id = {}",id);
        Driver driver = driverRepo.findById(id).orElse(null);
        if (driver != null) {
            List<Car> cars = carRepo.findCarsByDriverId(driver.getId());
            cars.forEach(car -> {
                car.setDriverId(null);
                carRepo.save(car);
            });
            driverRepo.delete(driver);
            log.info("< deleteDriver");
            return ResponseEntity.ok(new RestError("OK"));
        }
        errMess = "Driver doesn't exist";
        log.info("< deleteDriver with error = {}",errMess);
        return ResponseEntity.badRequest().body(new RestError(1, errMess));
    }

    @GetMapping(value = "/driver-cars")
    public ResponseEntity<RestError> driverCars(@RequestParam Long id) {
        log.info("> driverCars with id = {}",id);
        List<Car> carsList = carRepo.findCarsByDriverId(id);
        if (carsList.isEmpty()) {
            errMess = "This driver doesn't have any cars";
            log.info("< driverCars with message = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        } else {
            log.info("< driverCars");
            return ResponseEntity.ok(new RestError(carsList));
        }
    }

    @PutMapping(value = "/pin-driver")
    public ResponseEntity<RestError> pinDriver(@RequestBody String json) {
        log.info("> pinDriver with json = {}",json);
        JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
        String passportId = jo.get("passportId").getAsString();
        String carId = jo.get("carId").getAsString();
        Car car = carRepo.findByCarId(carId);
        Driver driver = driverRepo.findByPassportId(passportId);
        if (driver == null) {
            errMess = "Driver doesn't exist";
            log.info("< pinDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
        if (car == null) {
            errMess = "Car doesn't exist";
            log.info("< pinDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(2, errMess));
        }
        if (!car.getCategory().equals(driver.getCategory())) {
            errMess = "Category doesn't match";
            log.info("< pinDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(3, errMess));
        }
        try {
            driverService.pinDriverToCar(car, driver);
            log.info("< pinDriver");
            return ResponseEntity.ok(new RestError("OK"));
        } catch (Exception e) {
            log.info("< pinDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(4, "Ошибка"));
        }
    }

    @PutMapping(value = "/unpin-driver")
    public ResponseEntity<RestError> unpinDriver(@RequestBody String json) {
        log.info("> unpinDriver with json ={}",json);
        JsonObject jo = Parser.parser.parse(json).getAsJsonObject();
        String passportId = jo.get("passportId").getAsString();
        String carId = jo.get("carId").getAsString();
        Car car;
        Driver driver;
        if(carId != null && passportId != null){
            car = carRepo.findByCarId(carId);
            driver = driverRepo.findByPassportId(passportId);
        }else{
            errMess = "Fields are empty";
            log.info("< unpinDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(1, errMess));
        }
        if(car == null){
            errMess = "Car doesn't exist";
            log.info("< unpinDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(2, errMess));
        } else if(driver == null){
            errMess = "Driver doesn't exist";
            log.info("< unpinDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(3, errMess));
        }
        if(!car.getDriverId().equals(driver.getId())) {
            errMess = "This car doesn't pinned to this driver";
            log.info("< unpinDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(4, errMess));
        }
        try{
            driverService.unpinDriverFromCar(car);
            log.info("< unpinDriver");
            return ResponseEntity.ok(new RestError("OK"));
        } catch (Exception e){
            log.info("< unpinDriver with error = {}",errMess);
            return ResponseEntity.badRequest().body(new RestError(5, errMess));
        }
    }
}

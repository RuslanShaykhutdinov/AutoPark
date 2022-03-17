package com.autoPark.AutoPark.controller;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.service.CarService;
import com.autoPark.AutoPark.utils.RestError;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/Car")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }


}

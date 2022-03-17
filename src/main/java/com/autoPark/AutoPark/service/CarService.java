package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.repos.CarRepo;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    private final CarRepo carRepo;

    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }
}

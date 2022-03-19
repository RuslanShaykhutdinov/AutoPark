package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.repos.CarRepo;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.autoPark.AutoPark.category.Category.*;

@Service
public class CarService {
    private final CarRepo carRepo;

    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }


    public Boolean registration(Car car)   {
        if (carRepo.findByCarId(car.getCarId())!=null)
            return false;
        carRepo.save(car);
        return true;
    }


 public List<Car> getAll() {
        return (List<Car>) carRepo.findAll();
    }

    public Category toCategory(String str) {
        switch (str){
            case "MOTORCYCLE": return MOTORCYCLE;
            case "CAR": return CAR;
            case "TRUCK": return TRUCK;
            case "BUS": return BUS;
            case "TRAILER": return TRAILER;
            default: return null;
        }
    }
}

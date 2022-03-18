package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.repos.CarRepo;
import org.springframework.stereotype.Service;

import static com.autoPark.AutoPark.category.Category.*;

@Service
public class CarService {
    private final CarRepo carRepo;

    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }

    public Car registration(Car car)   {
        if (carRepo.findByCarId(car.getCarId())!=null)
            return null;
        return carRepo.save(car);
    }

    public Iterable<Car> getAll() {
        return carRepo.findAll();
    }

    public Category toCategory(String str) {
        if (str.equals("MOTORCYCLE")) return MOTORCYCLE;
        if (str.equals("CAR")) return CAR;
        if (str.equals("TRUCK")) return TRUCK;
        if (str.equals("BUS")) return BUS;
        if (str.equals("TRAILER")) return TRAILER;
        return null;
    }
}

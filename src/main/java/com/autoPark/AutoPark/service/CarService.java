package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.repos.CarRepo;
import org.springframework.stereotype.Service;


@Service
public class CarService {
    private final CarRepo carRepo;

    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }


    public Car registration(String carId, Category category,Long driverId)   {
        Car car=new Car(carId, category, driverId);
        if (carRepo.findByCarId(carId)!=null)
            return null;
        carRepo.save(car);
        return car;
    }

    public Car editCarById(Car car,String newCarId){
        car.setCarId(newCarId);
        carRepo.save(car);
        return car;
    }


}

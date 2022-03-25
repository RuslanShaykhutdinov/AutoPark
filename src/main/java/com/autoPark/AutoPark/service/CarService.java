package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.dto.Car;
import com.autoPark.AutoPark.repos.CarRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepo carRepo;

    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }


    public Boolean registration(Car car)   { // TODO use primitives
        if (carRepo.findByCarId(car.getCarId())!=null)
            return false;
        carRepo.save(car);
        return true;
    }
    public Boolean delete(Long Id){   // TODO убрать если не ипользуется
        if(carRepo.findById(Id)!=null){
            carRepo.deleteById(Id);
            return true;
        }else{
            return false;
        }
    }

    public Car editCarById(Car car,String newCarId){
        car.setCarId(newCarId);
        carRepo.save(car);
        return car;
    }

 public List<Car> getAll() { // TODO убрать
        return (List<Car>) carRepo.findAll();
    }

}

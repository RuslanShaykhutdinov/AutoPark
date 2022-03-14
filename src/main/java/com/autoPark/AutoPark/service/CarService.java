package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.entity.Car;
import com.autoPark.AutoPark.exception.MessageException;
import com.autoPark.AutoPark.model.CarModel;
import com.autoPark.AutoPark.repo.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class CarService {
    @Autowired
    CarRepo carRepo;


    public Car registration(Car car) throws MessageException {
        if (carRepo.findById(car.getId()) != null) {
            throw new MessageException("Машина уже существует");
        }
        return carRepo.save(car);

    }

    public CarModel getOne(Long id)  throws MessageException  {
        Car car =carRepo.findById(id).get();
        if (car==null) {
            throw new MessageException("Машина не найдена");
        }
        return CarModel.toModel(car);
    }

    public CarModel getOneByCarId(String id)  throws MessageException  {
        Car car =carRepo.findByCarId(id);
        if (car==null) {
            throw new MessageException("Машина не найдена");
        }
        return CarModel.toModel(car);
    }

    public Iterable<CarModel> getMany( boolean driver) {
        Iterable<Car> cars=carRepo.findAll();
        ArrayList<Car> cars1=new ArrayList<>();
        cars.forEach(car -> {
            if (driver == true || (driver == false && car.getDriver() == null))
                cars1.add(car);
        });
        return CarModel.toModels(cars1);
    }

    public Long delete(Long id){
        carRepo.deleteById(id);
        return id;
    }

}

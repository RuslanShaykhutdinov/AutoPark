package com.autoPark.AutoPark.repo;

import com.autoPark.AutoPark.entity.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepo extends CrudRepository<Car,Long > {
    Car findByCarId(String carId);
}


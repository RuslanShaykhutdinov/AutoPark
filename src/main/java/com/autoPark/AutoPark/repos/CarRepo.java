package com.autoPark.AutoPark.repos;

import com.autoPark.AutoPark.dto.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepo extends CrudRepository<Car,Long> {
        
}

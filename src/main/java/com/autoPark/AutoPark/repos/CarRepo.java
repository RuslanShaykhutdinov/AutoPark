package com.autoPark.AutoPark.repos;

import com.autoPark.AutoPark.dto.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepo extends CrudRepository<Car,Long> {
    @Query("Select c FROM Car c WHERE c.carId = ?1")
    Car findByCarId(String carId);
    @Query("Select c FROM Car c WHERE c.driverId =?1")
    List<Car> findCarsByDriverId(Long driverId);
}
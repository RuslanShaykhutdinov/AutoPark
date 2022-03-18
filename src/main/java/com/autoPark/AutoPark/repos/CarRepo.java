package com.autoPark.AutoPark.repos;

import com.autoPark.AutoPark.dto.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepo extends CrudRepository<Car,Long> {
    @Query("select c from car c where c.car_gov_num = ?1")
    Car findByCarId(String carId);
    /*
    @Query("select u from car u where u.car_category = :category")
    ArrayList <Car> findByCategory(Category category);
    @Query("select u from car u where u.driver = null")
    ArrayList <Car> findWithoutDriver();*/
}

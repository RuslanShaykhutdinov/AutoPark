package com.autoPark.AutoPark.repos;

import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CarRepo extends CrudRepository<Car,Long> {
/*    @Query("select u from car u where u.car_gov_num = :carId")
    Car findByCarId(String carId);
    @Query("select u from car u where u.car_category = :category")
    ArrayList <Car> findByCategory(Category category);
    @Query("select u from car u where u.driver = null")
    ArrayList <Car> findWithoutDriver();*/
}

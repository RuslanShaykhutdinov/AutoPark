package com.autoPark.AutoPark.repo;

import com.autoPark.AutoPark.entity.Driver;
import com.autoPark.AutoPark.model.DriverModel;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepo  extends CrudRepository<Driver,Long > {
    Driver findByPassportId(String passportId);
}
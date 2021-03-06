package com.autoPark.AutoPark.repos;

import com.autoPark.AutoPark.dto.Driver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepo extends CrudRepository<Driver,Long> {

    @Query("Select d FROM Driver d WHERE d.passportId = ?1")
    Driver findByPassportId(String passportId);
}

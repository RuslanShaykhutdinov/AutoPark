package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.repos.DriverRepo;
import org.springframework.stereotype.Service;

@Service
public class DriverService {
    private final DriverRepo driverRepo;
    private final CarRepo carRepo;

    public DriverService(DriverRepo driverRepo, CarRepo carRepo) {
        this.driverRepo = driverRepo;
        this.carRepo = carRepo;
    }
    public Boolean registration(Driver driver) {
        if (driverRepo.findByPassportId(driver.getPassportId()) != null) {
            return false;
        }
        driverRepo.save(driver);
        return true;
    }

    public Boolean editDriver(Long id, String passportId , String name) {
        Driver oldDriver = driverRepo.findById(id).orElse(null);
        if (oldDriver != null) {
            oldDriver.setName(name);
            oldDriver.setPassportId(passportId);
            driverRepo.save(oldDriver);
            return true;
        }
        return false;
    }
}

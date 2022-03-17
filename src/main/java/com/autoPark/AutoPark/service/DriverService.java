package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.DriverRepo;
import org.springframework.stereotype.Service;


@Service
public class DriverService {

    private final DriverRepo driverRepo;

    public DriverService(DriverRepo driverRepo) {
        this.driverRepo = driverRepo;
    }

    public Driver registration(Driver driver)   {
        if (driverRepo.findByPassportId(driver.getPassportId())!=null)
            return null;
        return driverRepo.save(driver);
    }

    public Driver getOne(Long id)   {
        return driverRepo.findById(id).get();
    }

    public Driver getOneByPassport(String id)  {
        return driverRepo.findByPassportId(id);
    }

    public Iterable<Driver> getMany( boolean car) {
        return (car) ? driverRepo.findAll() : driverRepo.findAll(); //driverRepo.findWithoutCar() ;
    }


    public Long delete(Long id){
        driverRepo.deleteById(id);
        return id;
    }
}

package com.autoPark.AutoPark.service;

import com.autoPark.AutoPark.entity.Car;
import com.autoPark.AutoPark.entity.Driver;
import com.autoPark.AutoPark.exception.MessageException;
import com.autoPark.AutoPark.model.CarModel;
import com.autoPark.AutoPark.model.DriverModel;
import com.autoPark.AutoPark.repo.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DriverService {
    @Autowired
    DriverRepo driverRepo;

    public Driver registration(Driver driver) throws MessageException {
        if (driverRepo.findById(driver.getId()) != null) {
            throw new MessageException("Клиен уже есть");
        }
        return driverRepo.save(driver);
    }

    public DriverModel getOne(Long id)  throws MessageException  {
        Driver driver =driverRepo.findById(id).get();
        if (driver==null) {
            throw new MessageException("Клиента нет");
        }
        return DriverModel.toModel(driver);
    }

    public DriverModel getOneByPassport(String id)  throws MessageException  {
        Driver driver =driverRepo.findByPassportId(id);
        if (driver==null) {
            throw new MessageException("Клиента нет");
        }
        return DriverModel.toModel(driver);
    }

    public Iterable<DriverModel> getMany( boolean car) {
        Iterable<Driver> drivers=driverRepo.findAll();
        ArrayList<Driver> drivers1=new ArrayList<>();
        drivers.forEach(driver -> {
            if (car == true || (car == false && driver.getCars() == null))
                drivers1.add(driver);
        });
        return DriverModel.toModels(drivers1);
    }

    public Long delete(Long id){
        driverRepo.deleteById(id);
        return id;
    }
}


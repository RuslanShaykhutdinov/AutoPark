package com.autoPark.AutoPark.service;


import com.autoPark.AutoPark.category.Category;
import com.autoPark.AutoPark.dto.Car;

import com.autoPark.AutoPark.dto.Driver;
import com.autoPark.AutoPark.repos.CarRepo;
import com.autoPark.AutoPark.repos.DriverRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DriverService {
    private final DriverRepo driverRepo;
    private final CarRepo carRepo;

    public DriverService(DriverRepo driverRepo, CarRepo carRepo) {
        this.driverRepo = driverRepo;
        this.carRepo = carRepo;
    }

    public void pinDriverToCar(Car car, Driver driver) {
        car.setDriverId(driver.getId());
        carRepo.save(car);
    }

    public void unpinDriverFromCar(Car car) {
        car.setDriverId(null);
        carRepo.save(car);
    }

    public Driver registration(String passportId, String name, Category category) {
        Driver driver = new Driver(passportId, name, category);
        if (driverRepo.findByPassportId(passportId) != null) {
            return null;
        }
        driverRepo.save(driver);
        return driver;
    }

    public List<Driver> withoutCars() {
        Set<Long> driversWithCars = new Set<Long>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Long> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Long aLong) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Long> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
        carRepo.carsWithDriver().forEach(car -> {
            driversWithCars.add(car.getDriverId());
        });
        List<Driver> drivers = new List<>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Driver> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Driver driver) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Driver> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends Driver> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public Driver get(int index) {
                return null;
            }

            @Override
            public Driver set(int index, Driver element) {
                return null;
            }

            @Override
            public void add(int index, Driver element) {

            }

            @Override
            public Driver remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Driver> listIterator() {
                return null;
            }

            @Override
            public ListIterator<Driver> listIterator(int index) {
                return null;
            }

            @Override
            public List<Driver> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        driverRepo.findAll().forEach(driver -> {
            if (!(driversWithCars.contains(driver.getId())))
                drivers.add(driver);
        });
        return drivers;
    }

    public Driver editDriver (Long id, String passportId, String name){ //TODO improve
        Driver oldDriver = driverRepo.findById(id).orElse(null);
        if (oldDriver != null) {
            oldDriver.setName(name);
            oldDriver.setPassportId(passportId);
            driverRepo.save(oldDriver);
            return oldDriver;
        }
        return null;
    }
}

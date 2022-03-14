package com.autoPark.AutoPark.entity;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(
        name="Driver",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"passportId"})
)
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String passportId;
    private String name;
    private Category category;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "Driver")
    private ArrayList<Car> cars;

    public Driver() {
    }

    public Driver(String passportId, String name, Category category) {
        this.passportId = passportId;
        this.name = name;
        this.category = category;
    }

    public Driver(String passportId, String name, Category category, ArrayList<Car> cars) {
        this.passportId = passportId;
        this.name = name;
        this.category = category;
        this.cars = cars;
    }

    public long getId() {
        return Id;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }
}

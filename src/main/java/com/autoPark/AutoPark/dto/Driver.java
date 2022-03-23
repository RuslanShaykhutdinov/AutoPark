package com.autoPark.AutoPark.dto;

import com.autoPark.AutoPark.category.Category;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 *
 * @author HououinKyoma2000
 */
@Entity
@Table(name = "driver",uniqueConstraints = @UniqueConstraint(columnNames={"passport_id"}))
public class Driver {
    @Column(name = "id")
    @Id
    @SequenceGenerator( name = "driverIdGenerator",
            sequenceName = "driver_id_seq",
            schema = "public",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "driverIdGenerator")
    private Long id;

    @Column(name = "passport_id",nullable = false)
    @Pattern(regexp = "[A-Z]{2}[0-9]{7}",
            message = "passportId should be in right form")
    private String passportId;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "driver_category",nullable = false)
    private Category category;


    public Driver() {
    }

    public Driver(String passportId, String name, Category category) {
        this.passportId = passportId;
        this.name = name;
        this.category = category;
    }

    public Long getId() {
        return id;
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
/*

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }
*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(id, driver.id) && Objects.equals(passportId, driver.passportId) && Objects.equals(name, driver.name) && category == driver.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passportId, name, category);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", passportId='" + passportId + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                '}';
    }
}

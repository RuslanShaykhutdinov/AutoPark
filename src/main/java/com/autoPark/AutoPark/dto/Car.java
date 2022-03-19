package com.autoPark.AutoPark.dto;

import com.autoPark.AutoPark.category.Category;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * Класс отвечающий за создание машины
 * @author SanjarU
 */
@Entity
@Table(name = "Car",uniqueConstraints = @UniqueConstraint(columnNames={"car_gov_num"}))
public class Car {
    @Column(name = "id")
    @Id
    @SequenceGenerator( name = "carIdGenerator",
                        sequenceName = "car_id_seq",
                        schema = "public",
                        initialValue = 5,
                        allocationSize = 35
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "carIdGenerator")
    private Long id;

    @Column(name = "car_gov_num")
    @Pattern(regexp = "((01)|(10)|(20)|(30)|(40)|(60)|(80)|(90))(([A-Z]+[0-9]{3})+([A-Z]{2})|([0-9]{3}[A-Z]{3}))",
            message = "Car ID should be in right form")
    private String carId;

    @Column(name = "car_category")
    private Category category;

    @Column(name = "owner_id")
    private Long driverId = null;
    /*@ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "owner_id", referencedColumnName = "id", updatable = false, insertable = false)
     private Driver driver;
 */
    public Car() {
    }

    public Car(String carId, Category category) {
        this.carId = carId;
        this.category=category;
    }

    public Car(String carId, Category category, Long driverId) {
        this.carId = carId;
        this.category = category;
        this.driverId = driverId;
    }


    public Long getId() {
        return id;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id) && Objects.equals(carId, car.carId) && category == car.category && Objects.equals(driverId, car.driverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carId, category, driverId);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", carId='" + carId + '\'' +
                ", category=" + category +
                ", driverId=" + driverId +
                '}';
    }
}
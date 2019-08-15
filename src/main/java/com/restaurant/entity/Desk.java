package com.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Класс и таблица называется Desk, т.к. пересечения с
 * командами в бд и совпадения с аннотациями
 */
@Entity
@Table(name = "desk",uniqueConstraints=@UniqueConstraint(columnNames= {"number","hall"}))
public class Desk implements Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    /**
     * Номер стола в зале
     */
    @Column(nullable = false)
    private int number;

    @Column
    private long hall;

    /**
     * Вместимость гостей
     */
    @Column
    private int capacity;

    @Column(name = "restaurant_id")
    private long restaurantId;

    @Column
    private long lastChange;

    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getHall() {
        return hall;
    }

    public void setHall(long hall) {
        this.hall = hall;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String toString() {
        return "Desk{" +
                "id=" + id +
                ", number=" + number +
                ", hall=" + hall +
                ", capacity=" + capacity +
                ", restaurantId=" + restaurantId +
                ", lastChange=" + lastChange +
                '}';
    }
}
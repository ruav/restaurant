package com.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="hostess")
public class Hostess implements Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToOne()
    @JoinTable(
            name="hostess_photo",
            joinColumns = @JoinColumn(name = "hostess_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")

    )
    private Photo photo;

    @Column(name="restaurant_Id")
    private long restaurantId;

    @Column
    private boolean work;

    @Column
    private long lastChange;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Hostess{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", photo=" + photo +
                ", restaurantId=" + restaurantId +
                ", work=" + work +
                ", lastChange=" + lastChange +
                '}';
    }
}
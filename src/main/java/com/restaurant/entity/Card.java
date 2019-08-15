package com.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="card")
public class Card implements Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private long hall;

    @Column
    private String map;

    @Column
    private Date relevantFrom;

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

    public long getHall() {
        return hall;
    }

    public void setHall(long hall) {
        this.hall = hall;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Date getRelevantFrom() {
        return relevantFrom;
    }

    public void setRelevantFrom(Date relevantFrom) {
        this.relevantFrom = relevantFrom;
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
        return "Card{" +
                "id=" + id +
                ", hall=" + hall +
                ", map='" + map + '\'' +
                ", relevantFrom=" + relevantFrom +
                ", restaurantId=" + restaurantId +
                ", lastChange=" + lastChange +
                '}';
    }
}

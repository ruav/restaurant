package com.restaurant.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "category", uniqueConstraints=@UniqueConstraint(columnNames= {"restaurant_Id","name"}))
public class Category implements DataWithLogo<Photo>, Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name="restaurant_Id")
    private long restaurantId;

    @Column(name = "name", nullable = false)
    private String name;

    @ColumnDefault("true")
    @Column(name = "active")
    private boolean active;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name="category_photo",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")

    )
    private Photo logo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Photo getLogo() {
        return logo;
    }

    @Override
    public void setLogo(Photo logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Category{" +
                "restaurantId=" + restaurantId +
                ", name='" + name + '\'' +
                '}';
    }
}

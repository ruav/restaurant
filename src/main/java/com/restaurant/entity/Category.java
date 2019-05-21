package com.restaurant.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "category",uniqueConstraints=@UniqueConstraint(columnNames= {"restaurant_Id","name"}))
public class Category implements Data, Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name="restaurant_Id")
    private long restaurantId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name="category_photo",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")

    )
    private Set<Photo> photos;

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

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "Category{" +
                "restaurantId=" + restaurantId +
                ", name='" + name + '\'' +
                '}';
    }
}

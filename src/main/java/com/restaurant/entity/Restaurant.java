package com.restaurant.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="restaurant",uniqueConstraints=@UniqueConstraint(columnNames= {"address","city","name"}))
public class Restaurant implements Data{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private long city;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="restaurantphoto",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")

    )
    private Set<Photo> photos;

    @Column(name = "avgprice", nullable = false)
    private int avgPrice;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longtitude", nullable = false)
    private Float longtitude;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCity() {
        return city;
    }

    public void setCity(long city) {
        this.city = city;
    }

    public int getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(int avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Float longtitude) {
        this.longtitude = longtitude;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city=" + city +
                ", avgPrice=" + avgPrice +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                '}';
    }
}

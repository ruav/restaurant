package com.restaurant.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="event")
public class Event implements Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;

    @Column(name = "start_hour")
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm")
    private Date startHour;

    @Column()
    private long type;

    @Column
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="event_photo",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private Set<Photo> photos;

    @Column
    private long restaurantId;

    private String link;

    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartHour() {
        return startHour;
    }

    public void setStartHour(Date startHour) {
        this.startHour = startHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", photos=" + photos +
                ", restaurantId=" + restaurantId +
                ", link='" + link + '\'' +
                '}';
    }
}

package com.restaurant.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="client", uniqueConstraints=@UniqueConstraint(columnNames= {"restaurant_Id","phone"}))
public class Client implements Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String phone;

    @OneToMany()
    @JoinTable(
            name="client_tag",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @Column
    private boolean vip;

    @Column
    private long lastChange;

    @Column(name = "restaurant_id")
    private long restaurantId;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
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
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", tags=" + tags +
                ", vip=" + vip +
                ", lastChange=" + lastChange +
                ", restaurantId=" + restaurantId +
                '}';
    }
}

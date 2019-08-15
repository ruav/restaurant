package com.restaurant.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation implements Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @Column
    private Date date;

    @Column
    private Date timeFrom;

    @Column
    private Date timeTo;

    @Column(name = "restaurant_id")
    private long restaurantId;

    @ColumnDefault("0")
    @Column
    private int guests;

    @OneToMany()
    @JoinTable(
            name="reservation_desk",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "desk_id")
    )
    private Set<Desk> desks;

    @OneToMany()
    @JoinTable(
            name="reservation_replacement",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "replacement_id")
    )
    private Set<Replacement> replacements;

    @OneToMany()
    @JoinTable(
            name="reservation_tags",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany()
    @JoinTable(
            name="reservation_status",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id")
    )
    private Set<Status> statuses;
    @Column
    private long lastChange;

    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Date getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Date timeTo) {
        this.timeTo = timeTo;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public Set<Desk> getDesks() {
        return desks;
    }

    public void setDesks(Set<Desk> desks) {
        this.desks = desks;
    }

    public Set<Replacement> getReplacements() {
        return replacements;
    }

    public void setReplacements(Set<Replacement> replacements) {
        this.replacements = replacements;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", date=" + date +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", restaurantId=" + restaurantId +
                ", guests=" + guests +
                ", desks=" + desks +
                ", replacements=" + replacements +
                ", tags=" + tags +
                ", statuses=" + statuses +
                ", lastChange=" + lastChange +
                '}';
    }
}
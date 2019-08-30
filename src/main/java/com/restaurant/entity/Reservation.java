package com.restaurant.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation implements Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @Column
    private Date timeFrom;

    @Column
    private Date timeTo;

    @Column(name = "restaurant_id")
    private long restaurantId;

    @ColumnDefault("0")
    @Column
    private int guests;

    @ManyToMany()
    @JoinTable(
            name="reservation_tables",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "desk_id")
    )
    private Set<Desk> tables;

    @OneToMany()
    @JoinTable(
            name="reservation_replacement",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "replacement_id")
    )
    private Set<Replacement> replacements;

    @ManyToMany()
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
    private List<Status> statuses;

    @Column
    private long lastChange;

    @Column(name = "client_id")
    private long clientId;

    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
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

    public Set<Desk> getTables() {
        return tables;
    }

    public void setTables(Set<Desk> tables) {
        this.tables = tables;
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

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", restaurantId=" + restaurantId +
                ", guests=" + guests +
                ", tables=" + tables +
                ", replacements=" + replacements +
                ", tags=" + tags +
                ", statuses=" + statuses +
                ", lastChange=" + lastChange +
                ", clientId=" + clientId +
                '}';
    }
}
package com.restaurant.entity;

import com.restaurant.vo.StatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="status")
public class Status implements Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private StatusEnum status;

    @Column
    private long hostess;

    @Column
    private long reservation;

    @Column
    private long lastChange;

    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public long getHostess() {
        return hostess;
    }

    public void setHostess(long hostess) {
        this.hostess = hostess;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    public long getReservation() {
        return reservation;
    }

    public void setReservation(long reservation) {
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", status=" + status +
                ", hostess=" + hostess +
                ", reservation=" + reservation +
                ", lastChange=" + lastChange +
                '}';
    }
}

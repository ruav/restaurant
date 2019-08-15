package com.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    @Column(unique = true, nullable = false)
    private int status;

    @Column
    private long hostess;

    @Column(name = "date_time")
    private Date dateTime;

    @Column
    private long lastChange;

    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getHostess() {
        return hostess;
    }

    public void setHostess(long hostess) {
        this.hostess = hostess;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", status=" + status +
                ", hostess=" + hostess +
                ", dateTime=" + dateTime +
                ", lastChange=" + lastChange +
                '}';
    }
}

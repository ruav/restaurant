package com.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="replacement")
public class Replacement implements Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private Date time;

    @Column
    private int deskFrom;

    @Column
    private int deskTo;

    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getDeskFrom() {
        return deskFrom;
    }

    public void setDeskFrom(int deskFrom) {
        this.deskFrom = deskFrom;
    }

    public int getDeskTo() {
        return deskTo;
    }

    public void setDeskTo(int deskTo) {
        this.deskTo = deskTo;
    }

    @Override
    public String toString() {
        return "Replacement{" +
                "id=" + id +
                ", when=" + time +
                ", deskFrom=" + deskFrom +
                ", deskTo=" + deskTo +
                '}';
    }
}
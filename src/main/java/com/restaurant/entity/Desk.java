package com.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Класс и таблица называется Desk, т.к. пересечения с
 * командами в бд и совпадения с аннотациями
 */
@Entity
@Table(name = "desk",uniqueConstraints=@UniqueConstraint(columnNames= {"number","hall"}))
public class Desk implements Data{

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="desk_id_seq", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="pk_sequence")
    @Column(unique = true)
    private long id;

    @Column(nullable = false)
    private int number;

    @Column
    private long hall;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getHall() {
        return hall;
    }

    public void setHall(long hall) {
        this.hall = hall;
    }

    @Override
    public String toString() {
        return "Table{" +
                "number=" + number +
                ", hall=" + hall +
                '}';
    }
}

package com.restaurant.dto.editable;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class ReservationModel {

    private long id;
    private Long timeFrom;
    private Long timeTo;
    private int guests;
    private List<Long> tables;
    private List<Long> tags;
    @JsonAlias("newtags")
    private List<String> newTags;
    private long hostess;
    private long client;
    private long lastchange;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Long getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Long timeTo) {
        this.timeTo = timeTo;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public List<Long> getTables() {
        return tables;
    }

    public void setTables(List<Long> tables) {
        this.tables = tables;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }

    public long getClient() {
        return client;
    }

    public void setClient(long client) {
        this.client = client;
    }

    public long getLastchange() {
        return lastchange;
    }

    public void setLastchange(long lastchange) {
        this.lastchange = lastchange;
    }

    public List<String> getNewTags() {
        return newTags;
    }

    public void setNewTags(List<String> newTags) {
        this.newTags = newTags;
    }

    public long getHostess() {
        return hostess;
    }

    public void setHostess(long hostess) {
        this.hostess = hostess;
    }

    @Override
    public String toString() {
        return "ReservationDto{" +
                "id=" + id +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", guests=" + guests +
                ", tables=" + tables +
                ", tags=" + tags +
                ", newTags=" + newTags +
                ", hostess=" + hostess +
                ", client=" + client +
                ", lastchange=" + lastchange +
                '}';
    }
}
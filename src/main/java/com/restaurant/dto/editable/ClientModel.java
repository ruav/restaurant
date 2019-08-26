package com.restaurant.dto.editable;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class ClientModel {

    private long id;
    private String name;
    private String phone;
    private List<Long> tags;
    @JsonAlias("newtags")
    private List<String> newTags;
    private boolean vip;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public List<String> getNewTags() {
        return newTags;
    }

    public void setNewTags(List<String> newTags) {
        this.newTags = newTags;
    }

    @Override
    public String toString() {
        return "ClientModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", tags=" + tags +
                ", newTags=" + newTags +
                ", vip=" + vip +
                '}';
    }
}

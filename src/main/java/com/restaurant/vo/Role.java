package com.restaurant.vo;

public enum Role {

    ROOT(0, "Суперпользователь"),
    SYSTEM(1, "Представитель системы"),
    RESTAURANT(2, "Учетная запись ресторана");

    private int num;
    private String name;

    Role(int num, String name) {
        this.num = num;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }
}

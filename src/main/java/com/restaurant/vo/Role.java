package com.restaurant.vo;

public enum Role {

    Root(0, "Суперпользователь"),
    system(1, "Представитель системы");

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

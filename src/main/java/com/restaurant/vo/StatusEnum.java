package com.restaurant.vo;

public enum StatusEnum {

    ORDER(0, "Заявка на сайте"),
    DRAFT(2, "Черновик"),
    WAITING(3, "В листе ожидания"),
    BOOKED(4, "Забронировано"),
    COME(5, "Пришли"),
    GET_AWAY(6, "Ушли"),
    NOT_COME(7, "Не пришли"),
    REFUSAL(8, "Отказ");



    private int num;
    private String name;

    StatusEnum(int num, String name) {
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

package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ReservationDto {

    @ApiModelProperty
    private long id;
    @ApiModelProperty
    private String date;
    @ApiModelProperty
    private String timeFrom;
    @ApiModelProperty
    private String timeTo;
    @ApiModelProperty
    private int guests;
    @ApiModelProperty
    private List<DeskDto> tables;
    @ApiModelProperty
    private List<ReplacementDto> replacements;
    @ApiModelProperty
    private List<TagDto> tags;
    @ApiModelProperty
    private long client;
    @ApiModelProperty
    private List<StatusDto> status;
    @ApiModelProperty
    private long lastchange;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public List<DeskDto> getTables() {
        return tables;
    }

    public void setTables(List<DeskDto> tables) {
        this.tables = tables;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
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

    @Override
    public String toString() {
        return "ReservationDto{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", timeFrom='" + timeFrom + '\'' +
                ", timeTo='" + timeTo + '\'' +
                ", guests=" + guests +
                ", tables=" + tables +
                ", tags=" + tags +
                ", client=" + client +
                ", lastchange=" + lastchange +
                '}';
    }

}

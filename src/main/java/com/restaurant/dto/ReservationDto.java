package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ReservationDto {

    @ApiModelProperty
    private long id;
    @ApiModelProperty
    private Long timeFrom;
    @ApiModelProperty
    private Long timeTo;
    @ApiModelProperty
    private int guests;
    @ApiModelProperty
    private List<Long> tables;
    @ApiModelProperty
    private List<ReplacementDto> replacements;
    @ApiModelProperty
    private List<Long> tags;
    @ApiModelProperty
    private long client;
    @ApiModelProperty
    private List<StatusDto> statuses;
    @ApiModelProperty
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


    public List<ReplacementDto> getReplacements() {
        return replacements;
    }

    public void setReplacements(List<ReplacementDto> replacements) {
        this.replacements = replacements;
    }

    public List<StatusDto> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusDto> statuses) {
        this.statuses = statuses;
    }


    @Override
    public String toString() {
        return "ReservationDto{" +
                "id=" + id +
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

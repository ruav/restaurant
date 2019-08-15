package com.restaurant.dto.Event;

import com.restaurant.dto.ClientDto;
import com.restaurant.dto.DeskDto;
import com.restaurant.dto.TagDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ReservationEventDto {


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
    private List<TagDto> tags;
    @ApiModelProperty
    private ClientDto client;
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

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
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

package com.example.concertsystem.dto;



import com.example.concertsystem.entity.Event;

import java.util.List;

public class EventImageResponse {
    Event event;
    List<String> imgUrls;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }


}

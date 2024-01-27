package com.example.concertsystem.dto;



import com.example.concertsystem.entity.Event;

import java.util.List;

public class EventImageResponse {
    EventResponse eventResponse;
    List<String> imgUrls;

    public EventResponse getEventResponse() {
        return eventResponse;
    }

    public void setEventResponse(EventResponse eventResponse) {
        this.eventResponse = eventResponse;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }


}

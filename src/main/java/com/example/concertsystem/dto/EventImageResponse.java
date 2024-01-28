package com.example.concertsystem.dto;



import com.example.concertsystem.entity.Event;

import java.util.List;

public class EventImageResponse {
    Event event;
    List<String> imgUrls;
    List<String> profileImgUrls;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public List<String> getProfileImgUrls() {
        return profileImgUrls;
    }

    public void setProfileImgUrls(List<String> profileImgUrls) {
        this.profileImgUrls = profileImgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }


}

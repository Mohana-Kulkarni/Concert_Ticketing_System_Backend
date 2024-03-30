package com.example.concertsystem.dto;



import com.example.concertsystem.entity.Event;

import java.util.List;

public class EventImageResponse {
    Event event;
    String verificationMode;
    List<String> trustedIssuers;
    List<String> imgUrls;

    public String getVerificationMode() {
        return verificationMode;
    }
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

    public void setVerificationMode(String verificationMode) {
        this.verificationMode = verificationMode;
    }

    public List<String> getTrustedIssuers() {
        return trustedIssuers;
    }

    public void setTrustedIssuers(List<String> trustedIssuers) {
        this.trustedIssuers = trustedIssuers;
    }



}

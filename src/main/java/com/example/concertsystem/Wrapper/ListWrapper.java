package com.example.concertsystem.Wrapper;

import com.example.concertsystem.dto.EventResponse;

import java.io.Serializable;
import java.util.List;

public class ListWrapper implements Serializable {
    private final List<EventResponse> list;

    public ListWrapper(List<EventResponse> list) {
        this.list = list;
    }

    public List<EventResponse> getList() {
        return list;
    }
}

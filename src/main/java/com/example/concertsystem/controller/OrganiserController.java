package com.example.concertsystem.controller;

import com.example.concertsystem.entity.Organiser;
import com.example.concertsystem.service.organiser.OrganiserService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/organisers")
public class OrganiserController {

    private OrganiserService organiserService;
    public OrganiserController(OrganiserService organiserService) {
        this.organiserService = organiserService;
    }

    @GetMapping("/id")
    public Organiser getOrganiser(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        return organiserService.getOrganiserById(id);
    }

    @PostMapping("/")
    public void addNewOrganiser(@RequestBody Organiser organiser) {
        organiserService.addOrganiser(organiser.name(), organiser.userName(), organiser.email(), organiser.govId(), organiser.walletId());
    }

    @PutMapping("/update/id")
    public void updateOrganiser(@RequestParam("id") String id, @RequestBody Organiser organiser) throws ExecutionException, InterruptedException {
        organiserService.updateOrganiser(id, organiser.name(), organiser.userName(), organiser.email(), organiser.govId(), organiser.walletId());
    }

    @DeleteMapping("/delete/id")
    public void deleteOrganiser(@RequestParam("id") String id) {
        organiserService.deleteOrganiser(id);
    }
}

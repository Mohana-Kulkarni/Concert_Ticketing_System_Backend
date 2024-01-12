package com.example.concertsystem.controller;

import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.service.tier.TierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/tiers")
public class TierController {
    @Autowired
    private TierService tierService;

    @GetMapping("/id={id}")
    public Tier getTierById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return tierService.getTierById(id);
    }

    @GetMapping("/name={name}")
    public Tier getTierByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        return tierService.getTierByName(name);
    }

    @PostMapping("/")
    public void addTier(@RequestBody Tier tier) {
        System.out.println(tier.name());
        System.out.println(tier.capacity());
        System.out.println(tier.price());
        tierService.addTier(tier.name(), tier.capacity(), tier.price());
    }

    @PutMapping("/{id}")
    public void updateTierById(@PathVariable String id, @RequestBody Tier tier) throws ExecutionException, InterruptedException {
        tierService.updateTier(id, tier.name(),tier.capacity(), tier.price());
    }

    @DeleteMapping("/{id}")
    public void deleteTierById(@PathVariable String id) {
        tierService.deleteTierById(id);
    }


}

package com.example.concertsystem.controller;

import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.service.artist.ArtistService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private ArtistService artistService;
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/id")
    public Artist getArtist(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        return artistService.getArtistById(id);
    }

    @PostMapping("/")
    public void addNewArtist(@RequestBody Artist artist) {
        artistService.addArtist(artist.name(), artist.userName(), artist.email(), artist.govId(), artist.profileImg());
    }

    @PutMapping("/update/id")
    public void updateArtist(@RequestParam("id") String id, @RequestBody Artist artist) throws ExecutionException, InterruptedException {
        artistService.updateArtist(id, artist.name(), artist.userName(), artist.email(), artist.govId(), artist.profileImg());
    }

    @DeleteMapping("/delete/id")
    public void deleteArtist(@RequestParam("id") String id) {
        artistService.deleteArtist(id);
    }
}

package com.example.concertsystem.controller;

import com.example.concertsystem.dto.ArtistResponse;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.service.artist.ArtistService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private ArtistService artistService;
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/id")
    public ArtistResponse getArtist(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        return artistService.getArtistById(id);
    }

    @GetMapping("/")
    public List<ArtistResponse> getArtist() throws ExecutionException, InterruptedException {
        return artistService.getAllArtist();
    }
    @PostMapping("/")
    public void addNewArtist(@RequestBody Artist artist, @RequestParam("profileImg")String profileImg) throws ExecutionException, InterruptedException, IOException {
        artistService.addArtist(artist.name(), artist.userName(), artist.email(), artist.govId(), profileImg);
    }

    @PutMapping("/update/id")
    public void updateArtist(@RequestParam("id") String id, @RequestBody Artist artist, @RequestParam("profileImg") MultipartFile profileImg) throws ExecutionException, InterruptedException {
        artistService.updateArtist(id, artist.name(), artist.userName(), artist.email(), artist.govId(), profileImg);
    }

    @DeleteMapping("/delete/id")
    public void deleteArtist(@RequestParam("id") String id) {
        artistService.deleteArtist(id);
    }
}

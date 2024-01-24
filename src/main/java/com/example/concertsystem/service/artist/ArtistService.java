package com.example.concertsystem.service.artist;

import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Organiser;

import java.util.concurrent.ExecutionException;

public interface ArtistService {
    void addArtist(String name, String userName, String email, String govId, String profileImg);

    Artist getArtistById(String id) throws ExecutionException, InterruptedException;
    void updateArtist(String id, String name, String userName, String email, String govId, String profileImg) throws ExecutionException, InterruptedException;
    void deleteArtist(String id);
}

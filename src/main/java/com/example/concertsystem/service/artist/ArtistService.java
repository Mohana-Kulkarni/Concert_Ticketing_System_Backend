package com.example.concertsystem.service.artist;

import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Organiser;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ArtistService {
    String addArtist(String name, String userName, String email, String govId, String profileImg) throws ExecutionException, InterruptedException;
    List<String> addArtistList(List<Artist> artistList) throws ExecutionException, InterruptedException;
    Artist getArtistById(String id) throws ExecutionException, InterruptedException;

    String getArtistIdByName(String artist) throws ExecutionException, InterruptedException;
    List<Artist> getAllArtist() throws ExecutionException, InterruptedException;
    List<Artist> getArtistsByIds(List<String> artistIds) throws ExecutionException, InterruptedException;
    void updateArtist(String id, String name, String userName, String email, String govId, String profileImg) throws ExecutionException, InterruptedException;
    void deleteArtist(String id);

}

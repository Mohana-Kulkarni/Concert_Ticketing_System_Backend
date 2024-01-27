package com.example.concertsystem.service.artist;

import com.example.concertsystem.dto.ArtistResponse;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Organiser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ArtistService {
    String addArtist(String name, String userName, String email, String govId, MultipartFile profileImg) throws ExecutionException, InterruptedException, IOException;
    List<String> addArtistList(List<Artist> artistList, List<MultipartFile> profileImages) throws ExecutionException, InterruptedException, IOException;
    ArtistResponse getArtistById(String id) throws ExecutionException, InterruptedException;

    String getArtistIdByName(String artist) throws ExecutionException, InterruptedException;
    List<ArtistResponse> getAllArtist() throws ExecutionException, InterruptedException;
    List<ArtistResponse> getArtistsByIds(List<String> artistIds) throws ExecutionException, InterruptedException;
    void updateArtist(String id, String name, String userName, String email, String govId, MultipartFile profileImg) throws ExecutionException, InterruptedException;
    void deleteArtist(String id);

}

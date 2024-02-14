package com.example.concertsystem.service.artist;

import com.example.concertsystem.dto.ArtistResponse;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Organiser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface ArtistService {
    Map<String, String> addArtist(String name, String userName, String email, String govId, String profileImg);
    List<String> addArtistList(List<Artist> artistList, List<String> profileImages);
    ArtistResponse getArtistById(String id);
    String getArtistIdByName(String artist);
    List<ArtistResponse> getAllArtist();
    List<ArtistResponse> getArtistsByIds(List<String> artistIds);
    boolean updateArtist(String id, String name, String userName, String email, String govId, String profileImg);
    boolean deleteArtist(String id);

}

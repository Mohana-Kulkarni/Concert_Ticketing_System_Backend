package com.example.concertsystem.controller;

import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.ArtistResponse;
import com.example.concertsystem.dto.SuccessResponse;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.service.artist.ArtistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/artists")
@Validated
public class ArtistController {

    private ArtistService artistService;
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/id")
    public ResponseEntity<ArtistResponse> getArtist(@RequestParam("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(artistService.getArtistById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<ArtistResponse>> getArtist(){
        return ResponseEntity.status(HttpStatus.OK).body(artistService.getAllArtist());

    }
    @PostMapping("/")
    public ResponseEntity<SuccessResponse> addNewArtist(@Valid @RequestBody Artist artist,@RequestParam("profileImg")String profileImg){
        Map<String, String> result = artistService.addArtist(artist.name(), artist.userName(), artist.email(), artist.govId(), profileImg);
        if(result.get("val").equals("true")) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_201, result.get("id")));
        } else if (result.get("val").equals("exists")) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_409, result.get("id")));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_UPDATE));
        }
    }

    @PutMapping("/update/id")
    public ResponseEntity<SuccessResponse> updateArtist(@RequestParam("id") String id,@Valid @RequestBody Artist artist, @RequestParam("profileImg") String profileImg) throws ExecutionException, InterruptedException {
        boolean result = artistService.updateArtist(id, artist.name(), artist.userName(), artist.email(), artist.govId(), profileImg);
        if(result) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_200, GlobalConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete/id")
    public ResponseEntity<SuccessResponse> deleteArtist(@RequestParam("id") String id) {
        boolean result = artistService.deleteArtist(id);
        if(result) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_200, GlobalConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_DELETE));
        }
    }
}

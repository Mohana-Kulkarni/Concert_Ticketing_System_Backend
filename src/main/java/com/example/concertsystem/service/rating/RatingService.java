package com.example.concertsystem.service.rating;

import com.example.concertsystem.dto.RatingResponse;
import com.example.concertsystem.entity.Comment;

import java.util.List;

public interface RatingService {
    String addRating(String content, String userId, String artistId);
    RatingResponse getRating(String artistId);

}

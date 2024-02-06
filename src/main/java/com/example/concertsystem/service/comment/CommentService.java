package com.example.concertsystem.service.comment;

import com.example.concertsystem.entity.Comment;

import java.util.List;

public interface CommentService {
    String addComment(String content, String userId, String eventId);
    List<Comment> getAllComments(String eventId);
    boolean updateComment(String id, String content, String userId, String eventId);
    boolean deleteComment(String id);

}

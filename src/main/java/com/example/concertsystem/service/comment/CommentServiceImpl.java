package com.example.concertsystem.service.comment;

import com.example.concertsystem.entity.Comment;

import java.util.List;

public class CommentServiceImpl implements CommentService{

    @Override
    public String addComment(String content, String userId, String eventId) {
        return null;
    }

    @Override
    public List<Comment> getAllComments(String eventId) {
        return null;
    }

    @Override
    public boolean updateComment(String id, String content, String userId, String eventId) {
        return false;
    }

    @Override
    public boolean deleteComment(String id) {
        return false;
    }
}

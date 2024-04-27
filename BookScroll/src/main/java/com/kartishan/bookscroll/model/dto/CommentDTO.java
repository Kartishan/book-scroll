package com.kartishan.bookscroll.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CommentDTO {
    private UUID id;
    private String title;
    private UUID userId;
    private String username;
    private UUID bookId;
    private UUID parentCommentId;
}
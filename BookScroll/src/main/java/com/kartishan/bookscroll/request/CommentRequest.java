package com.kartishan.bookscroll.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CommentRequest {
    UUID bookId;
    String tittle;
    UUID parentCommentId;
}

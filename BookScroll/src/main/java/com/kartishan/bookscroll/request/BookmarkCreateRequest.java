package com.kartishan.bookscroll.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BookmarkCreateRequest {
    private UUID bookId;
    private String cfiRange;
    private String text;
    private String comment;
}

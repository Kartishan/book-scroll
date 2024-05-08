package com.kartishan.bookscroll.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BookMarkDTO {
    private UUID id;
    private UUID bookId;
    private String cfiRange;
    private String text;
    private String comment;
}

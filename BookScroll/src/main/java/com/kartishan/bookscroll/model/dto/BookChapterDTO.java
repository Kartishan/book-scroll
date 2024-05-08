package com.kartishan.bookscroll.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookChapterDTO {
    private Long number;
    private String audioFileId;
    private String summary;
    private int duration;
}
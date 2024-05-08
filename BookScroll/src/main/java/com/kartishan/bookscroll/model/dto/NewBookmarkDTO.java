package com.kartishan.bookscroll.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewBookmarkDTO {
    private String cfiRange;
    private String text;
    private String comment;
}

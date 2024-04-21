package com.kartishan.bookscroll.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ScrollDTO {
    private UUID id;
    private String name;
    private UUID bookId;
    private String bookName;
    private UUID userId;
    private String username;
}

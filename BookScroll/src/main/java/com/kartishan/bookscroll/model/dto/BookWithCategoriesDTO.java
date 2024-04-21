package com.kartishan.bookscroll.model.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class BookWithCategoriesDTO {
    private UUID id;
    private String name;
    private String author;
    private String authorFull;
    private String description;
    private double rating;
    private Integer pageCount;
    private Set<String> categories;
}
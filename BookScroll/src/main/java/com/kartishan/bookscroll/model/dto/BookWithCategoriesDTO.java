package com.kartishan.bookscroll.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
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
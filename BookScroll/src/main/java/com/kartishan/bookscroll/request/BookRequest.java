package com.kartishan.bookscroll.request;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class BookRequest {
    private String name;
    private String author;
    private String authorFullName;
    private String description;
    private Integer pageCount;
    private Set<String> categories;
}

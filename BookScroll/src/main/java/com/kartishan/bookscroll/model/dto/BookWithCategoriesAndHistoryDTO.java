package com.kartishan.bookscroll.model.dto;

import lombok.experimental.SuperBuilder;
import lombok.Data;

import java.util.Date;

@Data
@SuperBuilder
public class BookWithCategoriesAndHistoryDTO extends BookWithCategoriesDTO {
    private Date viewTime;
}
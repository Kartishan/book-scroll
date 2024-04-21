package com.kartishan.bookscroll.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateUserBookPreferenceRequest {
    private UUID userId;
    private UUID bookId;
    private int rating;
}

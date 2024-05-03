package com.kartishan.bookscroll.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PlaybackPositionUpdateRequest {
    private UUID bookId;
    private Long chapterNumber;
    private double lastPlaybackPosition;
}

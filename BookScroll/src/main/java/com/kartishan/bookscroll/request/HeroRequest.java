package com.kartishan.bookscroll.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class HeroRequest {
    private String name;
    private String gender;
    private String shortDescription;
    private String physicalDescription;
    private String education;
    private String occupation;
    private String character;
    private boolean isMainCharacter;
    private UUID bookId;
    private Set<UUID> parentIds;
}

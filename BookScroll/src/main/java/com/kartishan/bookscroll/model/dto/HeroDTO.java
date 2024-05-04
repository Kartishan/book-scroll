package com.kartishan.bookscroll.model.dto;

import com.kartishan.bookscroll.model.Hero;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class HeroDTO {
    private String name;
    private String gender;
    private String shortDescription;
    private String physicalDescription;
    private String education;
    private String occupation;
    private String character;
    private boolean isMainCharacter;
    private Set<HeroSimpleDTO> parents;
    private Set<HeroSimpleDTO> children;
}

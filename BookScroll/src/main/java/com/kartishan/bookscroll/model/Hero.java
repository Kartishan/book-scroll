package com.kartishan.bookscroll.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hero {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String gender;
    private String shortDescription;
    private String physicalDescription;
    private String education;
    private String occupation;
    private String character;
    private boolean isMainCharacter;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToMany
    @JoinTable(
            name = "hero_parents",
            joinColumns = @JoinColumn(name = "hero_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    private Set<Hero> parents = new HashSet<>();

}
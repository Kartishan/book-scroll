package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.exceptions.BookNotFoundException;
import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.Hero;
import com.kartishan.bookscroll.model.dto.HeroDTO;
import com.kartishan.bookscroll.model.dto.HeroSimpleDTO;
import com.kartishan.bookscroll.repository.BookRepository;
import com.kartishan.bookscroll.repository.HeroRepository;
import com.kartishan.bookscroll.request.HeroRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HeroService {
    private final HeroRepository heroRepository;
    private final BookRepository bookRepository;

    public Hero addHero(HeroRequest heroRequest) {
        System.out.println("allo why");
        Book book = bookRepository.findById(heroRequest.getBookId()).orElseThrow(() -> new BookNotFoundException("Книга с идентификатором " + heroRequest.getBookId() + " не найдена."));

        Hero hero = new Hero();
        hero.setName(heroRequest.getName());
        hero.setGender(heroRequest.getGender());
        hero.setShortDescription(heroRequest.getShortDescription());
        hero.setPhysicalDescription(heroRequest.getPhysicalDescription());
        hero.setEducation(heroRequest.getEducation());
        hero.setOccupation(heroRequest.getOccupation());
        hero.setCharacter(heroRequest.getCharacter());
        hero.setMainCharacter(heroRequest.isMainCharacter());
        hero.setBook(book);
        if (heroRequest.getParentIds() != null && !heroRequest.getParentIds().isEmpty()) {
            Set<Hero> parents = heroRequest.getParentIds().stream()
                    .map(parentId -> heroRepository.findById(parentId)
                            .orElseThrow(() -> new RuntimeException("Родитель с id " + parentId + " не найден.")))
                    .collect(Collectors.toSet());

            hero.setParents(parents);
        }
        return heroRepository.save(hero);
    }

    public List<HeroDTO> findAllHeroesByBookId(UUID bookId) {
        List<Hero> heroes = heroRepository.findByBookId(bookId);
        return heroes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public HeroDTO convertToDto(Hero hero) {
        Set<HeroSimpleDTO> parentDtos = hero.getParents().stream()
                .map(this::convertToSimpleDto)
                .collect(Collectors.toSet());

        Set<HeroSimpleDTO> childrenDtos = heroRepository.findChildrenByHeroId(hero).stream()
                .map(this::convertToSimpleDto)
                .collect(Collectors.toSet());

        return HeroDTO.builder()
                .name(hero.getName())
                .gender(hero.getGender())
                .shortDescription(hero.getShortDescription())
                .physicalDescription(hero.getPhysicalDescription())
                .education(hero.getEducation())
                .occupation(hero.getOccupation())
                .character(hero.getCharacter())
                .isMainCharacter(hero.isMainCharacter())
                .parents(parentDtos)
                .children(childrenDtos)
                .build();
    }

    private HeroSimpleDTO convertToSimpleDto(Hero hero) {
        return HeroSimpleDTO.builder()
                .id(hero.getId())
                .name(hero.getName())
                .build();
    }

}

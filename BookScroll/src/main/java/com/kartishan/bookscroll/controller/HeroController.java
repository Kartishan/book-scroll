package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.model.Hero;
import com.kartishan.bookscroll.model.dto.HeroDTO;
import com.kartishan.bookscroll.request.HeroRequest;
import com.kartishan.bookscroll.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/heroes")
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @PostMapping("/add")
    public ResponseEntity<String> addHero(@RequestBody HeroRequest heroRequest) {
        try {
            heroService.addHero(heroRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Hero was successfully added.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }
    }
    @GetMapping("/all/{bookId}")
    public ResponseEntity<List<HeroDTO>> getHeroesByBookId(@PathVariable UUID bookId) {
        List<HeroDTO> heroDTOList = heroService.findAllHeroesByBookId(bookId);
        return ResponseEntity.ok(heroDTOList);
    }

}

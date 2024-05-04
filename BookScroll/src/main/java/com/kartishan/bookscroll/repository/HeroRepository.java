package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface HeroRepository extends JpaRepository<Hero, UUID>{
    @Query("SELECT h FROM Hero h WHERE :parent MEMBER OF h.parents")
    Set<Hero> findChildrenByHeroId(@Param("parent") Hero parent);

    List<Hero> findByBookId(UUID bookId);
}

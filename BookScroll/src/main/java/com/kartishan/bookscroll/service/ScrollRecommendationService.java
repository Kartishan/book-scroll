package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.model.Scroll;
import com.kartishan.bookscroll.model.UserScrollLike;
import com.kartishan.bookscroll.model.UserScrollViewHistory;
import com.kartishan.bookscroll.model.dto.ScrollDTO;
import com.kartishan.bookscroll.repository.ScrollRepository;
import com.kartishan.bookscroll.repository.UserScrollLikeRepository;
import com.kartishan.bookscroll.repository.UserScrollViewHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ScrollRecommendationService {
    private final ScrollRepository scrollRepository;
    private final UserScrollLikeRepository userScrollLikeRepository;
    private final UserScrollViewHistoryRepository userScrollViewHistoryRepository;
    private final ScrollService scrollService;

    private List<Scroll> getLikedScrollsByUser(UUID userId) {
        return userScrollLikeRepository.findAllByUserId(userId).stream()
                .map(UserScrollLike::getScroll)
                .collect(Collectors.toList());
    }

    private List<Scroll> getViewedScrollsByUser(UUID userId) {
        return userScrollViewHistoryRepository.findAllByUser_Id(userId).stream()
                .map(UserScrollViewHistory::getScroll)
                .collect(Collectors.toList());
    }

    public List<ScrollDTO> getPersonalizedScrolls(UUID userId, int limit) {
        List<Scroll> likedScrolls = getLikedScrollsByUser(userId);
        List<Scroll> viewedScrolls = getViewedScrollsByUser(userId);

        Map<UUID, Integer> scrollScores = new HashMap<>();

        likedScrolls.forEach(scroll -> scrollScores.merge(scroll.getId(), 2, Integer::sum));

        viewedScrolls.forEach(scroll -> scrollScores.merge(scroll.getId(), 1, Integer::sum));

        List<Scroll> recommendedScrolls = scrollScores.entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .map(entry -> scrollRepository.findById(entry.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (limit > 0 && recommendedScrolls.size() > limit) {
            recommendedScrolls = recommendedScrolls.subList(0, limit);
        }
        addRandomScrolls(recommendedScrolls, limit);

        List<ScrollDTO> recommendedScrollDTOs = recommendedScrolls.stream()
                .map(scrollService::getScrollDTO) // Преобразуем каждый Scroll в ScrollDTO
                .toList();

        return recommendedScrollDTOs;
    }
    private void addRandomScrolls(List<Scroll> scrolls, int limit) {
        Set<UUID> presentScrollIds = scrolls.stream()
                .map(Scroll::getId)
                .collect(Collectors.toSet());

        List<Scroll> allScrolls = scrollRepository.findAll();
        Collections.shuffle(allScrolls);
        for (Scroll scroll : allScrolls) {
            if (scrolls.size() >= limit) break;
            if (!presentScrollIds.contains(scroll.getId())) {
                scrolls.add(scroll);
            }
        }
    }
}
package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.model.*;
import com.kartishan.bookscroll.model.dto.ScrollDTO;
import com.kartishan.bookscroll.repository.*;
import com.kartishan.bookscroll.request.ScrollRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrollService {
    private final ScrollRepository scrollRepository;
    private final UserScrollLikeRepository userScrollLikeRepository;
    private final UserScrollViewHistoryRepository userScrollViewHistoryRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ScrollViewRepository scrollViewRepository;

    public Scroll getScroll(UUID id){
        Scroll scroll = scrollRepository.findById(id).orElseThrow();
        return scroll;
    }

    public Book getBookByScrollId(UUID scrollId) {
        Scroll scroll = scrollRepository.findById(scrollId)
                .orElseThrow(() -> new IllegalArgumentException("No scroll with id " + scrollId));
        return scroll.getBook();
    }

    public List<UserScrollLike> getUserScrollLikeHistoryByScrollId(UUID scrollId) {
        return userScrollLikeRepository.findAllByScroll_Id(scrollId);
    }

    public List<UserScrollViewHistory> getUserScrollViewHistoryByScrollId(UUID scrollId) {
        return userScrollViewHistoryRepository.findAllByScroll_Id(scrollId);
    }

    public void createScroll(UUID userId, ScrollRequest scrollRequest) {
        Book book = bookRepository.findById(scrollRequest.getBookId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        System.out.println(scrollRequest.getName());
        Scroll scroll = new Scroll();
        scroll.setName(scrollRequest.getName());
        scroll.setBook(book);
        scroll.setUser(user);
        scroll.setCfiRange(scrollRequest.getCfiRange());

        scrollRepository.save(scroll);
    }


    public void addScrollView(Scroll scroll){
        ScrollView scrollView = scrollViewRepository.findByScrollId(scroll.getId())
                .orElseGet(() -> {
                    ScrollView newScrollView = new ScrollView();
                    newScrollView.setScroll(scroll);
                    newScrollView.setViewCount(0L);
                    return newScrollView;
                });

        scrollView.setViewCount(scrollView.getViewCount() + 1);

        scrollViewRepository.save(scrollView);
    }
    public boolean getLikeStatus(UUID userId, UUID scrollId){
        UserScrollLike userScrollLike = userScrollLikeRepository.findByUserIdAndScrollId(userId, scrollId).get();
        return (userScrollLike.isLiked());
    }

    public void addScrollViewHistory(User user, Scroll scroll){
        UserScrollViewHistory history = new UserScrollViewHistory();
        history.setUser(user);
        history.setScroll(scroll);
        history.setViewTime(new Date());
        userScrollViewHistoryRepository.save(history);
    }

    public void likeScroll(UUID userId, UUID scrollId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Scroll scroll = scrollRepository.findById(scrollId)
                .orElseThrow(() -> new IllegalArgumentException("Scroll not found with id: " + scrollId));

        UserScrollLike userScrollLike = userScrollLikeRepository.findByUserAndScroll(user, scroll)
                .orElseGet(() -> {
                    UserScrollLike newUserScrollLike = new UserScrollLike();
                    newUserScrollLike.setUser(user);
                    newUserScrollLike.setScroll(scroll);
                    newUserScrollLike.setLiked(false);
                    return newUserScrollLike;
                });

        userScrollLike.setLiked(!userScrollLike.isLiked());

        userScrollLikeRepository.save(userScrollLike);
    }

    public boolean userLike(UUID scrollId, UUID userId){
        return userScrollLikeRepository.findByUserIdAndScrollId(userId, scrollId).get().isLiked();
    }

    public ScrollDTO getScrollDTO(Scroll scroll) {
        ScrollDTO scrollDTO = ScrollDTO.builder()
                .id(scroll.getId())
                .name(scroll.getName())
                .build();

        if (scroll.getBook() != null) {
            scrollDTO.setBookId(scroll.getBook().getId());
            scrollDTO.setBookName(scroll.getBook().getName());
        }

        if (scroll.getUser() != null) {
            scrollDTO.setUserId(scroll.getUser().getId());
            scrollDTO.setUsername(scroll.getUser().getUsername());
        }

        return scrollDTO;
    }
}

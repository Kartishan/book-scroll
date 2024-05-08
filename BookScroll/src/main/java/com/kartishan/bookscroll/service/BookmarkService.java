//package com.kartishan.bookscroll.service;
//
//import com.kartishan.bookscroll.model.Book;
////import com.kartishan.bookscroll.model.Bookmark;
//import com.kartishan.bookscroll.model.ShareBookMarkToken;
//import com.kartishan.bookscroll.model.User;
//import com.kartishan.bookscroll.model.dto.BookMarkDTO;
//import com.kartishan.bookscroll.repository.BookRepository;
//import com.kartishan.bookscroll.repository.BookmarkRepository;
//import com.kartishan.bookscroll.repository.ShareBookMarkTokenRepository;
//import com.kartishan.bookscroll.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class BookmarkService {
//
//    private final BookmarkRepository bookmarkRepository;
//    private final UserRepository userRepository;
//    private final BookRepository bookRepository;
//    private final ShareBookMarkTokenRepository shareBookMarkTokenRepository;
//
//    public List<Bookmark> getBookmarksByUserAndBook(UUID userId, UUID bookId) {
//        return bookmarkRepository.findByUserIdAndBookId(userId, bookId);
//    }
//
//    public Bookmark saveBookmark(UUID userId, UUID bookId, String cfiRange, String text,String comment) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
//        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Книга не найдена"));
//
//        Bookmark bookmark = Bookmark.builder()
//                .user(user)
//                .book(book)
//                .cfiRange(cfiRange)
//                .comment(comment)
//                .text(text)
//                .build();
//
//        return bookmarkRepository.save(bookmark);
//    }
//
//    public void deleteBookmark(UUID bookmarkId) {
//        bookmarkRepository.deleteById(bookmarkId);
//    }
//
//    public UUID createShareToken(List<UUID> bookmarkIds, UUID userId) {
//        List<Bookmark> bookmarks = bookmarkRepository.findAllById(bookmarkIds);
//
//        boolean allOwnedByUser = bookmarks.stream()
//                .allMatch(bookmark -> bookmark.getUser().getId().equals(userId));
//
//        if (!allOwnedByUser) {
//            throw new SecurityException("You can only share your own bookmarks.");
//        }
//
//        ShareBookMarkToken shareToken = new ShareBookMarkToken();
//        shareToken.setBookmarks(bookmarks);
//
//        shareToken = shareBookMarkTokenRepository.save(shareToken);
//
//        return shareToken.getId();
//    }
//
//    public List<BookMarkDTO> getBookmarksByShareToken(UUID tokenId) {
//        ShareBookMarkToken token = shareBookMarkTokenRepository.findById(tokenId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid share token!"));
//
//        // Конвертация Bookmark в BookMarkDTO
//        return token.getBookmarks().stream()
//                .map(bookmark -> BookMarkDTO.builder()
//                        .id(bookmark.getId())
//                        .bookId(bookmark.getBook().getId())
//                        .cfiRange(bookmark.getCfiRange())
//                        .text(bookmark.getText())
//                        .comment(bookmark.getComment())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//}

package com.kartishan.bookscroll.service.newBookMark;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.dto.NewBookmarkDTO;
import com.kartishan.bookscroll.model.newBookMark.BookmarkDocument;
import com.kartishan.bookscroll.model.newBookMark.BookmarkSharing;
import com.kartishan.bookscroll.model.newBookMark.UserBookMark;
import com.kartishan.bookscroll.repository.BookRepository;
import com.kartishan.bookscroll.repository.UserRepository;
import com.kartishan.bookscroll.repository.newBookMark.BookmarkMongoRepository;
import com.kartishan.bookscroll.repository.newBookMark.BookmarkSharingRepository;
import com.kartishan.bookscroll.repository.newBookMark.UserBookMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserBookMarkService {

    private final UserBookMarkRepository userBookMarkRepository;
    private final BookmarkMongoRepository bookmarkDocumentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookmarkSharingRepository bookmarkSharingRepository;

    public void addOrUpdateBookmark(UUID userId, UUID bookId, NewBookmarkDTO newBookmarkDTO) {
        Optional<UserBookMark> existingUserBookMark = userBookMarkRepository.findByUserIdAndBookId(userId, bookId);

        BookmarkDocument bookmarkDocument;
        if (existingUserBookMark.isPresent()) {
            bookmarkDocument = bookmarkDocumentRepository.findById(existingUserBookMark.get().getBookMarkId())
                    .orElseThrow(() -> new RuntimeException("BookmarkDocument not found"));
        } else {
            bookmarkDocument = new BookmarkDocument();
        }

        BookmarkDocument.Bookmark newBookmark = new BookmarkDocument.Bookmark();
        BeanUtils.copyProperties(newBookmarkDTO, newBookmark);
        newBookmark.setId(UUID.randomUUID());
        bookmarkDocument.getBookmarks().add(newBookmark);

        bookmarkDocumentRepository.save(bookmarkDocument);

        if (!existingUserBookMark.isPresent()) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

            UserBookMark userBookMark = UserBookMark.builder()
                    .user(user)
                    .book(book)
                    .bookMarkId(bookmarkDocument.getId())
                    .build();

            userBookMarkRepository.save(userBookMark);
        }
    }

    public List<BookmarkDocument.Bookmark> getBookmarks(UUID userId, UUID bookId) {
        UserBookMark userBookMark = userBookMarkRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("UserBookMark not found"));

        String bookmarkDocumentId = userBookMark.getBookMarkId();

        BookmarkDocument bookmarkDocument = bookmarkDocumentRepository.findById(bookmarkDocumentId)
                .orElseThrow(() -> new RuntimeException("BookmarkDocument not found"));

        return bookmarkDocument.getBookmarks();
    }

    public void removeBookmark(UUID userId, UUID bookId, UUID markId) {
        UserBookMark userBookMark = userBookMarkRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("UserBookMark not found"));

        String bookmarkDocumentId = userBookMark.getBookMarkId();

        BookmarkDocument bookmarkDocument = bookmarkDocumentRepository.findById(bookmarkDocumentId)
                .orElseThrow(() -> new RuntimeException("BookmarkDocument not found"));

        boolean removed = bookmarkDocument.getBookmarks().removeIf(bookmark -> markId.equals(bookmark.getId()));
        if (!removed) {
            throw new RuntimeException("Bookmark with specified cfiRange not found");
        }

        bookmarkDocumentRepository.save(bookmarkDocument);
    }

//    public void importBookmarks(UUID recipientUserId, UUID token, UUID bookId) {
//        BookmarkDocument existingBookmarkDocument = bookmarkDocumentRepository.findById(token.toString())
//                .orElseThrow(() -> new RuntimeException("BookmarkDocument not found"));
//
//        BookmarkDocument newBookmarkDocument = new BookmarkDocument();
//        newBookmarkDocument.setBookmarks(new ArrayList<>(existingBookmarkDocument.getBookmarks()));
//        bookmarkDocumentRepository.save(newBookmarkDocument);
//
//        Optional<UserBookMark> optionalUserBookMark = userBookMarkRepository.findByUserIdAndBookId(recipientUserId, bookId);
//        UserBookMark userBookMark;
//        if (optionalUserBookMark.isPresent()) {
//            userBookMark = optionalUserBookMark.get();
//            userBookMark.setBookMarkId(newBookmarkDocument.getId());
//        } else {
//            User recipientUser = userRepository.findById(recipientUserId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            Book book = bookRepository.findById(bookId)
//                    .orElseThrow(() -> new RuntimeException("Book not found"));
//
//            userBookMark = UserBookMark.builder()
//                    .user(recipientUser)
//                    .book(book)
//                    .bookMarkId(newBookmarkDocument.getId())
//                    .build();
//        }
//        userBookMarkRepository.save(userBookMark);
//    }

    public UUID shareBookmarks(UUID userId, UUID bookId) {
        UserBookMark userBookMark = userBookMarkRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("UserBookMark not found"));

        BookmarkDocument bookmarkDocument = bookmarkDocumentRepository.findById(userBookMark.getBookMarkId())
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        UUID token = UUID.randomUUID();

        BookmarkSharing bookmarkSharing = new BookmarkSharing();
        bookmarkSharing.setId(token);
        bookmarkSharing.setBookMarkId(bookmarkDocument.getId());
        bookmarkSharing.setBookId(bookId);
        bookmarkSharingRepository.save(bookmarkSharing);

        return token;
    }

    public void importBookmarks(UUID recipientUserId, UUID token) {
        BookmarkSharing bookmarkSharing = bookmarkSharingRepository.findById(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        Optional<UserBookMark> optionalRecipientBookMark = userBookMarkRepository.findByUserIdAndBookId(recipientUserId, bookmarkSharing.getBookId());
        BookmarkDocument recipientBookmarkDocument;
        if (optionalRecipientBookMark.isPresent()) {
            recipientBookmarkDocument = bookmarkDocumentRepository.findById(optionalRecipientBookMark.get().getBookMarkId())
                    .orElseThrow(() -> new RuntimeException("BookmarkDocument not found"));
        } else {
            recipientBookmarkDocument = new BookmarkDocument();
            bookmarkDocumentRepository.save(recipientBookmarkDocument);
            User recipientUser = userRepository.findById(recipientUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserBookMark newUserBookMark = UserBookMark.builder()
                    .user(recipientUser)
                    .book(bookRepository.findById(bookmarkSharing.getBookId()).orElseThrow(() -> new RuntimeException("Book not found")))
                    .bookMarkId(recipientBookmarkDocument.getId())
                    .build();
            userBookMarkRepository.save(newUserBookMark);
        }

        BookmarkDocument sharedBookmarkDocument = bookmarkDocumentRepository.findById(bookmarkSharing.getBookMarkId())
                .orElseThrow(() -> new RuntimeException("Shared BookmarkDocument not found"));
        recipientBookmarkDocument.getBookmarks().addAll(sharedBookmarkDocument.getBookmarks());
        bookmarkDocumentRepository.save(recipientBookmarkDocument);
    }

//    public void importBookmarks(UUID recipientUserId, UUID token, UUID bookId) {
//        BookmarkDocument sharedBookmarkDocument = bookmarkDocumentRepository.findById(token.toString())
//                .orElseThrow(() -> new RuntimeException("Shared BookmarkDocument not found"));
//
//        Optional<UserBookMark> optionalUserBookMark = userBookMarkRepository.findByUserIdAndBookId(recipientUserId, bookId);
//        BookmarkDocument recipientBookmarkDocument;
//        if (optionalUserBookMark.isPresent()) {
//            recipientBookmarkDocument = bookmarkDocumentRepository.findById(optionalUserBookMark.get().getBookMarkId())
//                    .orElseThrow(() -> new RuntimeException("Recipient BookmarkDocument not found"));
//        } else {
//            recipientBookmarkDocument = new BookmarkDocument();
//            bookmarkDocumentRepository.save(recipientBookmarkDocument);
//
//            User recipientUser = userRepository.findById(recipientUserId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            Book book = bookRepository.findById(bookId)
//                    .orElseThrow(() -> new RuntimeException("Book not found"));
//
//            UserBookMark newUserBookMark = UserBookMark.builder()
//                    .user(recipientUser)
//                    .book(book)
//                    .bookMarkId(recipientBookmarkDocument.getId())
//                    .build();
//            userBookMarkRepository.save(newUserBookMark);
//        }
//
//        recipientBookmarkDocument.getBookmarks().addAll(sharedBookmarkDocument.getBookmarks());
//
//        bookmarkDocumentRepository.save(recipientBookmarkDocument);
//    }
}

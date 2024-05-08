package com.kartishan.bookscroll.repository.newBookMark;

import com.kartishan.bookscroll.model.newBookMark.BookmarkDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookmarkMongoRepository extends MongoRepository<BookmarkDocument, String> {
}

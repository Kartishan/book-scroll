package com.kartishan.bookscroll.model.newBookMark;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "bookmarks")
@Data
@NoArgsConstructor
public class BookmarkDocument {

    @Id
    private String id;

    private List<Bookmark> bookmarks = new ArrayList<>();

    @Data
    public static class Bookmark {
        private UUID id;
        private String cfiRange;
        private String text;
        private String comment;
    }
}

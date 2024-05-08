package com.kartishan.bookscroll.model.newBookMark;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookmark_sharing")
public class BookmarkSharing {
    @Id
    private UUID id;

    private String bookMarkId;

    private UUID bookId;
}

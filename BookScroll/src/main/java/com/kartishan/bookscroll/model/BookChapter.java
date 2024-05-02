package com.kartishan.bookscroll.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_chapter")
@Entity
public class BookChapter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Long number;
    private String audioFileId;

    @Column(length = 9000)
    private String summary;

    private int duration;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}

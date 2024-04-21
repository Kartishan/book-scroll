package com.kartishan.bookscroll.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name="image")
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "name")
    private String name;
    private String originalFileName;
    private String contentType;
    private Long size;

    @Lob
    private byte[] bytes;
    @OneToOne
    @JoinColumn(name = "book_id", unique = true)
    private Book book;

}

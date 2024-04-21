package com.kartishan.bookscroll.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scroll_comment")
@Entity
public class ScrollComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scroll_id")
    @NonNull
    private Scroll scroll;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private ScrollComment parentComment;
}

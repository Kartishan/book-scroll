package com.kartishan.bookscroll.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_scroll_view_history")
@Entity
public class UserScrollViewHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "scroll_id")
    private Scroll scroll;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "view_time")
    private Date viewTime;
}

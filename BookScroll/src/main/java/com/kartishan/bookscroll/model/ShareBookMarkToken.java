//package com.kartishan.bookscroll.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//import java.util.UUID;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "share_bookmark_token")
//@Entity
//public class ShareBookMarkToken {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    @ManyToMany
//    @JoinTable(name = "share_token_bookmarks",
//            joinColumns = @JoinColumn(name = "share_token_id"),
//            inverseJoinColumns = @JoinColumn(name = "bookmark_id"))
//    private List<Bookmark> bookmarks;
//
//}

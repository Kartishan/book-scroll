package com.kartishan.bookscroll.response;

import com.kartishan.bookscroll.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationTokenResponse {
    @JsonProperty("user")
    private User user;
}
package com.bookend.security.dto;

import com.bookend.login.domain.entity.User;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SessionUser {
    private String name;
    private String emil;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.emil = user.getEmail();
        this.picture = user.getPicture();
    }
}

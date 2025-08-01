package com.example.model;

import java.util.Objects;

public class User {
    private String userId;
    private String name;

    public User(String userId,  String name) {
        this.userId = userId;
        this.name = name;
    }

    public boolean equalsUser(User user) {
        return this.equals(user);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name);
    }
}

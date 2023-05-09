package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends RuntimeException {
    long userId;

    public UserNotFoundException(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}

package ru.yandex.practicum.filmorate.exception;

public class MPANotFoundException extends RuntimeException {
    long id;

    public MPANotFoundException(long id) {
        this.id = id;
    }

    public long getMPAId() {
        return id;
    }
}

package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends RuntimeException {
    long id;

    public GenreNotFoundException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

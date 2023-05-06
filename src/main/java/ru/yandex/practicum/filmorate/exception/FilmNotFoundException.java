package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    long filmId;

    public FilmNotFoundException(Long filmId) {
        this.filmId = filmId;
    }

    public Long getFilmId() {
        return filmId;
    }
}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film upDateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilm(Long id);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);
}

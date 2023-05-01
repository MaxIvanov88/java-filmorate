package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film upDateFilm(Film film) {
        return filmStorage.upDateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    public Film addLikeByUser(Long userId, Long filmId) {
        if (!filmStorage.getAllFilms().contains(filmStorage.getFilm(filmId))) {
            throw new FilmNotFoundException("Id with number " + filmId + " does not exist");
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            throw new UserNotFoundException("Id with  number" + userId + " does not exist");
        }
        filmStorage.getFilm(filmId).addLike(userId);
        log.info("The user " + userId + " laked the film " + filmId);
        return filmStorage.getFilm(filmId);
    }

    public Film removeLikeByUser(Long userId, Long filmId) {
        if (!filmStorage.getFilm(filmId).getLikes().contains(userId)) {
            throw new UserNotFoundException("Id with  number" + userId + " does not exist");
        }
        if (!filmStorage.getAllFilms().contains(filmStorage.getFilm(filmId))) {
            throw new FilmNotFoundException("Id with number " + filmId + " does not exist");
        }
        filmStorage.getFilm(filmId).deleteLike(userId);
        log.info("The user " + userId + " delete lake the film " + filmId);
        return filmStorage.getFilm(filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Returned" + count + "most popular film");
        return filmStorage.getAllFilms().stream()
                .sorted(((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size())))
                .limit(count)
                .collect(Collectors.toList());
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@NotNull @Valid @RequestBody Film film) {
        log.info("Post request received: {}", film);
        validate(film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film upDateFilm(@NotNull @Valid @RequestBody Film film) {
        log.info("Put request received: {}", film);
        validate(film);
        return filmService.upDateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Long id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLikeByUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return filmService.addLikeByUser(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLikeByUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return filmService.removeLikeByUser(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Release date of the film {} is before min release date {}.", film.getReleaseDate(), MIN_RELEASE_DATE);
            throw new ValidationException("Release date of tne film cannot be earlier than min release date");
        }
    }
}

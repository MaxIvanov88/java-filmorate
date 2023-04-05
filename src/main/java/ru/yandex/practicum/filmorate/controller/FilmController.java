package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private int id = 0;

    @PostMapping
    public Film createFilm(@NotNull @Valid @RequestBody Film film) {
        log.info("Post request received: {}", film);
        if (films.containsKey(film.getId())) {
            log.error("Film with this id {} exists ", film.getId());
            throw new ValidationException("Film with this id exists");
        }
        validation(film);
        id++;
        film.setId(id);
        films.put(id, film);
        log.info("film added: {} ", film);
        return film;
    }

    @PutMapping
    public Film upDateFilm(@NotNull @Valid @RequestBody Film film) {
        log.info("Put request received: {}", film);
        if (!films.containsKey(film.getId())) {
            log.error("Film with this id {} there is not", film.getId());
            throw new ValidationException("Film with this id there is not");
        }
        validation(film);
        films.put(film.getId(), film);
        log.info("Film {} was updated", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmList = new ArrayList<>(films.values());
        log.info("Currently {} films:", filmList.size());
        return filmList;
    }

    private void validation(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.error("Film name {} cannot be empty", film.getName());
            throw new ValidationException("Film name cannot be empty");
        }
        if (film.getDescription().length() > 200) {
            log.error("Film description {} cannot be more then 200 symbol", film.getDescription());
            throw new ValidationException("Film description {} cannot be more then 200 symbol");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Release date of the film {} is before min release date {}.", film.getReleaseDate(), MIN_RELEASE_DATE);
            throw new ValidationException("Release date of tne film cannot be earlier than min release date");
        }
        if (film.getDuration() <= 0) {
            log.error("Film duration {} must be positive", film.getDuration());
            throw new ValidationException("Film duration must be positive");
        }
    }
}

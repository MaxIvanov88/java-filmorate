package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    @Override
    public Film createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.error("Film with this id {} exists ", film.getId());
            throw new ValidationException("Film with this id exists");
        }
        validate(film);
        id++;
        film.setId(id);
        films.put(id, film);
        log.info("film added: {} ", film);
        return film;
    }

    @Override
    public Film upDateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Film with this id {} there is not", film.getId());
            throw new FilmNotFoundException("Film with this id there is not");
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Film {} was updated", film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> filmList = new ArrayList<>(films.values());
        log.info("Currently {} films:", filmList.size());
        return filmList;
    }

    @Override
    public Film getFilm(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new FilmNotFoundException("Id with number " + id + " does not exist");
        }
    }

    private void validate(Film film) {
        if (film.getName().isBlank()) {
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

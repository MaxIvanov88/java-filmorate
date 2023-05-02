package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    @Override
    public Film createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.error("Film with this id {} exists ", film.getId());
            throw new ValidationException("Film with this id exists");
        }
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
}

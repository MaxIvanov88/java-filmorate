package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.List;

@Service
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    public Genre getGenre(int genreId) {
        if (genreDao.getGenre(genreId) != null) {
            return genreDao.getGenre(genreId);
        } else {
            throw new GenreNotFoundException(genreId);
        }
    }
}

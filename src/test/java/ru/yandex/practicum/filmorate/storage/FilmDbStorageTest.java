package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Film film1;
    private Film film2;
    private User user;

    @BeforeEach
    void beforeEach() {
        film1 = filmStorage.createFilm(new Film(0, "film1", "description1",
                LocalDate.of(2009, 12, 17), (long) 60, new MPA(1, null)));
        film2 = filmStorage.createFilm(new Film(0, "film2", "description2",
                LocalDate.of(2021, 11, 16), (long) 120, new MPA(2, null)));
        user = userStorage.createUser(new User(0, "user1@mail.ru", "login1", "User1",
                LocalDate.of(2001, 8, 1)));
    }

    @Test
    void testGetAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        assertEquals(2, films.size());
    }

    @Test
    void testGetFilm() {
        Film film = filmStorage.getFilm(film1.getId());
        assertEquals(film1.getId(), film.getId());
        assertEquals(film1.getName(), film.getName());
        assertEquals(film1.getDescription(), film.getDescription());
        assertEquals(film1.getReleaseDate(), film.getReleaseDate());
        assertEquals(film1.getDuration(), film.getDuration());

        film = filmStorage.getFilm(film2.getId());
        assertEquals(film2.getId(), film.getId());
        assertEquals(film2.getName(), film.getName());
        assertEquals(film2.getDescription(), film.getDescription());
        assertEquals(film2.getReleaseDate(), film.getReleaseDate());
        assertEquals(film2.getDuration(), film.getDuration());
    }

    @Test
    void testCreateFilm() {
        assertEquals(2, filmStorage.getAllFilms().size());
        Film film3 = new Film(0, "film3", "description",
                LocalDate.of(2022, 11, 1), (long) 180, new MPA(3, null));
        filmStorage.createFilm(film3);

        assertEquals(3, filmStorage.getAllFilms().size());
    }

    @Test
    void testUpdateFilm() {
        film1.setName("film11");
        film1.setReleaseDate(LocalDate.of(2000, 4, 3));
        filmStorage.upDateFilm(film1);

        Film film = filmStorage.getFilm(film1.getId());
        assertEquals("film11", film.getName());
        assertEquals(LocalDate.of(2000, 4, 3), film.getReleaseDate());

        assertEquals(new HashSet<>(), film.getGenres());
        Genre genre = new Genre(1, "Комедия");
        film1.getGenres().add(genre);
        filmStorage.upDateFilm(film1);

        film = filmStorage.getFilm(film1.getId());
        assertEquals(Set.of(genre), film.getGenres());

        long userId = user.getId();
        assertEquals(new HashSet<>(), film1.getLikes());
        filmStorage.addLike(film1.getId(), userId);
        filmStorage.upDateFilm(film1);

        film = filmStorage.getFilm(film1.getId());
        assertEquals(Set.of(userId), film.getLikes());
    }

    @Test
    void testAddLike() {
        long id = film1.getId();
        long userId = user.getId();
        assertEquals(new HashSet<>(), filmStorage.getFilm(id).getLikes());

        filmStorage.addLike(id, userId);

        assertEquals(Set.of(userId), filmStorage.getFilm(id).getLikes());
    }

    @Test
    void testDeleteLike() {
        long id = film1.getId();
        long userId = user.getId();

        filmStorage.addLike(id, userId);
        assertEquals(Set.of(userId), filmStorage.getFilm(id).getLikes());

        filmStorage.deleteLike(id, userId);

        assertEquals(new HashSet<>(), filmStorage.getFilm(id).getLikes());
    }
}

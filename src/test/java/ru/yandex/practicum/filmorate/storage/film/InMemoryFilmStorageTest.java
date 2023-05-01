package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFilmStorageTest {
    InMemoryFilmStorage inMemoryFilmStorage;
    Film film;

    @BeforeEach
    public void beforeEach() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        film = Film.builder()
                .name("TestFilm")
                .description("TestDescription")
                .duration(90)
                .releaseDate(LocalDate.of(2000, 12, 1))
                .build();
    }

    @Test
    public void createFilm() {
        inMemoryFilmStorage.createFilm(film);
        assertEquals(1, inMemoryFilmStorage.getAllFilms().size(), "Film not added");
    }

    @Test
    public void upDateFilm() {
        Film upDateFilm = Film.builder()
                .id(1)
                .name("newTestFilm")
                .description("newTestDescription")
                .duration(90)
                .releaseDate(LocalDate.of(2010, 12, 1))
                .build();
        inMemoryFilmStorage.createFilm(film);
        inMemoryFilmStorage.upDateFilm(upDateFilm);
        assertEquals(1, inMemoryFilmStorage.getAllFilms().size(), "Film list is wrong");
        assertTrue(inMemoryFilmStorage.getAllFilms().contains(upDateFilm), "Film updated with wrong");
    }

    @Test
    public void upDateFilmWrongId() {
        Film wrongFilm = Film.builder()
                .id(10)
                .name("newTestFilm")
                .description("newTestDescription")
                .duration(90)
                .releaseDate(LocalDate.of(2010, 12, 1))
                .build();
        inMemoryFilmStorage.createFilm(film);
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.upDateFilm(wrongFilm));
    }

    @Test
    public void createFilmNameEmpty() {
        Film wrongFilm = Film.builder()
                .name("")
                .description("newTestDescription")
                .duration(90)
                .releaseDate(LocalDate.of(2010, 12, 1))
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.createFilm(wrongFilm));
    }

    @Test
    public void createFilmDescriptionMore200Symbol() {
        Film wrongFilm = Film.builder()
                .name("testFilm")
                .description("newTestDescriptinewnewTestDescriptinewnewTestDescriptinewnewTestDescriptinew" +
                        "newTestDescriptinewnewTestDescriptinewnewTestDescriptinewnewTestDescriptinew" +
                        "newTestDescriptinewnewTestDescriptinewnewTestDescriptinewnewTestDescriptinewnewTestDescriptinew" +
                        "newTestDescriptinewnewTestDescriptinew")
                .duration(90)
                .releaseDate(LocalDate.of(2010, 12, 1))
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.createFilm(wrongFilm));
    }

    @Test
    public void createFilmBeforeMin_Release() {
        Film wrongFilm = Film.builder()
                .name("testFilm")
                .description("newTestDescription")
                .duration(90)
                .releaseDate(LocalDate.of(1800, 12, 1))
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.createFilm(wrongFilm));
    }

    @Test
    public void createFilmDurationIsNegative() {
        Film wrongFilm = Film.builder()
                .name("testFilm")
                .description("newTestDescription")
                .duration(-10)
                .releaseDate(LocalDate.of(2000, 12, 1))
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.createFilm(wrongFilm));
    }

    @Test
    public void createFilmNull() {
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.createFilm(null));
    }

    @Test
    public void createFilmNameNull() {
        Film wrongFilm = Film.builder()
                .description("newTestDescription")
                .duration(90)
                .releaseDate(LocalDate.of(2000, 12, 1))
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.createFilm(wrongFilm));
    }

    @Test
    public void createFilmDescriptionNull() {
        Film wrongFilm = Film.builder()
                .name("testFilm")
                .duration(90)
                .releaseDate(LocalDate.of(2000, 12, 1))
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.createFilm(wrongFilm));
    }

    @Test
    public void createFilmDurationNull() {
        Film wrongFilm = Film.builder()
                .name("testFilm")
                .description("newTestDescription")
                .releaseDate(LocalDate.of(2000, 12, 1))
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.createFilm(wrongFilm));
    }

    @Test
    public void createFilmReleaseDateNull() {
        Film wrongFilm = Film.builder()
                .name("testFilm")
                .description("newTestDescription")
                .duration(90)
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryFilmStorage.createFilm(wrongFilm));
    }
}


package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film = Film.builder()
                .name("TestFilm")
                .description("TestDescription")
                .duration(90)
                .releaseDate(LocalDate.of(2000, 12, 1))
                .build();
    }

    @Test
    public void createFilm() {
        filmController.createFilm(film);
        assertEquals(1, filmController.getAllFilms().size(), "Film not added");
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
        filmController.createFilm(film);
        filmController.upDateFilm(upDateFilm);
        assertEquals(1, filmController.getAllFilms().size(), "Film list is wrong");
        assertTrue(filmController.getAllFilms().contains(upDateFilm), "Film updated with wrong");
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
        filmController.createFilm(film);
        assertThrows(RuntimeException.class,
                () -> filmController.upDateFilm(wrongFilm));
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
                () -> filmController.createFilm(wrongFilm));
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
                () -> filmController.createFilm(wrongFilm));
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
                () -> filmController.createFilm(wrongFilm));
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
                () -> filmController.createFilm(wrongFilm));
    }

    @Test
    public void createFilmNull() {
        assertThrows(RuntimeException.class,
                () -> filmController.createFilm(null));
    }


}

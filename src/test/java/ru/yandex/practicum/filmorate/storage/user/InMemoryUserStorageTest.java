package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserStorageTest {
    InMemoryUserStorage inMemoryUserStorage;
    User user;

    @BeforeEach
    public void beforeEach() {
        inMemoryUserStorage = new InMemoryUserStorage();
        user = User.builder()
                .email("emai@email.ru")
                .login("login")
                .birthday(LocalDate.of(1988, 10, 2))
                .name("Name")
                .build();
    }

    @Test
    public void createUser() {
        inMemoryUserStorage.createUser(user);
        assertEquals(1, inMemoryUserStorage.getAllUsers().size(), "User not added");
    }

    @Test
    public void upDateUser() {
        User upDateUser = User.builder()
                .id(1)
                .email("newEmail@mail.ru")
                .login("newLogin")
                .birthday(LocalDate.of(1990, 12, 12))
                .name("NewName")
                .build();
        inMemoryUserStorage.createUser(user);
        inMemoryUserStorage.upDateUser(upDateUser);
        assertEquals(1, inMemoryUserStorage.getAllUsers().size(), "User list is wrong");
        assertTrue(inMemoryUserStorage.getAllUsers().contains(upDateUser), "User updated with wrong");
    }

    @Test
    public void createUserWithEmailEmpty() {
        User wrongUser = User.builder()
                .email("")
                .login("login")
                .birthday(LocalDate.of(1988, 10, 2))
                .name("Name")
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryUserStorage.createUser(wrongUser));
    }

    @Test
    public void createUserWithWrongEmail() {
        User wrongUser = User.builder()
                .email("email.ru")
                .login("login")
                .birthday(LocalDate.of(1988, 10, 2))
                .name("Name")
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryUserStorage.createUser(wrongUser));
    }

    @Test
    public void createUserLoginEmpty() {
        User wrongUser = User.builder()
                .email("email@email.ru")
                .login("")
                .birthday(LocalDate.of(1988, 10, 2))
                .name("Name")
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryUserStorage.createUser(wrongUser));
    }

    @Test
    public void createUserLoginWithSpaces() {
        User wrongUser = User.builder()
                .email("email@email.ru")
                .login("new login")
                .birthday(LocalDate.of(1988, 10, 2))
                .name("Name")
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryUserStorage.createUser(wrongUser));
    }

    @Test
    public void createUserBirthdayIsFuture() {
        User wrongUser = User.builder()
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.of(2222, 10, 2))
                .name("Name")
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryUserStorage.createUser(wrongUser));
    }

    @Test
    public void createUserNameEmpty() {
        User newUser = User.builder()
                .email("email@email.ru")
                .login("Login")
                .birthday(LocalDate.of(1988, 10, 2))
                .name("")
                .build();
        inMemoryUserStorage.createUser(newUser);
        assertEquals("Login", inMemoryUserStorage.getAllUsers().get(0).getName(), "Login don't assign name");
    }

    @Test
    public void createUserNull() {
        assertThrows(RuntimeException.class,
                () -> inMemoryUserStorage.createUser(null));
    }

    @Test
    public void createUserEmailNull() {
        User wrongUser = User.builder()
                .login("login")
                .birthday(LocalDate.of(1988, 10, 2))
                .name("Name")
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryUserStorage.createUser(wrongUser));
    }

    @Test
    public void createUserLoginNull() {
        User wrongUser = User.builder()
                .email("email@email.ru")
                .birthday(LocalDate.of(1988, 10, 2))
                .name("Name")
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryUserStorage.createUser(wrongUser));
    }

    @Test
    public void createUserBirthDayNull() {
        User wrongUser = User.builder()
                .login("login")
                .email("email@email.ru")
                .name("Name")
                .build();
        assertThrows(RuntimeException.class,
                () -> inMemoryUserStorage.createUser(wrongUser));
    }
}

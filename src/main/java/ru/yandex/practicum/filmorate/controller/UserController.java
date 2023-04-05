package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User createUser(@NotNull @Valid @RequestBody User user) {
        log.info("Post request received: {}", user);
        if (users.containsKey(user.getId())) {
            log.error("User with this id {} extents", user.getId());
            throw new ValidationException("User with this id extents");
        }
        validation(user);
        id++;
        user.setId(id);
        users.put(id, user);
        log.info("User added {}", user);
        return user;
    }

    @PutMapping
    public User upDateUser(@NotNull @Valid @RequestBody User user) {
        log.info("Put request received: {}", user);
        if (!users.containsKey(user.getId())) {
            log.error("User with this id there isn't");
            throw new ValidationException("User with this id there is not");
        }
        validation(user);
        users.put(id, user);
        log.info("User added {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        log.info("Currently {} users", userList.size());
        return userList;
    }

    private void validation(User user) {
        if (user.getEmail().isBlank()) {
            log.error("Email {} can't be empty", user.getEmail());
            throw new ValidationException("Email cannot be empty");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Email {} must contain the symbol '@'", user.getEmail());
            throw new ValidationException("Email must contain the symbol '@' ");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Login {} cannot contain spaces", user.getLogin());
            throw new ValidationException("Login cannot contain spaces ");
        }
        if (user.getLogin().isBlank()) {
            log.error("Login {} cannot be empty", user.getLogin());
            throw new ValidationException("Login cannot be empty");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("User name changed to login {}", user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("User birthday {} cannot be in the future", user.getBirthday());
            throw new ValidationException("User birthday cannot be in the future");
        }
    }
}




package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            log.error("User with this id {} extents", user.getId());
            throw new ValidationException("User with this id extents");
        }
        validate(user);
        id++;
        user.setId(id);
        users.put(user.getId(), user);
        log.info("User added {}", user);
        return user;
    }

    @Override
    public User upDateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("User with this id there isn't");
            throw new UserNotFoundException("User with this id there is not");
        }
        validate(user);
        users.put(id, user);
        log.info("User added {}", user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        log.info("Currently {} users", userList.size());
        return userList;
    }

    @Override
    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Id with this number does not exist");
        }
        return users.get(id);
    }

    private void validate(User user) {
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

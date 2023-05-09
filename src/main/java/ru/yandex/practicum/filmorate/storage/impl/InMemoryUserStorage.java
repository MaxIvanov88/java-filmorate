package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component("InMemoryUserStorage")
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
            throw new UserNotFoundException(user.getId());
        }
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
            throw new UserNotFoundException(id);
        }
        return users.get(id);
    }

    public void addFriend(long id, long friendId) {
        User user = getUser(id);
        User friendUser = getUser(friendId);

        user.getFriends().add(friendId);
        friendUser.getFriends().add(id);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        User user = getUser(id);
        User friendUser = getUser(friendId);

        user.getFriends().remove(friendId);
        friendUser.getFriends().remove(id);
    }

    @Override
    public Set<Long> getUserFriendsIds(long id) {
        return getUser(id).getFriends();
    }
}

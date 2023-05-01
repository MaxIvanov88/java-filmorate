package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User upDateUser(User user) {
        return userStorage.upDateUser(user);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public List<User> getFriends(Long userId) {
        List<User> list = new ArrayList<>();
        for (Long id : userStorage.getUser(userId).getFriends()) {
            list.add(userStorage.getUser(id));
        }
        return list;
    }

    public User addAsFriend(Long userId, Long friendId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            throw new UserNotFoundException("Id with  number" + userId + " does not exist");
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUser(friendId))) {
            throw new UserNotFoundException("Id with  number" + friendId + " does not exist");
        }
        userStorage.getUser(userId).addFriend(friendId);
        userStorage.getUser(friendId).addFriend(userId);
        return userStorage.getUser(userId);

    }

    public User removeFromFriends(Long userId, Long friendId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            throw new UserNotFoundException("Id with  number" + userId + " does not exist");
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUser(friendId))) {
            throw new UserNotFoundException("Id with  number" + friendId + " does not exist");
        }
        userStorage.getUser(userId).deleteFriend(friendId);
        userStorage.getUser(friendId).deleteFriend(userId);
        return userStorage.getUser(userId);
    }

    public List<User> getCommonFriends(Long user1Id, Long user2Id) {
        List<User> list = new ArrayList<>();
        for (Long id1 : userStorage.getUser(user1Id).getFriends()) {
            for (Long id2 : userStorage.getUser(user2Id).getFriends()) {
                if (id2.equals(id1)) {
                    list.add(userStorage.getUser(id2));
                }
            }
        }
        return list;
    }
}

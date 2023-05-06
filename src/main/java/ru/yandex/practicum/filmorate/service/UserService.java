package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
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
        return getFriendsBySetFriendsId(userStorage.getUserFriendsIds(userId));
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUser(id))) {
            throw new UserNotFoundException(id);
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUser(otherId))) {
            throw new UserNotFoundException(otherId);
        }
        Set<Long> commonId = new HashSet<>(userStorage.getUserFriendsIds(id));
        commonId.retainAll(userStorage.getUserFriendsIds(otherId));

        return getFriendsBySetFriendsId(commonId);
    }

    public User addAsFriend(Long userId, Long friendId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            throw new UserNotFoundException(userId);
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUser(friendId))) {
            throw new UserNotFoundException(friendId);
        }
        userStorage.addFriend(userId, friendId);
        return userStorage.getUser(userId);
    }

    public User removeFromFriends(Long userId, Long friendId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUser(userId))) {
            throw new UserNotFoundException(userId);
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUser(friendId))) {
            throw new UserNotFoundException(friendId);
        }
        userStorage.deleteFriend(userId, friendId);
        return userStorage.getUser(userId);
    }

    private List<User> getFriendsBySetFriendsId(Set<Long> friendsIds) {
        return friendsIds.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    User upDateUser(User user);

    List<User> getAllUsers();

    User getUser(Long id);

    Set<Long> getUserFriendsIds(long id);

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);
}

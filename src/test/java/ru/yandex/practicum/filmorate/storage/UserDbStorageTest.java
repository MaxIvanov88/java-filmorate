package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private User user1;
    private User user2;

    @BeforeEach
    void beforeEach() {
        user1 = userStorage.createUser(new User(0, "user1@mail.ru", "login1", "User1",
                LocalDate.of(2000, 8, 1)));
        user2 = userStorage.createUser(new User(0, "user2@mail.ru", "login2", "User2",
                LocalDate.of(2001, 8, 1)));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userStorage.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testGetUser() {
        User user = userStorage.getUser(user1.getId());
        assertEquals(user1, user);

        user = userStorage.getUser(user2.getId());
        assertEquals(user2, user);
    }

    @Test
    void testCreate() {
        assertEquals(2, userStorage.getAllUsers().size());
        User user3 = new User(0, "user3@mail.ru", "login3", "User3",
                LocalDate.of(2010, 4, 3));
        userStorage.createUser(user3);

        assertEquals(3, userStorage.getAllUsers().size());
    }

    @Test
    void testUpdateUser() {
        user1.setName("User11");
        user1.setBirthday(LocalDate.of(2010, 4, 3));
        userStorage.upDateUser(user1);

        User user = userStorage.getUser(user1.getId());
        assertEquals("User11", user.getName());
        assertEquals(LocalDate.of(2010, 4, 3), user.getBirthday());
    }

    @Test
    void testGetUserFriendsIds() {
        User user3 = new User(0, "user3@mail.ru", "login3", "User3",
                LocalDate.of(2010, 4, 3));
        userStorage.createUser(user3);

        long id = user1.getId();
        assertEquals(new HashSet<>(), userStorage.getUserFriendsIds(id));

        long friendId1 = user2.getId();
        long friendId2 = user3.getId();
        userStorage.addFriend(id, friendId1);
        userStorage.addFriend(id, friendId2);

        assertEquals(Set.of(friendId1, friendId2), userStorage.getUserFriendsIds(id));
    }

    @Test
    void testAddFriend() {
        long id = user1.getId();
        long friendId = user2.getId();
        assertEquals(new HashSet<>(), userStorage.getUserFriendsIds(id));

        userStorage.addFriend(id, friendId);

        assertEquals(Set.of(friendId), userStorage.getUserFriendsIds(id));
    }

    @Test
    void testDeleteFriend() {
        long id = user1.getId();
        long friendId = user2.getId();
        userStorage.addFriend(id, friendId);
        assertEquals(Set.of(friendId), userStorage.getUserFriendsIds(id));

        userStorage.deleteFriend(id, friendId);

        assertEquals(new HashSet<>(), userStorage.getUserFriendsIds(id));
    }
}

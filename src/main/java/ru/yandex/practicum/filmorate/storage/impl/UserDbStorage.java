package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.IdMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component("UserDbStorage")
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT *  FROM users;";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User getUser(Long id) {
        if (contains(id)) {
            String sql = "SELECT *  FROM users u WHERE u.user_id=?;";
            return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public User createUser(User user) {
        if (!contains(user.getId())) {
            String sql = "INSERT INTO users(name, birthdate, email, login) VALUES(?,?,?,?);";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
                stmt.setString(1, user.getName());
                stmt.setDate(2, Date.valueOf(user.getBirthday()));
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getLogin());

                return stmt;
            }, keyHolder);

            user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

            return user;
        } else {
            throw new IllegalArgumentException("user is exist");
        }
    }

    @Override
    public User upDateUser(User user) {
        if (contains(user.getId())) {
            String sql = "UPDATE users SET name=?, birthdate=?, email=?, login=? WHERE user_id=?;";
            jdbcTemplate.update(sql, user.getName(), user.getBirthday(), user.getEmail(),
                    user.getLogin(), user.getId());

            return user;
        } else {
            throw new UserNotFoundException(user.getId());
        }
    }

    @Override
    public void addFriend(long id, long friendId) {
        if (contains(id) && contains(friendId)) {
            String sql = "MERGE INTO friendship(user_id1, user_id2) VALUES(?, ?)";
            jdbcTemplate.update(sql, id, friendId);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        if (contains(id) && contains(friendId)) {
            String sql = "DELETE FROM friendship WHERE user_id1=? AND user_id2=?;";
            jdbcTemplate.update(sql, id, friendId);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public Set<Long> getUserFriendsIds(long id) {
        if (contains(id)) {
            String sql = "SELECT f.user_id2 AS id FROM friendship f " +
                    "LEFT JOIN users u ON f.user_id2=u.user_id WHERE f.user_id1=?;";
            return new HashSet<>(jdbcTemplate.query(sql, new IdMapper(), id));
        } else {
            throw new UserNotFoundException(id);
        }
    }

    private boolean contains(long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?;";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count != null && count != 0;
    }
}

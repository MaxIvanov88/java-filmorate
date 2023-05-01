package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private long id;
    @NotNull
    @Email
    private final String email;
    @NotNull
    private final String login;
    private String name;
    @NotNull
    private final LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

    public boolean addFriend(Long userId) {
        return friends.add(userId);
    }

    public boolean deleteFriend(Long userId) {
        return friends.remove(userId);
    }


}

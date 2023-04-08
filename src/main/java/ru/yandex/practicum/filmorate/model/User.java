package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @NotNull
    @Email
    private final String email;
    @NotNull
    private final String login;
    private String name;
    @NotNull
    private final LocalDate birthday;
}

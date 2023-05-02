package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private long id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final long duration;
    private final Set<Long> likes = new HashSet<>();

    public boolean addLike(Long userId) {
        return likes.add(userId);
    }

    public boolean deleteLike(Long userId) {
        return likes.remove(userId);
    }
}

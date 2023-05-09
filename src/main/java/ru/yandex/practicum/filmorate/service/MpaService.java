package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPADao;

import java.util.List;

@Service
public class MpaService {
    private final MPADao mpaDao;

    @Autowired
    public MpaService(MPADao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<MPA> getAll() {
        return mpaDao.getAll();
    }

    public MPA getMPA(int ratingId) {
        if (mpaDao.getMPA(ratingId) != null) {
            return mpaDao.getMPA(ratingId);
        } else {
            throw new MPANotFoundException(ratingId);
        }
    }
}

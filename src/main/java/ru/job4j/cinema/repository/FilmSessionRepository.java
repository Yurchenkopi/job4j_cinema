package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;

public interface FilmSessionRepository {

    FilmSession save(FilmSession filmSession);

    FilmSession findById(int id);

    Collection<FilmSession> findAll();

    Collection<FilmSession> findByHalls(int hallId);

    Collection<FilmSession> findByFilms(int filmId);

    void deleteById(int id);
}

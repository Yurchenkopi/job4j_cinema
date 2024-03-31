package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionService {

    FilmSessionDto save(FilmSession filmSession);

    Optional<FilmSessionDto> findById(int id);

    Collection<FilmSessionDto> findAll();

    Collection<FilmSessionDto> findByHalls(int hallId);

    Collection<FilmSessionDto> findByFilms(int filmId);

    void deleteById(int id);

}

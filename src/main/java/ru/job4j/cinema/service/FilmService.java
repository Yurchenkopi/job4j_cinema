package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmService {

    FilmDto save(Film film);

    Optional<FilmDto> findById(int id);

    Collection<FilmDto> findAll();

    void deleteById(int id);
}

package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmSessionDto;

import java.util.Collection;

public interface FilmSessionService {

    FilmSessionDto findById(int id);

    Collection<FilmSessionDto> findAll();

    Collection<FilmSessionDto> findAllByHalls(int hallId);

}

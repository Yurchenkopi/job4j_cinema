package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;

import java.util.Collection;

public interface FilmService {

    FilmDto findById(int id);

    Collection<FilmDto> findAll();
}

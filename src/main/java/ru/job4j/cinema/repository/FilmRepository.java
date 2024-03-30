package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Film;

import java.util.Collection;

public interface FilmRepository {

    Film save(Film film);

    Film findById(int id);

    void deleteById(int id);

    Collection<Film> findAll();
}

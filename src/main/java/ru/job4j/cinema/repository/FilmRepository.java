package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Film;

import java.util.Collection;

public interface FilmRepository {

    Film findById(int id);

    Collection<Film> findAll();
}

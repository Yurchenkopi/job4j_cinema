package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Genre;

import java.util.Collection;

public interface GenreRepository {

    Genre save(Genre genre);

    Genre findById(int id);

    void deleteById(int id);

    Collection<Genre> findAll();
}

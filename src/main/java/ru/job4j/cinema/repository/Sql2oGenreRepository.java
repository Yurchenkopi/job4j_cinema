package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;

import java.util.Collection;

@Repository
public class Sql2oGenreRepository implements GenreRepository {

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Genre save(Genre genre) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO genres(name)
                    VALUES
                    (:name);
                    """;
            var query = connection.createQuery(sql)
                    .addParameter("name", genre.getName());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            genre.setId(generatedId);
            return genre;
        }
    }

    @Override
    public Genre findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM genres WHERE id = :id;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            return query.executeAndFetchFirst(Genre.class);
        }
    }

    @Override
    public Collection<Genre> findAll() {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM genres;
                    """;
            var query = connection.createQuery(sql);
            return query.executeAndFetch(Genre.class);
        }
    }
}

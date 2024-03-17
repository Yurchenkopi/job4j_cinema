package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

@Repository
public class Sql2oGenreRepository implements GenreRepository {

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
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
}

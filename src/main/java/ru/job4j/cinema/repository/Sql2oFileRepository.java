package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.util.Optional;

@Repository
public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<File> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM files WHERE id = :id;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            var file = query.executeAndFetchFirst(File.class);
            return Optional.of(file);
        }
    }
}
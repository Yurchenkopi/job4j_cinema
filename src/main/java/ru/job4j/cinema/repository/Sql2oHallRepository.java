package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;

@Repository
public class Sql2oHallRepository implements HallRepository {

    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Hall findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM halls WHERE id = :id;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            return query.setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetchFirst(Hall.class);
        }
    }

    @Override
    public Collection<Hall> findAll() {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM halls;
                    """;
            var query = connection.createQuery(sql);
            return query.setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetch(Hall.class);
        }
    }

}

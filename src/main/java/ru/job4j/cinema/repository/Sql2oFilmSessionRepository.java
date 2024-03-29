package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;

@Repository
public class Sql2oFilmSessionRepository implements FilmSessionRepository {

    private final Sql2o sql2o;

    public Sql2oFilmSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public FilmSession save(FilmSession filmSession) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO film_sessions(film_id, hall_id, start_time, end_time, price)
                    VALUES
                    (:filmId, :hallId, :startTime, :endTime, :price);
                    """;
            var query = connection.createQuery(sql)
                    .addParameter("filmId", filmSession.getFilmId())
                    .addParameter("hallId", filmSession.getHallId())
                    .addParameter("startTime", filmSession.getStartTime())
                    .addParameter("endTime", filmSession.getEndTime())
                    .addParameter("price", filmSession.getPrice());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            filmSession.setId(generatedId);
            return filmSession;
        }
    }

    @Override
    public FilmSession findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM film_sessions WHERE id = :id;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            return query.setColumnMappings(FilmSession.COLUMN_MAPPING).executeAndFetchFirst(FilmSession.class);
        }
    }

    @Override
    public Collection<FilmSession> findAll() {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM film_sessions;
                    """;
            var query = connection.createQuery(sql);
            return query.setColumnMappings(FilmSession.COLUMN_MAPPING).executeAndFetch(FilmSession.class);
        }
    }

    @Override
    public Collection<FilmSession> findByHalls(int hallId) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM film_sessions WHERE hall_id = :hallId;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("hallId", hallId);
            return query.setColumnMappings(FilmSession.COLUMN_MAPPING).executeAndFetch(FilmSession.class);
        }
    }

    @Override
    public Collection<FilmSession> findByFilms(int filmId) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM film_sessions WHERE film_id = :filmId;
                    """;
            var query = connection.createQuery(sql);
            query.addParameter("filmId", filmId);
            return query.setColumnMappings(FilmSession.COLUMN_MAPPING).executeAndFetch(FilmSession.class);
        }
    }
}

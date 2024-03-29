package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.FilmSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oFilmSessionRepositoryTest {
    private static Sql2o sql2o;

    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFilmSessionRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
    }

    @AfterEach
    public void clearFilmSessions() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(
                    "DELETE FROM film_sessions WHERE id >= 0");
            query.executeUpdate();
        }
    }
    @Test
    public void whenSaveThenGetSame() {
        var currenDate = LocalDateTime.now();
        var expectedFilmSession = sql2oFilmSessionRepository.save(new FilmSession(
                9, 1, currenDate.with(LocalTime.of(9, 0)),
                currenDate.with(LocalTime.of(11, 0)), 300
        ));
        var savedFilmSession = sql2oFilmSessionRepository.findById(expectedFilmSession.getId());
        assertThat(savedFilmSession).usingRecursiveComparison().isEqualTo(expectedFilmSession);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var currenDate = LocalDateTime.now();
        var filmSession1 = sql2oFilmSessionRepository.save(new FilmSession(
                9, 1, currenDate.with(LocalTime.of(9, 0)),
                currenDate.with(LocalTime.of(11, 0)), 300
        ));
        var filmSession2 = sql2oFilmSessionRepository.save(new FilmSession(
                7, 2, currenDate.with(LocalTime.of(9, 0)),
                currenDate.with(LocalTime.of(11, 0)), 400
        ));
        var filmSession3 = sql2oFilmSessionRepository.save(new FilmSession(
                1, 3, currenDate.with(LocalTime.of(11, 20)),
                currenDate.with(LocalTime.of(13, 40)), 800
        ));
        var result = sql2oFilmSessionRepository.findAll();
        assertThat(result).isEqualTo(List.of(filmSession1, filmSession2, filmSession3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oFilmSessionRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oFilmSessionRepository.findById(0)).isNull();
    }
}

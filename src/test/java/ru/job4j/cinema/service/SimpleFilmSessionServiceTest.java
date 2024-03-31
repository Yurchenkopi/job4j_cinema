package ru.job4j.cinema.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.*;
import ru.job4j.cinema.repository.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleFilmSessionServiceTest {

    private static SimpleFilmSessionService simpleFilmSessionService;

    private static Sql2oFilmRepository sql2oFilmRepository;

    private static Sql2oHallRepository sql2oHallRepository;

    private static Sql2oGenreRepository sql2oGenreRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static Film film1;

    private static Film film2;

    private static Film film3;

    private static Hall hall1;

    private static Hall hall2;

    private static Hall hall3;

    private static Genre genre;

    private static File file;

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
        var sql2o = configuration.databaseClient(datasource);

        var sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oHallRepository = new Sql2oHallRepository(sql2o);

        simpleFilmSessionService = new SimpleFilmSessionService(
                sql2oFilmSessionRepository,
                sql2oFilmRepository,
                sql2oHallRepository
        );

        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = new File("test", "testPath");

        genre = new Genre("TestGenre");

        sql2oFileRepository.save(file);
        sql2oGenreRepository.save(genre);


        film1 = new Film(0, "Поймай меня, если сможешь",
                "Тестовое описание фильма с Леонардо...",
                2002, genre.getId(), 18, 141, file.getId());
        film2 = new Film(0, "Гладиатор",
                "Тестовое описание гладиатора...",
                2002, genre.getId(), 18, 141, file.getId());
        film3 = new Film(0, "Операция Ы",
                "Тестовое описание опирации Ы...",
                1970, genre.getId(), 0, 141, file.getId());
        hall1 = new Hall("TestHall1", 5, 15, "TestHallDescription1");
        hall2 = new Hall("TestHall2", 10, 25, "TestHallDescription2");
        hall3 = new Hall("TestHall3", 15, 45, "TestHallDescription3");

        sql2oFilmRepository.save(film1);
        sql2oFilmRepository.save(film2);
        sql2oFilmRepository.save(film3);
        sql2oHallRepository.save(hall1);
        sql2oHallRepository.save(hall2);
        sql2oHallRepository.save(hall3);

        clearAllFilmSessions();
    }

    @AfterEach
    public void clearFilmSessions() {
        clearAllFilmSessions();
    }

    @AfterAll
    public static void deleteFromTables() {
        sql2oFilmRepository.deleteById(film1.getId());
        sql2oFilmRepository.deleteById(film2.getId());
        sql2oFilmRepository.deleteById(film3.getId());
        sql2oGenreRepository.deleteById(genre.getId());
        sql2oFileRepository.deleteById(file.getId());
        sql2oHallRepository.deleteById(hall1.getId());
        sql2oHallRepository.deleteById(hall2.getId());
        sql2oHallRepository.deleteById(hall3.getId());
    }
    @Test
    public void whenSaveThenGetSame() {
        var currenDate = LocalDateTime.now();
        var expectedFilmSession = simpleFilmSessionService.save(new FilmSession(
                film1.getId(), hall1.getId(), currenDate.with(LocalTime.of(10, 0)),
                currenDate.with(LocalTime.of(11, 40)), 333
        ));
        var savedFilmSession = simpleFilmSessionService.findById(expectedFilmSession.getId()).get();
        assertThat(savedFilmSession).usingRecursiveComparison().isEqualTo(expectedFilmSession);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var currenDate = LocalDateTime.now();
        var filmSession1 = simpleFilmSessionService.save(new FilmSession(
                film1.getId(), hall1.getId(), currenDate.with(LocalTime.of(9, 0)),
                currenDate.with(LocalTime.of(11, 0)), 300
        ));
        var filmSession2 = simpleFilmSessionService.save(new FilmSession(
                film3.getId(), hall2.getId(), currenDate.with(LocalTime.of(12, 0)),
                currenDate.with(LocalTime.of(14, 30)), 400
        ));
        var filmSession3 = simpleFilmSessionService.save(new FilmSession(
                film3.getId(), hall1.getId(), currenDate.with(LocalTime.of(11, 20)),
                currenDate.with(LocalTime.of(13, 40)), 800
        ));
        var resultAll = simpleFilmSessionService.findAll();
        var resultByHalls = simpleFilmSessionService.findByHalls(hall1.getId());
        var resultByFilms = simpleFilmSessionService.findByFilms(film3.getId());

        assertThat(resultAll).usingRecursiveComparison().isEqualTo(List.of(filmSession1, filmSession2, filmSession3));
        assertThat(resultByHalls).usingRecursiveComparison().isEqualTo(List.of(filmSession1, filmSession3));
        assertThat(resultByFilms).usingRecursiveComparison().isEqualTo(List.of(filmSession2, filmSession3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(simpleFilmSessionService.findAll()).isEqualTo(emptyList());
        assertThat(simpleFilmSessionService.findByHalls(hall1.getId())).isEqualTo(emptyList());
        assertThat(simpleFilmSessionService.findByFilms(film1.getId())).isEqualTo(emptyList());
        assertThat(simpleFilmSessionService.findById(0)).isEmpty();
    }

    public static void clearAllFilmSessions() {
        simpleFilmSessionService.findAll()
                .forEach(filmSession -> simpleFilmSessionService.deleteById(filmSession.getId()));
    }
}

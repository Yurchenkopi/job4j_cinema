package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;

import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oFilmRepositoryTest {

    private static Sql2o sql2o;
    private static Sql2oFilmRepository sql2oFilmRepository;

    private static Sql2oGenreRepository sql2oGenreRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static Genre genre;

    private static File file;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFilmRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        genre = new Genre("TestGenre");
        file = new File("test", "test");

        sql2oGenreRepository.save(genre);
        sql2oFileRepository.save(file);

        clearAllFilms();

    }

    @AfterAll
    public static void deleteFromTables() {
        sql2oFileRepository.deleteById(file.getId());
        sql2oGenreRepository.deleteById(genre.getId());
    }

    @AfterEach
    public void clearFilms() {
        clearAllFilms();
    }

    @Test
    public void whenSaveThenGetSame() {
        var expectedFilm = sql2oFilmRepository.save(new Film(
                0, "Поймай меня, если сможешь",
                "Тестовое описание",
                2002, genre.getId(), 18, 141, file.getId()
        ));
        var savedFilm = sql2oFilmRepository.findById(expectedFilm.getId());
        assertThat(savedFilm).usingRecursiveComparison().isEqualTo(expectedFilm);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var film1 = sql2oFilmRepository.save(new Film(
                0, "Поймай меня, если сможешь",
                "Тестовое описание1",
                2002, genre.getId(), 18, 141, file.getId()
        ));
        var film2 = sql2oFilmRepository.save(new Film(
                0, "Гладиатор",
                "Тестовое описание2",
                2000, genre.getId(), 18, 155, file.getId()
        ));
        var film3 = sql2oFilmRepository.save(new Film(
                0, "Властелин колец3",
                "Тестовое описание",
                2003, genre.getId(), 10, 201, file.getId()
        ));
        var result = sql2oFilmRepository.findAll();
        assertThat(result).isEqualTo(List.of(film1, film2, film3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oFilmRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oFilmRepository.findById(0)).isNull();
    }

    public static void clearAllFilms() {
        sql2oFilmRepository.findAll()
                .forEach(film -> sql2oFilmRepository.deleteById(film.getId()));
    }

}

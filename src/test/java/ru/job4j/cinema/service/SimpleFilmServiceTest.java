package ru.job4j.cinema.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.Sql2oFileRepository;
import ru.job4j.cinema.repository.Sql2oFilmRepository;
import ru.job4j.cinema.repository.Sql2oFilmRepositoryTest;
import ru.job4j.cinema.repository.Sql2oGenreRepository;

import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleFilmServiceTest {

    private static SimpleFilmService simpleFilmService;

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
        var sql2o = configuration.databaseClient(datasource);

        var sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        genre = new Genre("TestGenre");
        file = new File("test", "test");

        sql2oGenreRepository.save(genre);
        sql2oFileRepository.save(file);

        simpleFilmService = new SimpleFilmService(sql2oFilmRepository, sql2oGenreRepository);

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
        var expectedFilmDto = simpleFilmService.save(new Film(
                0, "Поймай меня, если сможешь",
                "Тестовое описание",
                2002, genre.getId(), 18, 141, file.getId()
        ));
        var savedFilmDto = simpleFilmService.findById(expectedFilmDto.getId()).get();
        assertThat(savedFilmDto).usingRecursiveComparison().isEqualTo(expectedFilmDto);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var filmDto1 = simpleFilmService.save(new Film(
                0, "Поймай меня, если сможешь",
                "Тестовое описание1",
                2002, genre.getId(), 18, 141, file.getId()
        ));
        var filmDto2 = simpleFilmService.save(new Film(
                0, "Гладиатор",
                "Тестовое описание2",
                2000, genre.getId(), 18, 155, file.getId()
        ));
        var filmDto3 = simpleFilmService.save(new Film(
                0, "Властелин колец3",
                "Тестовое описание",
                2003, genre.getId(), 10, 201, file.getId()
        ));
        var result = simpleFilmService.findAll();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(filmDto1, filmDto2, filmDto3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(simpleFilmService.findAll()).isEqualTo(emptyList());
        assertThat(simpleFilmService.findById(0)).isEmpty();
    }

    public static void clearAllFilms() {
        simpleFilmService.findAll()
                .forEach(film -> simpleFilmService.deleteById(film.getId()));
    }

}

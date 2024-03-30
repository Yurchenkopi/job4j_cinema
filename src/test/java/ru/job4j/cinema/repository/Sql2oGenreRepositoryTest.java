package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oGenreRepositoryTest {


    private static Sql2o sql2o;
    private static Sql2oGenreRepository sql2oGenreRepository;


    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oGenreRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);

        clearAllGenres();
    }

    @AfterEach
    public void clearGenres() {
        clearAllGenres();
    }

    @AfterAll
    public static void restoreGenresTable() {
        var file = new java.io.File("db/scripts/009_dml_insert_genres.sql");
        if (file.exists()) {
            try (BufferedReader input = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), "UTF-8"))
            ) {
                StringBuilder sql = new StringBuilder();
                int read;
                while ((read = input.read()) != -1) {
                    sql.append((char) read);
                }
                try (var connection = sql2o.open()) {
                    var query = connection.createQuery(sql.toString());
                    query.executeUpdate();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("SQL script not found");
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var expectedGenre = sql2oGenreRepository.save(new Genre("Триллер"));
        var savedGenre = sql2oGenreRepository.findById(expectedGenre.getId());
        assertThat(savedGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var genre1 = sql2oGenreRepository.save(new Genre("Боевик"));
        var genre2 = sql2oGenreRepository.save(new Genre("Драма"));
        var genre3 = sql2oGenreRepository.save(new Genre("Фантастика"));
        var result = sql2oGenreRepository.findAll();
        assertThat(result).isEqualTo(List.of(genre1, genre2, genre3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oGenreRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oGenreRepository.findById(0)).isNull();
    }

    public static void clearAllGenres() {
        sql2oGenreRepository.findAll()
                .forEach(genre -> sql2oGenreRepository.deleteById(genre.getId()));
    }

}

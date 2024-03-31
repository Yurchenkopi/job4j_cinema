package ru.job4j.cinema.repository;

import org.junit.jupiter.api.*;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oHallRepositoryTest {
    private static Sql2o sql2o;

    private static Sql2oHallRepository sql2oHallRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oHallRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oHallRepository = new Sql2oHallRepository(sql2o);

        clearAllHalls();
    }

    @AfterEach
    public void clearHalls() {
        clearAllHalls();
    }

    @AfterAll
    public static void restoreHallTable() {
        var file = new java.io.File("db/scripts/011_dml_insert_halls.sql");
        if (file.exists()) {
            try (BufferedReader input = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))
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
        var expectedHall = sql2oHallRepository.save(new Hall("Зеленый", 20, 30, "Большой зал."));
        var savedHall = sql2oHallRepository.findById(expectedHall.getId());
        assertThat(savedHall).usingRecursiveComparison().isEqualTo(expectedHall);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var hall1 = sql2oHallRepository.save(new Hall(
                "Зеленый", 20, 30, "Большой зал."
        ));
        var hall2 = sql2oHallRepository.save(new Hall(
                "Красный", 15, 25, "Средний зал."
        ));
        var hall3 = sql2oHallRepository.save(new Hall(
                "Синий", 5, 15, "Малый зал."
        ));
        var result = sql2oHallRepository.findAll();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(hall1, hall2, hall3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oHallRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oHallRepository.findById(0)).isNull();
    }

    public static void clearAllHalls() {
        sql2oHallRepository.findAll()
                .forEach(hall -> sql2oHallRepository.deleteById(hall.getId()));
    }

}

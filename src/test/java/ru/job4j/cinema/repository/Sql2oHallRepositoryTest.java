package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

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
    }

    @AfterEach
    public void clearHalls() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(
                    "DELETE FROM halls WHERE id >= 0");
            query.executeUpdate();
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
        assertThat(result).isEqualTo(List.of(hall1, hall2, hall3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oHallRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oHallRepository.findById(0)).isNull();
    }

}

package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oUserRepositoryTest {
    private static Sql2o sql2o;

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);

        clearAllUsers();
    }

    @AfterEach
    public void clearUsers() {
        clearAllUsers();
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository.save(new User(0, "ya@ya.ru", "ivan", "1111"));
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveTheSameEmailUserThenEmptyResult() {
        sql2oUserRepository.save(new User(0, "ivan", "ya@ya.ru", "1111"));
        var user = sql2oUserRepository.save(new User(0, "roma", "ya@ya.ru", "1122"));
        assertThat(user).isEmpty();
    }

    public static void clearAllUsers() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM users WHERE id > 0");
            query.executeUpdate();
        }
    }
}

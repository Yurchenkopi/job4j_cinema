package ru.job4j.cinema.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.Sql2oUserRepository;
import ru.job4j.cinema.repository.Sql2oUserRepositoryTest;

import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleUserServiceTest {
    private static Sql2o sql2o;

    private static SimpleUserService simpleUserService;

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

        var sql2oUserRepository = new Sql2oUserRepository(sql2o);

        simpleUserService = new SimpleUserService(sql2oUserRepository);

        clearAllUsers();
    }

    @AfterEach
    public void clearUsers() {
        clearAllUsers();
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = simpleUserService.save(new User("ya@ya.ru", "ivan", "1111"));
        var savedUser = simpleUserService.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveTheSameEmailUserThenEmptyResult() {
        simpleUserService.save(new User("ivan", "ya@ya.ru", "1111"));
        var user = simpleUserService.save(new User("roma", "ya@ya.ru", "1122"));
        assertThat(user).isEmpty();
    }

    public static void clearAllUsers() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM users WHERE id >= 0");
            query.executeUpdate();
        }
    }
}

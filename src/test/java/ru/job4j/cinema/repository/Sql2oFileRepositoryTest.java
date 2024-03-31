package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oFileRepositoryTest {

    private static Sql2o sql2o;

    private static Sql2oFileRepository sql2oFileRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFileRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        clearAllFiles();
    }

    @AfterEach
    public void clearFiles() {
        clearAllFiles();
    }

    @AfterAll
    public static void restoreFileTable() {
        var file = new java.io.File("db/scripts/008_dml_insert_files.sql");
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
        var expectedFile = sql2oFileRepository.save(new File("TestFileName", "TestFilePath"));
        var savedFile = sql2oFileRepository.findById(expectedFile.getId()).get();
        assertThat(savedFile).usingRecursiveComparison().isEqualTo(expectedFile);
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oFileRepository.findById(0)).isEmpty();
    }

    public static void clearAllFiles() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM files WHERE id >= 0");
            query.executeUpdate();
        }
    }

}

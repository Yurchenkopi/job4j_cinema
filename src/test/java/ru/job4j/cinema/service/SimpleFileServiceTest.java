package ru.job4j.cinema.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.repository.Sql2oFileRepository;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleFileServiceTest {

    private static Sql2o sql2o;

    private static SimpleFileService simpleFileService;

    private static Sql2oFileRepository sql2oFileRepository;

    @TempDir
    java.io.File tempDir;

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

        simpleFileService = new SimpleFileService(sql2oFileRepository, "files");

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
    public void whenSaveThenGetSame() throws Exception {
        var tempFile = new java.io.File(tempDir, "testFile.txt");
        String path = tempFile.getPath();
        var expectedFileDto = new FileDto(path, new byte[] {1, 2, 3});
        Files.write(Path.of(path), expectedFileDto.getContent());
        var testFile = sql2oFileRepository.save(new File(tempFile.getName(), path));
        var savedFile = simpleFileService.getFileById(testFile.getId()).get();
        assertThat(savedFile).usingRecursiveComparison().isEqualTo(expectedFileDto);
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(simpleFileService.getFileById(0)).isEmpty();
    }

    public static void clearAllFiles() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM files WHERE id >= 0");
            query.executeUpdate();
        }
    }

}

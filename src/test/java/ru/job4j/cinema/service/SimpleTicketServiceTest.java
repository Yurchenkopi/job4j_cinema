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

public class SimpleTicketServiceTest {

    private static SimpleTicketService simpleTicketService;

    private static Sql2oTicketRepository sql2oTicketRepository;

    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;

    private static Sql2oFilmRepository sql2oFilmRepository;

    private static Sql2oHallRepository sql2oHallRepository;

    private static Sql2oGenreRepository sql2oGenreRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static Sql2oUserRepository sql2oUserRepository;

    private static FilmSession filmSession1;

    private static FilmSession filmSession2;

    private static FilmSession filmSession3;

    private static Film film1;

    private static Film film2;

    private static Film film3;

    private static Hall hall1;

    private static Hall hall2;

    private static Hall hall3;

    private static Genre genre;

    private static File file;

    private static User user1;

    private static User user2;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oTicketRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oHallRepository = new Sql2oHallRepository(sql2o);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);

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

        sql2oFilmRepository.save(film1);
        sql2oFilmRepository.save(film2);
        sql2oFilmRepository.save(film3);

        hall1 = new Hall("TestHall1", 5, 15, "TestHallDescription1");
        hall2 = new Hall("TestHall2", 10, 25, "TestHallDescription2");
        hall3 = new Hall("TestHall3", 15, 45, "TestHallDescription3");

        sql2oHallRepository.save(hall1);
        sql2oHallRepository.save(hall2);
        sql2oHallRepository.save(hall3);

        var currenDate = LocalDateTime.now();

        filmSession1 = new FilmSession(
                film1.getId(), hall1.getId(), currenDate.with(LocalTime.of(10, 0)),
                currenDate.with(LocalTime.of(11, 40)), 333
        );
        filmSession2 = new FilmSession(
                film2.getId(), hall2.getId(), currenDate.with(LocalTime.of(11, 20)),
                currenDate.with(LocalTime.of(13, 40)), 555
        );
        filmSession3 = new FilmSession(
                film3.getId(), hall3.getId(), currenDate.with(LocalTime.of(14, 40)),
                currenDate.with(LocalTime.of(17, 0)), 750
        );

        sql2oFilmSessionRepository.save(filmSession1);
        sql2oFilmSessionRepository.save(filmSession2);
        sql2oFilmSessionRepository.save(filmSession3);

        user1 = new User("ya@ya.ru", "ivan", "1111");
        user2 = new User("mail@mail.ru", "alex", "1111");

        sql2oUserRepository.save(user1);
        sql2oUserRepository.save(user2);

        simpleTicketService = new SimpleTicketService(
                sql2oTicketRepository,
                sql2oFilmRepository,
                sql2oHallRepository,
                sql2oFilmSessionRepository
        );


        clearAllTickets();
    }

    @AfterEach
    public void clearFilmSessions() {
        clearAllTickets();
    }

    @AfterAll
    public static void deleteFromTables() {
        sql2oFilmSessionRepository.deleteById(filmSession1.getFilmId());
        sql2oFilmSessionRepository.deleteById(filmSession2.getFilmId());
        sql2oFilmSessionRepository.deleteById(filmSession3.getFilmId());
        sql2oFilmRepository.deleteById(film1.getId());
        sql2oFilmRepository.deleteById(film2.getId());
        sql2oFilmRepository.deleteById(film3.getId());
        sql2oGenreRepository.deleteById(genre.getId());
        sql2oFileRepository.deleteById(file.getId());
        sql2oHallRepository.deleteById(hall1.getId());
        sql2oHallRepository.deleteById(hall2.getId());
        sql2oHallRepository.deleteById(hall3.getId());
        sql2oUserRepository.deleteById(user1.getId());
        sql2oUserRepository.deleteById(user2.getId());
    }

    @Test
    public void whenBuyThenGetSame() {
        var expectedTicketDto = simpleTicketService.buy(new Ticket(
                filmSession1.getId(), 5, 10, user1.getId()
        ));
        var savedTicket = simpleTicketService.findById(expectedTicketDto.get().getId());
        assertThat(savedTicket).usingRecursiveComparison().isEqualTo(expectedTicketDto);
    }

    @Test
    public void whenBuySeveralThenGetAll() {
        var ticketDto1 = simpleTicketService.buy(new Ticket(
                filmSession1.getId(), 5, 10, user2.getId()
        ));
        var ticketDto2 = simpleTicketService.buy(new Ticket(
                filmSession2.getId(), 7, 12, user1.getId()
        ));
        var ticketDto3 = simpleTicketService.buy(new Ticket(
                filmSession3.getId(), 5, 10, user1.getId()
        ));
        var resultByUser = simpleTicketService.findByUser(user1.getId());
        var resultByUsers = simpleTicketService.findAll();
        assertThat(resultByUser).usingRecursiveComparison().isEqualTo(List.of(ticketDto2.get(), ticketDto3.get()));
        assertThat(resultByUsers).usingRecursiveComparison().isEqualTo(List.of(ticketDto1.get(), ticketDto2.get(), ticketDto3.get()));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(simpleTicketService.findAll()).isEqualTo(emptyList());
        assertThat(simpleTicketService.findByUser(user1.getId())).isEqualTo(emptyList());
        assertThat(simpleTicketService.findById(0)).isEmpty();
    }

    @Test
    public void whenTryBuyTheSameTicketThenOptionalEmpty() {
        var ticket = new Ticket(
                filmSession1.getId(), 5, 10, user2.getId()
        );
        simpleTicketService.buy(ticket);
        assertThat(simpleTicketService.buy(ticket)).isEmpty();
    }

    @Test
    public void whenRefundTicketThenNothingFound() {
        var ticket = simpleTicketService.buy(new Ticket(
                filmSession1.getId(), 5, 10, user2.getId()
        ));
        var result = simpleTicketService.refund(ticket.get().getId());
        assertThat(result).isTrue();
        assertThat(sql2oTicketRepository.findAll()).isEqualTo(emptyList());
    }

    public static void clearAllTickets() {
        simpleTicketService.findAll()
                .forEach(ticket -> simpleTicketService.refund(ticket.getId()));
    }
}

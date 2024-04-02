package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.service.FilmSessionService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilmSessionControllerTest {

    private FilmSessionService filmSessionService;

    private FilmSessionController filmSessionController;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(FilmSessionService.class);
        filmSessionController = new FilmSessionController(filmSessionService);
    }

    @Test
    public void whenRequestFilmSessionsListByFilmsPageThenGetFilmSessionsListByFilmsPage() {
        var currenDate = LocalDateTime.now();
        var filmSession1 = new FilmSession(
                1, 1, currenDate.with(LocalTime.of(10, 0)), currenDate.with(LocalTime.of(11, 40)), 555
        );
        var filmSession3 = new FilmSession(
                1, 2, currenDate.with(LocalTime.of(11, 0)), currenDate.with(LocalTime.of(13, 40)), 600
        );
        var filmSession4 = new FilmSession(
                1, 2, currenDate.with(LocalTime.of(15, 0)), currenDate.with(LocalTime.of(17, 40)), 750
        );
        var filmSessionDto1 = new FilmSessionDto(filmSession1.getId(), "Поймай меня, если сможешь", 0, "Малый",
                filmSession1.getStartTime(), filmSession1.getEndTime(), filmSession1.getPrice(), 5, 5);
        var filmSessionDto3 = new FilmSessionDto(filmSession3.getId(), "Поймай меня, если сможешь", 0, "Большой",
                filmSession3.getStartTime(), filmSession3.getEndTime(), filmSession3.getPrice(), 15, 10);
        var filmSessionDto4 = new FilmSessionDto(filmSession4.getId(), "Поймай меня, если сможешь", 0, "Большой",
                filmSession4.getStartTime(), filmSession4.getEndTime(), filmSession4.getPrice(), 15, 10);

        var expectedFilmSessionsDto = List.of(filmSessionDto1, filmSessionDto3, filmSessionDto4);
        when(filmSessionService.findByFilms(anyInt())).thenReturn(expectedFilmSessionsDto);

        var model = new ConcurrentModel();
        var view = filmSessionController.getByFilms(model, filmSession1.getId());
        var actualFilmSessions = model.getAttribute("filmSessions");

        assertThat(view).isEqualTo("filmSessions/byFilms");
        assertThat(actualFilmSessions).isEqualTo(expectedFilmSessionsDto);
    }

    @Test
    public void whenRequestFilmSessionsListByHallsPageThenGetFilmSessionsListByHallsPage() {
        var currenDate = LocalDateTime.now();
        var filmSession1 = new FilmSession(
                2, 2, currenDate.with(LocalTime.of(12, 0)), currenDate.with(LocalTime.of(15, 40)), 655
        );
        var filmSession2 = new FilmSession(
                1, 2, currenDate.with(LocalTime.of(11, 0)), currenDate.with(LocalTime.of(13, 40)), 600
        );
        var filmSession3 = new FilmSession(
                1, 2, currenDate.with(LocalTime.of(15, 0)), currenDate.with(LocalTime.of(17, 40)), 750
        );
        var filmSessionDto1 = new FilmSessionDto(filmSession1.getId(), "Гладиатор", 0, "Большой",
                filmSession1.getStartTime(), filmSession1.getEndTime(), filmSession1.getPrice(), 15, 10);
        var filmSessionDto2 = new FilmSessionDto(filmSession2.getId(), "Поймай меня, если сможешь", 0, "Большой",
                filmSession2.getStartTime(), filmSession2.getEndTime(), filmSession2.getPrice(), 15, 10);
        var filmSessionDto3 = new FilmSessionDto(filmSession3.getId(), "Поймай меня, если сможешь", 0, "Большой",
                filmSession3.getStartTime(), filmSession3.getEndTime(), filmSession3.getPrice(), 15, 10);

        var expectedFilmSessionsDto = List.of(filmSessionDto1, filmSessionDto2, filmSessionDto3);
        when(filmSessionService.findByHalls(anyInt())).thenReturn(expectedFilmSessionsDto);

        var model = new ConcurrentModel();
        var view = filmSessionController.getByHalls(model, filmSession1.getId());
        var actualFilmSessions = model.getAttribute("filmSessions");

        assertThat(view).isEqualTo("filmSessions/byHalls");
        assertThat(actualFilmSessions).isEqualTo(expectedFilmSessionsDto);
    }

    @Test
    public void whenRequestTicketBuyPageThenGetTicketBuyPage() {
        List<Integer> expectedListOfRowNums = List.of(1, 2, 3, 4, 5);
        List<Integer> expectedListOfPlaceNums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        var currenDate = LocalDateTime.now();
        var filmSession1 = new FilmSession(
                2, 2, currenDate.with(LocalTime.of(12, 0)), currenDate.with(LocalTime.of(15, 40)), 655
        );
        var expectedFilmSession = new FilmSessionDto(filmSession1.getId(), "Гладиатор", 0, "Большой",
                filmSession1.getStartTime(), filmSession1.getEndTime(), filmSession1.getPrice(), 5, 10);

        when(filmSessionService.findById(anyInt())).thenReturn(Optional.of(expectedFilmSession));

        var model = new ConcurrentModel();
        var httpSession = mock(HttpSession.class);

        var view = filmSessionController.buyTicket(model, filmSession1.getId(), httpSession);

        var actualRowNums = model.getAttribute("rowNums");
        var actualPlaceNums = model.getAttribute("placeNums");
        var actualFilmSession = model.getAttribute("filmSession");

        assertThat(view).isEqualTo("tickets/buy.html");
        assertThat(actualRowNums).isEqualTo(expectedListOfRowNums);
        assertThat(actualPlaceNums).isEqualTo(expectedListOfPlaceNums);
        assertThat(actualFilmSession).isEqualTo(expectedFilmSession);
    }
}

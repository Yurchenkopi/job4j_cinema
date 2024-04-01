package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilmControllerTest {

    private FilmService filmService;

    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenRequestFilmListPageThenGetFilmListPage() {
        var filmDto1 = new FilmDto(0, "Поймай меня, если сможешь",
                "Тестовое описание1",
                2002, "Комедия", 18, 141, 1);
        var filmDto2 = new FilmDto(0, "Гладиатор",
                "Тестовое описание2",
                2000, "История", 18, 155, 1);
        var expectedFilmsDto = List.of(filmDto1, filmDto2);
        when(filmService.findAll()).thenReturn(expectedFilmsDto);

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilms = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilms).isEqualTo(expectedFilmsDto);
    }
}

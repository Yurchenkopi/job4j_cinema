package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.service.HallService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HallControllerTest {

    private HallService hallService;

    private HallController hallController;

    @BeforeEach
    public void initServices() {
        hallService = mock(HallService.class);
        hallController = new HallController(hallService);
    }

    @Test
    public void whenRequestHallListPageThenGetPageWithHalls() {
        var hall1 = new Hall("Зеленый", 20, 30, "Большой зал.");
        var hall2 = new Hall("Красный", 15, 25, "Средний зал.");
        var expectedHalls = List.of(hall1, hall2);
        when(hallService.findAll()).thenReturn(expectedHalls);

        var model = new ConcurrentModel();
        var view = hallController.getAll(model);
        var actualHalls = model.getAttribute("halls");

        assertThat(view).isEqualTo("halls/list");
        assertThat(actualHalls).isEqualTo(expectedHalls);
    }

    @Test
    public void whenRequestHallIdPageThenGetHallIdPage() {
        var expectedHall = new Hall("Зеленый", 20, 30, "Большой зал.");
        when(hallService.findById(anyInt())).thenReturn(expectedHall);

        var model = new ConcurrentModel();
        var view = hallController.getById(model, anyInt());
        var actualHall = model.getAttribute("hall");

        assertThat(view).isEqualTo("halls/one");
        assertThat(actualHall).isEqualTo(expectedHall);
    }
}

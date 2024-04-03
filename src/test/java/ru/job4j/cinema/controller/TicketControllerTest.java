package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketControllerTest {
    private TicketService ticketService;

    private TicketController ticketController;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
    }

    @Test
    public void whenRequestTicketIdPageThenGetTicketIdPage() {
        var currenDate = LocalDateTime.now();
        var expectedTicketDto = new TicketDto(
                1, "Название 1", 0, "Малый",
                currenDate.with(LocalTime.of(12, 0)), currenDate.with(LocalTime.of(15, 40)),
                5, 10);

        when(ticketService.findById(anyInt())).thenReturn(Optional.of(expectedTicketDto));

        var model = new ConcurrentModel();
        var view = ticketController.getById(model, expectedTicketDto.getId());
        var actualTicket = model.getAttribute("ticket");

        assertThat(view).isEqualTo("tickets/one");
        assertThat(actualTicket).isEqualTo(expectedTicketDto);
    }

}

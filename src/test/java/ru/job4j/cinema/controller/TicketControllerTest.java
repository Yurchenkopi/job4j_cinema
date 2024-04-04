package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        assertThat(actualTicket).usingRecursiveComparison().isEqualTo(expectedTicketDto);
    }

    @Test
    public void whenRequestTicketByUserPageThenGetTicketByUserPage() {
        var currenDate = LocalDateTime.now();
        var ticketDto1 = new TicketDto(
                1, "Название 1", 0, "Малый",
                currenDate.with(LocalTime.of(12, 0)), currenDate.with(LocalTime.of(15, 40)),
                5, 10);
        var ticketDto2 = new TicketDto(
                1, "Название 2", 0, "Большой>",
                currenDate.with(LocalTime.of(12, 0)), currenDate.with(LocalTime.of(15, 40)),
                6, 15);
        var expectedTicketDtoList = List.of(ticketDto1, ticketDto2);

        when(ticketService.findByUser(anyInt())).thenReturn(expectedTicketDtoList);

        var model = new ConcurrentModel();
        var view = ticketController.getByUsers(model, 1);
        var actualTicketList = model.getAttribute("tickets");

        assertThat(view).isEqualTo("tickets/byUsers");
        assertThat(actualTicketList).usingRecursiveComparison().isEqualTo(expectedTicketDtoList);
    }

    @Test
    public void whenPostTicketThenSameDataAndMessageSuccess() throws Exception {
        var currenDate = LocalDateTime.now();
        var expectedTicket = new Ticket(1, 5, 10, 1);
        var expectedMessage = String.format(
                "Поздравляем!%sВы приобрели билет на ряд-%d, место-%d.",
                System.lineSeparator(),
                expectedTicket.getRowNumber(), expectedTicket.getPlaceNumber()
        );
        var ticketDto = new TicketDto(
                1, "Название", 0, "Малый",
                currenDate.with(LocalTime.of(12, 0)), currenDate.with(LocalTime.of(15, 40)),
                expectedTicket.getRowNumber(), expectedTicket.getPlaceNumber());
        var ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.buy(ticketArgumentCaptor.capture())).thenReturn(Optional.of(ticketDto));

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(expectedTicket, model);

        var actualTicket = ticketArgumentCaptor.getValue();
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("messages/message");
        assertThat(actualTicket).usingRecursiveComparison().isEqualTo(expectedTicket);
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenTryToBuyTicketThenThrowException() throws Exception {
        var expectedException = new Exception("Не удалось купить билет. Место уже занято.");
        var expectedTicket = new Ticket(1, 5, 10, 1);

        when(ticketService.buy(any(Ticket.class))).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(expectedTicket, model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestTicketRefundThenTicketRefundPageAndMessageSuccess() {
        var expectedTicket = new Ticket(1, 5, 10, 1);
        var expectedMessage = "Ожидайте возврат денежных средств.";
        when(ticketService.refund(anyInt())).thenReturn(true);

        var model = new ConcurrentModel();
        var view = ticketController.refund(expectedTicket, model);

        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("messages/message");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenTryToRefundTicketThenErrorMessage() {
        var expectedTicket = new Ticket(1, 5, 10, 1);
        var expectedMessage = "Билет не найден.";
        when(ticketService.refund(anyInt())).thenReturn(false);

        var model = new ConcurrentModel();
        var view = ticketController.refund(expectedTicket, model);

        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}

package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private static List<Integer> MAX_NUM_PLACES = List.of(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30
            );

    private final TicketService ticketService;

    private final FilmSessionService filmSessionService;

    public TicketController(TicketService ticketService, FilmSessionService filmSessionService) {
        this.ticketService = ticketService;
        this.filmSessionService = filmSessionService;
    }

    @GetMapping("/{sessionId}")
    public String buyTicket(Model model, @PathVariable int sessionId) {
        List<Integer> selectedPlaces = MAX_NUM_PLACES.subList(0, filmSessionService.findById(sessionId).getPlaceCount());
        model.addAttribute("places", selectedPlaces);
        return "ticket/buy.html";
    }
}

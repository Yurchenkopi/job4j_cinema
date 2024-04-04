package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/users/{userid}")
    public String getByUsers(Model model, @PathVariable int userid) {
        model.addAttribute("tickets", ticketService.findByUser(userid));
        return "tickets/byUsers";
    }

    @GetMapping("/{ticketId}")
    public String getById(Model model, @PathVariable int ticketId) {
        model.addAttribute("ticket", ticketService.findById(ticketId).get());
        return "tickets/one";
    }

    @PostMapping("/buy")
    public String buyTicket(@ModelAttribute Ticket ticket, Model model) {
        try {
            ticketService.buy(ticket);
            var message = String.format(
                    "Поздравляем!%sВы приобрели билет на ряд-%d, место-%d.",
                    System.lineSeparator(),
                    ticket.getRowNumber(), ticket.getPlaceNumber()
            );
            model.addAttribute("message", message);
            return "messages/message";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @PostMapping("refund")
    public String refund(@ModelAttribute Ticket ticket, Model model) {
        var isDeleted = ticketService.refund(ticket.getId());
        if (!isDeleted) {
            model.addAttribute("message", "Билет не найден.");
            return "errors/404";
        }
        model.addAttribute("message", "Ожидайте возврат денежных средств.");
        return "messages/message";
    }

}

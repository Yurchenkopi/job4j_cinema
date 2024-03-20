package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    private final FilmSessionService filmSessionService;

    public SimpleTicketService(TicketRepository ticketRepository, FilmSessionService filmSessionService) {
        this.ticketRepository = ticketRepository;
        this.filmSessionService = filmSessionService;
    }

    @Override
    public Optional<Ticket> buy(Ticket ticket) {
        return Optional.empty();
    }

    @Override
    public boolean refund(Ticket ticket) {
        return false;
    }
}

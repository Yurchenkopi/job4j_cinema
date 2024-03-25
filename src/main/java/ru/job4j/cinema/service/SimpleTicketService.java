package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    private final FilmRepository filmRepository;

    private final HallRepository hallRepository;

    private final FilmSessionRepository filmSessionRepository;

    public SimpleTicketService(
            TicketRepository ticketRepository,
            FilmRepository filmRepository,
            HallRepository hallRepository,
            FilmSessionRepository filmSessionRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
        this.filmSessionRepository = filmSessionRepository;
    }

    @Override
    public Optional<Ticket> buy(Ticket ticket) throws Exception {
        return ticketRepository.buy(ticket);
    }

    @Override
    public TicketDto findById(int id) {
        return ticketToDto(ticketRepository.getById(id));
    }

    @Override
    public Collection<TicketDto> findByUser(int userId) {
        return ticketRepository.findByUser(userId).stream()
                .map(this::ticketToDto)
                .toList();
    }

    @Override
    public boolean refund(Ticket ticket) {
        return ticketRepository.refund(ticket);
    }

    private TicketDto ticketToDto(Ticket ticket) {
        int sessionId = ticket.getSessionId();
        var session = filmSessionRepository.findById(sessionId);
        var film = filmRepository.findById(session.getFilmId());
        var hall = hallRepository.findById(session.getHallId());
        return new TicketDto(
                ticket.getId(),
                film.getName(),
                film.getFileId(),
                hall.getName(),
                ticket.getRowNumber(),
                ticket.getPlaceNumber()
        );
    }
}

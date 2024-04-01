package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
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
    public Optional<TicketDto> buy(Ticket ticket) throws Exception {
        return Optional.ofNullable(ticketToDto(ticketRepository.buy(ticket)));
    }

    @Override
    public Optional<TicketDto> findById(int id) {
        return Optional.ofNullable(ticketToDto(Optional.ofNullable(ticketRepository.getById(id))));
    }

    @Override
    public Collection<TicketDto> findAll() {
        return ticketRepository.findAll().stream()
                .map(t -> ticketToDto(Optional.ofNullable(t)))
                .toList();
    }

    @Override
    public Collection<TicketDto> findByUser(int userId) {
        return ticketRepository.findByUser(userId).stream()
                .map(t -> ticketToDto(Optional.ofNullable(t)))
                .toList();
    }

    @Override
    public boolean refund(int ticketId) {
        return ticketRepository.refund(ticketId);
    }

    private TicketDto ticketToDto(Optional<Ticket> ticketOptional) {
        TicketDto rsl = null;
        if (ticketOptional.isPresent()) {
            var ticket = ticketOptional.get();
            int sessionId = ticket.getSessionId();
            var session = filmSessionRepository.findById(sessionId);
            var film = filmRepository.findById(session.getFilmId());
            var hall = hallRepository.findById(session.getHallId());
            rsl = new TicketDto(
                    ticket.getId(),
                    film.getName(),
                    film.getFileId(),
                    hall.getName(),
                    session.getStartTime(),
                    session.getEndTime(),
                    ticket.getRowNumber(),
                    ticket.getPlaceNumber()
            );
        }
        return rsl;
    }
}

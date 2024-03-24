package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketService {


    Optional<Ticket> buy(Ticket ticket) throws Exception;

    TicketDto findById(int id);

    Collection<TicketDto> findByUser(int userId);

    boolean refund(Ticket ticket);
}

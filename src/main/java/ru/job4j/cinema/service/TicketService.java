package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketService {


    Optional<TicketDto> buy(Ticket ticket) throws Exception;

    Optional<TicketDto> findById(int id);

    Collection<TicketDto> findAll();

    Collection<TicketDto> findByUser(int userId);

    boolean refund(int ticketId);
}

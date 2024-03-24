package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> buy(Ticket ticket) throws Exception;

    Ticket getById(int id);

    Collection<Ticket> findByUser(int userId);

    boolean refund(Ticket ticket);
}
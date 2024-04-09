package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> buy(Ticket ticket);

    Ticket getById(int id);

    Collection<Ticket> findByUser(int userId);

    Collection<Ticket> findAll();

    boolean refund(int ticketId);
}
package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

public interface TicketService {


    Optional<Ticket> buy(Ticket ticket);

    boolean refund(Ticket ticket);
}

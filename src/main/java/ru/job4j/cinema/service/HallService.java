package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;

@Service
public interface HallService {

    Hall save(Hall hall);

    Hall findById(int id);

    void deleteById(int id);

    Collection<Hall> findAll();
}

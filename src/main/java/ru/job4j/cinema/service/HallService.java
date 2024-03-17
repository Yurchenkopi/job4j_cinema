package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;

@Service
public interface HallService {

    Hall findById(int id);

    Collection<Hall> findAll();
}

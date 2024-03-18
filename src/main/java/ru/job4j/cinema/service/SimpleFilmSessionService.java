package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;

    private final FilmRepository filmRepository;

    private final HallRepository hallRepository;

    public SimpleFilmSessionService(
            FilmSessionRepository filmSessionRepository,
            FilmRepository filmRepository,
            HallRepository hallRepository
    ) {
        this.filmSessionRepository = filmSessionRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public FilmSessionDto findById(int id) {
        return filmSessionToDto(filmSessionRepository.findById(id));
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        return filmSessionRepository.findAll().stream()
                .map(this::filmSessionToDto)
                .toList();
    }

    @Override
    public Collection<FilmSessionDto> findAllByHalls(int hallId) {
        return filmSessionRepository.findAllByHalls(hallId).stream()
                .map(this::filmSessionToDto)
                .toList();
    }

    private FilmSessionDto filmSessionToDto(FilmSession filmSession) {
        int filmSessionId = filmSession.getId();
        var filmName = filmRepository.findById(filmSession.getFilmId()).getName();
        var hallName = hallRepository.findById(filmSession.getHallId()).getName();
        return new FilmSessionDto(
                filmSessionId,
                filmName,
                hallName,
                filmSession.getStartTime(),
                filmSession.getEndTime(),
                filmSession.getPrice()
        );
    }
}

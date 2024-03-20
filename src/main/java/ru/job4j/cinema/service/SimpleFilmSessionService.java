package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;

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
    public Collection<FilmSessionDto> findByHalls(int hallId) {
        return filmSessionRepository.findByHalls(hallId).stream()
                .map(this::filmSessionToDto)
                .toList();
    }

    @Override
    public Collection<FilmSessionDto> findByFilms(int filmId) {
        return filmSessionRepository.findByFilms(filmId).stream()
                .map(this::filmSessionToDto)
                .toList();
    }

    private FilmSessionDto filmSessionToDto(FilmSession filmSession) {
        int filmSessionId = filmSession.getId();
        var filmName = filmRepository.findById(filmSession.getFilmId()).getName();
        var hall = hallRepository.findById(filmSession.getHallId());
        return new FilmSessionDto(
                filmSessionId,
                filmName,
                filmSession.getFilmId(),
                hall.getName(),
                filmSession.getStartTime(),
                filmSession.getEndTime(),
                filmSession.getPrice(),
                hall.getRowCount(),
                hall.getPlaceCount()
        );
    }
}

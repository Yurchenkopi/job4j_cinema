package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;
import java.util.Optional;

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
    public FilmSessionDto save(FilmSession filmSession) {
        return filmSessionToDto(Optional.ofNullable(filmSessionRepository.save(filmSession)));
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        return Optional.ofNullable(filmSessionToDto(
                Optional.ofNullable(filmSessionRepository.findById(id))));
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        return filmSessionRepository.findAll().stream()
                .map(filmSession -> filmSessionToDto(Optional.ofNullable(filmSession)))
                .toList();
    }

    @Override
    public Collection<FilmSessionDto> findByHalls(int hallId) {
        return filmSessionRepository.findByHalls(hallId).stream()
                .map(filmSession -> filmSessionToDto(Optional.ofNullable(filmSession)))
                .toList();
    }

    @Override
    public Collection<FilmSessionDto> findByFilms(int filmId) {
        return filmSessionRepository.findByFilms(filmId).stream()
                .map(filmSession -> filmSessionToDto(Optional.ofNullable(filmSession)))
                .toList();
    }

    @Override
    public void deleteById(int id) {
        filmSessionRepository.deleteById(id);
    }

    private FilmSessionDto filmSessionToDto(Optional<FilmSession> filmSessionOptional) {
        FilmSessionDto rsl = null;
        if (filmSessionOptional.isPresent()) {
            var filmSession = filmSessionOptional.get();
            int filmSessionId = filmSession.getId();
            var filmName = filmRepository.findById(filmSession.getFilmId()).getName();
            var hall = hallRepository.findById(filmSession.getHallId());
            rsl = new FilmSessionDto(
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
        return rsl;
    }

}

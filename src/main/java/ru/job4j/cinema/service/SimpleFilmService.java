package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;

    private final GenreRepository genreRepository;

    public SimpleFilmService(
            FilmRepository filmRepository,
            GenreRepository genreRepository
    ) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public FilmDto findById(int id) {
        return filmToDto(filmRepository.findById(id));
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmRepository.findAll().stream()
                .map(this::filmToDto)
                .collect(Collectors.toList());
    }

    private FilmDto filmToDto(Film film) {
        int filmId = film.getId();
        var genreName = genreRepository.findById(film.getGenreId()).getName();
        return new FilmDto(
                filmId,
                film.getName(),
                film.getDescription(),
                film.getYear(),
                genreName,
                film.getMinimalAge(),
                film.getDurationInMinutes(),
                film.getFileId()
        );
    }
}

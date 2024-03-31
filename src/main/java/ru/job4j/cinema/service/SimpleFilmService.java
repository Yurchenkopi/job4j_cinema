package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;

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
    public FilmDto save(Film film) {
        return filmToDto(Optional.ofNullable(filmRepository.save(film)));
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        return Optional.ofNullable(filmToDto(Optional.ofNullable(filmRepository.findById(id))));
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmRepository.findAll().stream()
                .map(f -> filmToDto(Optional.ofNullable(f)))
                .toList();
    }

    @Override
    public void deleteById(int id) {
        filmRepository.deleteById(id);
    }

    private FilmDto filmToDto(Optional<Film> filmOptional) {
        FilmDto rsl = null;
        if (filmOptional.isPresent()) {
            var film = filmOptional.get();
            var genreName = genreRepository.findById(film.getGenreId()).getName();
            rsl = new FilmDto(
                    film.getId(),
                    film.getName(),
                    film.getDescription(),
                    film.getYear(),
                    genreName,
                    film.getMinimalAge(),
                    film.getDurationInMinutes(),
                    film.getFileId()
            );
        }
        return rsl;
    }

}

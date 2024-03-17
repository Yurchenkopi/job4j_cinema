package ru.job4j.cinema.dto;

import java.time.LocalDateTime;

public class FilmSessionDto {

    private int id;

    private String filmName;

    private String hallName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int price;

    public FilmSessionDto(
            int id, String filmName, String hallName,
            LocalDateTime startTime, LocalDateTime endTime,
            int price) {
        this.id = id;
        this.filmName = filmName;
        this.hallName = hallName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }
}

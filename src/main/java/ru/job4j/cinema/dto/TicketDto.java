package ru.job4j.cinema.dto;

import java.time.LocalDateTime;

public class TicketDto {

    private int id;

    private String filmName;

    private int fileId;

    private String hallName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int rowNumber;

    private int placeNumber;

    public TicketDto(
            int id, String filmName, int fileId,
            String hallName, LocalDateTime startTime,
            LocalDateTime endTime, int rowNumber, int placeNumber
    ) {
        this.id = id;
        this.filmName = filmName;
        this.fileId = fileId;
        this.hallName = hallName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rowNumber = rowNumber;
        this.placeNumber = placeNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }
}

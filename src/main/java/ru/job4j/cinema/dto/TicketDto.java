package ru.job4j.cinema.dto;

public class TicketDto {

    private int id;

    private String filmName;

    private int fileId;

    private String hallName;

    private int rowNumber;

    private int placeNumber;

    public TicketDto(int id, String filmName, int fileId, String hallName, int rowNumber, int placeNumber) {
        this.id = id;
        this.filmName = filmName;
        this.fileId = fileId;
        this.hallName = hallName;
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

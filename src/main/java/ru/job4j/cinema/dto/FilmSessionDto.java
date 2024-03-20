package ru.job4j.cinema.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FilmSessionDto {

    private int id;

    private String filmName;

    private int fileId;

    private String hallName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int price;

    private int rowCount;

    private int placeCount;

    private List<Integer> rowNums;

    private List<Integer> placeNums;

    public FilmSessionDto(
            int id, String filmName, int fileId, String hallName,
            LocalDateTime startTime, LocalDateTime endTime,
            int price, int rowCount, int placeCount
    ) {
        this.id = id;
        this.filmName = filmName;
        this.fileId = fileId;
        this.hallName = hallName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.rowCount = rowCount;
        this.placeCount = placeCount;
        rowNums = new ArrayList<>();
        placeNums = new ArrayList<>();
        for (int i = 1; i <= rowCount; i++) {
            rowNums.add(i);
        }
        for (int i = 1; i <= placeCount; i++) {
            placeNums.add(i);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    public List<Integer> getRowNums() {
        return rowNums;
    }

    public void setRowNums(List<Integer> rowNums) {
        this.rowNums = rowNums;
    }

    public List<Integer> getPlaceNums() {
        return placeNums;
    }

    public void setPlaceNums(List<Integer> placeNums) {
        this.placeNums = placeNums;
    }
}

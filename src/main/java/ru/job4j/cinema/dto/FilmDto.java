package ru.job4j.cinema.dto;

public class FilmDto {

    private int id;

    private String name;

    private String description;

    private int year;

    private String genre;

    private int minimalAge;

    private int durationInMinutes;

    private int fileId;

    public FilmDto(
            int id, String name, String description,
            int year, String genre, int minimalAge, int durationInMinutes,
            int fileId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.genre = genre;
        this.minimalAge = minimalAge;
        this.durationInMinutes = durationInMinutes;
        this.fileId = fileId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getMinimalAge() {
        return minimalAge;
    }

    public void setMinimalAge(int minimalAge) {
        this.minimalAge = minimalAge;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}

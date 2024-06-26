package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmSessionService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/filmSessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;

    public FilmSessionController(FilmSessionService filmSessionService) {
        this.filmSessionService = filmSessionService;
    }

    @GetMapping("/halls/{id}")
    public String getByHalls(Model model, @PathVariable int id) {
        model.addAttribute("filmSessions", filmSessionService.findByHalls(id));
        return "filmSessions/byHalls";
    }

    @GetMapping("/films/{id}")
    public String getByFilms(Model model, @PathVariable int id) {
        model.addAttribute("filmSessions", filmSessionService.findByFilms(id));
        return "filmSessions/byFilms";
    }

    @GetMapping("/{sessionId}")
    public String buyTicket(Model model, @PathVariable int sessionId, HttpSession session) {
        var listOfRowPlaces = filmSessionService.findById(sessionId).get().getRowNums();
        var listOfPlaces = filmSessionService.findById(sessionId).get().getPlaceNums();
        var user = (User) session.getAttribute("user");
        model.addAttribute("rowNums", listOfRowPlaces);
        model.addAttribute("placeNums", listOfPlaces);
        model.addAttribute("filmSession", filmSessionService.findById(sessionId).get());
        model.addAttribute("user", user);
        return "tickets/buy.html";
    }
}

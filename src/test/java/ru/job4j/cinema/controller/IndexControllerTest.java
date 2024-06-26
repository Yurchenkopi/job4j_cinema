package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IndexControllerTest {
    @Test
    public void whenRequestIndexPageThenGetIndexPage() {
        var view = new IndexController().getIndex();

        assertThat(view).isEqualTo("index");
    }
}
